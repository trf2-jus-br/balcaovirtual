var appCP = angular
		.module('sample.consulta-processual', [ 'ui.router', 'angular-storage',
				'angular-jwt', 'angularModalService', 'ngSanitize' ]);

appCP.config(function($stateProvider) {
	$stateProvider.state('consulta-processual', {
		url : '/consulta-processual',
		controller : 'ConsultaProcessualCtrl',
		templateUrl : 'resources/consulta-processual/consulta-processual.html',
		data : {
			requiresLogin : true
		}
	});
	$stateProvider.state('processo', {
		url : '/processo/{numero}',
		controller : 'ProcessoCtrl',
		templateUrl : 'resources/consulta-processual/processo.html',
		data : {
			requiresLogin : true
		}
	});
});

appCP
		.controller(
				'ConsultaProcessualCtrl',
				function ConsultaProcessualCtrl($scope, $http, store,
						jwtHelper, Upload, $timeout, ModalService,
						$stateParams, $state) {
					$scope.avancada = false;

					$scope.mostrarProcesso = function(numero) {
						var n = somenteNumeros(numero);
						$scope.$parent.promise = $http({
							url : 'api/v1/processo/' + n + "/validar",
							method : 'GET'
						})
								.then(
										function(response) {
											$state.go('processo', {
												numero : response.data.numero
											});
										},
										function(error) {
											$scope.errormsg = "Não foi possível obter informações sobre o processo '"
													+ $scope.numero
													+ "'. ("
													+ error.data.errormsg + ")";
										})
					}

				});

appCP
		.controller(
				'ProcessoCtrl',
				function ConsultaProcessualCtrl($scope, $http, store,
						jwtHelper, Upload, $timeout, ModalService,
						$stateParams, $window) {
					$scope.numero = $stateParams.numero;
					$scope.partes = false;
					$scope.dadosComplementares = false;
					$scope.tipoRepresentante = {
						A : 'Advogado',
						E : 'Escritório de Advocacia',
						M : 'Ministério Público',
						D : 'Defensor Público',
						P : 'Advogado Público'
					}

					$scope.init = function() {
						var numero = somenteNumeros($scope.numero);
						$scope.$parent.promise = $http({
							url : 'api/v1/processo/' + numero + "/validar",
							method : 'GET'
						})
								.then(
										function(response) {
											$scope.orgao = response.data.orgao;
											$scope.$parent.promise = $http(
													{
														url : 'api/v1/processo/'
																+ numero
																+ '/consultar?orgao='
																+ $scope.orgao,
														method : 'GET'
													})
													.then(
															function(response) {
																try {
																	$scope.proc = response.data.value;
																	$scope
																			.fixProc();
																	$scope
																			.getDescriptions();

																} catch (e) {
																	console
																			.error(e);
																}
															},
															function(error) {
																$scope.errormsg = error.data.errormsg;
															});
										},
										function(error) {
											$scope.errormsg = "Não foi possível obter informações sobre o processo "
													+ $scope.numero
													+ ": "
													+ error.data.errormsg;
										})
					}

					$scope.getDescriptions = function() {
						var db = $scope.proc.dadosBasicos;
						$scope.$parent.promise = [];

						// Carregar a classe
						$scope.$parent.promise
								.push($http(
										{
											url : 'api/v1/classe/'
													+ db.classeProcessual
													+ '?orgao=' + $scope.orgao,
											method : 'GET'
										})
										.then(
												function(response) {
													$scope.proc.fixed.classeProcessualDescricao = response.data.descricao;
													$scope.proc.fixed.classeProcessualDescricaoCompleta = response.data.descricaocompleta;
												},
												function(error) {
													$scope.errormsg = error.data.errormsg;
												}));

						// Carregar assuntos (partimos do princípio que sempre
						// há um assunto principal e que sempre é o primeiro)
						if (db.assunto && db.assunto.length > 0
								&& Number(db.assunto[0].codigoNacional) > 0) {
							for (var i = 0; i < db.assunto.length; i++) {
								var ass = db.assunto[i];
								$scope.getAssuntoDescription(ass);

							}
						}
					}
					$scope.getAssuntoDescription = function(ass) {
						$scope.$parent.promise
								.push($http(
										{
											url : 'api/v1/assunto/'
													+ ass.codigoNacional
													+ '?orgao=' + $scope.orgao,
											method : 'GET'
										})
										.then(
												function(response) {
													ass.descricao = response.data.descricao;
													ass.descricaoCompleta = response.data.descricaocompleta;
													if (ass.principal) {
														$scope.proc.fixed.assuntoPrincipalDescricao = response.data.descricao;
														$scope.proc.fixed.assuntoPrincipalDescricaoCompleta = response.data.descricaocompleta;
													}
												},
												function(error) {
													$scope.errormsg = error.data.errormsg;
												}));
					}

					$scope.fixProc = function() {
						var p = $scope.proc;

						p.dadosBasicos.numero = formatarProcesso(p.dadosBasicos.numero);
						p.fixed = {};

						for (var i = 0; i < p.dadosBasicos.polo.length; i++) {
							if (p.dadosBasicos.polo[i].polo == 'AT')
								p.fixed.partesAtivas = p.dadosBasicos.polo[i].parte;
							if (p.dadosBasicos.polo[i].polo == 'PA')
								p.fixed.partesPassivas = p.dadosBasicos.polo[i].parte;
						}

						if (!p.dadosBasicos.outroParametro)
							p.dadosBasicos.outroParametro = {};
						p.fixed.nomeMagistrado = p.dadosBasicos.outroParametro.nomeMagistrado;

						if (p.documento) {
							for (var i = 0; i < p.documento.length; i++) {
								var f = false;
								var doc = p.documento[i];
								if (p.movimento)
									for (var j = 0; j < p.movimento.length; j++) {
										var mov = p.movimento[j];
										if (mov
												.hasOwnProperty("idDocumentoVinculado")
												&& mov.idDocumentoVinculado
														.includes(doc.idDocumento)) {
											if (!mov.documento)
												mov.documento = [];
											mov.documento.unshift(doc);
											f = true;
											break;
										}
									}
								if (f)
									continue;
								if (!p.movimento)
									p.movimento = [];
								p.movimento.push({
									dataHora : doc.dataHora,
									documento : [ doc ]
								});
							}
						}

						if (p.movimento) {
							p.movimento = p.movimento.sort(function(a, b) {
								if (a.dataHora < b.dataHora)
									return 1;
								if (a.dataHora > b.dataHora)
									return -1;
								return 0;
							})

							p.fixed.movdoc = [];
							for (var j = 0; j < p.movimento.length; j++) {
								var mov = p.movimento[j];
								p.fixed.movdoc.push({
									teste : true,
									dataHora : mov.dataHora,
									mov : mov,
									doc : (mov.documento || [ {} ])[0],
									rowspan : (mov.documento || [ {} ]).length
								});
								if (mov.documento && mov.documento.length > 0) {
									for (var i = 1; i < mov.documento.length; i++) {
										p.fixed.movdoc.push({
											dataHora : mov.dataHora,
											doc : mov.documento[i]
										});
									}
								}
							}

							// Ativar a visualização da primeira decisão
							if (p.fixed.movdoc) {
								var fDecisao = true;
								var lastMovDoc = null;
								for (var i = 0; i < p.fixed.movdoc.length; i++) {
									var movdoc = p.fixed.movdoc[i];
									if (movdoc.mov)
										lastMovDoc = movdoc;
									if (movdoc.doc) {
										var doc = movdoc.doc;
										if (doc.outroParametro
												&& doc.outroParametro.textoMovimento) {
											if (fDecisao) {
												$scope.mostrarTexto(doc, true);
												fDecisao = false;
											} else
												doc.exibirTexto = false;
										}
									}
								}
							}

							$scope.fixMovDoc(p.fixed.movdoc);

							if (typeof p.dadosBasicos.valorCausa === 'number')
								p.fixed.valorCausa = "R$ "
										+ p.dadosBasicos.valorCausa
												.formatMoney(2, ',', '.');
							p.fixed.dataAjuizamento = $scope
									.formatDDMMYYYHHMM(p.dadosBasicos.dataAjuizamento);

							var op = p.dadosBasicos.outroParametro;
							if (op.processoVinculado) {
								op.processoVinculado = $scope
										.colocarLink(arrayToString(op.processoVinculado));
							}
							if (op.processoOriginario) {
								op.processoOriginario = $scope
										.colocarLink(op.processoOriginario);
							}
							if (op.peticaoPendenteJuntada)
								op.peticaoPendenteJuntada = arrayToString(op.peticaoPendenteJuntada);

							if (op.numProcAdm)
								op.numProcAdm = arrayToString(op.numProcAdm);
							if (op.numCDA) {
								if (typeof op.numCDA === 'string')
									op.numCDA = [ op.numCDA ];
								p.fixed.numCDAs = arrayToString(op.numCDA);
							}
							if (op.tipoAtuacaoParte) {
								var map = {};
								for (var i = 0; i < op.tipoAtuacaoParte.length; i++) {
									var str = op.tipoAtuacaoParte[i];
									var n = str.lastIndexOf(':');
									if (n >= 0)
										map[str.substring(0, n)] = str
												.substring(n + 1);
								}
								for (var i = 0; i < p.dadosBasicos.polo.length; i++) {
									for (var j = 0; j < p.dadosBasicos.polo[i].parte.length; j++) {
										p.dadosBasicos.polo[i].parte[j].tipoAtuacao = map[p.dadosBasicos.polo[i].parte[j].pessoa.nome];
									}
								}
							}
							console.log(p);
						}
					}

					// Corrige ordenação de peças avulsas
					$scope.fixMovDoc = function(a) {
						var lastIdDocumento;
						for (var i = 0; i < a.length; i++) {
							var movdoc = a[i];

							// verifica se a peça está fora de ordem
							if (movdoc.doc.idDocumento) {
								if (!lastIdDocumento === undefined)
									lastIdDocumento = Number(movdoc.doc.idDocumento);
								else if (Number(movdoc.doc.idDocumento) > lastIdDocumento) {
									// localizar o primeiro que já é menor do
									// que o que está fora de posição
									for (var j = 0; j < i; j++) {
										var md = a[j];
										if (md.doc.idDocumento
												&& Number(md.doc.idDocumento) < Number(movdoc.doc.idDocumento)) {
											a.move(i, j);
											break;
										}
									}
								} else {
									lastIdDocumento = Number(movdoc.doc.idDocumento);
								}
							}
						}

						// Marcar pares e impares
						var odd = false;
						for (var i = 0; i < a.length; i++) {
							if (a[i].mov)
								odd = !odd;
							a[i].odd = odd;
						}
					}

					$scope.colocarLink = function(s) {
						var a = s.split(', ');
						for (var i = 0; i < a.length; i++) {
							a[i] = '<a href="#!/processo/' + a[i] + '">'
									+ $scope.formatProcesso(a[i]) + '</a>';
						}
						return a.join(', ');
					}

					$scope.mostrarPartes = function() {
						$scope.partes = true;
					}

					$scope.mostrarDadosComplementares = function() {
						$scope.dadosComplementares = true;
					}

					$scope.mostrarPeca = function(idDocumento) {
						$scope.$parent.promise = $http(
								{
									url : 'api/v1/processo/' + $scope.numero
											+ '/peca/' + idDocumento
											+ '/pdf?orgao=' + $scope.orgao,
									method : 'GET'
								}).then(
								function(response) {
									var jwt = response.data.jwt;
									$window.open('api/v1/download/' + jwt + '/'
											+ $scope.numero + '-peca-'
											+ idDocumento + '.pdf');
								}, function(error) {
									alert(error.data.errormsg);
								});
					}

					$scope.mostrarTexto = function(doc, f) {
						for (var i = 0; i < $scope.proc.fixed.movdoc.length; i++) {
							var movdoc = $scope.proc.fixed.movdoc[i];
							if (doc == movdoc.doc) {
								for (var j = i; j >= 0; j--) {
									if ($scope.proc.fixed.movdoc[j].mov)
										break;
								}
								if ($scope.proc.fixed.movdoc[j].rowspan
										&& (j < $scope.proc.fixed.movdoc.length - 1)
										&& ($scope.proc.fixed.movdoc[j + 1].rowspan === undefined)) {
									$scope.proc.fixed.movdoc[j].rowspan += f ? 1
											: -1;
								}
								doc.exibirTexto = f;
							}
						}
					}

					$scope.formatDDMMYYYHHMM = function(s) {
						if (s === undefined)
							return;
						var r = s.substring(6, 8) + '/' + s.substring(4, 6)
								+ '/' + s.substring(0, 4) + ' '
								+ s.substring(8, 10) + ':'
								+ s.substring(10, 12);
						r = r.replace(" ", "&nbsp;");
						return r;
					}

					$scope.formatReal = function(c, d, t) {
						var n = this, c = isNaN(c = Math.abs(c)) ? 2 : c, d = d == undefined ? "."
								: d, t = t == undefined ? "," : t, s = n < 0 ? "-"
								: "", i = parseInt(n = Math.abs(+n || 0)
								.toFixed(c))
								+ "", j = (j = i.length) > 3 ? j % 3 : 0;
						return s
								+ (j ? i.substr(0, j) + t : "")
								+ i.substr(j).replace(/(\d{3})(?=\d)/g,
										"$1" + t)
								+ (c ? d + Math.abs(n - i).toFixed(c).slice(2)
										: "");
					}
					$scope.formatTexto = function(s) {
						if (s === undefined)
							return s;
						return s.replace(/^\s\s*/, '').replace(/\s\s*$/, '')
								.replace(/\n\s+\n/g,
										'<div class="break"></div>').replace(
										/\n/g, '<br/>')
					}

					$scope.imprimir = function() {
						window.print();
					}

					$scope.formatProcesso = formatarProcesso;

					$scope.init();
				});

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
	$stateProvider.state('lista-processos', {
		url : '/lista-processos',
		controller : 'ListaProcessosCtrl',
		templateUrl : 'resources/consulta-processual/lista-processos.html',
		params : {
			processos : []
		},
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
							// console.log(p);
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
									logEvento('consulta-processual',
											'mostrar pdf peça');
								}, function(error) {
									alert(error.data.errormsg);
								});
					}

					$scope.mostrarCompleto = function() {
						$scope.$parent.promise = $http(
								{
									url : 'api/v1/processo/' + $scope.numero
											+ '/pdf?orgao=' + $scope.orgao,
									method : 'GET'
								}).then(
								function(response) {
									var jwt = response.data.jwt;
									$window.open('api/v1/download/' + jwt + '/'
											+ $scope.numero + '.pdf');
									logEvento('consulta-processual',
											'mostrar pdf completo',
											'individual');
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

					logEvento('consulta-processual', 'consultar processo');
				});

appCP
		.controller(
				'ListaProcessosCtrl',
				function ListaProcessosCtrl($scope, $http, $filter, store,
						jwtHelper, $timeout, $stateParams, ModalService) {
					$scope.downloadExtensionId = "komegelldppbjndifhabfpjpddjaocfa";
					$timeout(
							function() {
								$scope.versionTRF2DownloadChromeExtension = document
										.getElementById("trf2-download-chrome-extension-active").value;
							}, 100);

					$scope.map = {};

					$scope.outlineAtivo = false;
					$scope.gui = {
						todos : true,
						filtro : ""
					};

					$scope.processos = [ {
						numero : '0000588-48.2004.4.02.5117'
					} ];
					$scope.processos = $stateParams.processos;

					$scope.init = function() {
						delete $scope.errormsg;
						$scope.$parent.promise = [];
						for (var i = 0; i < $scope.processos.length; i++) {
							if (!$scope.processos[i].disabled)
								$scope.processos[i].checked = true;
							if (!$scope.processos[i].validado) {
								$scope.validarProcesso($scope.processos[i]);
							}
						}
					}

					$scope.validarProcesso = function(processo) {
						var n = somenteNumeros(processo.numero);
						$scope.$parent.promise
								.push($http({
									url : 'api/v1/processo/' + n + "/validar",
									method : 'GET'
								})
										.then(
												function(response) {
													processo.numero = formatarProcesso(processo.numero);
													processo.orgao = response.data.orgao;
													processo.unidade = response.data.unidade;
													processo.validado = true;
												},
												function(error) {
													processo.checked = false;
													processo.disabled = true;
													processo.errormsg = "Não foi possível obter informações sobre o processo. ("
															+ error.data.errormsg
															+ ")";
												}));
					}

					$scope.filtrados = function() {
						var docs = $filter('filter')($scope.processos || [],
								$scope.gui.filtro);
						return docs;
					}

					$scope.filtradosEMarcados = function() {
						docs = $filter('filter')($scope.filtrados(),
								function(item) {
									return item.checked;
								});
						return docs;
					}

					$scope.marcarTodos = function() {
						var docs = $scope.filtrados();
						for (var i = 0; i < docs.length; i++) {
							var doc = docs[i];
							if (!doc.disabled)
								doc.checked = $scope.gui.todos;
						}
					}

					$scope.contarChecked = function() {
						return $scope.filtradosEMarcados().length;
					}

					$scope.baixarEmLote = function() {
						var processos = $scope.filtradosEMarcados();
						if (processos.length > 0) {
							delete $scope.errormsg;
							$scope.obterJwt(processos, 0);
						}
					}

					$scope.obterJwt = function(processos, i) {
						delete processos[i].jwt;
						delete processos[i].state;
						delete processos[i].perc;
						$http(
								{
									url : 'api/v1/processo/'
											+ somenteNumeros(processos[i].numero)
											+ '/pdf?orgao='
											+ processos[i].orgao,
									method : 'GET'
								}).then(function(response) {
							if (i + 1 < processos.length)
								$timeout(function() {
									$scope.obterJwt(processos, i + 1)
								});
							else
								$timeout($scope.continuarBaixando);
							processos[i].jwt = response.data.jwt;
							processos[i].state = "ready";
						}, function(error) {
							if (i + 1 < processos.length)
								$scope.obterJwt(processos, i + 1);
							processos[i].errormsg = error.data.errormsg;
						});
					}

					$scope.baixando = function() {
						var c = 0;
						var processos = $scope.filtradosEMarcados();
						for (var i = 0; i < processos.length; i++)
							if (processos[i].jwt
									&& [ "set", "go", "in_progress" ]
											.indexOf(processos[i].state) >= 0)
								c++;
						return c;
					}

					$scope.continuarBaixando = function() {
						if ($scope.baixando() == 0)
							if (!$scope.baixarProximo())
								return;
						$scope.atualizar();
					}

					$scope.atualizar = function() {
						chrome.runtime
								.sendMessage(
										$scope.downloadExtensionId,
										{
											command : "updates"
										},
										function(response) {
											$timeout($scope.continuarBaixando,
													500);
											if (response.success) {
												var updates = response.data;
												// console.log(updates);
												for ( var id in updates) {
													if (!updates
															.hasOwnProperty(id))
														continue;
													$scope
															.atualizarProcesso(updates[id]);
												}
											} else {
												console.log("error updating");
											}

										});
					}

					$scope.atualizarProcesso = function(update) {
						var processo = $scope.map[update.id];
						if (update.state && update.state.current)
							processo.state = update.state.current;
						if (update.error)
							processo.errormsg = update.error.current
						if (update.received && update.total)
							processo.perc = Math.round(update.received
									/ update.total * 100);
						if (update.state
								&& update.state.current != 'in_progress')
							delete processo.perc;
						if (update.state && update.state.current == 'complete')
							processo.checked = false;
					}

					$scope.baixarProximo = function() {
						var processos = $scope.filtradosEMarcados();
						for (var i = 0; i < processos.length; i++) {
							if (processos[i].jwt
									&& processos[i].state == "ready") {
								processos[i].state = "set";
								$scope.baixar(processos[i]);
								return true;
							}
						}
						return false;
					}

					$scope.baixar = function(processo) {
						var base = location.href;
						base = base.substring(0, base.indexOf("#!/"));

						// Make a simple request:
						chrome.runtime.sendMessage($scope.downloadExtensionId,
								{
									command : "download",
									url : absoluteUrl(base, 'api/v1/download/'
											+ processo.jwt + '/'
											+ processo.numero
											+ '.pdf?disposition=attachment'),
									filename : processo.numero + '.pdf'
								}, function(response) {
									processo.state = "go";
									processo.downloadId = response.data.id;
									$scope.map[processo.downloadId] = processo;
								});
						logEvento('consulta-processual',
								'mostrar pdf completo', 'em lote');
					}

					$scope.multiplosProcessos = function(arq) {
						ModalService.showModal({
							templateUrl : "resources/dialog-processos.html",
							controller : "MultiplosProcessosController",
							inputs : {
								title : "Múltiplos Processos",
								errormsg : ""
							}
						}).then(function(modal) {
							modal.element.modal();
							modal.close.then(function(result) {
								var arr = result.arrProcessos;
								if (!arr || arr.length == 0)
									return;
								for (var i = 0; i < arr.length; i++) {
									$scope.processos.push({
										numero : arr[i]
									});
								}
								$scope.init();
							});
						});
					}

					$scope.remover = function(p) {
						for (var i = 0; i < $scope.processos.length; i++)
							if (p == $scope.processos[i])
								$scope.processos.splice(i, 1);
					}

					$scope.baixarEmLoteAntigo = function() {
						delete $scope.errormsg;
						ModalService.showModal({
							templateUrl : "resources/dialog-progress.html",
							controller : "BaixarProcessosController",
							inputs : {
								title : "Baixando PDFs de Processos",
								errormsg : "",
								processos : $scope.filtradosEMarcados()
							}
						}).then(function(modal) {
							modal.element.modal();
							modal.close.then(function(result) {
								$timeout($scope.continuarBaixando, 500);
							});
						});
					}

					$scope.init();
				});

appCP.controller('BaixarProcessosController', function($scope, $element,
		$timeout, $http, $window, title, errormsg, processos, close) {
	$scope.title = title;
	$scope.errormsg = errormsg;
	$scope.processos = processos;

	$scope.clickclose = function() {
		$scope.close();
		// Manually hide the modal.
		$element.modal('hide');
	};

	// This close function doesn't need to use jQuery or bootstrap, because
	// the button has the 'data-dismiss' attribute.
	$scope.close = function() {
		// Manually hide the modal.
		$element.modal('hide');
		close({}, 500); // close, but give 500ms for bootstrap to animate
	};

	// This cancel function must use the bootstrap, 'modal' function because
	// the doesn't have the 'data-dismiss' attribute.
	$scope.cancel = function() {
		// Manually hide the modal.
		$element.modal('hide');
		// Now call close, returning control to the caller.
		close({}, 500); // close, but give 500ms for bootstrap to animate
	};

	$scope.iniciar = function() {
		$scope.i = 0;
		$scope.proximo();
	}

	$scope.somenteNumeros = function(s) {
		return s.split('-').join('').split('.').join('');
	}

	$scope.proximo = function() {
		var prox = function() {
			$scope.i++;
			if ($scope.i >= $scope.processos.length)
				return $timeout($scope.cancel, 500);
			$timeout($scope.proximo, 10);
		}

		var processo = $scope.processos[$scope.i];
		delete processo.errormsg;
		delete processo.confirmado;
		$scope.progressbarWidth = 100 * ($scope.i / $scope.processos.length);
		$scope.progressbarCaption = "Baixando " + processo.numero + " ("
				+ ($scope.i + 1) + "/" + $scope.processos.length + ")";

		var indice = $scope.i;
		$http(
				{
					url : 'api/v1/processo/'
							+ $scope.somenteNumeros(processo.numero)
							+ '/pdf?orgao=' + processo.orgao,
					method : 'GET'
				}).then(
				function(response) {
					var jwt = response.data.jwt;
					$window.open('api/v1/download/' + jwt + '/'
							+ processo.numero + '.pdf?disposition=attachment',
							'_self');
					logEvento('consulta-processual', 'mostrar pdf completo',
							'em lote');
					$scope.i = indice;
					prox();
				}, function(error) {
					processo.errormsg = error.data.errormsg;
					$scope.i = indice;
					prox();
				});
	}

	$scope.iniciar();
});

appCP.controller('MultiplosProcessosController', function($scope, $element,
		$timeout, title, errormsg, close) {
	var reProc = /^(\d{7})-?(\d{2})\.?(\d{4})\.?(4)\.?(02)\.?(\d{4})(\d{2})?$/;
	var reSep = /(?:\s+|\s*(?:,|;)\s*)/;

	$scope.pin = null;
	$scope.title = title;
	$scope.errormsg = errormsg;

	$scope.clickclose = function() {
		if (($scope.processos || "") == "") {
			$scope.errormsg = "Números de processos devem ser informados.";
			return;
		}
		$scope.close();
		// Manually hide the modal.
		$element.modal('hide');
	};

	// This close function doesn't need to use jQuery or bootstrap, because
	// the button has the 'data-dismiss' attribute.
	$scope.close = function() {
		if (($scope.processos || "") == "") {
			$scope.errormsg = "Números de processos devem ser informados.";
			return;
		}
		var arr = $scope.processos.split(reSep);
		for (var i = 0; i < arr.length; i++) {
			var m = reProc.exec(arr[i]);
			if (!m) {
				$scope.errormsg = "Número de processo inválido: '" + arr[i]
						+ "'";
				return;
			}
			arr[i] = formatarProcesso(arr[i]);
		}
		// Manually hide the modal.
		$element.modal('hide');
		close({
			processos : $scope.processos,
			arrProcessos : arr
		}, 500); // close, but give 500ms for bootstrap to animate
	};

	// This cancel function must use the bootstrap, 'modal' function because
	// the doesn't have the 'data-dismiss' attribute.
	$scope.cancel = function() {

		// Manually hide the modal.
		$element.modal('hide');

		// Now call close, returning control to the caller.
		close({}, 500); // close, but give 500ms for bootstrap to animate
	};

});

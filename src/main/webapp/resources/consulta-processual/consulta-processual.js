var appCP = angular.module('sample.consulta-processual', [ 'ui.router',
		'angular-storage', 'angular-jwt', 'ngFileUpload',
		'angularModalService', 'ngSanitize' ]);
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

appCP.controller('ConsultaProcessualCtrl', function ConsultaProcessualCtrl(
		$scope, $http, store, jwtHelper, Upload, $timeout, ModalService,
		$stateParams, $state) {
	$scope.avancada = false;

	$scope.mostrarProcesso = function(numero) {
		$state.go('processo', {
			numero : numero
		});
	}

});

appCP
		.controller(
				'ProcessoCtrl',
				function ConsultaProcessualCtrl($scope, $http, store,
						jwtHelper, Upload, $timeout, ModalService,
						$stateParams, $window) {
					$scope.numero = $stateParams.numero;
					$scope.ultimoTexto = false;
					$scope.partes = false;
					$scope.dadosComplementares = false;

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
																$scope.proc = response.data.value;
																$scope
																		.fixProc();
																$scope
																		.getDescriptions();
															},
															function(error) {
																alert(error.data.errormsg);
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
												}, function(error) {
													alert(error.data.errormsg);
												}));

						// Carregar assuntos (partimos do princípio que sempre
						// há um assunto principal e que sempre é o primeiro)
						if (db.assunto && db.assunto.length > 0
								&& Number(db.assunto[0].codigoNacional) > 0) {
							for (var i = 0; i < db.assunto.length; i++) {
								var ass = db.assunto[i];
								$scope.$parent.promise
										.push($http(
												{
													url : 'api/v1/assunto/'
															+ ass.codigoNacional
															+ '?orgao='
															+ $scope.orgao,
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
															alert(error.data.errormsg);
														}));
							}
						}
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
						if (p.dadosBasicos.outroParametro.ultimoTextoMovimento)
							p.fixed.ultimoTextoMovimento = p.dadosBasicos.outroParametro.ultimoTextoMovimento
									.replace(/^\s\s*/, '')
									.replace(/\s\s*$/, '').replace(/\n\s+\n/g,
											'<div class="break"></div>')
									.replace(/\n/g, '<br/>');
						p.fixed.nomeMagistrado = p.dadosBasicos.outroParametro.nomeMagistrado;

						if (p.documento)
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
											mov.documento.push(doc);
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

						if (p.movimento) {
							p.movimento = p.movimento.sort(function(a, b) {
								if (a.dataHora < b.dataHora)
									return 1;
								if (a.dataHora > b.dataHora)
									return -1;
								return 0;
							})

							var odd = false;
							p.fixed.movdoc = [];
							for (var j = 0; j < p.movimento.length; j++) {
								var mov = p.movimento[j];
								odd = !odd;
								p.fixed.movdoc.push({
									teste : true,
									dataHora : mov.dataHora,
									mov : mov,
									doc : (mov.documento || [ {} ])[0],
									rowspan : (mov.documento || [ {} ]).length,
									odd : odd
								});
								if (mov.documento && mov.documento.length > 0) {
									for (var i = 1; i < mov.documento.length; i++) {
										p.fixed.movdoc.push({
											dataHora : mov.dataHora,
											doc : mov.documento[i],
											odd : odd
										});
									}
								}
							}
						}

						if (typeof p.dadosBasicos.valorCausa === 'number')
							p.fixed.valorCausa = "R$ "
									+ p.dadosBasicos.valorCausa.formatMoney(2,
											',', '.');
						p.fixed.dataAjuizamento = $scope
								.formatDDMMYYYHHMM(p.dadosBasicos.dataAjuizamento);

						console.log(p);
					}

					$scope.mostrarUltimoTexto = function() {
						$scope.ultimoTexto = true;
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
								}).then(function(response) {
							var jwt = response.data.jwt;
							$window.open('api/v1/download/' + jwt);
						}, function(error) {
							alert(error.data.errormsg);
						});
					}

					$scope.formatDDMMYYYHHMM = function(s) {
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

					$scope.imprimir = function() {
						window.print();
					}

					$scope.formatProcesso = formatarProcesso;

					$scope.init();
				});

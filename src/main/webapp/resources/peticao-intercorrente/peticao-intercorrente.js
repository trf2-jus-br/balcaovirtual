var appPI = angular.module('sample.peticao-intercorrente',
		[ 'ui.router', 'angular-storage', 'angular-jwt', 'ngFileUpload',
				'angularModalService' ]);
appPI
		.config(function($stateProvider) {
			$stateProvider
					.state(
							'peticao-intercorrente',
							{
								url : '/peticao-intercorrente',
								controller : 'PeticaoIntercorrenteCtrl',
								templateUrl : 'resources/peticao-intercorrente/peticao-intercorrente.html',
								data : {
									requiresLogin : true
								}
							});
		});
appPI
		.controller(
				'PeticaoIntercorrenteCtrl',
				function PeticaoIntercorrenteCtrl($scope, $http, store,
						jwtHelper, Upload, $timeout, ModalService) {
					const
					reProc = /^(\d{7})-?(\d{2})\.?(\d{4})\.?(4)\.?(02)\.?(\d{4})(\d{2})?$/;
					const
					regex = /^(\d{7})-?(\d{2})\.?(\d{4})\.?(4)\.?(02)\.?(\d{4})(\d{2})?/;

					$scope.init = function() {
						$scope.editando = true;
						if (!$scope.tipos)
							$http(
									{
										url : 'api/v1/config/peticao/intercorrente/tipos',
										method : 'GET'
									}).then(function(response) {
								$scope.tipos = response.data.list;
							}, function(error) {
								alert(error.data.errormsg);
							});
					}

					$scope.carregarResumo = function(data) {
						$scope.dataDeProtocolo = data;
						$scope.$parent.promise = $http(
								{
									url : 'api/v1/peticao-intercorrente/listar?data='
											+ data,
									method : 'GET'
								}).then(function(response) {
							$scope.resumoPorData = response.data.list;
						}, function(error) {
							alert(error.data.errormsg);
						});
					}

					$scope.voltarParaQuantidade = function() {
						delete $scope.dataDeProtocolo;
						delete $scope.resumoPorData;
					}

					$scope.carregarProtocoladosRecentemente = function() {
						$scope.$parent.promise = $http({
							url : 'api/v1/peticao-intercorrente/contar',
							method : 'GET'
						}).then(function(response) {
							$scope.quantidadePorData = response.data.list;
						}, function(error) {
							alert(error.data.errormsg);
						});
					}

					$scope.voltarParaEdicao = function() {
						delete $scope.quantidadePorData;
					}

					$scope.validarArquivo = function(arq) {
						var a = arq;
						if (a.processo) {
							var processo = $scope.somenteNumeros(a.processo);
							a.status = "Validando...";
							delete a.errovalidacao;
							a.validando = true;
							a.valido = false;
							$http(
									{
										url : 'api/v1/processo/'
												+ $scope
														.somenteNumeros(a.processo)
												+ "/validar",
										method : 'GET'
									}).then(function(response) {
								var d = response.data;
								a.status = d.unidade + '/' + d.orgao;
								a.orgao = d.orgao;
								a.validando = false;
								a.valido = true;
							}, function(error) {
								a.validando = false;
								a.valido = false;
								a.errormsg = error.data.errormsg;
							});
						}
					}

					$scope.peticionar = function() {
						$scope.editando = false;
						ModalService.showModal({
							templateUrl : "resources/dialog-progress.html",
							controller : "PeticionarController",
							inputs : {
								title : "Protocolando Petições Intercorrentes",
								errormsg : "",
								arquivos : $scope.arquivos
							}
						}).then(function(modal) {
							modal.element.modal();
							modal.close.then(function(result) {
								$scope.organizarArquivos();
							});
						});
					}

					$scope.limpar = function() {
						$scope.editando = true;
						$scope.arquivos = [];
					}

					$scope.imprimir = function() {
						window.print();
					}

					$scope.multiplosProcessos = function(arq) {
						ModalService
								.showModal(
										{
											templateUrl : "resources/dialog-processos.html",
											controller : "ProcessosController",
											inputs : {
												title : "Múltiplos Processos",
												errormsg : ""
											}
										})
								.then(
										function(modal) {
											modal.element.modal();
											modal.close
													.then(function(result) {
														var arr = result.arrProcessos;
														if (!arr
																|| arr.length == 0)
															return;
														arq.processo = $scope
																.getProcesso($scope
																		.somenteNumeros(arr[0]));
														for (var i = 1; i < arr.length; i++) {
															var newArq = {
																file : arq.file,
																nome : arq.nome,
																processo : $scope
																		.getProcesso($scope
																				.somenteNumeros(arr[i])),
																bloq : arq.bloq,
																perc : arq.perc,
																size : arq.size,
																id : arq.id

															};
															$scope.arquivos
																	.push(newArq);
														}
														$scope
																.organizarArquivos();
														for (var i = 0; i < $scope.arquivos.length; i++) {
															var a = $scope.arquivos[i];
															if (a.file === arq.file) {
																delete a.status;
																$scope
																		.validarArquivo(a);
															}
														}
													});
										});
					}

					$scope.somenteNumeros = function(s) {
						return s.split('-').join('').split('.').join('');
					}

					$scope.alterarArquivo = function(arq) {
						var m = reProc.exec(arq.processo);
						if (!m) {
							arq.status = "Número de processo inválido";
							$scope.organizarArquivos();
							return;
						}
						arq.processo = $scope.getProcesso($scope
								.somenteNumeros(arq.processo));
						$scope.validarArquivo(arq);
						$scope.organizarArquivos();
					}

					$scope.removerArquivo = function(arq) {
						for (var i = 0; i < $scope.arquivos.length; i++)
							if (arq == $scope.arquivos[i])
								$scope.arquivos.splice(i, 1);
						$scope.organizarArquivos();
					}

					$scope.segredos = [ {
						codigo : 0,
						nome : 'Não'
					}, {
						codigo : 1,
						nome : 'Sim'
					} ];
					$scope.arquivos = [];

					$scope.selecionarTipo = function(arq, tipo) {
						arq.tipodescr = $scope.descricaoTipoPorCodigo(tipo);
						for (var i = 0; i < $scope.arquivos.length; i++) {
							var a = $scope.arquivos[i];
							if (a !== arq && !a.tipo) {
								a.tipo = tipo;
								a.tipodescr = arq.tipodescr;
							}
						}
					}

					$scope.descricaoTipoPorCodigo = function(tipo) {
						for (var i = 0; i < $scope.tipos.length; i++)
							if (($scope.tipos[i].orgao + '-' + $scope.tipos[i].id) == tipo)
								return $scope.tipos[i].descricao;
						return tipo;
					}

					$scope.selecionarSegredo = function(arq, segredo) {
						for (var i = 0; i < $scope.arquivos.length; i++) {
							var a = $scope.arquivos[i];
							if (a !== arq && a.segredo === undefined)
								a.segredo = segredo;
						}
					}

					$scope.organizarArquivos = function() {
						$scope.arquivosAProtocolar = 0;
						$scope.arquivos.sort(function(a, b) {
							if (a.processo && !b.processo)
								return -1;
							if (!a.processo && b.processo)
								return 1;
							if (a.processo != b.processo)
								return a.processo < b.processo ? -1 : 1;
							// if (a.bloq != b.bloq)
							// return a.bloq ? -1 : 1;
							if (a.nome != b.nome)
								return a.nome.replace(".pdf", "") < b.nome
										.replace(".pdf", "") ? -1 : 1;
							return 0;
						});

						var arq = {
							odd : false
						};
						var c = 0;

						for (var i = 0; i < $scope.arquivos.length; i++) {
							var a = $scope.arquivos[i];
							a.anexo = a.processo !== undefined
									&& a.processo == arq.processo;
							if (a.anexo) {
								arq.rowspan = arq.rowspan + 1;
								a.tipo = arq.tipo;
								a.segredo = arq.segredo;
								a.odd = arq.odd;
							} else {
								a.rowspan = 1;
								a.odd = !arq.odd;
								arq = a;
							}
							if (a.protocolado !== true && !a.anexo)
								$scope.arquivosAProtocolar++;
						}
					}

					$scope.$watch('files', function() {
						$scope.upload($scope.files);
					});
					$scope.$watch('file', function() {
						if ($scope.file != null) {
							$scope.files = [ $scope.file ];
						}
					});
					$scope.log = '';

					$scope.getProcesso = function(filename) {
						var m = regex.exec(filename);
						if (!m)
							return;
						var s = m[1] + '-' + m[2] + '.' + m[3] + '.' + m[4]
								+ '.' + m[5] + '.' + m[6];
						if (m[7])
							s += m[7];
						return s;
					}

					$scope.upload = function(files) {
						if (files && files.length) {
							for (var i = 0; i < files.length; i++) {
								var file = files[i];

								var proc = $scope.getProcesso(file.name);

								$scope.arquivos.push({
									file : file,
									nome : file.name,
									processo : proc,
									bloq : (proc ? true : false)
								});
								if (!file.$error) {
									Upload
											.upload({
												url : 'api/upload',
												data : {
													username : $scope.username,
													file : file
												}
											})
											.then(
													function(resp) {
														$timeout(function() {
															var arq = $scope
																	.arquivo(resp.config.data.file);
															arq.size = resp.data.size;
															arq.id = resp.data.id;
															$scope
																	.validarArquivo(arq);
														});
													},
													null,
													function(evt) {
														$scope
																.arquivo(evt.config.data.file).perc = parseInt(100.0
																* evt.loaded
																/ evt.total);
													});
								}
							}
							$scope.organizarArquivos();
						}
					};

					$scope.arquivo = function(file) {
						for (var j = 0; j < $scope.arquivos.length; j++) {
							if ($scope.arquivos[j].file == file)
								return $scope.arquivos[j];
						}
					}

					$scope.isAllValid = function() {
						for (var j = 0; j < $scope.arquivos.length; j++) {
							if (!$scope.arquivos[j].size)
								return false;
							if (!$scope.arquivos[j].processo)
								return false;
							if (!$scope.arquivos[j].tipo)
								return false;
							if ($scope.arquivos[j].segredo === undefined)
								return false;
						}
						return true;
					}

					$scope.formatDDMMYYY = function(s) {
						if (!s)
							return;
						var r = s.substring(8, 10) + '/' + s.substring(5, 7)
								+ '/' + s.substring(0, 4);
						return r;
					}

					$scope.formatDDMMYYYHHMM = function(s) {
						if (!s)
							return;
						var r = s.substring(8, 10) + '/' + s.substring(5, 7)
								+ '/' + s.substring(0, 4) + '&nbsp;'
								+ s.substring(11, 13) + ':'
								+ s.substring(14, 16);
						return r;
					}

					$scope.jwt = store.get('jwt');
					$scope.decodedJwt = $scope.jwt
							&& jwtHelper.decodeToken($scope.jwt);
					$scope.init();
				});

appPI.controller('ProcessosController', function($scope, $element, $timeout,
		title, errormsg, close) {
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

appPI.controller('PeticionarController', function($scope, $element, $timeout,
		$http, title, errormsg, arquivos, close) {
	$scope.title = title;
	$scope.errormsg = errormsg;
	$scope.arquivos = arquivos;

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
			if ($scope.i >= $scope.arquivos.length)
				$scope.cancel();
			$timeout($scope.proximo, 10);
		}

		var arq = $scope.arquivos[$scope.i];
		if (arq.protocolado) {
			prox();
			return;
		}
		delete arq.errormsg;
		delete arq.protocolado;
		$scope.progressbarWidth = 100 * ($scope.i / $scope.arquivos.length);
		$scope.progressbarCaption = "Enviando " + arq.processo + " ("
				+ ($scope.i + 1) + "/" + $scope.arquivos.length + ")";

		var indice = $scope.i;
		var pdfs = arq.id;
		while (indice < $scope.arquivos.length - 1) {
			if (!$scope.arquivos[indice + 1].anexo)
				break;
			indice++;
			pdfs += ',' + $scope.arquivos[indice].id;
		}

		$http(
				{
					url : 'api/v1/processo/'
							+ $scope.somenteNumeros(arq.processo)
							+ '/peticionar',
					method : "POST",
					data : {
						orgao : arq.orgao,
						tipopeticao : arq.tipo,
						nivelsigilo : arq.segredo,
						pdfs : pdfs
					}
				}).then(function(response) {
			for (var i = $scope.i; i <= indice; i++) {
				$scope.arquivos[i].status = response.data.status;
				$scope.arquivos[i].protocolado = true;
			}
			$scope.i = indice;
			prox();
		}, function(error) {
			for (var i = $scope.i; i <= indice; i++)
				$scope.arquivos[i].errormsg = error.data.errormsg;
			$scope.i = indice;
			prox();
		});
	}
	$scope.iniciar();
});

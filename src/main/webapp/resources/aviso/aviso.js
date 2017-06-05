var appPI = angular.module('sample.aviso', [ 'ui.router', 'angular-storage',
		'angular-jwt', 'angularModalService' ]);

appPI.config(function($stateProvider) {
	$stateProvider.state('lista-avisos', {
		url : '/lista-avisos',
		controller : 'ListarAvisosCtrl',
		templateUrl : 'resources/aviso/lista-avisos.html',
		data : {
			requiresLogin : true
		}
	});
});

appPI
		.controller(
				'ListarAvisosCtrl',
				function ConsultarAvisosCtrl($scope, $http, $filter, store,
						jwtHelper, $timeout, $state, ModalService) {
					$scope.outlineAtivo = false;
					$scope.gui = {
						todos : true,
						filtro : "",
						filtroOutline : function() {
							return function(item) {
								return $scope.outlineMap[item.filtro].ativo;
							};
						}
					};
					$scope.montarOutline = function(arr) {
						var outline = [];
						var map = {};
						for (var i = 0; i < arr.length; i++) {
							var a = arr[i]
							var ko = a.orgao;
							if (!map[ko]) {
								var o = {
									nome : a.orgao,
									filtro : ko
								};
								outline.push(o);
								map[ko] = o;
							}

							var kl = ko + ';' + a.localidade;
							if (!map[kl]) {
								var o = {
									nome : a.localidade,
									filtro : kl
								};
								if (!map[ko].item)
									map[ko].item = [];
								map[ko].item.push(o);
								map[kl] = o;
							}

							var kt = kl + ';' + a.unidadetipo;
							if (!map[kt]) {
								var o = {
									nome : a.unidadetipo,
									filtro : kt
								};
								if (!map[kl].item)
									map[kl].item = [];
								map[kl].item.push(o);
								map[kt] = o;
							}
							a.filtro = kt;
						}
						$scope.outlineMap = map;
						$scope.outline = outline;

						for (var i = 0; i < $scope.outline.length; i++) {
							$scope.outlineInitPai($scope.outline[i]);
							$scope.outline[i].ativo = true;
							$scope.toogle($scope.outline[i]);
						}
					}

					$scope.outlineInitPai = function(outline) {
						if (!outline.item)
							return;
						for (var i = 0; i < outline.item.length; i++) {
							outline.item[i].pai = outline;
							$scope.outlineInitPai(outline.item[i]);
						}
					}

					$scope.toogle = function(outline) {
						if (outline.ativo) {
							$scope.tooglePai(outline, outline.ativo);
						} else {
							$scope.toogleUncheckParentIfEmpty(outline);
						}
						if (!outline.item)
							return;
						for (var i = 0; i < outline.item.length; i++) {
							outline.item[i].ativo = outline.ativo;
							$scope.toogle(outline.item[i]);
						}
					}

					$scope.tooglePai = function(outline, ativo) {
						if (!outline.pai)
							return;
						outline.pai.ativo = ativo;
						$scope.tooglePai(outline.pai, ativo);
					}

					$scope.toogleUncheckParentIfEmpty = function(outline) {
						if (!outline.pai)
							return;
						for (var i = 0; i < outline.pai.item.length; i++) {
							if (outline.pai.item[i].ativo)
								return;
						}
						outline.pai.ativo = false;
						$scope.toogleUncheckParentIfEmpty(outline.pai);
					}

					$scope.init = function() {
						delete $scope.errormsg;
						$scope.$parent.promise = $http({
							url : 'api/v1/aviso/listar',
							method : 'GET'
						})
								.then(
										function(response) {
											for (var i = 0; i < response.data.status.length; i++) {
												if (response.data.status[i].errormsg) {
													if ($scope.errormsg === undefined)
														$scope.errormsg = '';
													else
														$scope.errormsg += '; ';
													$scope.errormsg += response.data.status[i].system
															+ ': '
															+ response.data.status[i].errormsg;
												}
											}
											$scope.avisos = response.data.list;
											if ($scope.avisos) {
												for (var i = 0; i < $scope.avisos.length; i++) {
													$scope.avisos[i].checked = true;
													$scope.avisos[i].disabled = false;
													$scope.avisos[i].processoFormatado = formatarProcesso($scope.avisos[i].processo);
													$scope.avisos[i].dataavisoFormatada = $scope
															.formatDDMMYYYYHHMM($scope.avisos[i].dataaviso);
												}
												$scope
														.montarOutline($scope.avisos);
											}
											logEvento('aviso', 'listar avisos');
										},
										function(error) {
											// $scope.avisos = {
											// "list" : [
											// {
											// "idaviso" :
											// "17052213081705221319",
											// "dataaviso" : "20170522131900",
											// "tipo" : "Intimação",
											// "processo" :
											// "05050453120154025101",
											// "unidade" : "12VFEF",
											// "unidadetipo" : "Execução
											// Fiscal",
											// "orgao" : "JFRJ",
											// "localidade" : "Rio de Janeiro"
											// },
											// {
											// "idaviso" :
											// "17052415461705241550",
											// "dataaviso" : "20170524155000",
											// "tipo" : "Intimação",
											// "processo" :
											// "00090434020174025151",
											// "unidade" : "09JEF",
											// "unidadetipo" : "Juizado Especial
											// Previdenciário",
											// "orgao" : "JFRJ",
											// "localidade" : "Rio de Janeiro"
											// } ]
											// }.list;
											// if ($scope.avisos) {
											// for (var i = 0; i <
											// $scope.avisos.length; i++) {
											// $scope.avisos[i].checked = true;
											// $scope.avisos[i].disabled =
											// false;
											// $scope.avisos[i].processoFormatado
											// =
											// formatarProcesso($scope.avisos[i].processo);
											// $scope.avisos[i].dataavisoFormatada
											// = $scope
											// .formatDDMMYYYYHHMM($scope.avisos[i].dataaviso);
											// }
											// $scope
											// .montarOutline($scope.avisos);
											// }
											$scope.errormsg = error.data.errormsg;
										});
					}

					$scope.formatDDMMYYYYHHMM = function(s) {
						var r = s.substring(6, 8) + '/' + s.substring(4, 6)
								+ '/' + s.substring(0, 4) + '&nbsp;'
								+ s.substring(8, 10) + ':'
								+ s.substring(10, 12);
						return r;
					}

					$scope.mostrarOutline = function() {
						$scope.outlineAtivo = !$scope.outlineAtivo;
						if (!$scope.outlineAtivo) {
							for ( var property in $scope.outlineMap) {
								if ($scope.outlineMap.hasOwnProperty(property)) {
									$scope.outlineMap[property].ativo = true;
								}
							}

						}
					}

					$scope.confirmarAviso = function(aviso) {
						delete $scope.errormsg;

						$scope.$parent.promise = $http(
								{
									url : 'api/v1/processo/' + aviso.processo
											+ '/aviso/' + aviso.idaviso
											+ '/receber?orgao=' + aviso.orgao,
									method : 'POST'
								}).then(function(response) {
							var d = response.data;
							aviso.teor = formatarTexto(d.teor);
							aviso.datarecebimento = d.datarecebimento;
							aviso.confirmado = true;
							aviso.checked = false;
							aviso.disabled = true;
							$scope.aviso = aviso;
							logEvento('aviso', 'confirmar', 'singular');
						}, function(error) {
							// var d = {
							// "idaviso" : "17020213331702021339",
							// "dataaviso" : "201705240000",
							// "processo" : "00102077020174020000",
							// "orgao" : "TRF2",
							// "teor" : "TEOR",
							// "datarecebimento" : "17/05/2017 09:21"
							// };
							// aviso.teor = $scope.formatTexto(d.teor);
							// aviso.datarecebimento = d.datarecebimento;
							// aviso.confirmado = true;
							// aviso.checked = false;
							// aviso.disabled = true;
							// $scope.aviso = aviso;
							aviso.errormsg = error.data.errormsg;
						});
					}

					$scope.exibirAviso = function(aviso) {
						$scope.aviso = aviso;
					}

					$scope.voltar = function() {
						delete $scope.aviso;
					}

					$scope.filtrados = function() {
						var docs = $filter('filter')($scope.avisos || [],
								$scope.gui.filtro);
						docs = $filter('filter')(docs,
								$scope.gui.filtroOutline());
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

					$scope.formatTexto = function(s) {
						return s.replace(/^\s\s*/, '').replace(/\s\s*$/, '')
								.replace(/\n\s+\n/g,
										'<div class="break"></div>').replace(
										/\n/g, '<br/>')
					}

					$scope.imprimir = function() {
						window.print();
					}

					$scope.confirmarEmLote = function() {
						delete $scope.errormsg;
						ModalService.showModal({
							templateUrl : "resources/dialog-progress.html",
							controller : "ConfirmarAvisosController",
							inputs : {
								title : "Confirmando Intimações/Citações",
								errormsg : "",
								avisos : $scope.filtradosEMarcados()
							}
						}).then(function(modal) {
							modal.element.modal();
							modal.close.then(function(result) {
							});
						});
					}

					$scope.listarProcessos = function() {
						var avisos = $scope.filtradosEMarcados();
						var map = {};
						var processos = [];
						for (var i = 0; i < avisos.length; i++) {
							if (map.hasOwnProperty(avisos[i].processo))
								continue;
							map[avisos[i].processo] = true;
							processos.push({
								numero : formatarProcesso(avisos[i].processo),
								orgao : avisos[i].orgao,
								unidade : avisos[i].unidade,
								validado : true
							})
						}
						$state.go('lista-processos', {
							processos : processos
						});
					}

					$scope.init();
				});

appPI.controller('ConfirmarAvisosController', function($scope, $element,
		$timeout, $http, title, errormsg, avisos, close) {
	$scope.title = title;
	$scope.errormsg = errormsg;
	$scope.avisos = avisos;

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
			if ($scope.i >= $scope.avisos.length)
				return $scope.cancel();
			$timeout($scope.proximo, 10);
		}

		var aviso = $scope.avisos[$scope.i];
		if (aviso.confirmado) {
			prox();
			return;
		}
		delete aviso.errormsg;
		delete aviso.confirmado;
		$scope.progressbarWidth = 100 * ($scope.i / $scope.avisos.length);
		$scope.progressbarCaption = "Enviando " + aviso.processo + " ("
				+ ($scope.i + 1) + "/" + $scope.avisos.length + ")";

		var indice = $scope.i;

		$http(
				{
					url : 'api/v1/processo/'
							+ $scope.somenteNumeros(aviso.processo) + '/aviso/'
							+ aviso.idaviso + '/receber?orgao=' + aviso.orgao,
					method : 'POST'
				}).then(function(response) {
			var d = response.data;
			aviso.teor = formatarTexto(d.teor);
			aviso.datarecebimento = d.datarecebimento;
			aviso.confirmado = true;
			aviso.checked = false;
			aviso.disabled = true;
			logEvento('aviso', 'confirmar', 'em lote');
			$scope.i = indice;
			prox();
		}, function(error) {
			// var d = {
			// "idaviso" : "17020213331702021339",
			// "dataaviso" : "201705240000",
			// "processo" : "00102077020174020000",
			// "orgao" : "TRF2",
			// "teor" : "TEOR",
			// "datarecebimento" : "17/05/2017 09:21"
			// };
			// aviso.teor = formatarTexto(d.teor);
			// aviso.datarecebimento = d.datarecebimento;
			// aviso.confirmado = true;
			// aviso.checked = false;
			// aviso.disabled = true;

			aviso.errormsg = error.data.errormsg;
			$scope.i = indice;
			prox();
		});
	}

	$scope.iniciar();
});
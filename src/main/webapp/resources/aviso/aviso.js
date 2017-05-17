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
				function ConsultarAvisosCtrl($scope, $http, store, jwtHelper,
						$timeout, ModalService) {

					$scope.outlineAtivo = false;

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

					$scope.filtroOutline = function() {
						return function(item) {
							return $scope.outlineMap[item.filtro].ativo;
						};
					};

					$scope.init = function() {
						$scope.$parent.promise = $http({
							url : 'api/v1/aviso/listar',
							method : 'GET'
						})
								.then(
										function(response) {
											$scope.avisos = response.data.list;
											$scope.avisosx = [ {
												orgao : 'TRF2',
												localidade : 'Rio de Janeiro',
												unidadetipo : 'Vara Criminal'
											}, {
												orgao : 'TRF2',
												localidade : 'Rio de Janeiro',
												unidadetipo : 'Vara Cível'
											}, ];
											if ($scope.avisos) {
												for (var i = 0; i < $scope.avisos.length; i++)
													$scope.avisos[i].processoFormatado = formatarProcesso($scope.avisos[i].processo);
												$scope
														.montarOutline($scope.avisos);
											}
										}, function(error) {
											alert(error.data.errormsg);
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
					}

					$scope.init();
				});

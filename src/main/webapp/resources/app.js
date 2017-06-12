//var sample = angular.module('sample', [ 'sample.home', 'sample.login',
//		'sample.signup', 'angular-jwt', 'angular-storage' ]);
//
//sample.config(function myAppConfig($urlRouterProvider, jwtInterceptorProvider,
//		$httpProvider) {
//	$urlRouterProvider.otherwise('/');
//
//	jwtInterceptorProvider.tokenGetter = function(store) {
//		return store.get('jwt');
//	}
//
//	$httpProvider.interceptors.push('jwtInterceptor');
//});
//
//sample.run(function($rootScope, $state, store, jwtHelper) {
//	$rootScope.$on('$stateChangeStart',
//			function(e, to) {
//				if (to.data && to.data.requiresLogin) {
//					if (!store.get('jwt')
//							|| jwtHelper.isTokenExpired(store.get('jwt'))) {
//						e.preventDefault();
//						$state.go('login');
//					}
//				}
//			});
//});

//sample.controller('AppCtrl', function AppCtrl($scope, $location) {
//	$scope.$on('$routeChangeSuccess',
//			function(e, nextRoute) {
//				if (nextRoute.$$route
//						&& angular.isDefined(nextRoute.$$route.pageTitle)) {
//					$scope.pageTitle = nextRoute.$$route.pageTitle
//							+ ' | Balcão Virtual';
//				}
//			});
//});

var app = angular.module('app', [ 'sample.peticao-intercorrente',
		'sample.aviso', 'sample.consulta-processual', 'sample.login',
		'sample.signup', 'angularModalService', 'ngAnimate', 'cgBusy',
		'angular-jwt', 'angular-storage', 'ngFileUpload' ]);

app.config(function($stateProvider, $urlRouterProvider, jwtInterceptorProvider,
		$httpProvider) {

	$urlRouterProvider.otherwise('/login');

	jwtInterceptorProvider.tokenGetter = function(store) {
		return store.get('jwt');
	}

	$httpProvider.interceptors.push('jwtInterceptor');

	$httpProvider.interceptors.push(function($timeout, $q, $injector) {
		var loginModal, $http, $state;

		// this trick must be done so that we don't receive
		// `Uncaught Error: [$injector:cdep] Circular dependency found`
		$timeout(function() {
			loginModal = $injector.get('loginModal');
			$http = $injector.get('$http');
			$state = $injector.get('$state');
		});

		return {
			responseError : function(rejection) {
				if (rejection.status !== 401) {
					return rejection;
				}

				var deferred = $q.defer();

				loginModal().then(function() {
					deferred.resolve($http(rejection.config));
				}, function() {
					//$state.go('login');
					deferred.reject(rejection);
				});

				return deferred.promise;
			}
		};
	});

	var sugestoesState = {
		controller : 'ctrlSugerir',
		name : 'sugestoes',
		url : '/sugestoes',
		templateUrl : 'resources/sugestoes.html',
		data : {
			requiresLogin : false
		}
	}

	var sobreState = {
		controller : 'ctrlSobre',
		name : 'sobre',
		url : '/sobre',
		templateUrl : 'resources/sobre.html',
		data : {
			requiresLogin : false
		}
	}

	$stateProvider.state(sugestoesState);
	$stateProvider.state(sobreState);
});

app.run(function($rootScope, $state, $timeout, store, jwtHelper, $injector,
		ModalService, loginModal) {
	$rootScope.$on('$stateChangeStart', function(e, toState, toParams) {

		var logged = store.get('jwt')
				&& !jwtHelper.isTokenExpired(store.get('jwt'));
		if (toState.data && toState.data.requiresLogin) {
			if (!logged) {
				e.preventDefault();
				loginModal().then(function() {
					$state.go(toState.name, toParams);
				}, function(error) {
					$state.go('sobre');
				});
			}
		} else if (logged && toState.name == 'login') {
			e.preventDefault();
			$state.go('consulta-processual');
		}
	});

	$rootScope.updateLogged = function() {
		var logged = true;
		var jwt = store.get('jwt');
		if (!jwt || jwtHelper.isTokenExpired(store.get('jwt'))) {
			logged = false;
			delete $rootScope.jwt;
		} else {
			var tokenPayload = jwtHelper.decodeToken(jwt);
			$rootScope.jwt = tokenPayload;
		}
		$rootScope.logged = logged;
	}
	$rootScope.updateLogged();

	$rootScope.hiddenAlert = {
		timeline : store.get('alert-timeline') == 'hide'
	}

	$rootScope.hideAlert = function(id) {
		store.set('alert-' + id, 'hide');
		$timeout(function() {
			$rootScope.hiddenAlert[id] = true;
		}, 1000);
	}
});

app.controller('routerCtrl', function($rootScope, $scope, $http, $window, $q,
		$location, $timeout, store) {
	$scope.promise = [];

	$scope.urlBaseAPI = "./api/v1";
	$scope.init = function() {
		$scope.promise = [];

		$http({
			url : $scope.urlBaseAPI + '/test?skip=all',
		}).then(function(response) {
			$rootScope.test = response.data;
		}, function(error) {
			$scope.message("Erro", error.data.errormsg);
		});
	}

	$timeout($scope.init, 10);
});

// Sugestoes
app.controller('ctrlSugerir', function($scope, $http, $templateCache,
		$interval, $window, ModalService) {
	$scope.fdSugerir = function() {
		if (!$scope.sugestao)
			return "";
		var obj = {
			nome : $scope.sugestao.nome,
			email : $scope.sugestao.email,
			mensagem : $scope.sugestao.mensagem
		};
		return formdata(obj);
	}

	$scope.sugerir = function() {
		$scope.$parent.promise = [];
		$scope.$parent.promise.push($http({
			url : $scope.urlBaseAPI + '/sugestao',
			method : "POST",
			data : $scope.fdSugerir(),
			headers : {
				'Content-Type' : 'application/x-www-form-urlencoded'
			}
		}).then(
				function(data, status, headers, config) {
					$scope.message("Sucesso",
							"Sua mensagem foi enviada. Muito obrigado!");
					$scope.sugestao = {};
				}, function(error) {
					$scope.message("Erro", error.data.errormsg);
				}));

		$scope.message = function(title, message) {
			ModalService.showModal({
				templateUrl : "resources/dialog-message.html",
				controller : "DismissController",
				inputs : {
					title : title,
					message : message
				}
			}).then(function(modal) {
				modal.element.modal();
			});
		};

	}

});

app.controller('ctrlSignIn', function($scope) {
});

// Sugestoes
app.controller('ctrlSobre', function($scope) {
});

app.controller('DismissController', function($scope, title, message, close) {
	$scope.title = title;
	$scope.message = message;

	$scope.dismissModal = function(result) {
		close(result, 200);
	};

});

app
		.controller(
				'ctrl',
				function($scope, $http, $interval, $window, $location, $filter,
						$timeout, $routeParams, ModalService) {

					$scope.composeErrorMessage = function(errordata) {
						var msg = "Erro.";
						try {
							if (errordata.hasOwnProperty("errordetails")) {
								var detail = {
									presentable : false,
									logged : false
								};
								if (errordata.hasOwnProperty("errordetails")
										&& errordata.errordetails.length > 0) {
									detail = errordata.errordetails[errordata.errordetails.length - 1];
									msg = "Não foi possível " + detail.context;
								}
								if (errordata.hasOwnProperty("errormsg")
										&& detail.presentable)
									msg = errordata.errormsg;
								if (detail.logged)
									msg += ", a TI já foi notificada.";
							} else if (errordata.hasOwnProperty("errormsg")) {
								msg = errormsg;
							}
						} catch (err) {

						}
						return msg;
					}

					$scope.presentError = function(id) {
						$scope.showErrorDetails = true;
						$scope.currentErrorId = id;
					}

					$scope.setError = function(response) {
						if (response === undefined) {
							delete $scope.errorDetails.geral;
							return;
						}
						var data;
						if (typeof response === 'string')
							data = {
								errormsg : response
							};
						else {
							data = response.data;
							if (response.data == null
									&& typeof response.statusText === 'string'
									&& response.statusText != '')
								data = {
									errormsg : response.statusText
								};
							else if (response.data == null
									&& typeof response.status === 'number')
								data = {
									errormsg : "http status " + response.status
								};
							else if (data != null
									&& (typeof data.errormsg == 'string')
									&& data.errormsg.lastIndexOf(
											"O conjunto de chaves não", 0) === 0)
								data.errormsg = $scope.errorMsgMissingCertificate;
						}
						$scope.errorDetails.geral = data;
					}
				});

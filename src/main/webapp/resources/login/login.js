var appLogin = angular.module('sample.login',
		[ 'ui.router', 'angular-storage' ]);

appLogin.config(function($stateProvider) {
	$stateProvider.state('login', {
		url : '/login',
		controller : 'LoginCtrl',
		templateUrl : 'resources/login/login.html',
		resolve : {
			$title : function() {
				return 'Login';
			}
		}
	});
	$stateProvider.state('logout', {
		url : '/logout',
		controller : 'LogoutCtrl',
		resolve : {
			$title : function() {
				return 'Logout';
			}
		}
	});
});

appLogin.controller('LoginCtrl', function LoginController($rootScope, $scope,
		$http, store, $state) {
	$scope.user = {};
	$scope.login = function() {
		$scope.$parent.promise = $http({
			url : 'api/v1/sessions/create',
			method : 'POST',
			data : $scope.user
		}).then(function(response) {
			store.set('jwt', response.data.id_token);
			$rootScope.updateLogged();
			$state.go('consulta-processual');
		}, function(error) {
			$scope.errormsg = error.data.errormsg;
		});
	}

})

appLogin.controller('LogoutCtrl', function LoginController($rootScope, $scope,
		$http, store, $state) {
	store.remove('jwt');
	$rootScope.updateLogged();
	$state.go('login');
});

appLogin.service('loginModal', function($q, $rootScope, ModalService) {
	return function() {
		return $q(function(resolve, reject) {
			ModalService.showModal({
				templateUrl : "resources/login/dialog-login.html",
				controller : "LoginDialogController",
				inputs : {
					title : "Login",
					errormsg : ""
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					if (result.logged)
						resolve();
					else
						reject();
				});
			}).catch(function(error) {
				reject();
				  // error contains a detailed error message.
				  console.log(error);
				});
		});
	};

});

appLogin.controller('LoginDialogController', function($scope, $element,
		$timeout, $http, store, $rootScope, errormsg, close) {
	$scope.logged = false;
	$scope.login = function() {
		$scope.$parent.promise = $http({
			url : 'api/v1/sessions/create',
			method : 'POST',
			data : $scope.user
		}).then(function(response) {
			store.set('jwt', response.data.id_token);
			$rootScope.updateLogged();
			logged = true;

			// Manually hide the modal.
			$element.modal('hide');
			close({
				logged : true
			}, 500); // close, but give 500ms for bootstrap to animate
		}, function(error) {
			$scope.errormsg = error.data.errormsg;
		});
	}

	$scope.errormsg = errormsg;

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
});

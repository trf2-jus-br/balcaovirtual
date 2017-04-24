angular.module('sample.login', [ 'ui.router', 'angular-storage' ]).config(
		function($stateProvider) {
			$stateProvider.state('login', {
				url : '/login',
				controller : 'LoginCtrl',
				templateUrl : 'resources/login/login.html'
			});
			$stateProvider.state('logout', {
				url : '/logout',
				controller : 'LogoutCtrl'
			});
		}).controller('LoginCtrl',
		function LoginController($rootScope, $scope, $http, store, $state) {

			$scope.user = {};

			$scope.login = function() {
				$http({
					url : 'api/v1/sessions/create',
					method : 'POST',
					data : $scope.user
				}).then(function(response) {
					store.set('jwt', response.data.id_token);
					$rootScope.updateLogged();
					$state.go('peticao-intercorrente');
				}, function(error) {
					$scope.errormsg = error.data.errormsg;
				});
			}

		}).controller('LogoutCtrl',
		function LoginController($rootScope, $scope, $http, store, $state) {
			store.remove('jwt');
			$rootScope.updateLogged();
			$state.go('peticao-intercorrente');
		});

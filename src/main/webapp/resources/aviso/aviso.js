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
appPI.controller('ListarAvisosCtrl', function ConsultarAvisosCtrl($scope,
		$http, store, jwtHelper, $timeout, ModalService) {

	$scope.init = function() {
		$scope.$parent.promise = $http({
			url : 'api/v1/aviso/listar',
			method : 'GET'
		}).then(function(response) {
			$scope.avisos = response.data.list;
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


	$scope.init();
});

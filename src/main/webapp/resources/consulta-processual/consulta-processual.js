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
		$stateParams) {
	$scope.avancada = false;
	$scope.numero = '0099602-77.2016.4.02.5151';
});

appCP.controller('ProcessoCtrl', function ConsultaProcessualCtrl($scope, $http,
		store, jwtHelper, Upload, $timeout, ModalService, $stateParams) {
	$scope.numero = $stateParams.numero;

	$scope.init = function() {
		var numero = somenteNumeros($scope.numero);
		$scope.$parent.promise = $http({
			url : 'api/v1/processo/' + numero + "/validar",
			method : 'GET'
		}).then(
				function(response) {
					$scope.$parent.promise = $http(
							{
								url : 'api/v1/processo/' + numero
										+ '/consultar?orgao='
										+ response.data.orgao,
								method : 'GET'
							}).then(function(response) {
						$scope.proc = response.data.value;
						$scope.fixProc();
					}, function(error) {
						alert(error.data.errormsg);
					});
				}, function(error) {
					alert(error.data.errormsg);
				});
	}

	$scope.fixProc = function() {
		var p = $scope.proc;

		p.dadosBasicos.numero = formatarProcesso(p.dadosBasicos.numero);

		for (var i = 0; i < p.dadosBasicos.polo.length; i++) {
			if (p.dadosBasicos.polo[i].polo == 'AT')
				p.partesAtivas = p.dadosBasicos.polo[i].parte;
			if (p.dadosBasicos.polo[i].polo == 'PA')
				p.partesPassivas = p.dadosBasicos.polo[i].parte;
		}

		console.log(p);
	}

	$scope.formatDDMMYYYHHMM = function(s) {
		var r = s.substring(6, 8) + '/' + s.substring(4, 6) + '/'
				+ s.substring(0, 4) + ' ' + s.substring(8, 10) + ':'
				+ s.substring(10, 12);
		r = r.replace(" ", "&nbsp;");
		return r;
	}

	$scope.formatProcesso = formatarProcesso;

	$scope.init();
});

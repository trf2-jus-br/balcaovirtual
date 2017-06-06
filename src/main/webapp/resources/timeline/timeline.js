angular.module('sample.timeline', [ 'ui.router', 'angular-storage' ]).config(
		function($stateProvider) {
			$stateProvider.state('timeline', {
				url : '/timeline',
				controller : 'TimelineCtrl',
				templateUrl : 'resources/timeline/timeline.html'
			});
		}).controller('TimelineCtrl',
		function TimelineController($rootScope, $scope, $http, store, $state) {

		});

var updateTimeline = function(processo) {
	var contains = function(m, descr) {
		return m.movimentoLocal.descricao.indexOf(descr) == 0;
	}

	var timeline = {
		autuacao : {
			passou : true
		},
		distribuicao : {},
		primeirodespacho : {},
		remessacarga : {},
		perito : {},
		audiencia : {},
		conclusao : {},
		sentenca : {},
		suspensao : {},
		apelacao : {},
		baixa : {},
		arquivamento : {},
	};

	var primeirodespacho = false;
	var movs = processo.movimento;
	var prev;
	for (var i = movs.length - 1; i >= 0; i--) {
		var e = undefined;
		var m = movs[i];
		if (!m.movimentoLocal)
			continue;
		if (contains(m, "Distribuição"))
			e = timeline.distribuicao;
		if (contains(m, "Conclusão")) {
			if (!primeirodespacho) {
				e = timeline.primeirodespacho;
				primeirodespacho = true;
			} else
				e = timeline.conclusao;
		}
		if (contains(m, "Remessa, Carga")) {
			e = timeline.remessacarga;
		}
		if (e) {
			e.passou = true;
			if (e.contador)
				e.contador += 1;
			else
				e.contador = 1;
			if (prev) {
				prev.esta = false;
				delete prev.complemento;
			}
			e.esta = true;
			e.complemento = m.complemento;
			prev = e;
		}
	}
	console.log(timeline);
	return timeline;
}
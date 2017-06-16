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
	var startsWith = function(m, descr) {
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

	var movs = processo.movimento;
	var prev;
	for (var i = movs.length - 1; i >= 0; i--) {
		var e = undefined;
		var m = movs[i];
		if (!m.movimentoLocal)
			continue;
		if (startsWith(m, "Distribuição")
				|| startsWith(m,
						'ART 286 (antigo 253) Distribuição por Dependência')
				|| startsWith(m, 'Redistribuição'))
			e = timeline.distribuicao;
		if (startsWith(m, "Conclusão")) {
			if (m.complemento[0] == 'Despacho' || m.complemento[0] == 'Decisão') {
				e = timeline.conclusao;
			} else if (m.complemento[0] == 'Sentença')
				e = timeline.sentenca;
		}
		if (startsWith(m, "Remessa, Carga"))
			e = timeline.remessacarga;
		if (startsWith(m, "Suspensão"))
			e = timeline.suspensao;
		if (startsWith(m, "Audiência"))
			e = timeline.audiencia;
		if (startsWith(m, "Baixa"))
			e = timeline.baixa;
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
			e.dataHora = m.dataHora;
			if (m.complemento) {
				e.complemento = [];
				if (m.complemento && m.complemento.length > 0)
					e.complemento[0] = m.complemento[0].trunc(30, true);
				if (m.complemento && m.complemento.length > 1)
					e.complemento[1] = m.complemento[1].trunc(30, true);
			}
			prev = e;
		}
	}
	console.log(timeline);
	return timeline;
}
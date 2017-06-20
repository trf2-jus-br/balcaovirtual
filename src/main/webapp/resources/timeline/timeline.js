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

var updateTimeline = function(orgao, processo) {
	var startsWith = function(m, descr) {
		return m.movimentoLocal.descricao.indexOf(descr) == 0;
	}

	var contains = function(m, a) {
		return a.indexOf(m.movimentoLocal.codigoMovimento) !== -1;
	}

	var timeline = {
		autuacao : {
			passou : true
		},
		distribuicao : {},
		primeirodespacho : {},
		intimacao : {},
		remessa : {},
		devolucao : {},
		juntada : {},
		audiencia : {},
		conclusao : {},
		sentenca : {
			texto : orgao == 'TRF2' ? 'Inteiro Teor' : 'Sentença'
		},
		suspensao : {},
		apelacao : {
			texto : orgao == 'TRF2' ? undefined : 'TRF2'
		},
		baixa : {},
		arquivamento : {},
	};

	var map = {

	}

	var movs = processo.movimento;
	var prev;
	for (var i = movs.length - 1; i >= 0; i--) {
		var e = undefined;
		var m = movs[i];
		if (!m.movimentoLocal)
			continue;
		if (contains(m, [ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 60, 61, 62, 63, 80,
				81, 83 ]))
			e = timeline.distribuicao;
		if (contains(m, [ 11, 76 ])) {
			if (m.complemento[0] == 'Despacho' || m.complemento[0] == 'Decisão') {
				e = timeline.conclusao;
			} else if (m.complemento[0] == 'Sentença')
				e = timeline.sentenca;
		}
		if (contains(m, [ 78 ])) // Inteiro Teor
			e = timeline.sentenca;
		if (contains(m, [ 12 ]))
			e = timeline.intimacao;
		if (contains(m, [ 14 ])) {
			if (m.complemento && m.complemento[0] == 'TRF - 2ª Região')
				e = timeline.apelacao;
			else
				e = timeline.remessa;
		}

		if (contains(m, [ 15 ]))
			e = timeline.devolucao;
		if (contains(m, [ 27 ]))
			e = timeline.juntada;
		if (contains(m, [ 101 ]))
			e = timeline.suspensao;
		if (contains(m, [ 19 ]))
			e = timeline.audiencia;
		if (contains(m, [ 26 ]))
			e = timeline.baixa;
		if (e) {
			e.passou = true;
			if (e.contador)
				e.contador += 1;
			else
				e.contador = 1;
			if (prev) {
				prev.esta = false;
				// delete prev.complemento;
			}
			e.esta = true;
			e.dataHora = m.dataHora;
			if (m.complemento) {
				e.complemento = [];
				if (m.complemento && m.complemento.length > 0)
					e.complemento[0] = m.complemento[0].trunc(30, true);
				if (m.complemento && m.complemento.length > 1)
					e.complemento[1] = m.complemento[1].trunc(30, true);
				if (m.complemento && m.complemento.length > 1
						&& e.complemento[1] == "Registro no Sistema")
					delete e.complemento[1];
			}
			prev = e;
		}
		if (e === timeline.conclusao || e === timeline.sentenca
				|| e === timeline.apelacao || e === timeline.baixa)
			for ( var k in timeline)
				if (timeline.hasOwnProperty(k))
					delete timeline[k].complemento;
	}
	return timeline;
}
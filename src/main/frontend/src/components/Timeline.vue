<template>
  <div>
    <div class="row justify-content-center" style="margin-top: -1em; margin-bottom: -1em;" ng-if="deviceDetector.raw.browser.chrome">
      <div class="col col-sm-12 col-md-10">
        <svg xmlns="http://www.w3.org/2000/svg" width="100%" height="100%" viewBox="0 0 800 300">
          <defs>
            <marker id="arrow" markerWidth="10" markerHeight="10" refX="0" refY="3" orient="auto" markerUnits="strokeWidth" viewBox="0 0 20 20">
              <path d="M0,0 L0,6 L9,3 z" />
            </marker>
          </defs>
          <g transform="translate(50, 0)">
            <!-- Parte superior -->
            <g id="intimacao" transform="translate(162, 75)" :class="classes.intimacao">
              <template v-if="timeline.intimacao.complemento">
                <text x="-25" y="-10" class="textocomplemento" text-anchor="end" alignment-baseline="middle" v-html="timeline.intimacao.complemento[0]"></text>
                <text x="-25" y="+10" class="textocomplemento" text-anchor="end" alignment-baseline="middle" v-html="timeline.intimacao.complemento[1]"></text>
              </template>
              <rect transform="rotate(60)" x="20 " y="-5 " width="65 " height="10 " class="linha " data-toggle="tooltip" data-html="true" :title="tooltips.intimacao" />
              <a href="">
                <circle r="20 " class="bola " @click.prevent="filtrarMovimentos( '#intimacao')" />
              </a>
              <text class="textointerno " text-anchor="middle " alignment-baseline="central ">{{contador(timeline.intimacao.contador)}}</text>
              <text y="-40 " class="texto " text-anchor="middle " alignment-baseline="middle ">Intimação/Cit.</text>
            </g>

            <g id="juntada " transform="translate(262, 75) " :class="classes.juntada">
              <rect transform="rotate(60)" x="20 " y="-5 " width="65 " height="10 " class="linha " data-toggle="tooltip" data-html="true" :title="tooltips.juntada" />
              <a href="">
                <circle r="20 " class="bola " @click.prevent="filtrarMovimentos( '#juntada')" />
              </a>
              <text class="textointerno " text-anchor="middle " alignment-baseline="central ">{{contador(timeline.juntada.contador)}}</text>
              <text y="-40 " class="texto " text-anchor="middle " alignment-baseline="middle ">Juntada</text>
            </g>

            <g id="audiencia " transform="translate(362, 75) " :class="classes.audiencia">
              <rect transform="rotate(60) " x="20 " y="-5 " width="65 " height="10 " class="linha " data-toggle="tooltip" data-html="true" :title="tooltips.audiencia" />
              <a href="">
                <circle r="20 " class="bola " @click.prevent="filtrarMovimentos( '#audiencia')" />
              </a>
              <text class="textointerno " text-anchor="middle " alignment-baseline="central ">{{contador(timeline.audiencia.contador)}}</text>
              <text y="-40 " class="texto " text-anchor="middle " alignment-baseline="middle ">Audiência</text>
            </g>

            <g id="suspensao " transform="translate(495, 75) " :class="classes.suspensao">
              <rect transform="rotate(120)" x="20 " y="-5 " width="65 " height="10 " class="linha " data-toggle="tooltip" data-html="true" :title="tooltips.suspensao" />
              <a href="">
                <circle r="20 " class="bola " @click.prevent="filtrarMovimentos( '#suspensao')" />
              </a>
              <text class="textointerno " text-anchor="middle " alignment-baseline="central ">{{contador(timeline.suspensao.contador)}}</text>
              <text y="-40 " class="texto " text-anchor="middle " alignment-baseline="middle ">Suspensão</text>
            </g>

            <g id="apelacao " transform="translate(595, 75) " :class="classes.apelacao" v-show="timeline.apelacao.texto ">
              <rect transform="rotate(120) " x="20 " y="-5 " width="65 " height="10 " class="linha " data-toggle="tooltip" data-html="true" :title="tooltips.apelacao" />
              <a href="">
                <circle r="20 " class="bola " @click.prevent="filtrarMovimentos( '#apelacao')" />
              </a>
              <text class="textointerno " text-anchor="middle " alignment-baseline="central ">{{contador(timeline.apelacao.contador)}}</text>
              <text y="-40 " class="texto " text-anchor="middle " alignment-baseline="middle ">{{timeline.apelacao.texto}}</text>
            </g>

            <g id="devolucaoapelacao " transform="translate(595, 75) " :class="classes.devolucaoapelacao" v-show="timeline.devolucaoapelacao.esta ">
              <line transform="rotate(120) " x1="30 " y1="-12 " x2="45 " y2="-12 " marker-end="url(#arrow) " />
            </g>

            <!-- Parte inferior -->
            <g id="devolucao " transform="translate(312, 225) " :class="classes.devolucao">
              <rect x="-80 " y="-5 " width="60 " height="10 " class="linha " data-toggle="tooltip" data-html="true" :title="tooltips.devolucao" />
              <rect transform="rotate(-60) " x="20 " y="-5 " width="65 " height="10 " class="linha " data-toggle="tooltip" data-html="true" :title="tooltips.devolucao" />
              <a href="">
                <circle r="20 " class="bola " @click.prevent="filtrarMovimentos( '#devolucao')" />
              </a>
              <text class="textointerno " text-anchor="middle " alignment-baseline="central ">{{contador(timeline.devolucao.contador)}}</text>
              <text y="40 " class="texto " text-anchor="middle " alignment-baseline="middle ">Devolução</text>
            </g>

            <g id="remessa " transform="translate(212, 225) " :class="classes.remessa">
              <template v-if="timeline.remessa.complemento">
                <text x="-25 " y="-10 " class="textocomplemento " text-anchor="end " alignment-baseline="middle " v-html="timeline.remessa.complemento[0] "></text>
                <text x="-25 " y="+10 " class="textocomplemento " text-anchor="end " alignment-baseline="middle " v-html="timeline.remessa.complemento[1] "></text>
              </template>
              <rect transform="rotate(-60) " x="20 " y="-5 " width="65 " height="10 " class="linha " data-toggle="tooltip" data-html="true" :title="tooltips.remessa" />
              <a href="">
                <circle r="20 " class="bola " @click.prevent="filtrarMovimentos( '#remessa')" />
              </a>
              <text class="textointerno " text-anchor="middle " alignment-baseline="central ">{{contador(timeline.remessa.contador)}}</text>

              <text y="40 " class="texto " text-anchor="middle " alignment-baseline="middle ">Remessa</text>
            </g>

            <!-- Segunda metade -->
            <g id="baixa " transform="translate(650, 150) " :class="classes.baixa">
              <rect x="-80 " y="-5 " width="60 " height="10 " class="linha " data-toggle="tooltip" data-html="true" :title="tooltips.baixa" />
              <a href="">
                <circle r="20 " class="bola " @click.prevent="filtrarMovimentos( '#baixa')" />
              </a>
              <text class="textointerno " text-anchor="middle " alignment-baseline="central ">{{contador(timeline.baixa.contador)}}</text>

              <text y="40 " class="texto " text-anchor="middle " alignment-baseline="middle ">Baixa</text>
            </g>

            <g id="sentenca " transform="translate(550, 150) " :class="classes.sentenca">
              <rect x="-80 " y="-5 " width="60 " height="10 " class="linha " data-toggle="tooltip" data-html="true" :title="tooltips.sentenca" />
              <a href="">
                <circle r="20 " class="bola " @click.prevent="filtrarMovimentos( '#sentenca')" />
              </a>
              <text class="textointerno " text-anchor="middle " alignment-baseline="central ">{{contador(timeline.sentenca.contador)}}</text>

              <text y="40 " class="texto " text-anchor="middle " alignment-baseline="middle ">{{timeline.sentenca.texto}}</text>
            </g>

            <g id="conclusao " transform="translate(450, 150) " :class="classes.conclusao">
              <rect x="-280 " y="-5 " width="260 " height="10 " class="linha " data-toggle="tooltip" data-html="true" :title="tooltips.conclusao" />
              <a href="">
                <circle r="20 " class="bola " @click.prevent="filtrarMovimentos( '#conclusao')" />
              </a>
              <text class="textointerno " text-anchor="middle " alignment-baseline="central ">{{contador(timeline.conclusao.contador)}}</text>

              <text y="40 " class="texto " text-anchor="middle " alignment-baseline="middle ">Conclusão</text>
            </g>

            <!-- Primeira metade -->
            <g id="distribuicao " transform="translate(150, 150) " :class="classes.distribuicao">
              <rect x="-80 " y="-5 " width="60 " height="10 " class="linha " data-toggle="tooltip" data-html="true" :title="tooltips.distribuicao" />
              <a href="">
                <circle r="20 " class="bola " @click.prevent="filtrarMovimentos( '#distribuicao') " />
              </a>
              <text class="textointerno " text-anchor="middle " alignment-baseline="central ">{{contador(timeline.distribuicao.contador)}}</text>
              <text y="40 " class="texto " text-anchor="middle " alignment-baseline="middle ">Ditribuição</text>
            </g>

            <g id="autuacao " transform="translate(50, 150) " :class="classes.autuacao">
              <circle r="20 " class="bola " />
              <text class="textointerno " text-anchor="middle " alignment-baseline="central ">{{contador(timeline.autuacao.contador)}}</text>
              <text y="40 " class="texto " text-anchor="middle " alignment-baseline="middle ">Autuação</text>
            </g>
          </g>
        </svg>
      </div>
      <div class="col col-sm-12 " v-if="!$parent.$parent.settings.timeline">
        <p class="alert alert-warning alert-dismissible fade show " role="alert ">
          <button type="button " @click.prevent="$parent.$parent.$emit('setting', 'timeline', true)" class="close " data-dismiss="alert " aria-label="Close ">
            <span aria-hidden="true ">&times;</span>
          </button>
          <strong>Linha do Tempo do Processo</strong> é um diagrama que resume as ocorrências observadas em um processo judicial. Situações que ainda não ocorreram permanecem em cinza. Eventos detectados no histórico de movimentos são apresentados como círculos pretos e o caminho até eles é marcado de azul. Quando um mesmo evento ocorre mais de uma vez, o número total de ocorrências é apresentado dentro do respectivo círculo. O último evento detectado é marcado com um círculo laranja e ao seu lado podem ser apresentados alguns detalhes referentes, apenas, a esta última ocorrência.
        </p>
      </div>
    </div>

    <div class="row" v-if="!deviceDetector.raw.browser.chrome">
      <div class="col col-sm-12 " v-show="!$parent.$parent.settings.timelineIncompatible">
        <p class="alert alert-danger alert-dismissible fade show " role="alert ">
          <button type="button " @click.prevent="$parent.$parent.$emit('setting', 'timelineIncompatible', true)" class="close " data-dismiss="alert " aria-label="Close ">
            <span aria-hidden="true ">&times;</span>
          </button>
          {{deviceDetector.raw.browser.chrome }} Seu navegador ({{deviceDetector.browser}}) não é compatível com a
          <strong>Timeline Processual
          </strong>, por favor, utilize uma versão recente do Google Chrome.
        </p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'timeline',
  props: ['timeline'],
  data () {
    return {
      errormsg: undefined,
      deviceDetector: { raw: { browser: { chrome: true } }, browser: 'chrome' }
    }
  },
  computed: {
    classes: function () {
      var o = {}

      for (var k in this.timeline) {
        if (!this.timeline.hasOwnProperty(k)) continue
        var ti = this.timeline[k]
        o[k] = {
          'passou': ti.passou,
          'esta': ti.esta,
          'transito-verde': ti.transito === 'verde',
          'transito-laranja': ti.transito === 'laranja',
          'transito-vermelho': ti.transito === 'vermelho',
          'transito-vinho': ti.transito === 'vinho'
        }
      }
      return o
    },

    tooltips: function () {
      var o = {}

      for (var k in this.timeline) {
        if (!this.timeline.hasOwnProperty(k)) continue
        var ti = this.timeline[k]
        if (!ti.tempo) continue
        o[k] = 'Tempo Médio: ' + Math.round(ti.tempoMedio / 24 / 60) + ' dias'
      }
      return o
    }
  },
  mounted: function () {
    window.jQuery('[data-toggle="tooltip"]').tooltip()
  },
  methods: {
    filtrarMovimentos: function (texto) {
      this.$parent.$emit('filtrar', texto)
    },
    contador: function (c) {
      if (!c || c === 1) return ''
      return c
    }
  }
}
</script>

<style scoped>
.bola {
  stroke: #ccc;
  stroke-width: 4;
  fill: white
}

.linha {
  fill: #ccc;
  stroke-width: 0;
  stroke: rgb(0, 0, 0)
}

.texto {
  fill: #ccc;
  font-weight: bold;
  font-size: 80%;
  /* font: 80% verdana, sans-serif; */
}

.textocomplemento {
  fill: #ccc;
  font-weight: bold;
  font-size: 80%;
  /* font: 80% verdana, sans-serif; */
}

.textointerno {
  pointer-events: none;
  fill: #ccc;
  font-weight: bold;
  /* font: 100% verdana, sans-serif; */
}

.passou CIRCLE {
  stroke: black;
}

.esta CIRCLE {
  stroke: #f0ad4e !important;
  /* fill: rgba(240, 173, 78, 20) !important; */
}

.esta LINE {
  stroke: #f0ad4e !important;
  stroke-width: 3;
}

MARKER {
  fill: #f0ad4e !important;
}

.esta TEXT.textocomplemento {
  fill: #f0ad4e;
}

.passou RECT {
  fill: #0275d8;
}

.passou TEXT.texto {
  fill: #0275d8;
}

.passou TEXT.textointerno {
  fill: black;
}

.transito-verde RECT {
  fill: #28a745 !important;
}

.transito-laranja RECT {
  fill: #ffc107 !important;
}

.transito-vermelho RECT {
  fill: #dc3545 !important;
}

.transito-vinho RECT {
  fill: #9e1313 !important;
}
</style>

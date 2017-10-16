<template>
  <div class="container-fluid content">
    <div class="row">
      <div class="col-md-12">
        <h4 class="text-center mt-3 mb-3">Intimações/Citações Confirmadas Recentemente</h4>
      </div>
    </div>

    <!-- QUANTIDADE POR DATA -->
    <div class="row">
      <div class="col col-12" v-if="quantidadePorData.length > 0">
        <table class="table table-striped mb-0 table-responsive">
          <thead class="thead-inverse">
            <tr>
              <th rowspan="2">Data</th>
              <th style="text-align: center;" colspan="2">Quantidade do Usuário</th>
              <th style="text-align: center;" colspan="2">Quantidade do Grupo</th>
            </tr>
            <tr>
              <th style="text-align: center;">Por Confirmação</th>
              <th style="text-align: center;">Por Omissão</th>
              <th style="text-align: center;">Por Confirmação</th>
              <th style="text-align: center;">Por Omissão</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="p in quantidadePorData">
              <td>
                <span v-html="p.dataFormatada"></span>
              </td>
              <td style="text-align: center;">
                <a href="" @click.prevent="listar(p.data, true, false, false)">{{p.quantidadeDoUsuarioPorConfirmacao}}</a>
              </td>
              <td style="text-align: center;">
                <a href="" @click.prevent="listar(p.data, false, true, false)">{{p.quantidadeDoUsuarioPorOmissao}}</a>
              </td>
              <td style="text-align: center;">
                <a href="" @click.prevent="listar(p.data, true, false, true)">{{p.quantidadeDoGrupoPorConfirmacao}}</a>
              </td>
              <td style="text-align: center;">
                <a href="" @click.prevent="listar(p.data, false, true, true)">{{p.quantidadeDoGrupoPorOmissao}}</a>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="col col-sm-12" v-if="quantidadePorData.length == 0">
        <p class="alert alert-warning">
          <strong>Atenção!</strong> Nenhuma intimação/citação confirmada nos últimos 7 dias.
        </p>
      </div>
      <div class="col-sm-12" style="padding-top: 1em;">
        <button type="button" @click="pesquisaAvancada()" v-if="quantidadePorData !== undefined" class="btn btn-success d-print-none">Pesquisa Avançada</button>
      </div>
    </div>
  </div>
</template>

<script>
import UtilsBL from '../bl/utils.js'
import { Bus } from '../bl/bus.js'

export default {
  name: 'aviso-confirmado-recentes',

  mounted () {
    this.$nextTick(() => {
      this.$http.get('aviso-confirmado/contar', { block: true }).then(response => {
        for (var i = 0; i < response.data.list.length; i++) {
          var qd = response.data.list[i]
          qd.dataFormatada = UtilsBL.formatDDMMYYYY(qd.data)
          this.quantidadePorData.push(qd)
        }
      }, error => {
        console.log(error)
        Bus.$emit('message', 'Erro', error.data.errormsg)
      })
    })
  },

  data () {
    return {
      quantidadePorData: []
    }
  },

  methods: {
    listar: function (data, porConfirmacao, porOmissao, doGrupo) {
      this.$router.push({
        name: 'Lista de Avisos Confirmados',
        params: {
          dataInicial: data,
          dataFinal: data,
          porConfirmacao: porConfirmacao,
          porOmissao: porOmissao,
          doGrupo: doGrupo
        }
      })
    },

    imprimir: function () {
      window.print()
    }
  }
}
</script>

<style scoped>
.protocolos-header {
  font-size: 150%;
  padding-bottom: 0.5rem;
}

.unbreakable {
  white-space: nowrap;
  word-break: keep-all;
  hyphens: none;
}

@media print {
  .table-peticao {
    font-size: 10pt;
  }
  .table-protocolo {
    font-size: 8pt;
  }
}
</style>

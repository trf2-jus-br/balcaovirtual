<template>
  <div class="container-fluid content">
    <div class="row">
      <div class="col-md-12">
        <h4 class="text-center mt-3 mb-3">Votos em Sessões de Julgamento</h4>
      </div>
      <div class="col col-sm-12" v-if="errormsg">
        <p class="alert alert-danger">
          {{ errormsg }}
        </p>
      </div>
    </div>

    <div class="row d-print-none">
      <div v-if="false" class="col-sm-auto ml-1 mb-3">
        <div class="input-group">
          <div class="input-group-prepend">
            <div class="input-group-text" id="btnGroupAddon">
              <span class="fa fa-map-marker"></span>
            </div>
          </div>
          <select id="sessao" class="form-control" v-model="sessao" @change="selecionarSessao" name="sessao">
            <option disabled selected hidden :value="undefined">[Selecionar]</option>
            <option v-for="i in sessoes" :key="i" :value="i">{{ i.nome }}</option>
          </select>
        </div>
      </div>
      <div class="col-sm-auto ml-1 mb-3">
        <div class="input-group">
          <div class="input-group-prepend">
            <div class="input-group-text" id="btnGroupAddon">
              <span class="fa fa-search"></span>
            </div>
          </div>
          <input type="text" class="form-control" placeholder="Filtrar" v-model="filtro" ng-model-options="{ debounce: 200 }" />
        </div>
      </div>
      <div class="col-auto ml-auto mb-3" v-if="(filtradosEMarcadosEVotaveis || []).length">
        <button v-if="false" type="button" @click="revisar()" class="btn btn-info ml-1" title="">
          <span class="fa fa-eye"></span> Revisar&nbsp;&nbsp;
          <span class="badge badge-pill badge-warning">{{ filtradosEMarcadosEVotaveis.length }}</span>
        </button>
        <button type="button" @click="votarEmLote()" class="btn btn-primary ml-1" title="">
          <span class="fa fa-check"></span> Acompanhar&nbsp;&nbsp;
          <span class="badge badge-pill badge-warning">{{ filtradosEMarcadosEVotaveis.length }}</span>
        </button>
        <button type="button" @click="pedirVistaEmLote()" class="btn btn-info ml-1" title="">
          <span class="fa fa-eye"></span> Pedir Vista&nbsp;&nbsp;
          <span class="badge badge-pill badge-warning">{{ filtradosEMarcadosEVotaveis.length }}</span>
        </button>
      </div>
    </div>

    <div class="row" v-if="!carregando && filtrados.length == 0">
      <div class="col col-sm-12">
        <p class="alert alert-warning"><strong>Atenção!</strong> Nenhum voto encontrado.</p>
      </div>
    </div>

    <div class="row" v-if="filtrados.length > 0">
      <div class="col-sm-12">
        <table class="table table-striped table-sm table-responsive">
          <thead class="thead-dark">
            <tr>
              <th style="text-align: center">
                <input type="checkbox" id="progress_checkall" name="progress_checkall" v-model="todos" @change="marcarTodos()" />
              </th>
              <th>Voto</th>
              <th>Tipo</th>
              <th>Relator</th>
              <th>Processo</th>
              <th>Autor</th>
              <th>Réu</th>
              <th>Data</th>
              <th>Unidade</th>
              <th>Sistema/Órgão</th>
              <th>Acompa&shy;nhantes</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="f in filtrados" :key="f.id">
              <td class="td-middle" style="text-align: center">
                <input type="checkbox" v-model="f.checked" :disabled="f.disabled" />
              </td>
              <td class="td-middle">
                <router-link
                  :to="{
                    name: 'Voto',
                    params: { numero: f.id, lista: filtrados },
                  }"
                  >{{ f.numeroDoDocumento }}</router-link
                >
              </td>
              <td class="td-middle text-center"><span v-if="f.tipoDoDocumento == 'Despacho/Decisão'" class="fa fa-thumbs-o-up text-success"></span><span v-else class="fa fa-thumbs-o-down text-danger"></span></td>
              <td class="td-middle">
                <span :title="'Nome: ' + f.nomeDoUsuarioQueIncluiu">{{ f.identificadorDoUsuarioQueIncluiu }}</span>
              </td>
              <td class="td-middle">
                <span class="unbreakable">
                  <router-link
                    :to="{
                      name: 'Processo',
                      params: { numero: f.numeroDoProcesso },
                      query: { avisos: $parent.cAvisos },
                    }"
                    target="_blank"
                    >{{ f.processoFormatado }}</router-link
                  >
                </span>
              </td>
              <td class="td-middle">{{ f.autor }}</td>
              <td class="td-middle">{{ f.reu }}</td>
              <td class="td-middle">
                {{ f.dataDeInclusaoFormatada }}
              </td>
              <td class="td-middle">{{ f.siglaDaUnidade }}</td>
              <td class="td-middle">
                <span :title="'Identificador: ' + f.sistema">{{ $parent.test.properties["balcaovirtual." + f.sistema + ".name"] }}</span>
              </td>
              <td class="td-middle">
                {{ f.descricaoDoStatus }}
                <span v-if="f.errormsg" :class="{ red: true }">Erro {{ f.errormsg }} </span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import UtilsBL from "../bl/utils.js";
import { Bus } from "../bl/bus.js";

export default {
  components: {},

  mounted() {
    this.errormsg = undefined;

    if (this.$route.params.manter) return;

    this.$store.dispatch("carregarVotos");
  },

  data() {
    return {
      voto: undefined,
      votos: [],
      sessao: undefined,
      filtro: undefined,
      todos: true,
      errormsg: undefined,
      carregando: true,
    };
  },

  computed: {
    lista() {
      return this.$store.state.votos ? this.$store.state.votos : [];
    },
    filtrados: function() {
      var a = this.lista;
      a = UtilsBL.filtrarPorSubstring(a, this.filtro);
      var procnum, procline;
      for (var i = 0; i < a.length; i++) {
        if (procnum !== a[i].numeroDoProcesso) {
          procnum = a[i].numeroDoProcesso;
          procline = i;
          a[i].rows = 1;
        } else {
          a[procline].rows++;
          a[i].rows = 0;
        }
      }
      return a;
    },

    filtradosEMarcados: function() {
      return this.filtrados.filter(function(item) {
        return item.checked;
      });
    },

    filtradosEMarcadosEVotaveis: function() {
      return this.filtradosEMarcados.filter(function(item) {
        return item.status === "4" || item.status === "2";
      });
    },
    sessoes() {
      return [];
    },
  },

  methods: {
    marcarTodos: function() {
      var docs = this.filtrados;
      for (var i = 0; i < docs.length; i++) {
        var doc = docs[i];
        if (!doc.disabled) doc.checked = this.todos;
      }
    },

    revisar: function() {
      var a = this.filtradosEMarcadosEAssinaveis;
      this.$router.push({
        name: "Voto",
        params: { numero: a[0].id, lista: a },
      });
    },

    votarEmLote: function() {
      var a = this.filtradosEMarcadosEAssinaveis;
      Bus.$emit("votar", a, this.removerDocumentosDesabilitados);
    },

    pedirVistaEmLote: function() {
      var a = this.filtradosEMarcadosEAssinaveis;
      Bus.$emit("pedirVista", a, this.removerDocumentosDesabilitados);
    },

    removerDocumentosDesabilitados: function() {
      console.log(this.lista);
      var a = this.lista.filter(function(item) {
        return !item.disabled;
      });
      this.$set(this, "lista", a);
      console.log(this.lista);
    },
  },
};
</script>

<style scoped>
.destaque {
  color: red;
}

.td-middle {
  vertical-align: middle;
}
</style>

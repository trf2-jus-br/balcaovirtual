<template>
  <div class="container-fluid content">
    <div class="row">
      <div class="col-md-12">
        <h4 class="text-center mt-3 mb-3">Minutas para Conferir e Assinar</h4>
      </div>
      <div class="col col-sm-12" v-if="errormsg">
        <p class="alert alert-danger">
          {{ errormsg }}
        </p>
      </div>
    </div>

    <div class="alert alert-success d-print-none pb-0 pt-2" v-if="assinaveisEPadrao.length">
      <div class="row align-items-center">
        <div class="col mb-2">
          <span class="mr-2" v-if="assinaveisEPadrao.length === 1"
            ><strong>Encontramos 1 minuta exatamente igual ao padrão</strong>. Clique no botão verde para assiná-la.</span
          >
          <span class="mr-2" v-else
            ><strong>Encontramos {{ assinaveisEPadrao.length }} minutas exatamente iguais aos padrões</strong>. Clique no botão verde para
            assiná-las em lote.</span
          >
        </div>
        <div class="col col-12 col-md-auto mb-2">
          <button type="button" @click="assinarComSenhaEmLote()" class="btn btn-success ml-1" title="">
            <span class="fa fa-certificate"></span> Assinar&nbsp;&nbsp;
            <span class="badge badge-pill badge-warning">{{ assinaveisEPadrao.length }}</span>
          </button>
        </div>
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
          <select id="mesa" class="form-control" v-model="mesa" @change="selecionarMesa" name="mesa">
            <option disabled selected hidden :value="undefined">[Selecionar]</option>
            <option v-for="i in mesas" :value="i">{{ i.nome }}</option>
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
      <div class="col-auto ml-auto mb-3" v-if="(filtradosEMarcadosEAssinaveis || []).length">
        <button type="button" @click="revisar()" class="btn btn-info ml-1" title="">
          <span class="fa fa-eye"></span> Revisar&nbsp;&nbsp;
          <span class="badge badge-pill badge-warning">{{ filtradosEMarcadosEAssinaveis.length }}</span>
        </button>
        <button type="button" @click="assinarComSenhaEmLote()" class="btn btn-primary ml-1" title="">
          <span class="fa fa-certificate"></span> Assinar&nbsp;&nbsp;
          <span class="badge badge-pill badge-warning">{{ filtradosEMarcadosEAssinaveis.length }}</span>
        </button>
      </div>
    </div>

    <div class="row" v-if="!carregando && filtrados.length == 0">
      <div class="col col-sm-12">
        <p class="alert alert-warning"><strong>Atenção!</strong> Nenhuma minuta encontrada.</p>
      </div>
    </div>

    <div class="row" v-if="filtrados.length > 0">
      <div class="col-sm-12">
        <!-- CARDS -->
        <div v-if="false">
          <div
            :class="{
              card: true,
              'mt-5': true,
              'alert-warning': !f.similaridade,
              'alert-success': f.similaridade === 1.0,
              'alert-primary': f.similaridade < 1.0,
            }"
            v-for="f in filtrados"
            :key="f.id"
          >
            <div class="card-header">
              <input type="checkbox" v-model="f.checked" :disabled="f.disabled" class="mr-2" />
              {{ f.tipoDoDocumento }}
              <router-link
                :to="{
                  name: 'Documento',
                  params: { numero: f.id, lista: filtrados },
                }"
                >{{ f.numeroDoDocumento }}</router-link
              >
              -
              <router-link
                :to="{
                  name: 'Processo',
                  params: { numero: f.numeroDoProcesso },
                  query: { avisos: $parent.cAvisos },
                }"
                target="_blank"
                >{{ f.processoFormatado }}</router-link
              >
            </div>
            <div class="card-body">
              <p class="card-text" v-html="f.conteudoPreprocessado"></p>
            </div>
            <div class="card-footer">
              <button type="button" @click="assinarComSenhaEmLote()" class="btn btn-primary ml-1" title="">
                <span class="fa fa-certificate"></span> Assinar&nbsp;&nbsp;
                <span class="badge badge-pill badge-warning">{{ filtradosEMarcadosEAssinaveis.length }}</span>
              </button>
            </div>
          </div>
        </div>

        <!-- TABELA -->
        <table class="table table-striped table-sm table-responsive" v-if="true">
          <thead class="thead-dark">
            <tr>
              <th style="text-align: center">
                <input type="checkbox" id="progress_checkall" name="progress_checkall" v-model="todos" @change="marcarTodos()" />
              </th>
              <th>Documento</th>
              <th>Tipo</th>
              <th>Responsável</th>
              <th>Processo</th>
              <th>Autor</th>
              <th>Réu</th>
              <th>Data</th>
              <th>Unidade</th>
              <th>Sistema/Órgão</th>
              <th>Situação</th>
              <th style="text-align: center" v-if="padraoAtivo">Padrão</th>
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
                    name: 'Documento',
                    params: { numero: f.id, lista: filtrados },
                  }"
                  >{{ f.numeroDoDocumento }}</router-link
                >
              </td>
              <td class="td-middle">{{ f.tipoDoDocumento }}</td>
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
              <td class="td-middle text-center" v-if="padraoAtivo">
                <router-link v-if="f.similaridade === 1.0" :to="{ name: 'Padrao', params: { numero: f.idPadrao } }"
                  ><span class="fa fa-check-circle text-success"></span
                ></router-link>
                <router-link
                  :to="{ name: 'Padrao', params: { numero: f.idPadrao } }"
                  v-if="f.similaridade && f.similaridade < 1.0"
                  v-html="(f.similaridade * 100).toFixed(0) + '%'"
                ></router-link>
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
import ProcessoBL from "../bl/processo.js";
import { Bus } from "../bl/bus.js";

export default {
  components: {},

  async mounted() {
    this.errormsg = undefined;
    if (this.$route.params.manter) return;
    await this.$store.dispatch("carregarMesas");
    if (this.filtradosEMarcadosEAssinaveis.length && this.$route.params.revisar) this.revisar();
  },

  data() {
    return {
      mesa: undefined,
      mesas: [],
      filtro: undefined,
      todos: true,
      errormsg: undefined,
      carregando: true,
    };
  },

  computed: {
    lista() {
      return this.$store.state.documentos ? this.$store.state.documentos : [];
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

    filtradosEMarcadosEAssinaveis: function() {
      return this.filtradosEMarcados.filter(function(item) {
        return item.status === "4" || item.status === "2";
      });
    },

    assinaveisEPadrao: function() {
      return this.lista.filter(function(item) {
        return (item.status === "4" || item.status === "2") && item.similaridade === 1.0 && item.idPadrao;
      });
    },

    padraoAtivo() {
      return this.lista && this.lista.filter((i) => i.idPadrao).length > 0;
    },
  },

  methods: {
    selecionarMesa: function() {
      this.$http.get("mesa/" + this.mesa.id, { block: true }).then(
        (response) => {
          this.lista.length = 0;
          var list = response.data.list;
          for (var i = 0; i < list.length; i++) {
            this.lista.push(this.fixItem(list[i]));
          }
          this.carregando = false;
        },
        (error) => UtilsBL.errormsg(error, this)
      );
    },

    fixItem: function(item) {
      UtilsBL.applyDefauts(item, {
        rows: 1,
        checked: true,
        disabled: false,
        dataDeInclusao: undefined,
        dataDeInclusaoFormatada: undefined,
        id: undefined,
        numeroDoDocumento: undefined,
        tipoDoDocumento: undefined,
        numeroDoProcesso: undefined,
        autor: undefined,
        reu: undefined,
        processoFormatado: undefined,
        descricaoDoStatus: undefined,
        identificadorDoUsuarioQueIncluiu: undefined,
        nomeDoUsuarioQueIncluiu: undefined,
        conteudo: undefined,
        sistema: undefined,
        lembretes: undefined,
        errormsg: undefined,
      });
      if (item.numeroDoProcesso !== undefined) {
        item.processoFormatado = ProcessoBL.formatarProcesso(item.numeroDoProcesso);
      }
      if (item.dataDeInclusao !== undefined) {
        item.dataDeInclusaoFormatada = UtilsBL.formatJSDDMMYYYY(item.dataDeInclusao);
      }
      if (item.lembretes) {
        for (var i = 0; i < item.lembretes.length; i++) {
          item.lembretes[i].dataDeInclusaoFormatada = UtilsBL.formatJSDDMMYYYY(item.lembretes[i].dataDeInclusao);
        }
      }
      return item;
    },

    marcarTodos: function() {
      var docs = this.filtrados;
      for (var i = 0; i < docs.length; i++) {
        var doc = docs[i];
        if (!doc.disabled) doc.checked = this.todos;
      }
    },

    assinarDocumento: function(item) {
      this.chamarAssijus([this.criarAssinavel(item)]);
    },

    assinarDocumentos: function() {
      var list = [];
      for (var i = 0; i < this.filtradosEMarcadosEAssinaveis.length; i++) {
        list.push(this.criarAssinavel(this.filtradosEMarcadosEAssinaveis[i]));
      }
      if (list.length > 0) this.chamarAssijus(list);
    },

    chamarAssijus: function(list) {
      var json = JSON.stringify({ list: list });
      this.$http
        .post(this.$parent.test.properties["balcaovirtual.assijus.endpoint"] + "/api/v1/store", { payload: json }, { block: true })
        .then(
          (response) => {
            var callback = window.location.href + "";
            console.log(callback);
            window.location.href =
              this.$parent.test.properties["balcaovirtual.assijus.endpoint"] +
              "/?endpointautostart=true&endpointlistkey=" +
              response.data.key +
              "&endpointcallback=" +
              encodeURI(callback).replace("#", "__hashsign__");
          },
          (error) => UtilsBL.errormsg(error, this)
        );
    },

    revisar: function() {
      var a = this.filtradosEMarcadosEAssinaveis;
      this.$router.push({
        name: "Documento",
        params: { numero: a[0].id, lista: a },
      });
    },

    assinarComSenhaEmLote: function() {
      var a = this.filtradosEMarcadosEAssinaveis;
      Bus.$emit("iniciarAssinaturaComSenha", a, this.removerDocumentosDesabilitados);
      // Bus.$emit('assinarComSenha', a)
    },

    removerDocumentosDesabilitados: function() {
      console.log(this.lista);
      var a = this.lista.filter(function(item) {
        return !item.disabled;
      });
      this.$set(this, "lista", a);
      console.log(this.lista);
    },

    editar: function() {
      this.$refs.etiqueta.show();
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

<template>
  <div class="container-fluid content">
    <div>
      <div class="row">
        <div class="col-md-12">
          <h4 class="text-center mt-3 mb-3">Padrões de Minutas</h4>
        </div>

        <div class="col col-sm-12" v-if="errormsg">
          <p class="alert alert-danger">{{ errormsg }}</p>
        </div>

        <div class="col col-sm-12" v-show="lista !== undefined &amp;&amp; lista.length == 0">
          <p class="alert alert-warning"><strong>Atenção!</strong> Nenhum padrão cadastrado.</p>
        </div>
      </div>

      <div v-if="false" class="row mb-3 d-print-none" v-show="lista &amp;&amp; lista.length > 0">
        <div class="col-sm-2">
          <div class="input-group">
            <div class="input-group-addon">&#128269;</div>
            <input type="text" class="form-control" placeholder="Filtrar" v-model="filtro" ng-model-options="{ debounce: 200 }" />
          </div>
        </div>
        <div v-if="filtradosEMarcados.length" class="col-sm-2 ml-sm-auto">
          <div v-if="filtradosEMarcadosEConfirmaveis.length" class="col-sm-2">
            <button class="btn btn-primary btn-block" data-style="expand-left" @click="confirmarEmLote()">
              Confirmar&nbsp;&nbsp;
              <span class="badge badge-pill badge-warning">{{ filtradosEMarcadosEConfirmaveis.length }}</span>
            </button>
          </div>
        </div>
      </div>

      <div class="row" v-show="lista &amp;&amp; lista.length > 0">
        <div
          :class="{
            col: true,
          }"
        >
          <div class="table-responsive">
            <table class="table table-sm table-striped mb-0 table-protocolo">
              <thead class="thead-dark">
                <tr>
                  <th v-if="false" class="d-print-none" style="text-align: center">
                    <input type="checkbox" id="progress_checkall" name="progress_checkall" v-model="todos" @change="marcarTodos()" />
                  </th>

                  <th>
                    <a @click="sort('id')">
                      Código
                      <span v-show="orderByField == 'id'">
                        <span v-show="!reverseSort">&#8679;</span>
                        <span v-show="reverseSort">&#8681;</span>
                      </span>
                    </a>
                  </th>
                  <th>
                    <a @click="sort('dataDeInclusao')">
                      Data Inclusão
                      <span v-show="orderByField == 'dataDeInclusao'">
                        <span v-show="!reverseSort">&#8679;</span>
                        <span v-show="reverseSort">&#8681;</span>
                      </span>
                    </a>
                  </th>
                  <th>
                    <a @click="sort('conteudo')">
                      Conteúdo
                      <span v-show="orderByField == 'conteudo'">
                        <span v-show="!reverseSort">&#8679;</span>
                        <span v-show="reverseSort">&#8681;</span>
                      </span>
                    </a>
                  </th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="r in filtrados" :key="r.id">
                  <td v-if="false" class="d-print-none" style="text-align: center">
                    <input type="checkbox" value="true" v-model="r.checked" :disabled="r.disabled" class="chk-assinar" />
                  </td>
                  <td>
                    <span v-html="r.id"></span>
                  </td>
                  <td>
                    <span v-html="r.dataDeInclusaoFormatada"></span>
                  </td>
                  <td>
                    <span v-html="r.conteudo"></span>
                  </td>
                  <td align="right">
                    <router-link
                      :to="{ name: 'Padrao', params: { numero: r.id, conteudo: r.conteudo } }"
                      class="btn btn-sm btn-primary d-print-none"
                    >
                      Editar
                    </router-link>
                    <button type="button" @click="remover(r)" class="btn btn-sm btn-success d-print-none ml-1">
                      Excluir
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="col-sm-12 d-print-none" style="padding-top: 1em;">
          <button type="button" @click="imprimir()" id="imprimir" class="btn btn-info float-right ml-3">
            Imprimir
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import UtilsBL from "../bl/utils.js";
import { Bus } from "../bl/bus.js";

export default {
  name: "padroes",
  mounted() {
    // Carragar a lista de avisos pendentes
    this.$nextTick(function() {
      this.$http.get("padrao", { block: true }).then(
        (response) => {
          this.fixPadroes(response.data);
        },
        (error) => UtilsBL.errormsg(error, this)
      );
    });
  },
  data() {
    return {
      outlineMap: {},
      orderByField: "dataaviso",
      reverseSort: false,
      todos: true,
      outlineAtivo: false,
      filtro: undefined,
      errormsg: undefined,
      lista: undefined,
    };
  },
  computed: {
    filtrados: function() {
      console.log("recalculando filtrados...", this.modified);
      if (this.lista === undefined) return [];
      var a = this.lista;
      a = UtilsBL.filtrarPorSubstring(a, this.filtro);

      a.sort((x, y) => {
        if (x[this.orderByField] !== y[this.orderByField]) {
          var r = 0;
          if (x[this.orderByField] === undefined && y[this.orderByField] !== undefined) r = -1;
          else if (x[this.orderByField] !== undefined && y[this.orderByField] === undefined) r = 1;
          else r = x[this.orderByField] < y[this.orderByField] ? -1 : 1;
          if (!this.reverseSort) r = -r;
          return r;
        }
        return 0;
      });

      return a;
    },

    filtradosEMarcados: function() {
      return this.filtrados.filter(function(item) {
        return item.checked;
      });
    },

    filtradosEMarcadosEConfirmaveis: function() {
      return this.filtrados.filter(function(item) {
        return item.idaviso;
      });
    },
  },
  methods: {
    fixPadroes: function(data) {
      if (data.list) {
        var a = [];
        for (var i = 0; i < data.list.length; i++) {
          var o = {};
          UtilsBL.applyDefauts(o, data.list[i]);
          o.checked = true;
          o.dataDeInclusaoFormatada = UtilsBL.formatJSDDMMYYYYHHMM(o.dataDeInclusao);
          a.push(o);
        }
        this.$set(this, "lista", a);
      }
      UtilsBL.logEvento("padrao", "listar padroes");
    },

    sort: function(field) {
      if (field !== this.orderByField) {
        this.orderByField = field;
        this.reverseSort = true;
      } else {
        this.reverseSort = !this.reverseSort;
      }
    },

    remover: function(o) {
      this.$http.delete("padrao/" + o.id, { block: true }).then(
        () => {
          this.lista = this.lista.filter((i) => i !== o);
        },
        (error) => UtilsBL.errormsg(error, this)
      );
    },

    marcarTodos: function() {
      var docs = this.filtrados;
      for (var i = 0; i < docs.length; i++) {
        var doc = docs[i];
        if (!doc.disabled) doc.checked = this.todos;
      }
    },

    confirmarEmLote: function() {
      var a = this.filtradosEMarcadosEConfirmaveis;
      Bus.$emit("prgStart", "Confirmando Intimações/Citações", a.length, (i) => this.confirmarAviso(a[i], true));
    },

    imprimir: function() {
      window.print();
    },
  },
};
</script>

<!-- Add 'scoped' attribute to limit CSS to this component only -->
<style scoped>
.red {
  color: red;
}

.protocolado {
  color: green;
}

.odd {
  background-color: rgba(0, 0, 0, 0.05);
}

.card-consulta-processual div p b {
  color: #fff;
}

.card-consulta-processual div p {
  margin-bottom: 0.5rem;
}

.card-consulta-processual div i {
  line-height: 3rem;
  height: 3rem;
  color: #fff;
  float: right;
  font-size: 4rem;
  margin: 0rem -0.5rem 0rem 0rem;
}

.card-text-descr {
  margin-bottom: 0;
}

article {
  border: 1px solid black;
}
</style>

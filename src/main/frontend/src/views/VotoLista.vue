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
      <div class="col-sm-auto ml-1 mb-3">
        <div class="input-group">
          <div class="input-group-prepend">
            <div class="input-group-text" id="btnGroupAddon">
              <span class="fa fa-calendar"></span>
            </div>
          </div>
          <select id="sessao" class="form-control" v-model="sessao" name="sessao">
            <option selected :value="undefined">[Todas]</option>
            <option v-for="i in sessoes" :key="i" :value="i">{{ i }}</option>
          </select>
        </div>
      </div>
      <div class="col-sm-auto ml-1">
        <div class="input-group mb-3">
          <div class="input-group-prepend">
            <div class="input-group-text" id="btnGroupAddon">
              <span class="fa fa-search"></span>
            </div>
          </div>
          <input type="text" class="form-control" placeholder="Filtrar" v-model="filtro" ng-model-options="{ debounce: 200 }" />
        </div>
      </div>
      <div class="col-auto ml-auto" v-if="(filtradosEMarcadosEVotaveis || []).length">
        <button v-if="false" type="button" @click="revisar()" class="btn btn-info ml-1 mb-3" title="">
          <span class="fa fa-eye"></span> Revisar&nbsp;&nbsp;
          <span class="badge badge-pill badge-dark">{{ filtradosEMarcadosEVotaveis.length }}</span>
        </button>
        <button type="button" @click="acompanharEmLote()" class="btn btn-success ml-1 mb-3" title="">
          <span class="fa fa-thumbs-o-up"></span> Acompanhar o Relator&nbsp;&nbsp;
          <span class="badge badge-pill badge-dark">{{ filtradosEMarcadosEVotaveis.length }}</span>
        </button>
        <button type="button" @click="divergirEmLote()" class="btn btn-danger ml-1 mb-3" title="">
          <span class="fa fa-thumbs-o-down"></span> Acompanhar a Divergência&nbsp;&nbsp;
          <span class="badge badge-pill badge-dark">{{ filtradosEMarcadosEVotaveis.length }}</span>
        </button>
        <button type="button" @click="pedirVistaEmLote()" class="btn btn-warning ml-1 mb-3" title="">
          <span class="fa fa-eye"></span> Pedir Vista&nbsp;&nbsp;
          <span class="badge badge-pill badge-dark">{{ filtradosEMarcadosEVotaveis.length }}</span>
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
        <div class="table-responsive">
          <table class="table table-sm table-borderless">
            <tbody>
              <template v-for="f in filtrados">
                <tr v-if="f.grupoExibir" :key="f.id + ':grupo1'" class="table-group">
                  <th colspan="8" class="pt-3 pb-0 pl-0">
                    <h4 class="mb-1">{{ f.grupo }}</h4>
                  </th>
                </tr>
                <tr v-if="f.grupoExibir" :key="f.id + ':grupo2'" class="table-head thead-dark">
                  <th style="text-align: center">
                    <input v-model="todos[f.grupo]" type="checkbox" name="progress_checkall" @change="marcarTodos(f.grupo)" />
                  </th>
                  <th>Item</th>
                  <th>Status</th>
                  <th class="text-center">Placar</th>
                  <th>Relator</th>
                  <th>Processo</th>
                  <th>Autor</th>
                  <th>Réu</th>
                  <th></th>
                </tr>
                <tr v-bind:class="{ odd: f.odd }" :key="f.id + ':titulo'">
                  <td class="td-middle" style="text-align: center">
                    <input type="checkbox" v-model="f.checked" :disabled="f.disabled" />
                  </td>
                  <td class="td-middle">
                    <router-link
                      :to="{
                        name: 'Voto',
                        params: { numero: f.id, lista: filtrados },
                      }"
                      >{{ f.sequencia }}</router-link
                    >
                  </td>
                  <td class="td-middle text-center">
                    <span v-if="f.statusCodigo === '9'" class="fa fa-thumbs-o-up text-success"></span>
                    <span v-if="f.statusCodigo === '10'" class="fa fa-thumbs-o-down text-danger"></span>
                    <span v-if="f.statusCodigo === '5'" class="fa fa-eye text-warning"></span>
                  </td>
                  <td class="td-middle text-center">
                    <a class="text-primary" :id="'placar' + f.id" v-if="f.acompanhamentos != '0' || f.divergencias != '0'"
                      >{{ f.acompanhamentos }} x {{ f.divergencias }}</a
                    >
                  </td>
                  <td class="td-middle">
                    <span :title="'Nome: ' + f.relator">{{ f.relator }}</span>
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
                    {{ f.descricaoDoStatus }}
                    <span v-if="f.errormsg" :class="{ red: true }">Erro {{ f.errormsg }} </span>
                  </td>
                </tr>
                <tr v-if="f.grupoEspacar" :key="f.id + ':grupo3'" class="table-group">
                  <th colspan="6" class="pb-2 pb-0 pl-0"></th>
                </tr>
              </template>
            </tbody>
          </table>

          <table v-if="false" class="table table-striped table-sm">
            <thead class="thead-dark">
              <tr>
                <th style="text-align: center">
                  <input type="checkbox" id="progress_checkall" name="progress_checkall" v-model="todos" @change="marcarTodos()" />
                </th>
                <th>Voto</th>
                <th>Relator</th>
                <th>Processo</th>
                <th>Autor</th>
                <th>Réu</th>
                <th>Data</th>
                <th>Unidade</th>
                <th>Sistema/Órgão</th>
                <th class="text-center">Placar</th>
                <th></th>
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
                <td class="td-middle">
                  <span :title="'Nome: ' + f.relator">{{ f.relator }}</span>
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
                <td class="td-middle text-center">
                  <a class="text-primary" :id="'placar' + f.id" v-if="f.acompanhamentos != '0' || f.divergencias != '0'"
                    >{{ f.acompanhamentos }} x {{ f.divergencias }}</a
                  >
                </td>
                <td class="td-middle">
                  {{ f.descricaoDoStatus }}
                  <span v-if="f.errormsg" :class="{ red: true }">Erro {{ f.errormsg }} </span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <template v-for="f in filtrados">
          <b-popover v-if="f.votosProferidos" :target="'placar' + f.id" triggers="hover" placement="left" :key="f.id">
            <template #title>Votos e Pedidos de Vista</template>
            <table class="table table-striped table-sm">
              <thead class="">
                <tr>
                  <th>Magistrado</th>
                  <th>Voto</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="v in f.votosProferidos" :key="v.magistrado">
                  <td>{{ v.magistrado }}</td>
                  <td>{{ v.voto }}</td>
                </tr>
              </tbody>
            </table>
          </b-popover>
        </template>
      </div>
    </div>
  </div>
</template>

<script>
import { Bus } from "../bl/bus.js";

export default {
  name: "Votos",
  components: {},

  async mounted() {
    this.errormsg = undefined;

    if (this.$route.params.manter) return;

    await this.$store.dispatch("carregarVotos");
    if (this.lista) {
      this.lista.forEach((i) => {
        if (i.checked && !this.todos[i.grupo]) this.$set(this.todos, i.grupo, true);
      });
    }
  },

  data() {
    return {
      voto: undefined,
      votos: [],
      todos: {},
      errormsg: undefined,
      carregando: true,
    };
  },

  computed: {
    filtro: {
      get() {
        return this.$store.state.votosFiltro;
      },
      set(value) {
        this.$store.commit("setVotosFiltro", value);
      },
    },
    sessao: {
      get() {
        return this.$store.state.votosSessao;
      },
      set(value) {
        this.$store.commit("setVotosSessao", value);
      },
    },
    lista() {
      return this.$store.state.votos ? this.$store.state.votos : [];
    },
    sessoes() {
      if (!this.lista) return [];
      const a = [];
      let last = undefined;
      this.lista.forEach((i) => {
        if (i.grupo === last) return;
        last = i.grupo;
        a.push(last);
      });
      return a;
    },
    filtrados: function() {
      return this.$store.getters.votosFiltrados;
    },

    filtradosEMarcados: function() {
      return this.filtrados.filter(function(item) {
        return item.checked;
      });
    },

    filtradosEMarcadosEVotaveis: function() {
      return this.filtradosEMarcados.filter(function(item) {
        return true;
      });
    },
  },

  methods: {
    marcarTodos(grupo) {
      const docs = this.filtrados;
      for (let i = 0; i < docs.length; i++) {
        const doc = docs[i];
        if (doc.grupo === grupo) doc.checked = this.todos[grupo] && !doc.statusCodigo;
      }
    },

    revisar: function() {
      var a = this.filtradosEMarcadosEVotaveis;
      this.$router.push({
        name: "Voto",
        params: { numero: a[0].id, lista: a },
      });
    },

    acompanharEmLote: function() {
      var a = this.filtradosEMarcadosEVotaveis;
      if (a.length > 1)
        this.$bvModal
          .msgBoxConfirm("Tem certeza que deseja acompanhar o relator em " + a.length + " votos?", {
            title: "Acompanhar o Relator",
            size: "sm",
            okVariant: "warning",
            okTitle: "Prosseguir",
            cancelTitle: "Cancelar",
            centered: true,
          })
          .then((value) => {
            if (value) Bus.$emit("acompanhar", a);
          })
          .catch((err) => {});
      else Bus.$emit("acompanhar", a);
    },

    divergirEmLote: function() {
      var a = this.filtradosEMarcadosEVotaveis;
      if (a.length > 1)
        this.$bvModal
          .msgBoxConfirm("Tem certeza que deseja acompanhar a divergência em " + a.length + " votos?", {
            title: "Acompanhar a Divergência",
            size: "sm",
            okVariant: "warning",
            okTitle: "Prosseguir",
            cancelTitle: "Cancelar",
            centered: true,
          })
          .then((value) => {
            if (value) Bus.$emit("divergir", a);
          })
          .catch((err) => {});
      else Bus.$emit("divergir", a);
    },

    pedirVistaEmLote: function() {
      var a = this.filtradosEMarcadosEVotaveis;
      if (a.length > 1)
        this.$bvModal
          .msgBoxConfirm("Tem certeza que deseja pedir vista em " + a.length + " votos?", {
            title: "Pedir Vista",
            size: "sm",
            okVariant: "warning",
            okTitle: "Prosseguir",
            cancelTitle: "Cancelar",
            centered: true,
          })
          .then((value) => {
            if (value) Bus.$emit("pedirVista", a);
          })
          .catch((err) => {});
      else Bus.$emit("pedirVista", a);
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

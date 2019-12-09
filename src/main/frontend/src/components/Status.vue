<template>
  <div class="container-fluid content">
    <div class="row">
      <div class="col col-sm-12" v-if="errormsg">
        <p class="alert alert-danger">
          {{ errormsg }}
        </p>
      </div>

      <div class="col-md-12">
        <h4 class="mt-3 mb-3">Detalhes do Usuário</h4>
      </div>
    </div>

    <div class="row" v-if="$parent.jwt">
      <div class="form-group col-md-4">
        <my-input
          :disabled="true"
          name="nome"
          label="Nome"
          v-model="$parent.jwt.name"
          :edit="true"
        ></my-input>
      </div>
      <div class="form-group col-md-4">
        <my-input
          :disabled="true"
          name="cpf"
          v-model="$parent.jwt.cpf"
          label="CPF"
          :edit="true"
          mask="999.999.999-99"
        ></my-input>
      </div>
      <div class="form-group col-md-4">
        <my-input
          :disabled="true"
          name="email"
          v-model="$parent.jwt.email"
          label="E-mail"
          :edit="true"
        ></my-input>
      </div>
    </div>

    <!-- QUANTIDADE POR DATA -->
    <div class="row" v-if="$parent.jwt">
      <div class="col-md-12">
        <h4 class="mt-3 mb-3">Identificação do Usuário nos Sistemas</h4>
      </div>
      <div class="col col-12">
        <div class="table-responsive">
          <table class="table table-striped mb-0">
            <thead class="thead-dark">
              <tr>
                <th>Sistema/Órgão</th>
                <th>Origem</th>
                <th>Perfil</th>
                <th>Unidade</th>
                <th>Entidade</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="u in $parent.jwt.user" :key="u.id">
                <td>
                  <span :title="'Sigla do Sistema: ' + u.sistema">{{
                    $parent.test.properties[
                      "balcaovirtual." + u.sistema + ".name"
                    ]
                  }}</span>
                </td>
                <td>
                  <span :title="'ID Usuário: ' + u.ieusu">{{
                    u.origin === "int" ? "interno" : "externo"
                  }}</span>
                </td>
                <td>
                  <span>{{ u.perfil }}</span>
                </td>
                <td>
                  <span :title="'ID Unidade: ' + u.ieunidade">{{
                    u.unidade
                  }}</span>
                </td>
                <td>
                  <span :title="'ID Entidade: ' + u.ieentidade">{{
                    u.entidade
                  }}</span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-md-12">
        <h4 class="mt-3 mb-3">Sistemas Conectados ao Balcão Virtual</h4>
      </div>
    </div>

    <!-- QUANTIDADE POR DATA -->
    <div class="row">
      <div class="col col-12">
        <div class="table-responsive">
          <table class="table table-striped mb-0">
            <thead class="thead-dark">
              <tr>
                <th>Sistema/Órgão</th>
                <th>API</th>
                <th>MNI</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="s in $parent.sistemas" :key="s.id">
                <td>
                  <span :title="'Identificador: ' + s">{{
                    $parent.test.properties["balcaovirtual." + s + ".name"]
                  }}</span>
                </td>
                <td>
                  <span v-bind:class="classeDoTeste(apis, s)">
                    {{ mensagemDoTeste(apis, s) }}
                  </span>
                </td>
                <td>
                  <span v-bind:class="classeDoTeste(mnis, s)">
                    {{ mensagemDoTeste(mnis, s) }}
                  </span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import UtilsBL from "../bl/utils.js";
import MyInput from "./MyInput";

export default {
  name: "aviso-confirmado-recentes",

  mounted() {
    this.$nextTick(() => {
      this.$http.get("test", { block: false }).then(
        response => this.processaRespostaDoTeste(response),
        error => {
          if (error.status === 503) this.processaRespostaDoTeste(error);
          else UtilsBL.errormsg(error, this);
        }
      );
    });
  },

  data() {
    return {
      test: undefined,
      apis: {},
      mnis: {},
      errormsg: undefined
    };
  },

  methods: {
    classeDoTeste: function(v, s) {
      if (v === undefined || v[s] === undefined) return "";
      if (v[s].available) return "text-success";
      else return "text-danger";
    },

    mensagemDoTeste: function(v, s) {
      if (v === undefined || v[s] === undefined) return "Testando...";
      if (v[s].available) return "OK!";
      else return v[s].errormsg;
    },

    processaRespostaDoTeste: function(response) {
      var dependencies = response.data.dependencies;
      for (var i = 0; i < this.$parent.sistemas.length; i++) {
        var sistema = this.$parent.sistemas[i];
        var sistemaSlug = sistema.replace(/\./g, "-");
        for (var j = 0; j < dependencies.length; j++) {
          if (dependencies[j].service === sistemaSlug + "-api")
            this.$set(this.apis, sistema, dependencies[j]);
          else if (dependencies[j].service === sistemaSlug + "-mni")
            this.$set(this.mnis, sistema, dependencies[j]);
        }
      }
    }
  },

  components: {
    MyInput
  }
};
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

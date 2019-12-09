<template>
  <div>
    <validation-observer v-slot="{ invalid }">
      <b-modal
        id="cota"
        ref="modal"
        v-model="showModal"
        title="Assinatura com Senha"
        cancel-title="Cancelar"
        ok-title="Assinar"
        :ok-disabled="invalid"
        @show="resetModal"
        @hidden="resetModal"
        @ok="handleOk"
      >
        <form>
          <div class="row">
            <div class="form-group col col-sm-6">
              <my-input
                label="Usuário"
                id="username"
                name="username"
                v-model="username"
                validate="required"
                disabled="true"
              ></my-input>
            </div>
            <div class="form-group col col-sm-6">
              <my-input
                type="password"
                label="Senha"
                id="password"
                name="password"
                v-model="password"
                validate="required"
              ></my-input>
            </div>
            <div class="col col-sm-12">
              <small id="usernameHelp" class="form-text text-muted">
                <strong>Atenção</strong>! Ao clicar em 'Assinar',
                {{
                  documentos && documentos.length > 1
                    ? "serão assinados " + documentos.length + " documentos"
                    : "será assinado 1 documento"
                }}.</small
              >
            </div>
          </div>
        </form>
      </b-modal>
    </validation-observer>
  </div>
</template>

<script>
import { Bus } from "../bl/bus.js";

export default {
  name: "assinatura",

  mounted() {},

  data() {
    return {
      showModal: false,
      errormsg: undefined,
      username: undefined,
      password: undefined,
      documentos: undefined,
      invalid: undefined
    };
  },

  methods: {
    show: function(documentos, cont) {
      console.log("show");
      this.showModal = true;
      this.errormsg = undefined;
      this.username = this.$parent.jwt.username;
      this.documentos = documentos;
      this.cont = cont;
    },

    resetModal: function() {},

    handleOk: function() {
      Bus.$emit(
        "assinarComSenha",
        this.documentos,
        this.username,
        this.password,
        this.cont
      );
    }
  }
};
</script>

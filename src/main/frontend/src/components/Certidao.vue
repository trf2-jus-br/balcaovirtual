<template>
  <div class="hello">
    <!--=== Profile ===-->
    <div class="container content profile">
      <div class="row pt-5" v-if="errormsg">
        <div class="col col-sm-12">
          <p class="alert alert-danger">
            {{ errormsg }}
          </p>
        </div>
      </div>

      <div class="row pt-5" v-if="warningmsg">
        <div class="col col-sm-12">
          <p class="alert alert-warning">
            {{ warningmsg }}
          </p>
        </div>
      </div>

      <div v-if="!errormsg &amp;&amp; (numero || html)">
        <div class="row">
          <div class="col-md-12">
            <h4 class="text-center mt-3 mb-3">Certidão {{ numero }}</h4>
          </div>
        </div>

        <div
          v-if="tipo == 'NEGATIVO' || tipo == 'POSITIVA'"
          class="row no-gutters mt-2"
        >
          <div class="col col-auto ml-auto mb-3">
            <button
              type="button"
              @click="imprimir()"
              id="imprimir"
              class="btn btn-info d-print-none"
            >
              <span class="fa fa-print"></span>
              Imprimir
            </button>
          </div>
        </div>

        <div class="row" v-if="tipo == 'REQUERER'">
          <div class="col col-sm-12">
            <p class="alert alert-warning" role="alert">
              <span v-html="html"></span>
              <br />

              <button
                type="button"
                @click="requerer()"
                id="requerer"
                class="btn btn-warning mt-3 mb-3"
              >
                Requerer
              </button>
            </p>
          </div>
        </div>

        <div class="row" v-if="tipo == 'REQUERIDO' || tipo == 'AUTENTICADO'">
          <div class="col col-sm-12">
            <p class="alert alert-info" role="alert" v-html="html"></p>
          </div>
        </div>

        <div class="row" v-if="tipo == 'NEGATIVO' || tipo == 'POSITIVA'">
          <div class="col col-sm-12">
            <p
              :class="{
                alert: true,
                'alert-success': tipo == 'NEGATIVO',
                'alert-danger': tipo == 'POSITIVA'
              }"
              role="alert"
              v-html="html"
            ></p>
          </div>
        </div>

        <div ref="conteudo" class="d-none" v-html="html"></div>
      </div>
    </div>
  </div>
</template>

<script>
import he from "he";

export default {
  name: "certidao",
  mounted() {
    this.$nextTick(function() {
      if (this.$route.name === "Emitir Certidão") this.emitir();
      if (this.$route.name === "Autenticar Certidão") this.autenticar();
      if (this.$route.name === "Reimprimir Certidão") this.reimprimir();
    });
  },
  data() {
    return {
      token: this.$route.params.token
        ? this.$route.params.token
        : this.$route.query.token,
      requisitante: this.$route.params.requisitante,
      numero: this.$route.params.numero,
      cpfcnpj: this.$route.params.cpfcnpj,
      sistema: this.$route.params.sistema
        ? this.$route.params.sistema
        : this.$route.query.sistema,
      tipo: undefined,
      html: undefined,
      nome: undefined,
      qs: undefined,
      params: undefined,
      errormsg: undefined,
      warningmsg: undefined,
      certidao: undefined
    };
  },
  methods: {
    emitir: function() {
      this.warningmsg = "Processando, aguarde...";
      this.$http
        .post(
          "certidao/emitir/" +
            this.requisitante +
            "/" +
            this.cpfcnpj +
            "?sistema=" +
            this.sistema +
            "&token=" +
            this.token,
          undefined,
          { block: true }
        )
        .then(
          response => {
            this.warningmsg = undefined;
            this.tipo = response.data.tipo;
            this.numero = response.data.numero;
            this.html = response.data.html;
            this.nome = response.data.nome;
            this.qs = response.data.qs;
            this.params = response.data.params;
          },
          error => {
            this.warningmsg = undefined;
            this.errormsg = error.data.errormsg;
          }
        );
    },

    requerer: function() {
      this.warningmsg = "Processando, aguarde...";
      this.$http
        .post(
          "certidao/requerer/" +
            this.requisitante +
            "/" +
            this.cpfcnpj +
            "?sistema=" +
            this.sistema +
            "&token=" +
            this.token +
            "&nome=" +
            encodeURIComponent(this.nome) +
            "&params=" +
            this.params,
          { block: true }
        )
        .then(
          response => {
            this.warningmsg = undefined;
            this.tipo = response.data.tipo;
            this.numero = response.data.numero;
            this.html = response.data.html;
          },
          error => {
            this.warningmsg = undefined;
            this.errormsg = error.data.errormsg;
          }
        );
    },

    autenticar: function() {
      this.warningmsg = "Processando, aguarde...";
      this.$http
        .get(
          "certidao/autenticar/" +
            this.numero +
            "/" +
            this.cpfcnpj +
            "?sistema=" +
            this.sistema +
            "&token=" +
            this.token
        )
        .then(
          response => {
            this.warningmsg = undefined;
            this.tipo = response.data.tipo;
            this.numero = response.data.numero;
            this.html = response.data.html;
          },
          error => {
            this.warningmsg = undefined;
            this.errormsg = error.data.errormsg;
          }
        );
    },

    reimprimir: function() {
      this.warningmsg = "Processando, aguarde...";
      this.$http
        .get(
          "certidao/reimprimir/" +
            this.numero +
            "/" +
            this.cpfcnpj +
            "?sistema=" +
            this.sistema +
            "&token=" +
            this.token
        )
        .then(
          response => {
            this.warningmsg = undefined;
            this.tipo = response.data.tipo;
            this.numero = response.data.numero;
            this.html = response.data.html;
          },
          error => {
            this.warningmsg = undefined;
            this.errormsg = error.data.errormsg;
          }
        );
    },

    imprimir_old: function() {
      window.print();
    },

    imprimir: function(disposition) {
      var filename = (this.numero + "-" + this.cpfcnpj + "-" + this.sistema).replace(/\./g, "-");
      filename = "certidao-" + filename + ".pdf";
      this.$http
        .post(
          "imprimir/" + filename,
          {
            html: he.encode(this.$refs["conteudo"].innerHTML, {
              allowUnsafeSymbols: true,
              useNamedReferences: true
            }),
            disposition:
              disposition === "attachment"
                ? "?disposition=attachment"
                : "?disposition=inline"
          },
          {
            headers: {
              "Content-Type": "application/json"
            },
            block: true
          }
        )
        .then(
          response => {
            window.location =
              process.env.VUE_APP_API_URL + "/imprimir/" + filename;
          },
          error => {
            this.warningmsg = undefined;
            this.errormsg = error.data.errormsg;
          }
        );
    }
  }
};
</script>

<!-- Add 'scoped' attribute to limit CSS to this component only -->
<style scoped></style>

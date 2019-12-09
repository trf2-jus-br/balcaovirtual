<template>
  <!--=== Profile ===-->
  <div class="container content profile">
    <div class="row">
      <div class="col-md-12">
        <h4 class="text-center mt-3 mb-3">
          Consulta Processual {{ $parent.jwt ? "" : "Pública" }}
        </h4>
      </div>
    </div>

    <div>
      <form class="row justify-content-center">
        <div class="col col-sm-12 col-md-6" v-if="!avancada">
          <div class="jumbotron d-block mx-auto pt-5 pb-5 mb-3">
            <p v-if="errormsg" class="alert alert-danger" role="alert">
              {{ errormsg }}
            </p>
            <div>
              <div class="row">
                <div class="col">
                  <div class="form-group">
                    <label for="numero">Número do Processo</label>
                    <input
                      type="text"
                      class="form-control"
                      id="numero"
                      placeholder=""
                      v-model="numero"
                      autofocus
                    />
                  </div>
                </div>
              </div>
              <div class="row pt-3">
                <div class="col" v-if="$parent.test.properties">
                  <button
                    v-if="false"
                    class="btn btn-secondary"
                    @click="avancada = true"
                  >
                    Pesquisa Avançada...
                  </button>
                  <invisible-recaptcha
                    ref="captcha"
                    v-if="!$parent.jwt"
                    :sitekey="sitekey"
                    :validate="captchaValidate"
                    :callback="consultar"
                    class="btn btn-warning float-right"
                    type="button"
                    id="consultar"
                    :disabled="
                      recaptchaLoading ||
                        numero === undefined ||
                        numero.trim() === ''
                    "
                    badge="bottomleft"
                  >
                    Consultar
                  </invisible-recaptcha>
                  <button
                    v-if="$parent.jwt"
                    :disabled="numero === undefined || numero.trim() === ''"
                    @click.prevent="mostrarProcesso(numero)"
                    class="btn btn-primary float-right"
                  >
                    Consultar
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="col-12 text-center text-muted" v-if="!$parent.jwt">
          Para acessar peças restritas é necessário fazer o
          <router-link :to="{ name: 'Login' }" tag="a">login</router-link>.
        </div>
        <div v-if="avancada">
          <div class="row">
            <div
              class="col col-sm-12 alert alert-info alert-dismissible fade show"
              role="alert"
            >
              <button
                type="button"
                class="close"
                data-dismiss="alert"
                aria-label="Close"
              >
                <span aria-hidden="true">&times;</span>
              </button>
              <strong>Atenção!</strong> Preencha um ou mais campos abaixo e
              clique em "Pesquisar".
            </div>
            <div class="form-group col col-md-4">
              <label for="numero">Número do Processo</label>
              <input
                type="text"
                class="form-control"
                id="numero"
                placeholder=""
                v-model="numero"
              />
            </div>
            <div class="form-group col col-md-4">
              <label for="cpfcnpj">CPF/CNPJ da Parte</label>
              <input
                type="text"
                class="form-control"
                id="cpfcnpj"
                placeholder=""
                v-model="cpfcnpj"
              />
            </div>
            <div class="form-group col col-md-4">
              <label for="oab">Registro da OAB do Representante</label>
              <input
                type="text"
                class="form-control"
                id="oab"
                placeholder=""
                v-model="oab"
              />
            </div>
          </div>
          <div class="row">
            <div class="form-group col col-md-6">
              <label for="parte">Nome da Parte</label>
              <input
                type="text"
                class="form-control"
                id="parte"
                placeholder=""
                v-model="parte"
              />
            </div>
            <div class="form-group col col-md-6">
              <label for="procurador">Nome do Procurador</label>
              <input
                type="text"
                class="form-control"
                id="procurador"
                placeholder=""
                v-model="procurador"
              />
            </div>
            <div class="form-group col col-md-2">
              <label for="sistema">Sistema</label>
              <select class="form-control" id="sistema" v-model="sistema">
                <option selected value="">[Todos]</option>
                <option>br.jus.jfrj.eproc</option>
                <option>br.jus.trf2.apolo</option>
                <option>br.jus.jfrj.apolo</option>
                <option>br.jus.jfes.apolo</option>
              </select>
            </div>

            <div class="form-group col col-md-4">
              <label for="localidade">Localidade</label>
              <select class="form-control" id="orgao" v-model="orgao">
                <option value="" selected>[Todas]</option>
                <option value="1">Rio de Janeiro</option>
                <option value="2">Niterói</option>
                <option value="3">Campos dos Goytacazes</option>
                <option value="4">Volta Redonda</option>
                <option value="5">Nova Friburgo</option>
                <option value="6">Petrópolis</option>
                <option value="7">Itaboraí</option>
                <option value="8">São Pedro da Aldeia</option>
                <option value="9">Resende</option>
                <option value="10">São João de Meriti</option>
                <option value="11">Angra dos Reis</option>
                <option value="12">Itaperuna</option>
                <option value="13">Três Rios</option>
                <option value="14">Magé</option>
                <option value="15">Teresópolis</option>
                <option value="16">Macaé</option>
                <option value="17">São Gonçalo</option>
                <option value="18">Duque de Caxias</option>
                <option value="19">Barra do Piraí</option>
                <option value="20">Nova Iguaçu</option>
                <option value="21">Campo Grande</option>
                <option value="50">Núcleo de Conciliação</option>
                <option value="90">Rio de Janeiro - Turma Recursal</option>
              </select>
            </div>
            <div class="form-group col col-md-3">
              <label for="numeroOriginario"
                >Número do Processo Originário</label
              >
              <input
                type="text"
                class="form-control"
                id="numeroOriginario"
                placeholder=""
                v-model="numeroOriginario"
              />
            </div>
            <div class="form-group col col-md-3">
              <label for="inquerito col col-md-4">Número do Inquérito</label>
              <input
                type="text"
                class="form-control"
                id="inquerito"
                placeholder=""
                v-model="inquerito"
              />
            </div>

            <div class="form-check col col-md-4">
              <label class="form-check-label">
                <input
                  type="checkbox"
                  class="form-check-input"
                  v-model="baixados"
                />
                Incluir Processos Baixados
              </label>
            </div>
          </div>
          <div class="row">
            <div class="col">
              <button class="btn btn-primary float-right">Pesquisar</button>
            </div>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import ProcessoBL from "../bl/processo.js";
import InvisibleRecaptcha from "vue-invisible-recaptcha";

export default {
  name: "consulta-simples",

  mounted() {
    if (this.$route.params.numero) {
      this.numero = this.$route.params.numero;
      if (this.$parent.jwt) this.mostrarProcesso(this.numero);
      else if (this.$route.query.token)
        this.mostrarProcesso(this.numero, undefined, this.$route.query.token);
      else this.fire(this.$refs.captcha);
    }
  },

  data() {
    return {
      recaptchaLoading: false,
      errormsg: undefined,
      avancada: false,
      numero: undefined,
      cpfcnpj: undefined,
      procurador: undefined,
      orgao: undefined,
      numeroOriginario: undefined,
      baixados: undefined,
      parte: undefined,
      oab: undefined,
      inquerito: undefined
    };
  },

  beforeRouteUpdate(to, from, next) {
    // this.numero = to.params.numero
    // if (this.$parent.jwt) this.mostrarProcesso(this.numero)
    // else this.$refs.captcha.click()
    next();
  },

  computed: {
    sitekey: function() {
      if (this.$parent.test && this.$parent.test.properties)
        return this.$parent.test.properties["balcaovirtual.recaptcha.site.key"];
      return "waiting...";
    }
  },

  methods: {
    consultar: function(recaptchaToken) {
      this.recaptchaLoading = false;
      this.mostrarProcesso(this.numero, recaptchaToken);
    },
    mostrarProcesso: function(numero, recaptchaToken, token) {
      var n = ProcessoBL.somenteNumeros(this.numero);
      if (n === "") return;
      this.$http
        .get(
          "processo/" +
            n +
            "/validar" +
            (recaptchaToken ? "?captcha=" + recaptchaToken : "") +
            (token ? "?token=" + token : ""),
          { block: true, blockmin: 0, blockmax: 20 }
        )
        .then(
          response => {
            var p =
              response.data.list && response.data.list.length === 1
                ? response.data.list[0]
                : {};
            if (p.unidade && !p.usuarioautorizado) {
              this.errormsg =
                "Processo em segredo de justiça. (" + p.unidade + ")";
              return;
            }
            if (!p.numero) {
              this.errormsg = `Processo "${this.numero}" não encontrado`;
              return;
            }
            this.$router.push({
              name: "Processo",
              params: {
                numero: p.numero,
                token: response.data.token,
                validar: response.data
              }
            });
          },
          error => {
            this.errormsg =
              error.data.errormsg ||
              `Erro obtendo informações sobre o processo "${this.numero}"`;
          }
        );
    },
    captchaValidate: function() {
      this.recaptchaLoading = true;
    },
    fire: function(captcha) {
      setTimeout(() => {
        if (captcha.loaded) captcha.click();
        else this.fire(captcha);
      }, 200);
    }
  },
  components: {
    "invisible-recaptcha": InvisibleRecaptcha
  }
};
</script>

<!-- Add 'scoped' attribute to limit CSS to this component only -->
<style scoped></style>

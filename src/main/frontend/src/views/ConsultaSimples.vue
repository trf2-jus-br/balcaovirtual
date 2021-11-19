<template>
  <!--=== Profile ===-->
  <div class="container content profile">
    <div class="row">
      <div class="col-md-12">
        <h4 class="text-center mt-3 mb-3">Consulta Processual {{ $parent.jwt ? "" : "Pública" }}</h4>
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
                    <input type="text" class="form-control" id="numero" placeholder="" v-model="numero" autofocus />
                  </div>
                </div>
              </div>
              <div class="row pt-3">
                <div class="col" v-if="$parent.test.properties">
                  <button class="btn btn-secondary" @click="avancada = true">
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
                    :disabled="recaptchaLoading || numero === undefined || numero.trim() === ''"
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
        <div v-if="avancada">
          <div class="row">
            <div class="col col-12">
              <p v-if="errormsg" class="alert alert-danger" role="alert">
                {{ errormsg }}
              </p>
              <div v-else class="alert alert-info alert-dismissible fade show" role="alert">
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
                <strong>Atenção!</strong> Preencha <u>apenas um dos campos</u> abaixo e clique em "Pesquisar". Partes com muitos processos retornarão até 100 processos por sistema/órgão, consultar por número de processo caso o resultado não seja o esperado.
              </div>
            </div>
            <div class="form-group col col-md-6">
              <label for="numero">Número do Processo (<b>Até 100 números separados por vírgula ","</b>)</label>
              <input type="text" class="form-control" id="numero" placeholder="" v-model="numero" @input="cpfcnpj = undefined; parte = undefined" />
            </div>
            <div class="form-group col col-md-6">
              <label for="cpfcnpj">CPF/CNPJ da Parte</label>
              <input type="text" class="form-control" id="cpfcnpj" placeholder="" v-model="cpfcnpj" @input="numero = undefined; parte = undefined" />
            </div>
            <div v-if="false" class="form-group col col-md-4">
              <label for="oab">Registro da OAB do Representante</label>
              <input type="text" class="form-control" id="oab" placeholder="" v-model="oab" />
            </div>
          </div>
          <div class="row">
            <div class="form-group col col-md-12">
              <label for="parte"><b>Nome Completo</b> da Parte</label>
              <input type="text" class="form-control" id="parte" placeholder="" v-model="parte" @input="cpfcnpj = undefined; numero = undefined" />
            </div>
            <div v-if="false" class="form-group col col-md-6">
              <label for="procurador">Nome do Procurador</label>
              <input type="text" class="form-control" id="procurador" placeholder="" v-model="procurador" />
            </div>
            <div v-if="false" class="form-group col col-md-2">
              <label for="sistema">Sistema</label>
              <select class="form-control" id="sistema" v-model="sistema">
                <option selected value="">[Todos]</option>
                <option>br.jus.jfrj.eproc</option>
                <option>br.jus.trf2.apolo</option>
                <option>br.jus.jfrj.apolo</option>
                <option>br.jus.jfes.apolo</option>
              </select>
            </div>

            <div v-if="false" class="form-group col col-md-4">
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
            <div v-if="false" class="form-group col col-md-3">
              <label for="numeroOriginario">Número do Processo Originário</label>
              <input type="text" class="form-control" id="numeroOriginario" placeholder="" v-model="numeroOriginario" />
            </div>
            <div v-if="false" class="form-group col col-md-3">
              <label for="inquerito col col-md-4">Número do Inquérito</label>
              <input type="text" class="form-control" id="inquerito" placeholder="" v-model="inquerito" />
            </div>

            <div v-if="false" class="form-check col col-md-4">
              <label class="form-check-label">
                <input type="checkbox" class="form-check-input" v-model="baixados" />
                Incluir Processos Baixados
              </label>
            </div>
          </div>

          <div class="row">
            <div class="col-sm-12">
              <invisible-recaptcha
                ref="captcha"
                v-if="!$parent.jwt"
                :sitekey="sitekey"
                :validate="captchaValidate"
                :callback="consultarProcessosComToken"
                class="btn btn-warning float-right"
                type="button"
                id="consultar"
                :disabled="recaptchaLoading || !dadosValidos"
                badge="bottomleft"
              >
                Consultar
              </invisible-recaptcha>
              <button
                v-if="$parent.jwt"
                :disabled="!dadosValidos"
                @click.prevent="consultarProcessos()"
                class="btn btn-primary float-right"
              >
                Consultar
              </button>
            </div>
          </div>
          <div class="row mt-4" v-if="processos && processos.length > 0">
            <div class="col-sm-12">
              <div class="table-responsive">
                <table class="table table-striped table-sm">
                  <thead class="thead-dark">
                    <tr>
                      <th>Processo</th>
                      <th>Autor</th>
                      <th>Réu</th>
                      <th v-if="$parent.jwt">Último Movimento</th>
                      <th>Sistema/Órgão</th>
                      <th v-if="$parent.jwt"> Unidade</th>
                      <th v-if="$parent.jwt"> Suporte</th>
                      <th v-if="$parent.jwt">Acesso</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="p in processos" :key="p.numero">
                      <td>
                        <span class=" unbreakable">
                          <router-link
                            :to="{
                              name: 'Consultar Processo',
                              params: { numero: p.numero},
                              query: {sistema:p.sistema, avisos: $parent.cAvisos, token: token },
                            }"
                            target="_blank"
                            >{{ p.numeroFormatado }}</router-link
                          >
                        </span>
                      </td>
                      <td>{{ p.autor }}</td>
                      <td>{{ p.reu }}</td>
                      <td v-if="$parent.jwt">
                        <span
                          :class="{
                            destaque: p.recente === undefined || (p.dataultimomovimento !== undefined && p.recente < p.dataultimomovimento),
                          }"
                          v-html="p.dataultimomovimentoFormatada"
                        ></span>
                      </td>
                      <td>
                        <span :title="'Sigla do Sistema: ' + p.sistema">{{
                          $parent.test.properties["balcaojus." + p.sistema + ".name"]
                        }}</span>
                      </td>
                      <td v-if="$parent.jwt">{{ p.unidade }}</td>
                      <td v-if="$parent.jwt">{{ p.digitalFormatado }}</td>
                      <td v-if="$parent.jwt">{{ p.acesso }}</td>
                      
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
        <div class="col-12 text-center text-muted" v-if="!$parent.jwt">
          Para acessar peças restritas é necessário fazer o
          <router-link :to="{ name: 'Login' }" tag="a">login</router-link>.
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import ProcessoBL from "../bl/processo.js";
import UtilsBL from "../bl/utils.js";
import InvisibleRecaptcha from "vue-invisible-recaptcha";

export default {
  name: "consulta-simples",

  mounted() {
    if (this.$route.params.numero) {
      this.numero = this.$route.params.numero;
      if (this.$parent.jwt) this.mostrarProcesso(this.numero);
      else if (this.$route.query.token) this.mostrarProcesso(this.numero, undefined, this.$route.query.token);
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
      inquerito: undefined,
      processos: undefined,
      token: undefined,
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
      if (this.$parent.test && this.$parent.test.properties) return this.$parent.test.properties["balcaojus.recaptcha.site.key"];
      return "waiting...";
    },
    dadosValidos() {
      return (
        (this.numero && this.numero.trim() !== "") ||
        (this.cpfcnpj && this.cpfcnpj.trim() !== "") ||
        (this.parte && this.parte.trim() !== "")
      );
    },
  },

  methods: {
    consultar: function(recaptchaToken) {
      this.recaptchaLoading = false;
      this.mostrarProcesso(this.numero, recaptchaToken);
    },
    mostrarProcesso: function(numero, recaptchaToken, token) {
      this.token = undefined;
      var n = ProcessoBL.somenteNumerosOuVirgulas(this.numero);
      if (n === "") return;
      this.$http
        .get("processo/validar?numero=" + n + (recaptchaToken ? "&captcha=" + recaptchaToken : "") + (token ? "&token=" + token : ""), {
          block: true,
          blockmin: 0,
          blockmax: 20,
        })
        .then(
          (response) => {
            var p = response.data.list && response.data.list.length === 1 ? response.data.list[0] : {};
            if (p.unidade && !p.usuarioautorizado) {
              this.errormsg = "Processo em segredo de justiça. (" + p.unidade + ")";
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
                validar: response.data,
              },
            });
          },
          (error) => {
            this.errormsg = error.data.errormsg || `Erro obtendo informações sobre o processo "${this.numero}"`;
          }
        );
    },

    consultarProcessosComToken: function(recaptchaToken) {
      this.recaptchaLoading = false;
      this.consultarProcessos(recaptchaToken);
    },

    consultarProcessos: function(recaptchaToken, token) {
      var n = ProcessoBL.somenteNumerosOuVirgulas(this.numero);
      var params;
      if (this.numero && this.numero.trim() !== "") params = "numero=" + n;
      else if (this.cpfcnpj && this.cpfcnpj.trim() !== "") params = "documento=" + this.cpfcnpj.trim();
      else if (this.parte && this.parte.trim() !== "") params = "nome=" + this.parte.trim();
      if (!params) return;
      this.errormsg = undefined;
      this.processos = undefined;
      this.token = undefined;
      this.$http
        .get("processo/validar?" + params + (recaptchaToken ? "&captcha=" + recaptchaToken : "") + (token ? "&token=" + token : ""), {
          block: true,
          blockmin: 0,
          blockmax: 20,
        })
        .then(
          (response) => {
            if (!response.data.list || response.data.list.length === 0) {
              this.errormsg = `Nenhum processo encontrado`;
              return;
            }
            if (response.data.list.length === 1) {
              var p = response.data.list[0];
              if (p) {
                if (p.unidade && !p.usuarioautorizado) {
                  this.errormsg = "Processo em segredo de justiça. (" + p.unidade + ")";
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
                    validar: response.data,
                  },
                });
              }
            } else {
              this.processos = [];
              for (var i = 0; i < response.data.list.length; i++) {
                var processo = response.data.list[i];
                this.processos.push(this.fixProcesso(processo));
              }
              this.token = response.data.token;
            }
          },
          (error) => {
            this.errormsg = error.data.errormsg || `Erro obtendo informações sobre o processo "${this.numero}"`;
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
    },
    fixProcesso: function(p) {
      UtilsBL.applyDefauts(p, {
        dataultimomovimento: undefined,
        dataultimomovimentoFormatada: undefined,
        numero: undefined,
        numeroFormatado: undefined,
        sistema: undefined,
        unidade: undefined,
        usuarioautorizado: undefined,
        segredodejustica: undefined,
        segredodejusticadesistema: undefined,
        segredodejusticaabsoluto: undefined,
        acesso: undefined,
        digital: undefined,
        digitalFormatado: undefined,
        validade: undefined,
        checked: true,
        disabled: false,
        state: undefined,
        errormsg: undefined,
        perc: undefined,
        bytes: undefined,
        status: undefined,
        uuid: undefined,
        inbox: undefined,
        favorito: undefined,
        recente: undefined,
        validado:true,
      });
      if (p.dataultimomovimento !== undefined) {
        p.dataultimomovimentoFormatada = UtilsBL.formatJSDDMMYYYYHHMM(p.dataultimomovimento);
      }
      if (p.numero !== undefined) {
        p.numero = ProcessoBL.somenteNumeros(p.numero);
        p.numeroFormatado = ProcessoBL.formatarProcesso(p.numero);
      }
      if (p.validado) {
        if (p.digital !== undefined) p.digitalFormatado = p.digital ? "Digital" : "Físico";
        if ((p.segredodejusticaabsoluto || p.segredodejusticadesistema) && p.usuarioautorizado) p.acesso = "Autorizado";
        else if (p.segredodejusticaabsoluto) p.acesso = "Segredo Absoluto";
        else if (p.segredodejusticadesistema) p.acesso = "Segredo de Sistema";
        else p.acesso = "Público";
      }
      return p;
    },
  },
  components: {
    "invisible-recaptcha": InvisibleRecaptcha,
  },
};
</script>

<!-- Add 'scoped' attribute to limit CSS to this component only -->
<style scoped></style>

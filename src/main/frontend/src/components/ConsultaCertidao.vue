<template>
  <!--=== Profile ===-->
  <div class="container content profile">

    <div class="row">
      <div class="col-md-12">
        <h4 class="text-center mt-3 mb-3">Certidões</h4>
      </div>
    </div>

    <div class="row justify-content-center d-print-none">
      <div class="col-sm-auto mb-3">
        <div class="btn-group">
          <label class="btn" :class="{'active btn-primary': pasta === 'emitir',  'btn-outline-primary': pasta !== 'emitir'}">
            <input v-show="false" type="radio" v-model="pasta" value="emitir" autocomplete="off" @change="$validator.validateAll()">
            <span class="fa fa-plus"></span> Emitir
          </label>
          <label class="btn btn-outline-primary" :class="{'active btn-primary': pasta === 'autenticar', 'btn-outline-primary': pasta !== 'autenticar'}">
            <input v-show="false" type="radio" v-model="pasta" value="autenticar" autocomplete="off" @change="$validator.validateAll()">
            <span class="fa fa-check"></span> Consultar Autenticidade
          </label>
          <label class="btn btn-outline-primary" :class="{'active btn-primary': pasta === 'reimprimir', 'btn-outline-primary': pasta !== 'reimprimir'}">
            <input v-show="false" type="radio" v-model="pasta" value="reimprimir" autocomplete="off" @change="$validator.validateAll()">
            <span class="fa fa-print"></span> Imprimir Certidão Já Requerida
          </label>
        </div>
      </div>
    </div>

    <div>
      <form class="row justify-content-center">
        <div class="col col-sm-12 col-md-6">
          <div class="jumbotron d-block mx-auto pt-5 pb-5">
            <p v-if="errormsg" class="alert alert-danger" role="alert">{{errormsg}}</p>
            <div>
              <div class="row">
                <div class="col">
                  <div class="form-group">
                    <my-select name="sistema" label="Órgão" v-model="sistema" :list="sistemas" :edit="true" v-validate="'required'" :error="errors.first('sistema')"></my-select>
                  </div>
                </div>
              </div>
              <div v-show="pasta == 'emitir'" class="row">
                <div class="col">
                  <div class="form-group">
                    <label for="requisitante">CPF do Requisitante</label>
                    <my-input name="requisitante" v-model="requisitante" themask="###.###.###-##" v-validate="pasta == 'emitir' ? 'required|cpf' : ''" :error="errors.first('requisitante')"></my-input>
                  </div>
                </div>
              </div>
              <div v-show="pasta != 'emitir'" class="row">
                <div class="col">
                  <div class="form-group">
                    <label for="numero">Número da Certidão</label>
                    <my-input name="numero" v-model="numero" themask="####.########" v-validate="pasta != 'emitir' ? 'required|cert' : ''" :error="errors.first('numero')"></my-input>
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col">
                  <div class="form-group">
                    <label for="cpfcnpj">CPF/CNPJ da Certidão</label>

                    <my-input name="cpfcnpj" v-model="cpfcnpj" :themask="['###.###.###-##', '##.###.###/####-##']" v-validate="'required|cpfcnpj'" :error="errors.first('cpfcnpj')"></my-input>
                  </div>
                </div>
              </div>
              <div class="row pt-3">
                <div class="col" v-if="$parent.test.properties">
                  <invisible-recaptcha ref="captcha" v-if="this.$parent.test && this.$parent.test.properties" :sitekey="sitekey" :validate="captchaValidate" :callback="consultar"
                      class="btn btn-warning float-right" type="button" id="consultar" :disabled="!camposPreenchidos || recaptchaLoading || errors.any()" badge="bottomleft">
                      {{pasta === 'emitir' ? 'Emitir' : pasta === 'autenticar' ? 'Autenticar' : 'Reimprimir'}}
                  </invisible-recaptcha>
                </div>
              </div>
            </div>
          </div>
        </div>
      </form>
    </div>

  </div>
</template>

<script>
import UtilsBL from '../bl/utils.js'
import InvisibleRecaptcha from 'vue-invisible-recaptcha'

export default {
  name: 'consulta-simples',

  mounted () {
    if (this.$route.params.numero) {
      this.numero = this.$route.params.numero
      if (this.$parent.jwt) this.mostrarProcesso(this.numero)
      else if (this.$route.query.token) this.mostrarProcesso(this.numero, undefined, this.$route.query.token)
      else this.fire(this.$refs.captcha)
    }
  },

  data () {
    return {
      pasta: 'emitir',
      sistema: 'br.jus.jfrj.apolo',
      recaptchaLoading: false,
      errormsg: undefined,
      numero: undefined,
      requisitante: undefined,
      cpfcnpj: undefined
    }
  },

  beforeRouteUpdate (to, from, next) {
    // this.numero = to.params.numero
    // if (this.$parent.jwt) this.mostrarProcesso(this.numero)
    // else this.$refs.captcha.click()
    next()
  },

  computed: {
    sitekey: function() {
      if (this.$parent.test && this.$parent.test.properties) return this.$parent.test.properties['balcaovirtual.recaptcha.site.key']
      return 'undefined'
    },
    sistemas: function() {
      if (!this.$parent.sistemasCertificadores) return []
      var a = []
      for (var i = 0; i < this.$parent.sistemasCertificadores.length; i++) {
        a.push({id: this.$parent.sistemasCertificadores[i], nome: this.$parent.test.properties['balcaovirtual.' + this.$parent.sistemasCertificadores[i] + '.cert.name']})
      }
      return a
    },
    camposPreenchidos: function() {
      if (this.pasta === 'emitir') return !!this.requisitante && !!this.cpfcnpj
      if (this.pasta === 'autenticar') return !!this.numero && !!this.cpfcnpj
      if (this.pasta === 'reimprimir') return !!this.numero && !!this.cpfcnpj
      return false
    }
  },

  methods: {
    consultar: function (recaptchaToken) {
      this.recaptchaLoading = false
      this.$validator.validateAll().then((result) => {
        if (!result) {
          // this.errormsg = 'Por favor, verifique o preenchimento dos campos marcados em vermelho e repita a operação.'
          return
        } else {
          if (this.pasta === 'emitir') this.emitir(recaptchaToken)
          if (this.pasta === 'autenticar') this.autenticar(recaptchaToken)
          if (this.pasta === 'reimprimir') this.reimprimir(recaptchaToken)
        }
      })
    },
    obterToken: function (recaptchaToken, token, cont) {
      this.$http.get('certidao/obter-token' + '?sistema=' + this.sistema + '&requisitante=' + UtilsBL.somenteNumeros(this.requisitante) + '&cpfcnpj=' + UtilsBL.somenteNumeros(this.cpfcnpj) + '&numero=' + UtilsBL.somenteNumeros(this.numero) + (recaptchaToken ? '&captcha=' + recaptchaToken : '') + (token ? '?token=' + token : ''), { block: true, blockmin: 0, blockmax: 20 }).then(
        response => {
          var token = response.data.token
          if (!token) {
            this.errormsg = 'Não foi possível acessar o módulo de certidões'
            return
          }
          cont(token)
        },
        error => {
          this.errormsg = error.data.errormsg || 'Erro processando certidão'
        })
    },
    emitir: function (recaptchaToken, token) {
      this.obterToken(recaptchaToken, token, (token) => this.$router.push({ name: 'Emitir Certidão', params: {requisitante: UtilsBL.somenteNumeros(this.requisitante), cpfcnpj: UtilsBL.somenteNumeros(this.cpfcnpj)}, query: {sistema: this.sistema, token: token} }))
    },
    autenticar: function (recaptchaToken, token) {
      this.obterToken(recaptchaToken, token, (token) => this.$router.push({ name: 'Autenticar Certidão', params: {cpfcnpj: UtilsBL.somenteNumeros(this.cpfcnpj), numero: UtilsBL.somenteNumeros(this.numero)}, query: {sistema: this.sistema, token: token} }))
    },
    reimprimir: function (recaptchaToken, token) {
      this.obterToken(recaptchaToken, token, (token) => this.$router.push({ name: 'Reimprimir Certidão', params: {cpfcnpj: UtilsBL.somenteNumeros(this.cpfcnpj), numero: UtilsBL.somenteNumeros(this.numero)}, query: {sistema: this.sistema, token: token} }))
    },
    captchaValidate: function() {
      this.recaptchaLoading = true
    },
    fire: function(captcha) {
      setTimeout(() => {
        if (captcha.loaded) captcha.click()
        else this.fire(captcha)
      }, 200)
    }
  },
  components: {
    'invisible-recaptcha': InvisibleRecaptcha
  }
}
</script>

<!-- Add 'scoped' attribute to limit CSS to this component only -->
<style scoped>

</style>

<template>
  <!--=== Profile ===-->
  <div class="container content profile">

    <div class="row">
      <div class="col-md-12">
        <h4 class="text-center mt-3 mb-3">Consulta Processual {{$parent.jwt ? '' : 'Pública'}}</h4>
      </div>
    </div>

    <div class="row justify-content-center d-print-none">
      <div class="col-sm-auto mb-3">
        <div class="btn-group">
          <label class="btn" :class="{'active btn-primary': pasta === 'emitir',  'btn-outline-primary': pasta !== 'emitir'}">
            <input v-show="false" type="radio" v-model="pasta" value="emitir" autocomplete="off">
            <span class="fa fa-plus"></span> Emitir
          </label>
          <label class="btn btn-outline-primary" :class="{'active btn-primary': pasta === 'autenticar', 'btn-outline-primary': pasta !== 'autenticar'}">
            <input v-show="false" type="radio" v-model="pasta" value="autenticar" autocomplete="off">
            <span class="fa fa-check"></span> Consultar Autenticidade
          </label>
          <label class="btn btn-outline-primary" :class="{'active btn-primary': pasta === 'reimprimir', 'btn-outline-primary': pasta !== 'reimprimir'}">
            <input v-show="false" type="radio" v-model="pasta" value="reimprimir" autocomplete="off">
            <span class="fa fa-print"></span> Imprimir Certidão Já Requerida
          </label>
        </div>
      </div>
    </div>

    <div>
      <form class="row justify-content-center">
        <div class="col col-sm-12 col-md-6" v-if="!avancada">
          <div class="jumbotron d-block mx-auto pt-5 pb-5">
            <p v-if="errormsg" class="alert alert-danger" role="alert">{{errormsg}}</p>
            <div>
              <div class="row">
                <div class="col">
                  <div class="form-group">
                    <label for="orgao">Órgão</label>
                    <select class="form-control" id="orgao" v-model="orgao">
                      <option value="br.jus.trf2">TRF2</option>
                      <option value="br.jus.jfrj">JFRJ</option>
                      <option value="br.jus.jfes">JFES</option>
                    </select>
                  </div>
                </div>
              </div>
              <div v-show="pasta == 'emitir'" class="row">
                <div class="col">
                  <div class="form-group">
                    <label for="requisitante">CPF do Requisitante</label>
                    <my-input name="requisitante" v-model="requisitante" themask="###.###.###-##" v-validate="'required|cpf'" :error="errors.first('requisitante')"></my-input>
                  </div>
                </div>
              </div>
              <div v-show="pasta != 'emitir'" class="row">
                <div class="col">
                  <div class="form-group">
                    <label for="numero">Número da Certidão</label>
                    <input type="text" class="form-control" id="numero" placeholder="" v-model="numero" autofocus>
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
                  <invisible-recaptcha ref="captcha" v-if="!$parent.jwt" :sitekey="sitekey" :validate="captchaValidate" :callback="consultar"
                      class="btn btn-warning float-right" type="button" id="consultar" :disabled="recaptchaLoading || numero === undefined || numero.trim() === ''" badge="bottomleft">
                      Emitir
                  </invisible-recaptcha>
                  <button v-if="$parent.jwt" :disabled="numero === undefined || numero.trim() === ''" @click.prevent="mostrarProcesso(numero)" class="btn btn-primary float-right">{{pasta == 'emitir' ? 'Emitir' : pasta == 'autenticar' ? 'Autenticar' : 'Reimprimir'}}</button>
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
import ProcessoBL from '../bl/processo.js'
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
      orgao: 'br.jus.jfrj',
      recaptchaLoading: false,
      errormsg: undefined,
      avancada: false,
      numero: undefined,
      requisitante: undefined,
      cpfcnpj: undefined,
      procurador: undefined,
      numeroOriginario: undefined,
      baixados: undefined,
      parte: undefined,
      oab: undefined,
      inquerito: undefined
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
      return 'waiting...'
    }
  },

  methods: {
    consultar: function (recaptchaToken) {
      this.recaptchaLoading = false
      this.mostrarProcesso(this.numero, recaptchaToken)
    },
    mostrarProcesso: function (numero, recaptchaToken, token) {
      var n = ProcessoBL.somenteNumeros(this.numero)
      if (n === '') return
      this.$http.get('processo/' + n + '/validar' + (recaptchaToken ? '?captcha=' + recaptchaToken : '') + (token ? '?token=' + token : ''), { block: true, blockmin: 0, blockmax: 20 }).then(
        response => {
          var p = (response.data.list && response.data.list.length === 1) ? response.data.list[0] : {}
          if (p.unidade && !p.usuarioautorizado) {
            this.errormsg = 'Processo em segredo de justiça. (' + p.unidade + ')'
            return
          }
          if (!p.numero) {
            this.errormsg = `Processo "${this.numero}" não encontrado`
            return
          }
          this.$router.push({ name: 'Processo', params: { numero: p.numero, token: response.data.token, validar: p } })
        },
        error => {
          this.errormsg = error.data.errormsg || `Erro obtendo informações sobre o processo "${this.numero}"`
        })
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

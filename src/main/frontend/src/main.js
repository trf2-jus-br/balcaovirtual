// The Vue build version to load with the `import` command (runtime-only or
// standalone) has been set in webpack.base.conf with an alias.
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
// import '../static/fps-line-icons/css/fpslineicons.css'
// import 'typicons.font/src/font/typicons.css'
import 'font-awesome/css/font-awesome.min.css'

import 'babel-polyfill'
// import 'jquery'
// import 'tether'
import 'bootstrap'

import Vue from 'vue'
import VueResource from 'vue-resource'
import VueClip from 'vue-clip'
import { ValidationProvider, ValidationObserver, extend } from 'vee-validate'
import ptBR from 'vee-validate/dist/locale/pt_BR'
import BootstrapVue, { ModalPlugin } from 'bootstrap-vue'
import App from './App'
import router from './router'
import { Bus } from './bl/bus.js'
import ValidacaoBL from './bl/validacao.js'
import vSelect from 'vue-select'
import CKEditor from '@ckeditor/ckeditor5-vue'
import AwesomeMask from 'awesome-mask'
import MySelect from './components/MySelect'
import MyInput from './components/MyInput'
import VueTheMask from 'vue-the-mask'
// import {mask} from 'vue-the-mask'

extend('email', {
  message: field => 'Email inválido.',
  validate: function (value) {
    // eslint-disable-next-line
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
    return re.test(String(value))
  }
})

extend('required', {
  message: field => 'Campo obrigatório.',
  validate: function(s) { return !!s },
  computesRequired: true
})

extend('cpf', {
  message: field => 'CPF inválido',
  validate: ValidacaoBL.validarCPF
})

extend('cnpj', {
  message: field => 'CNPJ inválido',
  validate: ValidacaoBL.validarCNPJ
})

extend('cpfcnpj', {
  message: field => 'CPF/CNPJ inválido',
  validate: ValidacaoBL.validarCPFCNPJ
})

extend('oab', {
  message: field => 'OAB inválido',
  validate: ValidacaoBL.validarOAB
})

extend('cert', {
  message: field => 'Número de certidão inválido',
  validate: ValidacaoBL.validarCertidao
})

ptBR.messages.cpf = field => 'CPF ' + field + ' inválido'
ptBR.messages.cnpj = field => 'CNPJ ' + field + ' inválido'
ptBR.messages.oab = field => 'OAB ' + field + ' inválido'
// Validator.localize('pt_BR', ptBR)

Vue.use(VueResource)
Vue.use(VueClip)
// Vue.use(VeeValidate)
Vue.component('ValidationProvider', ValidationProvider)
Vue.component('ValidationObserver', ValidationObserver)
Vue.use(BootstrapVue)
Vue.use(ModalPlugin)
Vue.use(CKEditor)
Vue.use(VueTheMask)

Vue.component('my-select', MySelect, { inheritAttrs: false })
Vue.component('my-input', MyInput, { inheritAttrs: false })
// Vue.directive('themask', mask)
Vue.directive('awemask', AwesomeMask)

Vue.component('v-select', vSelect)

Vue.config.productionTip = false

console.log(process.env.API_URL)
Vue.http.options.root = process.env.API_URL

/* eslint-disable no-new */
new Vue({
  el: '#app',
  mounted() {
    Vue.http.interceptors.push(function(request, next) {
      if (request.block) Bus.$emit('block', request.blockmin, request.blockmax)

      // continue to next interceptor
      next(function(response) {
        if (request.block) Bus.$emit('release')
        if (response.status === 401) router.push({ name: 'Login' })
      })
    })
  },
  router,
  template: '<App/>',
  components: {
    App
  }
})

var regServiceWorker = function() {
  if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('./service-worker.js', {
      scope: '/balcaovirtual/'
    }).then(() => console.log('Service Worker registered successfully.')).catch(error => console.log('Service Worker registration failed:', error))
  }
}

regServiceWorker()


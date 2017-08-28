// The Vue build version to load with the `import` command (runtime-only or
// standalone) has been set in webpack.base.conf with an alias.
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
// import '../static/fps-line-icons/css/fpslineicons.css'

import 'babel-polyfill'
// import 'jquery'
// import 'tether'
import 'bootstrap'

import Vue from 'vue'
import VueResource from 'vue-resource'
import VueClip from 'vue-clip'
import VeeValidate from 'vee-validate'
import BootstrapVue from 'bootstrap-vue'
import App from './App'
import router from './router'
import { Bus } from './bl/bus.js'
import vSelect from 'vue-select'

Vue.use(VueResource)
Vue.use(VueClip)
Vue.use(VeeValidate)
Vue.use(BootstrapVue)

Vue.component('v-select', vSelect)

Vue.config.productionTip = false

console.log(process.env.API_URL)
Vue.http.options.root = process.env.API_URL

/* eslint-disable no-new */
new Vue({
  el: '#app',
  mounted () {
    Vue.http.interceptors.push(function (request, next) {
      if (request.block) Bus.$emit('block')

      // continue to next interceptor
      next(function (response) {
        if (request.block) Bus.$emit('release')
        if (response.status === 401) location.reload()
      })
    })
  },
  router,
  template: '<App/>',
  components: {
    App
  }
})

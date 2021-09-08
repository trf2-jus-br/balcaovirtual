// The Vue build version to load with the `import` command (runtime-only or
// standalone) has been set in webpack.base.conf with an alias.
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap-vue/dist/bootstrap-vue.css";
// import '../static/fps-line-icons/css/fpslineicons.css'
// import 'typicons.font/src/font/typicons.css'
import "font-awesome/css/font-awesome.min.css";

import "@babel/polyfill";
import "mutationobserver-shim";
import "./plugins/bootstrap-vue";
import store from "./store";
// import 'jquery'
// import 'tether'
import "bootstrap";

import Vue from "vue";
import VueResource from "vue-resource";
import VueAnalytics from "vue-analytics";
import VueClip from "vue-clip";
import {
  ValidationProvider,
  ValidationObserver,
  extend
} from "vee-validate";
import ptBR from "vee-validate/dist/locale/pt_BR";
import BootstrapVue, {
  ModalPlugin,
  TooltipPlugin
} from "bootstrap-vue";
import App from "./App";
import router from "./router";
import {
  Bus
} from "./bl/bus.js";
import ValidacaoBL from "./bl/validacao.js";
import vSelect from "vue-select";
import AwesomeMask from "awesome-mask";
import MySelect from "./components/MySelect";
import MyInput from "./components/MyInput";
import VueTheMask from "vue-the-mask";
// import {mask} from 'vue-the-mask'
import vSelectPage from "v-selectpage";

Vue.config.productionTip = false;

extend("email", {
  message: field => "Email inválido.",
  validate: function (value) {
    // eslint-disable-next-line
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
    return re.test(String(value));
  }
});

extend("required", {
  message: field => "Campo obrigatório.",
  validate: function (s) {
    return !!s;
  },
  computesRequired: true
});

extend("cpf", {
  message: field => "CPF inválido",
  validate: ValidacaoBL.validarCPF
});

extend("cnpj", {
  message: field => "CNPJ inválido",
  validate: ValidacaoBL.validarCNPJ
});

extend("cpfcnpj", {
  message: field => "CPF/CNPJ inválido",
  validate: ValidacaoBL.validarCPFCNPJ
});

extend("oab", {
  message: field => "OAB inválido",
  validate: ValidacaoBL.validarOAB
});

extend("cert", {
  message: field => "Número de certidão inválido",
  validate: ValidacaoBL.validarCertidao
});

ptBR.messages.cpf = field => "CPF " + field + " inválido";
ptBR.messages.cnpj = field => "CNPJ " + field + " inválido";
ptBR.messages.oab = field => "OAB " + field + " inválido";
// Validator.localize('pt_BR', ptBR)

Vue.use(VueResource);
Vue.use(VueClip);
Vue.use(VueAnalytics, {
  id: "UA-100085353-2",
  router
});
// Vue.use(VeeValidate)
Vue.component("ValidationProvider", ValidationProvider);
Vue.component("ValidationObserver", ValidationObserver);
Vue.use(BootstrapVue);
Vue.use(ModalPlugin);
Vue.use(TooltipPlugin);
Vue.use(VueTheMask);
Vue.use(vSelectPage, {
  language: "pt_br",
  pagination: false
});

Vue.component("my-select", MySelect, {
  inheritAttrs: false
});
Vue.component("my-input", MyInput, {
  inheritAttrs: false
});
// Vue.directive('themask', mask)
Vue.directive("awemask", AwesomeMask);

Vue.component("v-select", vSelect);

Vue.config.productionTip = false;

console.log(process.env.VUE_APP_API_URL);
Vue.http.options.root = process.env.VUE_APP_API_URL;
Vue.http.options.credentials = true;

/* eslint-disable no-new */
new Vue({
  mounted() {
    Vue.http.interceptors.push(function (request, next) {
      if (request.block) Bus.$emit("block", request.blockmin, request.blockmax);

      // continue to next interceptor
      next(function (response) {
        if (request.block) Bus.$emit("release");
        if (response.status === 401) Bus.$emit("unauthorized");
      });
    });
  },
  router,
  store,
  render: h => h(App),
  template: "<App/>",
  components: {
    App
  }
}).$mount("#app");
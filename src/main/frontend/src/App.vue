<template>
  <div>
    <div class="wrapper">
      <div :class="{ dimmed: loading }">
        <!--=== Header ===-->
        <div class="header d-print-none">
          <!-- Navbar -->
          <nav
            class="navbar navbar navbar-expand-lg navbar-light"
            :class="{
              'navbar-dark bg-success':
                test.properties['balcaovirtual.env'] === 'desenv',
              'navbar-dark bg-secondary':
                test.properties['balcaovirtual.env'] === 'homolo',
              'navbar-dark bg-primary':
                test.properties['balcaovirtual.env'] === 'prod'
            }"
          >
            <a class="navbar-brand pt-0 pb-0" href="#">
              <img
                id="logo-header"
                src="./assets/balcao-virtual-38px.png"
                alt="Logo Balcão Virtual"
                height="38"
              />
              <img
                class="ml-2"
                id="logo-header2"
                src="./assets/trf2-38px-2.png"
                alt="Logo TRF2"
                height="38"
              />
            </a>

            <button
              class="navbar-toggler"
              type="button"
              data-toggle="collapse"
              data-target="#navbarSupportedContent"
              aria-controls="navbarSupportedContent"
              aria-expanded="false"
              aria-label="Toggle navigation"
            >
              <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="navbarSupportedContent">
              <ul class="navbar-nav mr-auto">
                <li class="nav-item">
                  <router-link
                    class="nav-link"
                    active-class="active"
                    :to="{ name: 'Consulta Simples' }"
                    tag="a"
                    exact
                    >Consulta</router-link
                  >
                </li>
                <li
                  v-if="test.properties['balcaovirtual.cert.systems']"
                  class="nav-item"
                >
                  <router-link
                    class="nav-link"
                    active-class="active"
                    :to="{ name: 'Consultar Certidão' }"
                    tag="a"
                    exact
                    >Certidões</router-link
                  >
                </li>
                <li class="nav-item" v-if="jwt &amp;&amp; jwt.username">
                  <router-link
                    class="nav-link"
                    active-class="active"
                    :to="{ name: 'Lista de Processos' }"
                    tag="a"
                    exact
                    >Processos</router-link
                  >
                </li>
                <li class="nav-item" v-if="false">
                  <router-link
                    class="nav-link"
                    active-class="active"
                    :to="{ name: 'Lista de Etiquetas' }"
                    tag="a"
                    exact
                    >Etiquetas</router-link
                  >
                </li>
                <li
                  class="nav-item"
                  v-if="jwt &amp;&amp; jwt.username &amp;&amp; !(jwt.origin === 'int')"
                >
                  <router-link
                    class="nav-link"
                    active-class="active"
                    :to="{ name: 'Petição Inicial' }"
                    tag="a"
                    >Petição Inicial</router-link
                  >
                </li>
                <li
                  class="nav-item"
                  v-if="jwt &amp;&amp; jwt.username &amp;&amp; !(jwt.origin === 'int')"
                >
                  <router-link
                    class="nav-link"
                    active-class="active"
                    :to="{ name: 'Petição Intercorrente' }"
                    tag="a"
                    >Petição Intercorrente</router-link
                  >
                </li>
                <li
                  class="nav-item"
                  v-if="jwt &amp;&amp; jwt.username &amp;&amp; !(jwt.origin === 'int')"
                >
                  <router-link
                    class="nav-link"
                    active-class="active"
                    :to="{ name: 'Lista de Avisos' }"
                    tag="a"
                    >Intimação/Citação<sup v-if="cAvisos &amp;&amp; cAvisos > 0"
                      ><span
                        class="badge badge-pill badge-danger active-opacity"
                        >{{ cAvisos }}</span
                      ></sup
                    ><sup v-if="cAvisos === undefined" style="opacity: 0.5;"
                      ><span class="badge badge-pill badge-light"
                        >Aguarde...</span
                      ></sup
                    ></router-link
                  >
                </li>
                <li class="nav-item" v-if="mesaAtiva">
                  <router-link
                    class="nav-link"
                    active-class="active"
                    :to="{ name: 'Mesa' }"
                    tag="a"
                    >Minutas</router-link
                  >
                </li>
                <li class="nav-item" v-if="jwt &amp;&amp; jwt.username">
                  <router-link
                    class="nav-link"
                    active-class="active"
                    :to="{ name: 'Sugestões' }"
                    tag="a"
                    >Sugestões</router-link
                  >
                </li>
                <li class="nav-item">
                  <router-link
                    class="nav-link"
                    active-class="active"
                    :to="{ name: 'Sobre' }"
                    tag="a"
                    >Sobre</router-link
                  >
                </li>
              </ul>
              <ul class="navbar-nav navbar-right">
                <li class="nav-item" v-if="!jwt">
                  <router-link
                    class="nav-link"
                    active-class="active"
                    :to="{ name: 'Login' }"
                    tag="a"
                    >Login</router-link
                  >
                </li>
                <li
                  class="nav-item dropdown"
                  v-if="jwt &amp;&amp; jwt.username"
                >
                  <a
                    class="nav-link dropdown-toggle"
                    href=""
                    id="navbarDropdownMenuLink"
                    data-toggle="dropdown"
                    aria-haspopup="true"
                    aria-expanded="false"
                  >
                    <span>{{ ident }}</span>
                  </a>
                  <div
                    class="dropdown-menu logout"
                    aria-labelledby="navbarDropdownMenuLink"
                  >
                    <router-link
                      class="dropdown-item"
                      active-class="active"
                      :to="{ name: 'Status' }"
                      tag="a"
                      exact
                      >Status</router-link
                    >
                    <a class="dropdown-item" @click="logout">Logout</a>
                  </div>
                </li>
              </ul>
            </div>
          </nav>
          <!-- End Navbar -->
        </div>
        <!--=== End Header ===-->
      </div>
      <div id="app">
        <div v-if="updateAvailable" class="container mt-3">
          <p class="alert alert-warning">
            Uma nova versão está disponível, clique
            <a
              @click.prevent="atualizar"
              href=""
              style="text-decoration: underline; color:blue;"
              >aqui</a
            >
            para atualizar.
          </p>
        </div>
        <div
          v-if="!updateAvailable && !notificacoesAtivas && jwt"
          class="container mt-3"
        >
          <p class="alert alert-warning">
            Para receber atualizações de seus processos favoritos, por favor,
            clique
            <a
              @click.prevent="habilitarNotificacoes"
              href=""
              style="text-decoration: underline; color:blue;"
              >aqui</a
            >
            e permita o envio de notificações.
          </p>
        </div>
        <progressModal ref="progressModal"></progressModal>
        <progressModalAsync ref="progressModalAsync"></progressModalAsync>
        <messageBox ref="messageBox"></messageBox>
        <assinatura ref="assinatura" title="Assinatura"></assinatura>

        <top-progress ref="topProgress" :height="5" color="#000"></top-progress>
        <transition :name="transitionName" mode="out-in">
          <keep-alive include="mesa">
            <router-view :key="$route.fullPath"></router-view>
          </keep-alive>
        </transition>
        <div v-if="notificacoesBloqueadas && jwt" class="container mt-3">
          <p class="alert alert-warning">
            Não estamos conseguindo enviar notificações para você. Por favor,
            desbloqueie o recebimento de notificações e clique
            <a
              @click.prevent="atualizar"
              href=""
              style="text-decoration: underline; color:blue;"
              >aqui</a
            >
            para tentarmos novamente.
          </p>
        </div>
      </div>
    </div>
    <div class="foot">
      <span :title="test.timestamp">v{{ test.version }}</span>
    </div>
  </div>
</template>

<script>
import AuthBL from "./bl/auth.js";
import UtilsBL from "./bl/utils.js";
import topProgress from "./components/TopProgress";
import { Bus } from "./bl/bus.js";
import ProgressModal from "./components/ProgressModal";
import ProgressModalAsync from "./components/ProgressModalAsync";
import MessageBox from "./components/MessageBox";
import Assinatura from "./components/Assinatura";
import { initializeFirebase } from "./bl/push.js";
import { register } from "register-service-worker";
import firebase from "firebase/app";
import router from "./router";

export default {
  name: "app",
  mounted() {
    UtilsBL.overrideProperties(
      this.settings,
      JSON.parse(localStorage.getItem("bv-settings")) || {}
    );
    this.$router.beforeEach((to, from, next) => {
      this.transitionName = to.params.transitionName;
      next();
      if (to.meta && to.meta.title) {
        document.title = to.meta.title(to);
      } else {
        document.title = "Balcão Virtual - " + to.name;
      }
    });

    Bus.$on("update-available", () => {
      this.updateAvailable = true;
      console.log("update-available");
    });

    Bus.$on("notification-permission-granted", token => {
      console.log("notification-permisson-granted", token);
      this.incluirToken(token);
    });

    Bus.$on("notification-permission-denied", token => {
      console.log("notification-permisson-denied", token);
      this.notificacoesBloqueadas = true;
    });

    Bus.$on("block", (min, max) => {
      if (this.blockCounter === 0) {
        this.$nextTick(function() {
          if (this.blockCounter > 0 && this.$refs.topProgress) {
            this.$refs.topProgress.start(min, max);
          }
        }, 200);
        this.loading = true;
      }
      this.blockCounter++;
    });

    Bus.$on("release", () => {
      this.blockCounter--;
      if (this.blockCounter === 0) {
        this.loading = false;
        this.$nextTick(function() {
          if (this.blockCounter === 0 && this.$refs.topProgress) {
            this.$refs.topProgress.done();
          }
        }, 200);
      }
    });

    Bus.$on("unathorized", () => {
      this.jwt = undefined;
      router.push({ name: "Login" });
    });


    this.$on("updateLogged", token => {
      this.cAvisos = undefined;
      this.avisos = undefined;
      if (token) {
        AuthBL.setIdToken(token);
        this.jwt = AuthBL.decodeToken(token);
        // $rootScope.updateLogged();
        // $state.go('consulta-processual');
        if (this.jwt) {
          this.inicializarNotificoes();
          if (this.$route.query.avisos) {
            this.cAvisos = this.$route.query.avisos;
            this.avisos = undefined;
          } else {
            // Carragar a lista de avisos pendentes
            this.$nextTick(function() {
              this.$http
                .get("aviso/listar", { block: false })
                .then(
                  response => {
                    this.avisos = response.data;
                    this.cAvisos = this.avisos.list.length;
                  },
                  error => console.log("Erro carregando avisos", error)
                );
            });
          }
        }
      }
    });

    this.$on("setting", (key, value) => {
      this.settings[key] = value;
      var json = JSON.stringify(this.settings);
      localStorage.setItem("bv-settings", json);
    });

    var prg = this.$refs.progressModal;

    Bus.$on("prgStart", (title, total, callbackNext, callbackEnd) => {
      prg.start(title, total, callbackNext, callbackEnd);
    });

    Bus.$on("prgCaption", caption => {
      prg.caption = caption;
    });

    Bus.$on("prgNext", () => {
      prg.next();
    });

    Bus.$on("message", (title, message) => {
      this.$refs.messageBox.show(title, message);
    });

    var prgAsync = this.$refs.progressModalAsync;

    Bus.$on("prgAsyncStart", (title, key, callbackEnd) => {
      prgAsync.start(title, key, callbackEnd);
    });

    Bus.$on("iniciarAssinaturaComSenha", (documentos, cont) => {
      console.log("iniciando assinatura com senha");
      if (this.$refs.assinatura) this.$refs.assinatura.show(documentos, cont);
    });

    Bus.$on("assinarComSenha", (documentos, username, password, cont) => {
      this.assinarComSenhaEmLote(documentos, username, password, cont);
    });

    this.token = AuthBL.getIdToken();
    if (this.token && AuthBL.isTokenExpired(this.token)) this.token = undefined;
    this.$emit("updateLogged", this.token);

    this.$nextTick(function() {
      this.$http.get("test?skip=all").then(
        response => {
          this.test = response.data;

          if (this.test.properties["balcaovirtual.systems"]) {
            this.nomesSistemas = "";
            var a = this.test.properties["balcaovirtual.systems"].split(",");
            this.sistemas = [];
            for (var i = 0; i < a.length; i++) {
              this.sistemas.push(a[i]);
              if (i > 0) {
                if (i === a.length - 1) this.nomesSistemas += " e ";
                else this.nomesSistemas += ", ";
              }
              this.nomesSistemas += this.test.properties[
                "balcaovirtual." + a[i] + ".name"
              ];
            }
          }

          if (this.test.properties["balcaovirtual.cert.systems"]) {
            this.nomesSistemasCertificadores = "";
            a = this.test.properties["balcaovirtual.cert.systems"].split(",");
            this.sistemasCertificadores = [];
            for (i = 0; i < a.length; i++) {
              this.sistemasCertificadores.push(a[i]);
              if (i > 0) {
                if (i === a.length - 1)
                  this.nomesSistemasCertificadores += " e ";
                else this.nomesSistemasCertificadores += ", ";
              }
              this.nomesSistemasCertificadores += this.test.properties[
                "balcaovirtual." + a[i] + ".cert.name"
              ];
            }
          }

          if (
            this.test.properties["balcaovirtual.wootric.token"] &&
            this.test.properties["balcaovirtual.wootric.token"] !==
              "[undefined]" &&
            this.jwt
          ) {
            // This loads the Wootric survey
            // window.wootric_survey_immediately = true
            window.wootricSettings = {
              email: this.jwt.username,
              created_at: 1234567890,
              account_token: this.test.properties["balcaovirtual.wootric.token"]
            };
            window.wootric("run");
          }

          this.inicializarNotificoes();
        },
        error => UtilsBL.errormsg(error, this)
      );
    });

    try {
      this.registerServiceWorker();
    } catch (e) {
      console.log(e);
    }

    try {
      if (localStorage.getItem("bv-notificacoes-ativas"))
        this.notificacoesAtivas = true;
      this.registerMessagingServiceWorker();
    } catch (e) {
      console.log(e);
    }
  },

  data() {
    return {
      blockCounter: 0,
      loading: false,
      test: { properties: {} },
      nomesSistemas: undefined,
      sistemas: undefined,
      sistemasCertificadores: undefined,
      errormsg: undefined,
      settings: {
        timeline: undefined,
        timelineIncompatible: undefined,
        filtrarMovimentos: undefined,
        mostrarNotas: undefined,
        mostrarPartes: undefined,
        mostrarDadosComplementares: undefined,
        mostrarProcessosRelacionados: undefined
      },
      token: undefined,
      jwt: undefined,
      avisos: undefined,
      cAvisos: undefined,

      updateAvailable: false,
      notificacoesBloqueadas: false,
      notificacoesAtivas: false,
      notificacoesToken: undefined,
      messagingServiceWorkerRegistration: undefined,
      nofificacoesInicializadas: false,

      transitionName: "none"
    };
  },
  computed: {
    mesaAtiva: function() {
      var f =
        this.jwt &&
        this.jwt.username &&
        (this.jwt.origin === "int" || this.jwt.origin === "int/ext");
      return f;
    },

    ident: function() {
      if (!this.jwt) return;
      if (!this.jwt.user) return this.jwt.username;
      var a = [];
      for (var prop in this.jwt.user) {
        if (this.jwt.user.hasOwnProperty(prop)) {
          a.push(this.test.properties["balcaovirtual." + prop + ".name"]);
        }
      }
      if (a.length === 0) return this.jwt.username;
      if (a.length === 1) return this.jwt.username + " - " + a[0];
      return this.jwt.username + " - " + a[0] + "+" + (a.length - 1);
    }
  },
  methods: {
    isTokenValid: function() {
      return this.token && !AuthBL.isTokenExpired(this.token);
    },

    logout: function() {
      AuthBL.logout();
      this.jwt = undefined;
      this.$emit("updateLogged", undefined);
      this.$router.push({ name: "Consulta Simples" });
    },

    assinarComSenha: function(d, username, password, lote) {
      this.errormsg = undefined;
      Bus.$emit("prgCaption", "Assinando " + d.numeroDoDocumento);

      this.$http
        .post(
          "mesa/" +
            "null" +
            "/documento/" +
            d.id +
            "/assinar-com-senha?sistema=" +
            d.sistema,
          {
            username: username,
            password: password
          },
          { block: !lote }
        )
        .then(
          () => {
            d.errormsg = undefined;
            d.status = 5;
            d.descricaoDoStatus = "Assinada";
            d.checked = false;
            d.disabled = true;
            UtilsBL.logEvento(
              "assinatura em lote",
              "assinado",
              "assinado com senha"
            );
            Bus.$emit("prgNext");
          },
          error => {
            if (lote) d.errormsg = error.data.errormsg;
            else Bus.$emit("message", "Erro", error.data.errormsg);
            Bus.$emit("prgNext");
          }
        );
    },

    assinarComSenhaEmLote: function(documentos, username, password, cont) {
      Bus.$emit(
        "prgStart",
        "Assinando Com Senha",
        documentos.length,
        i =>
          this.assinarComSenha(
            documentos[i],
            username,
            password,
            documentos.length !== 1
          ),
        cont
      );
    },

    atualizar: function() {
      window.location.reload();
    },

    habilitarNotificacoes: function() {
      localStorage.setItem("bv-notificacoes-ativas", JSON.stringify(true));
      this.notificacoesAtivas = true;
      this.nofificacoesInicializadas = true;
      this.inicializarNotificoes();
    },

    incluirToken: function(token) {
      this.notificacoesToken = token;
      this.$http
        .post("notificacao/incluir-token?token=" + encodeURIComponent(token))
        .then(
          response => {
            if (response.status === 200 && response.data.status === "OK") {
              Bus.$emit(
                "message",
                "Sucesso",
                "Notificações ativas. Muito obrigado!"
              );
              UtilsBL.logEvento("notificacao", "notificacao-ativa");
            }
          },
          error => {
            Bus.$emit("message", "Erro", error.data.errormsg);
          }
        );
    },

    registerMessagingServiceWorker: function() {
      var self = this;
      register("firebase-messaging-sw.js", {
        registrationOptions: { scope: "static/" },
        ready() {
          console.log("Messaging service worker is active.");
        },
        registered(registration) {
          console.log("Messaging service worker has been registered.");
          self.messagingServiceWorkerRegistration = registration;
          self.inicializarNotificoes();
        },
        cached() {
          console.log("Messaging content has been cached for offline use.");
        },
        updatefound() {
          console.log("Messaging new content is downloading.");
        },
        updated() {
          console.log("Messaging new content is available; please refresh.");
        },
        offline() {
          console.log(
            "Messaging reports: No internet connection found. App is running in offline mode."
          );
        },
        error(error) {
          console.error(
            "Error during messaging service worker registration:",
            error
          );
        }
      });
    },

    inicializarNotificoes: function() {
      if (
        this.notificacoesAtivas &&
        this.jwt &&
        this.test &&
        this.messagingServiceWorkerRegistration
      ) {
        if (!firebase.apps.length) {
          initializeFirebase(
            this.messagingServiceWorkerRegistration,
            this.test.properties
          );
        } else if (this.notificacoesToken) {
          this.incluirToken(this.notificacoesToken);
        }
      }
    },

    registerServiceWorker: function() {
      try {
        register("./service-worker.js", {
          registrationOptions: { scope: "/balcaovirtual/" },
          ready() {
            console.log("Service worker is active.");
          },
          registered() {
            console.log("Service worker has been registered.");
          },
          cached() {
            console.log("Content has been cached for offline use.");
          },
          updatefound() {
            console.log("New content is downloading.");
          },
          updated() {
            console.log("New content is available; please refresh.");
            Bus.$emit("update-available");
          },
          offline() {
            console.log(
              "No internet connection found. App is running in offline mode."
            );
          },
          error(error) {
            console.error("Error during service worker registration:", error);
          }
        });
      } catch (e) {
        console.log(e);
      }
    }
  },
  components: {
    topProgress,
    progressModal: ProgressModal,
    progressModalAsync: ProgressModalAsync,
    assinatura: Assinatura,
    messageBox: MessageBox
  }
};
</script>

<style>
.close-button {
  float: right;
}

.icone-em-linha {
  color: black;
  opacity: 0.2;
}

.icone-em-linha:hover {
  opacity: 1;
}

.dimmed {
  position: relative;
}

.dimmed:before {
  content: " ";
  z-index: 10;
  display: block;
  position: absolute;
  height: 100%;
  top: 0;
  left: 0;
  right: 0;
  background: rgba(255, 255, 255, 0.5);
}

.highlight {
  background-color: #ffff00 !important;
}

.highlightable {
  transition-property: background-color;
  transition-duration: 1s;
  transition-timing-function: easy-out;
  transition-delay: 0s;
}

html {
  height: 100%;
  box-sizing: border-box;
}

*,
*:before,
*:after {
  box-sizing: inherit;
}

body {
  position: relative;
  margin: 0;
  padding-bottom: 6rem;
  min-height: 100%;
}

div.dropdown-menu.logout {
  right: 0px;
  left: auto;
  min-width: 0px;
}

div.foot {
  position: absolute;
  right: 0;
  bottom: 0;
  left: 0;
}

div.foot span {
  font-size: 80%;
  color: #ccc;
  border-top: 1px solid #ccc;
  float: right;
  margin-right: 0.5em;
  margin-bottom: 0.3em;
}

.red {
  color: red;
}

.green {
  color: green;
}

.odd {
  background-color: rgba(0, 0, 0, 0.05);
}

h1::first-letter {
  margin-top: 0;
}

h2::first-letter {
  margin-top: 0;
}

h3::first-letter {
  margin-top: 0;
}

h4::first-letter {
  margin-top: 0;
}

h5::first-letter {
  margin-top: 0;
}

h6::first-letter {
  margin-top: 0;
}

.text-success-dark {
  color: #5cb85c;
}

@media print {
  body {
    font-size: 70%;
    margin: 0;
    padding: 0 !important;
    min-width: 768px;
  }
  .container {
    width: auto;
    min-width: 750px;
  }
}

.break {
  margin-bottom: 0.6rem;
}

.card-body label {
  text-transform: uppercase;
  color: #777;
  margin-bottom: 0px;
  font-size: 80%;
}

.text-white .card-body a {
  color: #fff;
}

.dropdown-submenu {
  position: relative;
}

.dropdown-submenu > .dropdown-menu {
  top: 0;
  left: 100%;
  margin-top: -6px;
  margin-left: -1px;
  -webkit-border-radius: 0 6px 6px 6px;
  -moz-border-radius: 0 6px 6px;
  border-radius: 0 6px 6px 6px;
}

.dropdown-submenu:hover > .dropdown-menu {
  display: block;
}

.dropdown-submenu > a:after {
  display: block;
  content: " ";
  float: right;
  width: 0;
  height: 0;
  border-color: transparent;
  border-style: solid;
  border-width: 5px 0 5px 5px;
  border-left-color: #ccc;
  margin-top: 5px;
  margin-right: -10px;
}

.dropdown-submenu:hover > a:after {
  border-left-color: #fff;
}

.dropdown-submenu.pull-left {
  float: none;
}

.dropdown-submenu.pull-left > .dropdown-menu {
  left: -100%;
  margin-left: 10px;
  -webkit-border-radius: 6px 0 6px 6px;
  -moz-border-radius: 6px 0 6px 6px;
  border-radius: 6px 0 6px 6px;
}

div.sobre p {
  font-size: 120%;
}

div.sobre ol li {
  font-size: 120%;
  padding-bottom: 12pt;
}

table.table-peticao tbody td {
  vertical-align: middle;
}

.drop-box {
  background: #f8f8f8;
  border: 5px dashed #ddd;
  width: 100%;
  text-align: center;
  padding: 1em;
}

.dragover {
  border: 5px dashed blue;
}

body1 {
  background-color: #eee !important;
}

.form-signin {
  max-width: 330px;
  padding: 15px;
  margin: 0 auto;
}

.form-signin .form-signin-heading,
.form-signin .checkbox {
  margin-bottom: 10px;
}

.form-signin .checkbox {
  font-weight: normal;
}

.form-signin .form-control {
  position: relative;
  height: auto;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  padding: 10px;
  font-size: 16px;
}

.form-signin .form-control:focus {
  z-index: 2;
}

.form-signin input[type="email"] {
  margin-bottom: -1px;
  border-bottom-right-radius: 0;
  border-bottom-left-radius: 0;
}

.form-signin input[type="password"] {
  margin-bottom: 10px;
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}

h1 {
  text-align: center;
}

.metas {
  text-align: center;
  width: 100%;
}

.transparent {
  filter: alpha(opacity=0);
  opacity: 0;
  -moz-opacity: 0;
  -webkit-opacity: 0;
}

div.metas {
  text-align: center;
  display: inline-block;
}

div.metas table {
  width: auto;
  margin: 0 auto !important;
}

em.invalid {
  display: block;
  margin-top: 6px;
  padding: 0 1px;
  font-style: normal;
  font-size: 12px;
  line-height: 15px;
  color: #ee9393;
}

.active-opacity {
  opacity: 0.75;
}

a.active .active-opacity {
  opacity: 1 !important;
}

.slide-left-enter-active,
.slide-left-leave-active,
.slide-right-enter-active,
.slide-right-leave-active {
  transition-duration: 0.3s;
  transition-property: height, opacity, transform;
  transition-timing-function: cubic-bezier(0.55, 0, 0.1, 1);
  overflow: hidden;
}

.slide-left-enter,
.slide-right-leave-active {
  opacity: 0;
  transform: translate(10em, 0);
}

.slide-left-leave-active,
.slide-right-enter {
  opacity: 0;
  transform: translate(-10em, 0);
}
</style>

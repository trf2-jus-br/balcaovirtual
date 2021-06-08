import Vue from "vue";
import Router from "vue-router";
import store from '../store'
import ConsultaSimples from "@/components/ConsultaSimples";
import Processo from "@/components/Processo";
import ProcessoLista from "@/components/ProcessoLista";
import PeticaoInicial from "@/components/PeticaoInicial";
import PeticaoIntercorrente from "@/components/PeticaoIntercorrente";
import ConsultaCertidao from "@/components/ConsultaCertidao";
import Certidao from "@/components/Certidao";
import AvisoLista from "@/components/AvisoLista";
import AvisoConfirmadoRecentes from "@/components/AvisoConfirmadoRecentes";
import AvisoConfirmadoLista from "@/components/AvisoConfirmadoLista";
import EtiquetaLista from "@/components/EtiquetaLista";
import Mesa from "@/components/Mesa";
import Documento from "@/components/Documento";
import Login from "@/components/Login";
import Sugestoes from "@/components/Sugestoes";
import Sobre from "@/components/Sobre";
import Status from "@/components/Status";
import ProcessoBL from "../bl/processo.js";
import PadraoLista from "@/components/PadraoLista";
import Padrao from "@/components/Padrao";

Vue.use(Router);

const router = new Router({
  routes: [{
      path: "/login",
      name: "Login",
      component: Login
    },
    {
      path: "/",
      name: "Consulta Simples",
      component: ConsultaSimples
    },
    {
      path: "/consultar/:numero",
      name: "Consultar",
      component: ConsultaSimples
    },
    {
      path: "/processo/:numero",
      name: "Processo",
      component: Processo,
      meta: {
        title: route => {
          return "Processo " + ProcessoBL.formatarProcesso(ProcessoBL.somenteNumeros(route.params.numero)) + "..";
        }
      }
    },
    {
      path: "/processo-lista",
      name: "Lista de Processos",
      component: ProcessoLista
    },
    {
      path: "/consultar-certidao",
      name: "Consultar Certidão",
      component: ConsultaCertidao
    },
    {
      path: "/emitir-certidao/:requisitante/:cpfcnpj",
      name: "Emitir Certidão",
      component: Certidao
    },
    {
      path: "/autenticar-certidao/:numero/:cpfcnpj",
      name: "Autenticar Certidão",
      component: Certidao
    },
    {
      path: "/reimprimir-certidao/:numero/:cpfcnpj",
      name: "Reimprimir Certidão",
      component: Certidao
    },
    {
      path: "/etiqueta-lista",
      name: "Lista de Etiquetas",
      component: EtiquetaLista
    },
    {
      path: "/peticao-inicial",
      name: "Petição Inicial",
      component: PeticaoInicial
    },
    {
      path: "/peticao-intercorrente",
      name: "Petição Intercorrente",
      component: PeticaoIntercorrente
    },
    {
      path: "/aviso-lista",
      name: "Lista de Avisos",
      component: AvisoLista
    },
    {
      path: "/mesa",
      name: "Mesa",
      component: Mesa
    },
    {
      path: "/documento/:numero",
      name: "Documento",
      component: Documento,
      meta: {
        title: route => {
          return "Documento " + route.params.numero + "..";
        }
      }
    },
    {
      path: "/padrao-lista",
      name: "Lista de Padrões",
      component: PadraoLista
    },
    {
      path: "/padrao/:numero",
      name: "Padrao",
      component: Padrao,
      meta: {
        title: route => {
          return "Padrao " + route.params.numero + "..";
        }
      }
    },
    {
      path: "/aviso-confirmado-recentes",
      name: "Avisos Confirmados Recentemente",
      component: AvisoConfirmadoRecentes
    },
    {
      path: "/aviso-confirmado-lista/:dataInicial/:dataFinal/:porConfirmacao/:porOmissao/:doGrupo",
      name: "Lista de Avisos Confirmados",
      component: AvisoConfirmadoLista
    },
    {
      path: "/sugestoes",
      name: "Sugestões",
      component: Sugestoes
    },
    {
      path: "/status",
      name: "Status",
      component: Status
    },
    {
      path: "/sobre",
      name: "Sobre",
      component: Sobre
    },
    {
      path: "*",
      redirect: "/"
    }
  ],

  scrollBehavior(to, from, savedPosition) {
    if (to.name === "Documento") return {
      x: 0,
      y: 0
    };
  },

});

router.afterEach(() => {
  if (store.state.errorMsg)
    store.commit('setErrorMsg', undefined)
})

export default router
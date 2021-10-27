import Vue from "vue";
import Router from "vue-router";
import store from '../store'
import Home from "@/views/Home";
import ConsultaSimples from "@/views/ConsultaSimples";
import Processo from "@/views/Processo";
import ProcessoLista from "@/views/ProcessoLista";
import PeticaoInicial from "@/views/PeticaoInicial";
import PeticaoIntercorrente from "@/views/PeticaoIntercorrente";
import ConsultaCertidao from "@/views/ConsultaCertidao";
import Certidao from "@/views/Certidao";
import AvisoLista from "@/views/AvisoLista";
import AvisoConfirmadoRecentes from "@/views/AvisoConfirmadoRecentes";
import AvisoConfirmadoLista from "@/views/AvisoConfirmadoLista";
import EtiquetaLista from "@/views/EtiquetaLista";
import Mesa from "@/views/Mesa";
import Documento from "@/views/Documento";
import Login from "@/views/Login";
import TrocarSenha from "@/views/TrocarSenha";
import Sugestoes from "@/views/Sugestoes";
import Ajuda from "@/views/Ajuda";
import Status from "@/views/Status";
import ProcessoBL from "../bl/processo.js";
import PadraoLista from "@/views/PadraoLista";
import Padrao from "@/views/Padrao";
import VotoLista from "@/views/VotoLista";
import Voto from "@/views/Voto";

Vue.use(Router);

const router = new Router({
  routes: [{
      path: "/login",
      name: "Login",
      component: Login
    }, {
      path: "/trocar-senha",
      name: "Trocar Senha",
      component: TrocarSenha
    },
    {
      path: "/consultar",
      name: "Consulta Simples",
      component: ConsultaSimples
    },
    {
      path: "/",
      name: "Home",
      component: Home
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
      path: "/voto-lista",
      name: "Lista de Votos",
      component: VotoLista
    },
    {
      path: "/voto/:numero",
      name: "Voto",
      component: Voto,
      meta: {
        title: route => {
          return "Voto " + route.params.numero + "..";
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
      path: "/ajuda",
      name: "Ajuda",
      component: Ajuda
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
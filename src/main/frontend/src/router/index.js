import Vue from 'vue'
import Router from 'vue-router'
import ConsultaSimples from '@/components/ConsultaSimples'
import Processo from '@/components/Processo'
import ProcessoLista from '@/components/ProcessoLista'
import PeticaoIntercorrente from '@/components/PeticaoIntercorrente'
import AvisoLista from '@/components/AvisoLista'
import Login from '@/components/Login'
import Sugestoes from '@/components/Sugestoes'
import Sobre from '@/components/Sobre'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/',
      name: 'Consulta Simples',
      component: ConsultaSimples
    },
    {
      path: '/processo/:numero',
      name: 'Processo',
      component: Processo
    },
    {
      path: '/processo-lista',
      name: 'Lista de Processos',
      component: ProcessoLista
    },
    {
      path: '/peticao-intercorrente',
      name: 'Petição Intercorrente',
      component: PeticaoIntercorrente
    },
    {
      path: '/aviso-lista',
      name: 'Lista de Avisos',
      component: AvisoLista
    },
    {
      path: '/sugestoes',
      name: 'Sugestões',
      component: Sugestoes
    },
    {
      path: '/sobre',
      name: 'Sobre',
      component: Sobre
    }
  ]
})

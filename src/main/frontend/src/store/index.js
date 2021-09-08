import Vue from "vue";
import Vuex from "vuex";
import UtilsBL from '../bl/utils'
import DocumentoBL from '../bl/documento'
import VotoBL from '../bl/voto'
import VuexPersist from 'vuex-persist'

const vuexPersist = new VuexPersist({
  key: 'bv-data',
  storage: window.localStorage,
  reducer: (state) => ({
    exibirDiferencas: state.exibirDiferencas
  })
})

Vue.use(Vuex);

const store = new Vuex.Store({
  state: {
    mesa: undefined,
    mesas: undefined,
    mesaFiltro: undefined,
    documentos: undefined,
    exibirDiferencas: true,

    votos: undefined,
    votosFiltro: undefined,
    votosSessao: undefined,

    successMsg: undefined,
    errorMsg: undefined,
  },
  getters: {
    documentosFiltrados: state => {
      var a = state.documentos;
      if (!a) return []
      if (!state.mesaFiltro) return a
      a = UtilsBL.filtrarPorSubstring(a, state.mesaFiltro);
      var procnum, procline;
      for (var i = 0; i < a.length; i++) {
        if (procnum !== a[i].numeroDoProcesso) {
          procnum = a[i].numeroDoProcesso;
          procline = i;
          a[i].rows = 1;
        } else {
          a[procline].rows++;
          a[i].rows = 0;
        }
      }
      return a;
    },
    votosFiltrados: state => {
      var a = state.votos;
      if (!a) return []
      if (state.votosSessao)
        a = a.filter(i => i.grupo === state.votosSessao)
      if (state.votosFiltro)
        a = UtilsBL.filtrarPorSubstring(a, state.votosFiltro);
      var procnum, procline;
      var grupo;
      for (var i = 0; i < a.length; i++) {
        if (procnum !== a[i].numeroDoProcesso) {
          procnum = a[i].numeroDoProcesso;
          procline = i;
          a[i].rows = 1;
        } else {
          a[procline].rows++;
          a[i].rows = 0;
        }
      }
      var odd = false;
      for (i = 0; i < a.length; i++) {
        a[i].grupoExibir = a[i].grupo !== grupo;
        grupo = a[i].grupo;
        if (a[i].grupoExibir) odd = false;
        if (a[i].grupoExibir && i > 0) a[i - 1].grupoEspacar = true;
        if (a[i].rows) odd = !odd;
        a[i].odd = odd;
      }
      return a;
    }
  },
  mutations: {
    setMesas(state, val) {
      state.mesas = val
    },
    setMesa(state, val) {
      state.mesa = val
    },
    setMesaFiltro(state, val) {
      state.mesaFiltro = val
    },
    setDocumentos(state, val) {
      state.documentos = val
    },
    updateDocumento(state, val) {
      var i = DocumentoBL.findIndice(state, val.id)
      if (i === undefined) throw "Documento não encontrado"

      // Se fosse necessário atualizar o registro sem substituir o objeto
      //
      // const old = state.documentos[i]
      // Object.assign(old, val)
      // for (var key in old) {
      //   if (old.hasOwnProperty(key) && !val.hasOwnProperty(key)) {
      //     old[key] = undefined;
      //   }
      // }

      Vue.set(state.documentos, i, val)
    },
    setVotos(state, val) {
      state.votos = val
    },
    setVotosFiltro(state, val) {
      state.votosFiltro = val
    },
    setVotosSessao(state, val) {
      state.votosSessao = val
    },
    updateVoto(state, val) {
      var i = VotoBL.findIndice(state, val.id)
      if (i === undefined) throw "Voto não encontrado"
      // state.votos[i] = VotoBL.fix(val)
      const voto = VotoBL.fix(val)
      Vue.set(state.votos, i, voto)
    },

    setExibirDiferencas(state, val) {
      state.exibirDiferencas = val
    },
    clearMsg(state) {
      state.successMsg = undefined
      state.errorMsg = undefined
    },
    setSuccessMsg(state, val) {
      state.successMsg = val
      state.errorMsg = undefined
    },
    setErrorMsg(state, val) {
      state.successMsg = undefined
      state.errorMsg = val
    },
    setError: function (state, val) {
      var errormsg = val.data.errormsg;
      if (errormsg === undefined && val.statusText && val.statusText !== "") {
        errormsg = val.statusText;
      }
      if (errormsg === undefined) {
        errormsg = "Erro desconhecido!";
      }
      state.successMsg = undefined
      state.errorMsg = errormsg
    },
  },
  actions: {
    async carregarMesas({
      commit,
      dispatch
    }) {
      await Vue.http.get("mesa", {
        block: true
      }).then(
        (response) => {
          var mesas = [];
          var list = response.data.list;
          for (var i = 0; i < list.length; i++) {
            var m = list[i];
            mesas.push({
              id: m.id,
              nome: m.nome
            });
          }
          commit('setMesas', mesas);
          if (mesas.length > 0) {
            commit('setMesa', mesas[0]);
            dispatch('carregarMesa')
          }
        },
        (error) => commit("setError", error)
      );
    },
    async carregarMesa({
      commit,
      state
    }) {
      await Vue.http.get("mesa/" + state.mesa.id, {
        block: true
      }).then(
        (response) => {
          var lista = state.documentos || [];
          lista.length = 0;
          var list = response.data.list;
          for (var i = 0; i < list.length; i++) {
            lista.push(DocumentoBL.fix(list[i]));
          }
          commit('setDocumentos', lista)
        },
        (error) => commit("setError", error)
      );
    },
    async adicionarPadrao({
      commit,
      state,
      dispatch
    }, id) {
      var doc = DocumentoBL.find(state, id);
      if (!doc) throw "Documento não encontrado";
      await Vue.http.post("padrao", {
        html: doc.conteudo
      }, {
        block: true
      }).then(
        (response) => {
          var newDoc = {
            ...doc
          };
          newDoc.similaridade = 1.0;
          newDoc.idPadrao = response.data.padrao.id;
          newDoc.diferencas = newDoc.conteudo;
          commit('updateDocumento', newDoc)
          dispatch('carregarMesa')
        },
        (error) => commit("setError", error)
      );
    },

    async removerPadrao({
      commit,
      state,
      dispatch
    }, id) {
      var doc = DocumentoBL.find(state, id);
      if (!doc) throw "Documento não encontrado";
      await Vue.http.delete("padrao/" + doc.idPadrao, {
        block: true
      }).then(
        (response) => {
          var newDoc = {
            ...doc
          };
          newDoc.similaridade = undefined;
          newDoc.idPadrao = undefined;
          newDoc.diferencas = undefined;
          commit('updateDocumento', newDoc)
          dispatch('carregarMesa')
        },
        (error) => commit("setError", error)
      );
    },

    async salvarDocumento({
      commit,
      state,
      dispatch
    }, val) {
      await Vue.http
        .post(
          "mesa/" + "null" + "/documento/" + val.documento.id + "/salvar?sistema=" + val.documento.sistema, {
            html: val.html,
          }, {
            block: true
          }
        )
        .then(
          (response) => {
            dispatch('carregarMesa')
            UtilsBL.logEvento("mesa", "salvar", "minuta");
          },
          (error) => commit("setError", error)
        );
    },

    async carregarVotos({
      commit,
      state
    }) {
      await Vue.http.get("votos", {
        block: true
      }).then(
        (response) => {
          var lista = [];
          var list = response.data.list;
          for (var i = 0; i < list.length; i++) {
            lista.push(VotoBL.fix(list[i]));
            if (list[0].grupo === list[i].grupo && !list[i].statusCodigo) list[i].checked = true
          }
          commit('setVotos', lista)
          if (lista.length && state.votosSessao === undefined)
            commit('setVotosSessao', lista[0].grupo)
        },
        (error) => commit("setError", error)
      );
    },

    async acompanhar({
      commit,
      state,
      dispatch
    }, val) {
      await Vue.http
        .post(
          "votos/" + val.documento.id + "/acompanhar?sistema=" + val.documento.sistema, {}, {
            block: true
          }
        )
        .then(
          (response) => {
            commit("updateVoto", response.data.voto)
            UtilsBL.logEvento("voto", "acompanhar", "acompanhar");
            commit("clearMsg");
          },
          (error) => commit("setError", error)
        );
    },

    async divergir({
      commit,
      state,
      dispatch
    }, val) {
      await Vue.http
        .post(
          "votos/" + val.documento.id + "/divergir?sistema=" + val.documento.sistema, {}, {
            block: true
          }
        )
        .then(
          (response) => {
            commit("updateVoto", response.data.voto)
            UtilsBL.logEvento("voto", "divergir", "divergir");
            commit("clearMsg");
          },
          (error) => commit("setError", error)
        );
    },

    async pedirVista({
      commit,
      state,
      dispatch
    }, val) {
      await Vue.http
        .post(
          "votos/" + val.documento.id + "/pedir-vista?sistema=" + val.documento.sistema, {
            html: val.html,
          }, {
            block: true
          }
        )
        .then(
          (response) => {
            commit("updateVoto", response.data.voto)
            UtilsBL.logEvento("voto", "pedirVista", "pedirVista");
            commit("clearMsg");
          },
          (error) => commit("setError", error)
        );
    },


  },
  modules: {},
  plugins: [vuexPersist.plugin]
});

export default store
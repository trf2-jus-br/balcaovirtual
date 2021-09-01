import Vue from "vue";
import Vuex from "vuex";
import UtilsBL from '../bl/utils'
import DocumentoBL from "../bl/documento.js";
import VuexPersist from 'vuex-persist'

const vuexPersist = new VuexPersist({
  key: 'bv-data',
  storage: window.localStorage,
  reducer: (state) => ({
    exibirDiferencas: state.exibirDiferencas
  })
})

Vue.use(Vuex);

const findDocumentoIndice = function (state, id) {
  if (!state.documentos) return;
  for (var i = 0; i < state.documentos.length; i++) {
    if (state.documentos[i].id === id) return i;
  }
  return;
}

const findDocumento = function (state, id) {
  return state.documentos[findDocumentoIndice(state, id)];
}

const store = new Vuex.Store({
  state: {
    mesa: undefined,
    mesas: undefined,
    mesaFiltro: undefined,
    documentos: undefined,
    exibirDiferencas: true,
    successMsg: undefined,
    errorMsg: undefined,
  },
  getters: {
    documentosFiltrados: state => {
      var a = state.documentos;
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
      var i = findDocumentoIndice(state, val.id)
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
      var doc = findDocumento(state, id);
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
      var doc = findDocumento(state, id);
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

  },
  modules: {},
  plugins: [vuexPersist.plugin]
});

export default store
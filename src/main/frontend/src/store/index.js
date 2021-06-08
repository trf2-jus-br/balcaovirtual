import Vue from "vue";
import Vuex from "vuex";
import UtilsBL from '../bl/utils'
import ProcessoBL from '../bl/processo'
import VuexPersist from 'vuex-persist'

const vuexPersist = new VuexPersist({
  key: 'bv-data',
  storage: window.localStorage,
  reducer: (state) => ({
    exibirDiferencas: state.exibirDiferencas
  })
})

Vue.use(Vuex);

const fixDocumento = function (item) {
  UtilsBL.applyDefauts(item, {
    rows: 1,
    checked: true,
    disabled: false,
    dataDeInclusao: undefined,
    dataDeInclusaoFormatada: undefined,
    id: undefined,
    numeroDoDocumento: undefined,
    tipoDoDocumento: undefined,
    numeroDoProcesso: undefined,
    autor: undefined,
    reu: undefined,
    processoFormatado: undefined,
    descricaoDoStatus: undefined,
    identificadorDoUsuarioQueIncluiu: undefined,
    nomeDoUsuarioQueIncluiu: undefined,
    conteudo: undefined,
    sistema: undefined,
    lembretes: undefined,
    errormsg: undefined,
  });
  if (item.numeroDoProcesso !== undefined) {
    item.processoFormatado = ProcessoBL.formatarProcesso(item.numeroDoProcesso);
  }
  if (item.dataDeInclusao !== undefined) {
    item.dataDeInclusaoFormatada = UtilsBL.formatJSDDMMYYYY(item.dataDeInclusao);
  }
  if (item.lembretes) {
    for (var i = 0; i < item.lembretes.length; i++) {
      item.lembretes[i].dataDeInclusaoFormatada = UtilsBL.formatJSDDMMYYYY(item.lembretes[i].dataDeInclusao);
    }
  }
  return item;
}

const findDocumentoIndice = function (state, id) {
  if (!state.documentos) return;
  for (var i = 0; i < state.documentos.length; i++) {
    console.log(state.documentos[i].id + " - " + id)
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
    documentos: undefined,
    exibirDiferencas: true,
    successMsg: undefined,
    errorMsg: undefined,
  },
  mutations: {
    setMesas(state, val) {
      state.mesas = val
    },
    setMesa(state, val) {
      state.mesa = val
    },
    setDocumentos(state, val) {
      state.documentos = val
    },
    updateDocumento(state, val) {
      console.log("oi1")
      var i = findDocumentoIndice(state, val.id)
      if (i === undefined) throw "Documento não encontrado"
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
          var lista = [];
          var list = response.data.list;
          for (var i = 0; i < list.length; i++) {
            lista.push(fixDocumento(list[i]));
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
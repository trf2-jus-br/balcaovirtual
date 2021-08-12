import UtilsBL from '../bl/utils'
import ProcessoBL from '../bl/processo'

export default {
  fix(item) {
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
      votosProferidos: undefined,
      errormsg: undefined,
    });
    if (item.numeroDoProcesso !== undefined) {
      item.processoFormatado = ProcessoBL.formatarProcesso(item.numeroDoProcesso);
    }
    if (item.dataDeInclusao !== undefined) {
      item.dataDeInclusaoFormatada = UtilsBL.formatJSDDMMYYYY(item.dataDeInclusao);
    }
    if (item.votosProferidos) {
      for (var i = 0; i < item.votosProferidos.length; i++) {
        item.votosProferidos[i].dataDeInclusaoFormatada = UtilsBL.formatJSDDMMYYYY(item.votosProferidos[i].dataDeInclusao);
      }
    }
    if (item.lembretes) {
      for (var j = 0; i < item.lembretes.length; i++) {
        item.lembretes[j].dataDeInclusaoFormatada = UtilsBL.formatJSDDMMYYYY(item.lembretes[j].dataDeInclusao);
      }
    }
    return item;
  },

  findIndice(state, id) {
    if (!state.votos) return;
    for (var i = 0; i < state.votos.length; i++) {
      console.log(state.votos[i].id + " - " + id)
      if (state.votos[i].id === id) return i;
    }
    return;
  },

  find(state, id) {
    return state.votos[this.findIndice(state, id)];
  }

};
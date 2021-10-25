import UtilsBL from '../bl/utils'
import ProcessoBL from '../bl/processo'

export default {
  fix(item) {
    UtilsBL.applyDefauts(item, {
      rows: 1,
      checked: false,
      disabled: false,
      dataDeInclusao: undefined,
      dataDeInclusaoFormatada: undefined,
      id: undefined,
      tipoDeInclusao: undefined,
      numeroDoDocumento: undefined,
      tipoDoDocumento: undefined,
      numeroDoProcesso: undefined,
      autor: undefined,
      reu: undefined,
      processoFormatado: undefined,
      descricaoDoStatus: undefined,
      identificadorDoUsuarioQueIncluiu: undefined,
      nomeDoUsuarioQueIncluiu: undefined,
      htmlMinuta: undefined,
      sistema: undefined,
      lembretes: undefined,
      destaques:undefined,
      votosProferidos: undefined,
      errormsg: undefined,
      odd: undefined,
      grupo: undefined,
      grupoExibir: undefined,
      grupoEspacar: undefined,
      statusCodigo: undefined,
      statusVoto: undefined
    });
    if (item.numeroDoProcesso !== undefined) {
      item.processoFormatado = ProcessoBL.formatarProcesso(item.numeroDoProcesso);
    }
    if (item.dataDeInclusao !== undefined) {
      item.dataDeInclusaoFormatada = UtilsBL.formatJSDDMMYYYY(item.dataDeInclusao);
    }
    if (item.dataDeInclusaoFormatada && item.siglaDaUnidade)
      item.grupo = item.dataDeInclusaoFormatada + " - " + item.siglaDaUnidade

    if (item.votosProferidos) {
      for (var i = 0; i < item.votosProferidos.length; i++) {
        const v = item.votosProferidos[i]
        v.dataDeInclusaoFormatada = UtilsBL.formatJSDDMMYYYY(v.dataDeInclusao);
        if (v.proprio) {
          item.statusCodigo = v.codigoTipo
          item.statusVoto = v.voto
        }
      }
    }

    if (item.destaques) {
      for (var k = 0; k < item.destaques.length; k++) {
        const v = item.destaques[k]
        v.dataDeInclusaoFormatada = UtilsBL.formatJSDDMMYYYY(v.dataDeInclusao);
      }
    }

    if (item.lembretes) {
      for (var j = 0; j < item.lembretes.length; j++) {
        item.lembretes[j].dataDeInclusaoFormatada = UtilsBL.formatJSDDMMYYYY(item.lembretes[j].dataDeInclusao);
      }
    }
    return item;
  },

  findIndice(state, id) {
    if (!state.votos) return;
    for (var i = 0; i < state.votos.length; i++) {
      if (state.votos[i].id === id) return i;
    }
    return;
  },

  find(state, id) {
    return state.votos[this.findIndice(state, id)];
  }

};
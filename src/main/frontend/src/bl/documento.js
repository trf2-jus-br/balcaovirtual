import UtilsBL from "../bl/utils.js";
import ProcessoBL from '../bl/processo'

export default {

    preprocess: function (s) {
        if (!s) return;
        return s
            .replace('contentEditable="true"', 'contentEditable="false" data-bv_edit="true"')
            .replace('contenteditable="true"', 'contentEditable="false" data-bv_edit="true"')
            .replace(/&#x2013;/g, "-");
    },

    preprocessParaExibir: function (s) {
        if (!s) return;
        if (s.includes('<p>&#xA0;</p>')) {
            s = s.replace('<p>&#xA0;</p>', '');
        }
        return s;
    },

    posprocess: function (s) {
        if (!s) return;
        return s.replace('contentEditable="false" data-bv_edit="true"', 'contentEditable="true"');
    },

    fix: function (item) {
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
            // conteudoPreprocessado: undefined,
            diferencas: undefined,
            // diferencasPreprocessadas: undefined,
            sistema: undefined,
            lembretes: undefined,
            errormsg: undefined,
        })
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
        // if (item.conteudo)
        //     item.conteudoPreprocessado = this.preprocessParaExibir(this.preprocess(item.conteudo))
        // if (item.diferencas)
        //     item.diferencasPreprocessadas = this.preprocessParaExibir(this.preprocess(item.diferencas))
        return item;
    }
}
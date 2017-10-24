<template>
  <div>
    <b-modal id="cota" ref="modal" v-model="showModal" title="Cota nos Autos" hide-header-close>
      <form>
        <label class="control-label" for="texto" style="width: 100%">Texto</label>
        <b-form-input type="text" list="lst_cotas" name="texto" id="texto" v-model="texto" class="form-control" aria-describedby="cotaHelp" :class="{'is-invalid': errors.has('texto') }" style="width: 100%" autofocus v-validate.initial="'required'"></b-form-input>
        <datalist id="lst_cotas">
          <option v-for="t in textos">{{t}}</option>
        </datalist>
        <span v-if="false" v-show="errors.has('texto')" class="help is-danger">{{ errors.first('texto') }}</span>
        <small id="cotaHelp" class="form-text text-muted">
          <strong>Atenção</strong>! Ao clicar em prosseguir, o texto acima será convertido em um PDF e instantaneamente protocolado na forma de uma Petição Intercorrente. Por favor, verifique o texto antes de clicar em 'Prosseguir'.</small>
        <em v-if="errormsg &amp;&amp; errormsg !== ''" for="processos" class="invalid">{{errormsg}}</em>
      </form>
      <div style="width: 100%" slot="modal-footer">
        <b-btn variant="outline-warning" @click="preview" :disabled="errors.any()">
          Prever Cota
        </b-btn>
        <b-btn class="float-right ml-2" variant="primary" @click="save" :disabled="errors.any()">
          Prosseguir
        </b-btn>
        <b-btn class="float-right" variant="secondary" @click="$refs.modal.hide(false)">
          Cancelar
        </b-btn>
      </div>
    </b-modal>
    <pdf-preview ref="pdfPreview" title="Pré-visualização da Cota" :src="previewUrl"></pdf-preview>
  </div>
</template>

<script>
import UtilsBL from '../bl/utils.js'
import PdfPreview from './PdfPreviewModal'

var textos = [
  'MM. Juízo, ciente, nada a opor.',
  'Devolvo os autos tendo em vista que a União não é parte.',
  'A União requer o ingresso no feito e vista dos autos após as Informações da Autoridade Coatora.',
  'A União requer o arquivamento dos autos, nos termos do art. 40 da LEF e da Portaria PGFN nº 396/2016 (RDCC).',
  'A União informa que a(s) Inscrição(ões) demanda(s) permanecem parceladas.'
]

export default {
  name: 'processo-cota',

  props: ['processo', 'orgao'],

  data () {
    return {
      textos: textos,
      showModal: false,
      errormsg: undefined,
      texto: '',
      previewUrl: undefined
    }
  },

  methods: {
    show: function () {
      this.showModal = true
      this.errormsg = undefined
    },

    cancel: function (e) {
      e.cancel()
    },

    save: function () {
      if ((this.texto || '') === '') {
        this.errormsg = 'Texto deve ser informado.'
        return
      }

      this.$http.post('processo/' + this.processo + '/cota/enviar', {
        orgao: this.orgao,
        nivelsigilo: '0',
        texto: this.texto
      }, { block: true }).then(response => {
        UtilsBL.logEvento('peticionamento', 'enviar', 'cota')
        this.$emit('ok', response.data.status)
      }, error => {
        this.$emit('erro', error.data.errormsg, this.texto)
      })

      this.$refs.modal.hide(true)
    },

    preview: function () {
      this.$http.post('processo/' + this.processo + '/cota/previsao-pdf', {
        orgao: this.orgao,
        nivelsigilo: '0',
        texto: this.texto
      }, { block: true }).then(response => {
        this.previewUrl = this.$http.options.root + '/download/' + response.data.jwt + '/cota.pdf'
        this.$refs.pdfPreview.show(true)
      }, error => UtilsBL.errormsg(error, this))
    }
  },

  components: {
    'pdf-preview': PdfPreview
  }
}
</script>

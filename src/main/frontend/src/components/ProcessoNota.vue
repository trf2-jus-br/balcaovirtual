<template>
  <div class="card card-consulta-processual mb-3" style="background-color: #f8ff99">
    <div class="card-header">
      <strong>{{titulo}}</strong>
      <button type="button" class="close d-print-none" @click="esconderNotas()">
        <span aria-hidden="true">&times;</span>
      </button>
      <span class="pull-right chars-left">{{caracteresRestantes}}</span>
    </div>
    <div class="card-body">
      <div class="alert alert-danger" role="alert" v-if="errormsg">{{errormsg}}</div>
      <textarea ref="notaUnidade" v-model="textoAtual" @input="notaAlterada"></textarea> texto: {{texto}} id: {{id}} pessoal: {{pessoal}} data: {{dataalteracao}}
    </div>
  </div>
</template>

<script>
import UtilsBL from '../bl/utils.js'
import Debounce from 'debounce'

export default {
  name: 'processo-nota',
  props: ['titulo', 'id', 'texto', 'pessoal', 'dataalteracao', 'processo', 'orgao'],
  mounted () {
  },
  data () {
    return {
      state: 'uninitialized',
      textoAtual: undefined,
      textoAnterior: undefined,
      textoDebonced: undefined,
      errormsg: undefined
    }
  },

  computed: {
    caracteresRestantes: function () {
      var count = this.textoAtual !== undefined ? UtilsBL.getUTF8Length(this.textoAtual) : 0
      var limit = 2000
      if (count === 0) return ''
      return (limit - count) + '/' + limit + ' restantes'
    }
  },

  watch: {
    texto: function () {
      this.textoAtual = this.texto
    }
  },

  methods: {
    esconderNotas: function () {
      this.$parent.$parent.$parent.$emit('setting', 'mostrarNotas', false)
    },

    atualizar: function (nota) {
      if (nota === undefined) nota = { idnota: undefined, texto: undefined, dataalteracao: undefined, pessoal: this.pessoal }
      console.log('emiti:', nota)
      this.$emit('salva', nota)
    },

    refletirAlteracao: Debounce(function () {
      console.log('debounced', this.textoAtual)
      if (this.textoAtual !== undefined && this.textoAtual.trim() === '') this.textoAtual = undefined
      // if (this.textoAtual === this.textoAnterior) return

      if (this.textoAtual) {
        if (this.id) this.putNota(this.textoAtual)
        else this.postNota(this.textoAtual)
      } else {
        if (this.id) this.deleteNota()
      }
    }, 2000),

    notaAlterada: function () {
      this.state = 'changed'
      this.refletirAlteracao()
    },

    postNota: function (texto) {
      this.$http.post('processo/' + this.processo + '/nota?orgao=' + this.orgao, {
        texto: texto,
        pessoal: this.pessoal
      }).then(
        response => {
          this.atualizar(response.data.nota)
        },
        error => UtilsBL.errormsg(error, this))
    },

    putNota: function (texto) {
      this.$http.put('processo/' + this.processo + '/nota/' + this.id + '?orgao=' + this.orgao, {
        texto: texto,
        pessoal: this.pessoal,
        dataalteracao: this.dataalteracao
      }).then(
        response => {
          this.atualizar(response.data.nota)
        },
        error => UtilsBL.errormsg(error, this))
    },

    deleteNota: function () {
      this.$http.delete('processo/' + this.processo + '/nota/' + this.id + '?orgao=' + this.orgao, {}).then(
        response => {
          this.atualizar()
        },
        error => UtilsBL.errormsg(error, this))
    }
  }
}
</script>

<!-- Add 'scoped' attribute to limit CSS to this component only -->
<style scoped>
textarea {
  border: none;
  background: none;
  width: 100%;
  resize: none;
  overflow: hidden;
  min-height: 50px;
}

.chars-left {
  margin-right: 1em;
}
</style>

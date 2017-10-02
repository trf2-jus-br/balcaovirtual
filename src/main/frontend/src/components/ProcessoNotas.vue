<template>
  <div class="d-print-none mt-3" v-show="$parent.$parent.settings.mostrarNotas">
    <div class="card-deck">
      <div class="card card-consulta-processual mb-3" style="background-color: #f8ff99">
        <div class="card-header">
          <strong>Notas da Unidade</strong>
          <button type="button" class="close d-print-none" @click="mostrarNotas(false)">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="card-body">
          <textarea ref="notaUnidade" v-model="notaUnidade.text" @keyup="notasAlteradas()"></textarea>
        </div>
      </div>

      <div class="card card-consulta-processual mb-3" style="background-color: #99ebff">
        <div class="card-header">
          <strong>Notas Pessoais</strong>
          <button type="button" class="close d-print-none" @click="mostrarNotas(false)">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="card-body">
          <textarea ref="notaPessoal" v-model="notaPessoal.texto" @keyup="notasAlteradas()"></textarea>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import UtilsBL from '../bl/utils.js'

export default {
  name: 'processo-notas',
  props: ['processo', 'orgao'],
  mounted () {
    this.$http.get('processo/' + this.processo + '/nota?orgao=' + this.orgao).then(
      response => {
        for (var i = 0; i < response.data.list.length; i++) {
          this.atualizar(response.data.list[i])
        }
      },
      error => {
        UtilsBL.errormsg(error, this)
      })
  },
  data () {
    return {
      notaUnidade: { id: undefined, texto: undefined, textoAnterior: undefined, dataatualizacao: undefined, errormsg: undefined, pessoal: false },
      notaPessoal: { id: undefined, texto: undefined, textoAnterior: undefined, dataatualizacao: undefined, errormsg: undefined, pessoal: true }
    }
  },
  methods: {
    mostrarNotas: function (show) {
      this.$parent.$parent.$emit('setting', 'mostrarNotas', show)

      this.$nextTick(() => {
        this.$refs.notaUnidade.focus()
        this.notasAlteradas()
      })
    },

    atualizar: function (nota) {
      var n = nota.pessoal ? this.notaPessoal : this.notaUnidade
      var alterado = n.dataatualizacao === nota.dataatualizacao
      n.id = nota.idnota
      n.texto = nota.texto
      n.textoAnterior = nota.texto
      n.dataatualizacao = nota.dataatualizacao
      if (alterado) this.notasAlteradas()
    },

    notaAlterada: function (nota) {
      if (nota.texto !== undefined && nota.texto.trim() === '') nota.texto = undefined
      if (nota.texto === nota.textoAnterior) return

      if (nota.texto) {
        if (nota.id) putNota(nota)
        else postNota(nota)
      } else {
        if (nota.id) deleteNota(nota)
      }
    },


    notasAlteradas: function () {
      this.notaAlterada(this.notaUnidade)
      this.notaAlterada(this.notaPessoal)

      this.$emit(this.notaUnidade.texto || this.notaPessoal.texto ? 'ativar' : 'desativar')
      this.$refs.notaUnidade.style.height = '5px'
      this.$refs.notaPessoal.style.height = '5px'
      var h = Math.max(this.$refs.notaUnidade.scrollHeight, this.$refs.notaPessoal.scrollHeight)
      this.$refs.notaUnidade.style.height = h + 'px'
      this.$refs.notaPessoal.style.height = h + 'px'
    },

    postNota: function (nota) {
      this.$http.post('processo/' + this.processo + '/nota?orgao=' + this.orgao, {}).then(
        response => {
          this.atualizar(response.data.nota)
        },
        error => UtilsBL.errormsg(error, this.nota))
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
</style>

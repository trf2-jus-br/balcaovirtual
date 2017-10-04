<template>
  <div class="d-print-none mt-3" v-show="$parent.$parent.settings.mostrarNotas">
    <div class="card-deck">
      <processo-nota :processo="processo" :orgao="orgao" titulo="Notas da Unidade" style="background-color: #f8ff99" :pessoal="notaUnidade.pessoal" :id="notaUnidade.idnota" :texto="notaUnidade.texto" :dataalteracao="notaUnidade.dataalteracao" @salva="notaSalva"></processo-nota>
      <processo-nota :processo="processo" :orgao="orgao" titulo="Notas Pessoais" style="background-color: #f8ff99" :pessoal="notaPessoal.pessoal" :id="notaPessoal.idnota" :texto="notaPessoal.texto" :dataalteracao="notaPessoal.dataalteracao" @salva="notaSalva"></processo-nota>
    </div>
  </div>
</template>

<script>
import UtilsBL from '../bl/utils.js'
import ProcessoNota from './ProcessoNota'

export default {
  name: 'processo-notas',
  props: ['processo', 'orgao'],
  mounted () {
    this.$http.get('processo/' + this.processo + '/nota?orgao=' + this.orgao).then(
      response => {
        for (var i = 0; i < response.data.list.length; i++) {
          var n = response.data.list[i].pessoal ? this.notaPessoal : this.notaUnidade
          UtilsBL.overrideProperties(n, response.data.list[i])
          console.log('carreguei: ', n)
        }
      },
      error => UtilsBL.errormsg(error, this))
  },
  data () {
    return {
      notaUnidade: { idnota: undefined, texto: undefined, textoAnterior: undefined, dataalteracao: undefined, errormsg: undefined, pessoal: false },
      notaPessoal: { idnota: undefined, texto: undefined, textoAnterior: undefined, dataalteracao: undefined, errormsg: undefined, pessoal: true }
    }
  },
  methods: {
    notaSalva: function (nota) {
      console.log('salvei:', nota)
      var n = nota.pessoal ? this.notaPessoal : this.notaUnidade
      UtilsBL.overrideProperties(n, nota)
    },

    mostrarNotas: function (show) {
      this.$parent.$parent.$emit('setting', 'mostrarNotas', show)

      this.$nextTick(() => {
        this.$refs.notaUnidade.focus()
        this.notasAlteradas()
      })
    },

    notasAlteradas: function () {
      this.$emit(this.notaUnidade.texto || this.notaPessoal.texto ? 'ativar' : 'desativar')
      this.$refs.notaUnidade.style.height = '5px'
      this.$refs.notaPessoal.style.height = '5px'
      var h = Math.max(this.$refs.notaUnidade.scrollHeight, this.$refs.notaPessoal.scrollHeight)
      this.$refs.notaUnidade.style.height = h + 'px'
      this.$refs.notaPessoal.style.height = h + 'px'
    }
  },

  components: {
    'processo-nota': ProcessoNota
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

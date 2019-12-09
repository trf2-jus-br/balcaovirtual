<template>
  <div
    class="d-print-none mt-3"
    v-show="$parent.$parent.settings.mostrarNotas"
    v-if="$parent.$parent.jwt"
  >
    <div class="card-deck">
      <processo-nota
        v-show="temCodigoDeUnidade"
        ref="notaUnidade"
        :processo="processo"
        :sistema="sistema"
        titulo="Notas da Unidade"
        style="background-color: #f8ff99"
        :pessoal="false"
        @input="notaUnidadeAlterada"
      ></processo-nota>
      <processo-nota
        ref="notaPessoal"
        :processo="processo"
        :sistema="sistema"
        titulo="Notas Pessoais"
        style="background-color: #f8ff99"
        :pessoal="true"
        @input="notaPessoalAlterada"
      ></processo-nota>
    </div>
  </div>
</template>

<script>
import ProcessoNota from "./ProcessoNota";

export default {
  name: "processo-notas",
  props: ["processo", "sistema"],
  mounted() {
    var jwt = this.$parent.$parent.jwt;
    if (jwt && jwt.user[this.sistema]) {
      this.temCodigoDeUnidade = jwt.user[this.sistema].ieunidade;
    }
  },
  data() {
    return {
      temCodigoDeUnidade: false,
      notaUnidade: undefined,
      notaPessoal: undefined
    };
  },
  methods: {
    notaSalva: function(nota) {},

    mostrarNotas: function(show) {
      this.$parent.$parent.$emit("setting", "mostrarNotas", show);

      this.$nextTick(() => {
        // this.$refs.notaUnidade.focus()
        this.notasAlteradas();
      });
    },

    notaUnidadeAlterada: function(texto) {
      this.notaUnidade = texto !== undefined;
      this.notasAlteradas();
    },

    notaPessoalAlterada: function(texto) {
      this.notaPessoal = texto !== undefined;
      this.notasAlteradas();
    },

    notasAlteradas: function() {
      this.$emit(this.notaUnidade || this.notaPessoal ? "ativar" : "desativar");
      this.$refs.notaUnidade.$refs.textarea.style.height = "5px";
      this.$refs.notaPessoal.$refs.textarea.style.height = "5px";
      var h = Math.max(
        this.$refs.notaUnidade.$refs.textarea.scrollHeight,
        this.$refs.notaPessoal.$refs.textarea.scrollHeight
      );
      this.$refs.notaUnidade.$refs.textarea.style.height = h + "px";
      this.$refs.notaPessoal.$refs.textarea.style.height = h + "px";
    }
  },

  components: {
    "processo-nota": ProcessoNota
  }
};
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

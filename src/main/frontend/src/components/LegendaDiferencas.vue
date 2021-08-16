<template>
  <div>
    <div v-if="diferencas">
      <p class="text-muted mt-2 mb-0" style="font-size: 80%;" v-if="diferencas && documento.similaridade < 1.0 && exibirDiferencas">
        Em relação ao <router-link :to="{ name: 'Padrao', params: { numero: documento.idPadrao } }">padrão</router-link>, palavas incluídas
        aparecem em <span class="editNewInline">verde</span>, excluídas em <span class="editOldInline">vermelho</span> e alteradas em
        <span class="replaceInline">roxo</span>.
      </p>
      <p class="text-muted mt-2 mb-0" style="font-size: 80%;" v-if="diferencas && documento.similaridade < 1.0 && !exibirDiferencas">
        O fundo azul indica que o conteúdo do documento tem alguma similaridade com um
        <router-link :to="{ name: 'Padrao', params: { numero: documento.idPadrao } }">padrão</router-link>.
      </p>
      <p class="text-muted mt-2 mb-0" style="font-size: 80%;" v-if="diferencas && documento.similaridade === 1.0">
        O fundo verde indica que o conteúdo do documento é exatamente igual ao
        <router-link :to="{ name: 'Padrao', params: { numero: documento.idPadrao } }">padrão</router-link>.
      </p>
    </div>
  </div>
</template>
<script>
export default {
  name: "legenda-diferencas",
  props: {
    diferencas: { type: String },
    exibirDiferencas: { type: Boolean },
    documento: {
      type: Object,
      default: function() {
        return {};
      },
    },
  },
  computed: {
    d() {
      return this.diferencas;
    },
  },
  methods: {
    voltar: function() {
      this.$emit("voltar");
    },

    imprimir: function() {
      window.print();
    },
  },
};
</script>

<template>
  <div class="container content pt-5"></div>
</template>

<script>
export default {
  name: "home",
  data() {
    return {};
  },
  mounted() {
    this.$nextTick(() => {
      console.log("home mounted");
      if (!this.$parent.jwt) {
        this.$router.push({ name: "Login" });
      } else if (this.$parent.jwt.isDesembargador() ) {
        this.$router.push({ name: "Lista de Votos" });
      } else if (this.$parent.jwt.isMagistrado()) this.$router.push({ name: "Mesa", params: { revisar: true } });
      else this.$router.push({ name: "Consulta Simples" });
    });
  },
};
</script>

<style scoped></style>

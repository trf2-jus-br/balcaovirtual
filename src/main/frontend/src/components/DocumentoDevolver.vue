<template>
  <div>
    <b-modal
      id="documentoDevolver"
      v-model="showModal"
      title="Devolver Minuta"
      cancel-title="Cancelar"
      ok-title="Devolver"
      hide-header-close
      @show="resetModal"
      @hidden="resetModal"
      @ok="handleOk"
    >
      <form>
        <my-input
          label="Lembrete (opcional)"
          id="lembrete"
          name="lembrete"
          v-model="lembrete"
          aria-describedby="lembreteHelp"
          autofocus
          v-on:keyup.enter="$handleOk"
        ></my-input>
      </form>
    </b-modal>
  </div>
</template>

<script>
export default {
  name: "documento-devolver",

  data() {
    return {
      showModal: false,
      errormsg: undefined,
      lembrete: ""
    };
  },

  methods: {
    show: function() {
      this.showModal = true;
    },

    resetModal: function(e) {
      this.errormsg = undefined;
    },

    handleOk: function(e) {
      if (!e.trigger === "ok") return;
      this.$emit("ok", this.lembrete);
    }
  }
};
</script>

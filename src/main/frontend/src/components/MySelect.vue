<template>
  <div>
    <label v-if="label" :for="name">{{ label }}</label>
    <div v-if="!edit">{{ nome }}</div>
    <validation-provider
      :rules="disabled ? '' : validate"
      :immediate="immediate"
      v-slot="{ errors }"
    >
      <select
        v-if="edit"
        :id="name"
        class="form-control"
        :disabled="disabled"
        v-bind:value="value"
        v-on:input="$emit('input', $event.target.value)"
        v-on:change="$emit('change')"
        :name="name"
        :class="{ 'is-invalid': errors.length > 0 }"
        v-bind="$attrs"
      >
        <option disabled selected hidden :value="undefined"
          >[Selecionar]</option
        >
        <option v-for="l in list" :value="l.id">{{ l.nome }}</option>
      </select>
      <div class="invalid-feedback">{{ errors[0] }}</div>
    </validation-provider>
  </div>
</template>

<script>
export default {
  name: "my-select",
  props: {
    disabled: { type: Boolean, default: false },
    immediate: { type: Boolean, default: true },
    value: String,
    label: String,
    name: String,
    list: Array,
    edit: { type: Boolean, default: true },
    validate: String
  },
  data() {
    return {};
  },
  computed: {
    nome: function() {
      for (var i = 0; i < this.list.length; i++) {
        if (this.list[i].id === this.value) return this.list[i].nome;
      }
      return "";
    }
  }
};
</script>

<template>
  <div>
    <label v-if="label" :for="name">{{ label }}</label>
    <div v-if="!edit">{{ value }}</div>
    <validation-provider
      :rules="validate"
      :immediate="immediate"
      v-slot="{ errors }"
      ref="vp"
    >
      <input
        :type="type"
        v-if="edit &amp;&amp; themask"
        :id="name"
        class="form-control"
        v-themask="themask != undefined ? themask : undefined"
        v-bind:value="value"
        v-on:input="$emit('input', $event.target.value)"
        v-on:change="$emit('change', $event.target.value)"
        :name="name"
        :class="{ 'is-invalid': errors.length > 0 }"
        v-bind="$attrs"
      />
      <input
        :type="type"
        v-if="edit &amp;&amp; !themask"
        :id="name"
        class="form-control"
        v-awemask="mask != undefined ? mask : ''"
        v-bind:value="value"
        v-on:input="$emit('input', $event.target.value)"
        v-on:change="$emit('change', $event.target.value)"
        :name="name"
        :class="{ 'is-invalid': errors.length > 0 }"
        v-bind="$attrs"
      />
      <div class="invalid-feedback">{{ errors[0] }}</div>
    </validation-provider>
  </div>
</template>

<script>
import AwesomeMask from "awesome-mask";
import { mask } from "vue-the-mask";

export default {
  mounted() {
    this.$on("change", val => {
      this.$refs.vp
        .validate()
        .then(r => this.$emit(r.valid ? "valid" : "invalid"));
    });
  },
  name: "my-input",
  props: {
    immediate: { type: Boolean, default: true },
    type: { type: String, default: "text" },
    value: String,
    label: String,
    name: String,
    mask: String,
    themask: [String, Array],
    edit: { type: Boolean, default: true },
    error: String,
    validate: String
  },
  data() {
    return {};
  },
  directives: {
    awemask: AwesomeMask,
    themask: mask
  }
};
</script>

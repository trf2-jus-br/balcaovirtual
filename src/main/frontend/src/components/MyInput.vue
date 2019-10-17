<template>
  <div>
    <label v-if="label" :for="name">{{label}}</label>
    <div v-if="!edit">{{value}}</div>
    <input type="text" v-if="edit &amp;&amp; themask" :id="name" class="form-control" 
      :placeholder="placeholder" v-themask="themask != undefined ? themask : undefined" 
      :disabled="disabled"
      v-bind:value="value" v-on:input="$emit('input', $event.target.value)" v-on:change="$emit('change', $event.target.value)" 
      :name="name" :class="{ 'is-invalid': error }">
    <input type="text" v-if="edit &amp;&amp; !themask" :id="name" class="form-control" 
      :placeholder="placeholder" v-awemask="mask != undefined ? mask : ''"
      :disabled="disabled"
      v-bind:value="value" v-on:input="$emit('input', $event.target.value)" v-on:change="$emit('change', $event.target.value)" 
      :name="name" :class="{ 'is-invalid': error }">
    <div v-if="error" class="invalid-feedback">{{error}}</div>
  </div>
</template>

<script>
import AwesomeMask from 'awesome-mask'
import {mask} from 'vue-the-mask'

export default {
  name: 'my-input',
  props: {
    value: String,
    label: String,
    name: String,
    placeholder: String,
    mask: String,
    themask: [String, Array],
    edit: {type: Boolean, default: true},
    error: String,
    disabled: Boolean
  },
  data() {
    return {}
  },
  directives: {
    'awemask': AwesomeMask,
    'themask': mask
  }
}
</script>

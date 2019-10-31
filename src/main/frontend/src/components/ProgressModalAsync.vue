<template>
  <div>
    <b-modal id="progressModal" v-model="showModal" :title="title" no-close-on-backdrop ok-title="Cancelar" ok-only hide-header-close no-close-on-esc>
      <p>{{progressbarCaption}}</p>
      <div class="progress">
        <div id="progressbar-ad" class="progress-bar" role="progressbar" aria-valuenow="{progressbarWidth}" aria-valuemin="0" aria-valuemax="100" :style="{'width' : progressbarWidth + '%'}">
          <span class="sr-only"></span>
        </div>
      </div>
    </b-modal>
  </div>
</template>

<script>

export default {
  name: 'progressBarAsync',

  data () {
    return {
      showModal: false,
      i: 0,
      title: undefined,
      caption: undefined,
      callbackEnd: function () { },
      progressbarWidth: 0,
      progressbarCaption: undefined
    }
  },

  mounted () {
    this.refresh()
  },

  methods: {
    start: function (title, key, callbackEnd) {
      this.title = title
      this.key = key
      this.callbackEnd = callbackEnd

      this.showModal = true
      this.refresh()
    },

    cancel: function () {
      this.showModal = false
    },

    refresh: function () {
      this.$http.get('status/' + this.key).then(
        response => {
          var r = response.data
          this.progressbarWidth = 100 * (r.indice / r.contador)
          this.progressbarCaption = r.mensagem
          if (r.indice === r.contador) {
            this.showModal = false
            this.callbackEnd(this.i)
          } else {
            setTimeout(() => { this.refresh() }, 1000)
          }
        },
        error => {
          this.errormsg = error.data.errormsg || 'Erro obtendo informações de status'
        })
    }
  }
}
</script>

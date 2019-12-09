<template>
  <div>
    <validation-observer v-slot="{ invalid }">
      <b-modal
        ref="processoPecaDetalhes"
        id="processoPecaDetalhes"
        v-model="showModal"
        title="Marcar Páginas"
        cancel-title="Cancelar"
        ok-title="Salvar Marcador"
        hide-header-close
        no-close-on-esc
        @show="resetModal"
        @hidden="resetModal"
        @hide="hide"
      >
        <b-form>
          <div class="row">
            <div class="col col-md-8 form-group">
              <my-input
                label="Marcador"
                list="lst_userIdTypes"
                id="texto"
                name="texto"
                v-model="texto"
                validate="required"
                autofocus
                v-on:keyup.enter="$refs.processoPecaDetalhes.hide(true)"
              ></my-input>
              <datalist id="lst_userIdTypes">
                <option v-for="m in marcadores" :key="m.id">{{ m }}</option>
              </datalist>
            </div>
            <div class="col col-md-4">
              <label class="control-label" for="estilo" style="width: 100%"
                >Modalidade</label
              >
              <b-form-select
                v-model="estilo"
                :options="estilosfiltrados"
                class="mb-3"
              >
              </b-form-select>
            </div>
          </div>
          <div class="form-check">
            <input
              id="intervaloDePaginas"
              class="mt-3 mb-3"
              type="checkbox"
              v-model="intervalo"
              :disabled="pagmin === 0 || pagmax === 0"
            />
            <label class="form-check-label pl-0" for="intervaloDePaginas"
              >Intervalo de Páginas</label
            >
          </div>
          <div class="row" v-if="intervalo">
            <div class="col form-group">
              <my-input
                label="Página Inicial"
                id="paginicial"
                name="paginicial"
                v-model="paginicial"
                :placeholder="pagmin"
                :validate="'between:' + pagmin + ',' + pagmax + '|required'"
                @change="validar()"
              ></my-input>
            </div>
            <div class="col form-group">
              <my-input
                label="Página Final"
                id="pagfinal"
                name="pagfinal"
                v-model="pagfinal"
                :placeholder="pagmax"
                :validate="'between:' + paginicial + ',' + pagmax + '|required'"
                @change="validar()"
              ></my-input>
            </div>
          </div>
          <em
            v-if="errormsg &amp;&amp; errormsg !== ''"
            for="processos"
            class="invalid"
            >{{ errormsg }}</em
          >
        </b-form>
        <div style="width: 100%" slot="modal-footer">
          <b-btn v-if="editando" variant="outline-danger" @click="remove">
            Remover
          </b-btn>
          <b-btn
            class="float-right ml-2"
            variant="primary"
            @click="$refs.processoPecaDetalhes.hide('ok')"
            :disabled="invalid"
          >
            Gravar
          </b-btn>
          <b-btn
            class="float-right"
            variant="secondary"
            @click="$refs.processoPecaDetalhes.hide('cancel')"
          >
            Cancelar
          </b-btn>
        </div>
      </b-modal>
    </validation-observer>
  </div>
</template>

<script>
export default {
  name: "processo-peca-detalhes",
  data() {
    return {
      interno: this.$parent.$parent.jwt
        ? this.$parent.$parent.jwt.isInterno(this.$parent.sistema)
        : undefined,
      ieunidade:
        this.$parent.$parent.jwt &&
        this.$parent.$parent.jwt.user[this.$parent.sistema]
          ? this.$parent.$parent.jwt.user[this.$parent.sistema].ieunidade
          : undefined,
      editando: false,
      texto: "",
      marcadores: [],
      intervalo: false,
      showModal: false,
      errormsg: undefined,
      paginicial: undefined,
      pagfinal: undefined,
      pagmin: undefined,
      pagmax: undefined,
      estilo: undefined,
      estilos: [
        {
          text: "Pessoal (int)",
          value: "1",
          interno: true,
          pessoal: true
        },
        {
          text: "Unidade (int)",
          value: "2",
          interno: true,
          pessoal: false
        },
        {
          text: "Pessoal (ext)",
          value: "3",
          interno: false,
          pessoal: true
        },
        {
          text: "Grupo (ext)",
          value: "4",
          interno: false,
          pessoal: false
        }
      ]
    };
  },

  computed: {
    estilosfiltrados: function() {
      return this.estilos.filter(
        i =>
          i.interno === this.interno &&
          (i.pessoal ||
            (this.interno && this.ieunidade) ||
            (!this.interno && i.ieentidade))
      );
    }
  },

  watch: {
    intervalo: function(v) {
      if (v) return;
      this.paginicial = this.pagmin;
      this.pagfinal = this.pagmax;
    }
  },

  methods: {
    show: function(marca, marcadores, pagmin, pagmax) {
      this.marcadores = marcadores;
      this.pagmin = pagmin | 0;
      this.pagmax = pagmax | 0;
      this.paginicial = undefined;
      this.pagfinal = undefined;
      if (marca) {
        this.texto = marca.texto;
        this.estilo = marca.idestilo;
        this.paginicial = marca.paginicial;
        this.pagfinal = marca.pagfinal;
        this.editando = true;
      } else {
        this.texto = undefined;
        this.editando = false;
        this.estilo =
          this.$parent.$parent.jwt.origin === "int"
            ? this.ieunidade
              ? "2"
              : "1"
            : "3";
      }
      if (this.paginicial === undefined) this.paginicial = this.pagmin;
      if (this.pagfinal === undefined) this.pagfinal = this.pagmax;
      this.intervalo =
        this.paginicial !== this.pagmin || this.pagfinal !== this.pagmax;
      this.showModal = true;
      this.errormsg = undefined;
    },

    remove: function() {
      this.$emit("remove");
      this.$refs.processoPecaDetalhes.hide(false);
    },

    resetModal: function() {
      console.log("reset");
    },

    hide: function(e) {
      if (e.trigger === "ok") {
        if ((this.texto || "") === "") {
          this.errormsg = "Texto do marcador deve ser informado.";
          e.preventDefault();
          return;
        }

        console.log("emitindo");
        this.$emit("ok", {
          texto: this.texto,
          idestilo: this.estilo,
          intervalo: this.intervalo,
          paginicial: this.paginicial !== 0 ? this.paginicial : undefined,
          pagfinal: this.pagfinal !== 0 ? this.pagfinal : undefined
        });
      }
    }
  }
};
</script>

<template>
  <div class="container content pt-5">
    <div class="row">
      <div class="col-md-12">
        <div class="headline pb-1">
          <h2>Sugestões</h2>
        </div>
        <p>
          Precisamos ouvir sugestões e críticas para que possamos evoluir. Para
          enviá-las, basta preencher o formulário abaixo e clicar em "Enviar
          mensagem".
        </p>
        <br />
        <validation-observer v-slot="{ invalid }">
          <form class="css-form" role="form">
            <div class="row">
              <div class="col-lg-6">
                <div :class="{ 'form-group': true }">
                  <my-input
                    label="Nome"
                    id="nome"
                    name="nome"
                    v-model="sugestao.nome"
                    placeholder=""
                    validate="required"
                  ></my-input>
                </div>
              </div>
              <div class="col-lg-6">
                <div :class="{ 'form-group': true }">
                  <my-input
                    label="E-mail"
                    id="email"
                    name="email"
                    v-model="sugestao.email"
                    placeholder=""
                    validate="required|email"
                  ></my-input>
                </div>
              </div>
            </div>

            <div class="row">
              <div class="col-lg-12">
                <div :class="{ 'form-group': true }">
                  <label for="nome">Mensagem</label>
                  <validation-provider
                    rules="required"
                    :immediate="true"
                    v-slot="{ errors }"
                  >
                    <textarea
                      rows="4"
                      :class="{
                        'form-control': true,
                        'is-invalid': errors.length > 0
                      }"
                      v-model="sugestao.mensagem"
                      id="nome"
                      name="mensagem"
                      placeholder=""
                    >
                    </textarea>
                    <div class="invalid-feedback">{{ errors[0] }}</div>
                  </validation-provider>
                </div>
              </div>
            </div>
            <div class="form-group">
              <div class="row">
                <div class="col-lg-12">
                  <button
                    class="btn btn-primary float-right"
                    :disabled="invalid"
                    @click.prevent="sugerir()"
                  >
                    Enviar mensagem
                  </button>
                </div>
              </div>
            </div>
          </form>
        </validation-observer>
      </div>
      <!--/row-->
    </div>
  </div>
</template>

<script>
import { Bus } from "../bl/bus.js";

export default {
  name: "sugestao",

  data() {
    return {
      sugestao: {
        nome: undefined,
        email: undefined,
        mensagem: undefined
      }
    };
  },

  methods: {
    sugerir: function() {
      this.$http.post("sugestao", this.sugestao).then(
        response => {
          Bus.$emit(
            "message",
            "Sucesso",
            "Sua mensagem foi enviada. Muito obrigado!"
          );
          this.sugestao.nome = undefined;
          this.sugestao.email = undefined;
          this.sugestao.mensagem = undefined;
        },
        error => {
          Bus.$emit("message", "Erro", error.data.errormsg);
        }
      );
    }
  }
};
</script>

<!-- Add 'scoped' attribute to limit CSS to this component only -->
<style scoped></style>

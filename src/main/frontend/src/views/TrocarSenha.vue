<template>
  <div class="container content pt-5">
    <div class="row justify-content-center">
      <div class="col col-sm-12 col-md-6">
        <div class="jumbotron d-block mx-auto">
          <h2 class="text-center pb-3">Troca de Senha</h2>
          <p v-if="errormsg" class="alert alert-danger" role="alert">
            {{ errormsg }}
          </p>
          <validation-observer v-slot="{ invalid }">
            <form role="form">
              <div class="form-group">
                <label for="password">Senha Atual</label>
                <my-input type="password" v-model="user.password" :edit="true" validate="required"></my-input>
              </div>
              <div class="form-group">
                <label for="password">Senha Nova</label>
                <my-input type="password" v-model="user.newpassword" :edit="true" validate="required|password"></my-input>
              </div>
              <div class="form-group">
                <label for="password">Repita a Nova Senha</label>
                <my-input type="password" v-model="repeat" :edit="true" :validate="'required|password|confirmed:' + user.newpassword"></my-input>
              </div>
              <div class="row pt-3">
                <div class="col">
                  <button class="btn btn-primary d-block mx-auto" @click.prevent="trocarSenha()" :disabled="invalid">
                    Enviar
                  </button>
                </div>
              </div>
            </form>
          </validation-observer>
          <!-- <a href="signup()">Click here to Signup</a> -->
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import UtilsBL from "../bl/utils.js";

export default {
  name: "TrocarSenha",
  data() {
    return {
      errormsg: undefined,
      repeat: undefined,
      user: {
        username: this.$parent.jwt.username,
      },
    };
  },
  methods: {
    trocarSenha: function() {
      this.$http.post("trocar-senha", this.user, { block: true }).then(
        (response) => {
          this.$parent.$emit("updateLogged", response.data.id_token);
          this.$router.push({ name: "Home" });
        },
        (error) => UtilsBL.errormsg(error, this)
      );
    },
  },
};
</script>

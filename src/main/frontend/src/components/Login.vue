<template>
  <div class="container content pt-5">
    <div class="row">
      <div class="col col-md-6 offset-md-3">
        <div class="jumbotron d-block mx-auto">
          <h2 class="text-center pb-3">Autenticação</h2>
          <p v-if="errormsg" class="alert alert-danger" role="alert">{{errormsg}}</p>
          <form role="form">
            <div class="form-group">
              <label for="username">Usuário</label>
              <input type="text" class="form-control" v-model="user.username" id="username" placeholder="Username" autocorrect="off" autocapitalize="none">
            </div>
            <div class="form-group">
              <label for="password">Senha</label>
              <input type="password" class="form-control" id="password" v-model="user.password" placeholder="Password">
            </div>
            <div class="row pt-3">
              <div class="col">
                <button class="btn btn-primary d-block mx-auto" @click.prevent="login()">Enviar</button>
              </div>
            </div>
          </form>
          <!-- <a href="signup()">Click here to Signup</a> -->
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'login',
  data () {
    return {
      errormsg: undefined,
      user: {}
    }
  },
  methods: {
    login: function () {
      this.$http.post('sessions/create', this.user, { block: true }).then(response => {
        this.$parent.$emit('updateLogged', response.data.id_token)
      }, error => {
        this.errormsg = error.data.errormsg
      })
    }
  }
}
</script>

<style scoped>

</style>

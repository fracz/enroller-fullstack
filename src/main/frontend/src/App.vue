<template>
  <div id="app">
    <h1>
      <img src="./assets/logo.svg" alt="Enroller" class="logo" />
      System do zapisów na zajęcia
    </h1>
    <div v-if="authenticatedUsername">
      <h2>
        Witaj {{ authenticatedUsername }}!
        <a @click="logout()" class="float-right button-outline button"
          >Wyloguj</a
        >
      </h2>
      <meetings-page :username="authenticatedUsername"></meetings-page>
    </div>
    <div v-else>
      <button :class="wantToRegister ? 'button-outline' : ''"
        @click="wantToRegister = false">Loguję się </button>
      <button :class="wantToRegister ? '' : 'button-outline'"
        @click="wantToRegister = true">Rejestruję się</button>


        <div v-if="errorMessage" class="alert-warning">{{ errorMessage}}</div>


      <login-form v-if="!wantToRegister" @login="login($event)"></login-form>

      <login-form v-else @login="register($event)" button-label="Zarejestruj się"></login-form>
    </div>
  </div>
</template>

<script>
import "milligram";
import LoginForm from "./LoginForm";
import MeetingsPage from "./meetings/MeetingsPage";

export default {
  components: { LoginForm, MeetingsPage },
  data() {
    return {
      authenticatedUsername: "",
      wantToRegister: false,
      errorMessage: ''
    };
  },
  methods: {
    login(user) {
      this.authenticatedUsername = user.login;
    },
    register(user) {
      this.errorMessage='';
      this.$http
        .post("participants", user)
        .then((response) => {
          //udało się
          this.wantToRegister = false;
          this.errorMessage='';
        })
        .catch((response) => {
          //nie udało się
          this.errorMessage='Nazwa użytkownika jest zajęta';
        });
    },
    logout() {
      this.authenticatedUsername = "";
    },
  },
};
</script>

<style>
#app {
  max-width: 1000px;
  margin: 0 auto;
}

.logo {
  vertical-align: middle;
}
.alert-warning{
  border: 3px red dotted;
  padding: 3px;
  text-align: center;
  background: pink;
  border-radius: 30%;
}
</style>


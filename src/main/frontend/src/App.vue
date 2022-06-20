<template>
  <div id="app">
    <h1>
      <img src="./assets/logo.svg" alt="Enroller" class="logo">
      System do zapisów na zajęcia
    </h1>
    <div v-if="authenticatedUsername">
      <h2>Witaj {{ authenticatedUsername }}!
        <a @click="logout()" class="float-right  button-outline button">Wyloguj</a>
      </h2>
      <meetings-page :username="authenticatedUsername"></meetings-page>
    </div>
    <div v-else>
      <button @click="registering = false" :class="registering ? 'button-outline' : ''">Loguję się</button>
      <button @click="registering = true" :class="!registering ? 'button-outline' : ''">Rejestruję się</button>
      <div :class="'alert alert-' + (this.isError ? 'error' : 'success')" v-if="message">{{ message }}</div>
      <login-form @submit="registering ? register($event) : login($event)" :button-label="loginButtonLabel"></login-form>
    </div>
  </div>
</template>

<script>
    import "milligram";
    import LoginForm from "./LoginForm";
    import MeetingsPage from "./meetings/MeetingsPage";
    import Vue from "vue";

    export default {
        components: {LoginForm, MeetingsPage},
        data() {
            return {
                authenticatedUsername: "",
                registering: false,
                message: '',
                isError: false
            };
        },
        methods: {
            register(user) {
                this.clearMessage();
                this.$http.post('participants', user)
                    .then(() => {
                        this.success('Konto zostało założone. Możesz się zalogować.');
                        this.registering = false;
                    })
                    .catch(response => this.failure('Błąd przy zakładaniu konta. Kod odpowiedzi: ' + response.status));
            },
            login(user) {
                this.clearMessage();
                this.$http.post('tokens', user)
                    .then(response => {
                        const token = response.body.token;
                        this.storeAuth(user.login, token);
                    })
                    .catch(() => this.failure('Logowanie nieudane.'));
            },
            storeAuth(username, token) {
                this.authenticatedUsername = username;
                Vue.http.headers.common.Authorization = 'Bearer ' + token;
                localStorage.setItem('username', username);
                localStorage.setItem('token', token);
            },
            logout() {
                this.authenticatedUsername = '';
                delete Vue.http.headers.common.Authorization;
                localStorage.clear();
            },
            success(message) {
                this.message = message;
                this.isError = false;
            },
            failure(message) {
                this.message = message;
                this.isError = true;
            },
            clearMessage() {
                this.message = undefined;
            }
        },
        mounted() {
            const username = localStorage.getItem('username');
            const token = localStorage.getItem('token');
            if (username && token) {
                this.storeAuth(username, token);
                // if token expired or user has been deleted - logout!
                this.$http.get(`participants/${username}`).catch(() => this.logout());
            }
        },
        computed: {
            loginButtonLabel() {
                return this.registering ? 'Zarejestruj się' : 'Zaloguj się';
            }
        }
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

  .alert {
    padding: 10px;
    margin-bottom: 10px;
    border: 2px solid black;
  }
  .alert-success {
      background: lightgreen;
      border-color: darken(lightgreen, 10%);
    }
  .alert-error {
    background: indianred;
    border-color: darken(indianred, 10%);
    color: white;

  }
</style>


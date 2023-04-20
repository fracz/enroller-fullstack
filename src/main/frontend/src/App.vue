<template>
  <div id="app">
    <h1>Witaj w systemie do zapisów na zajęcia</h1>

    <div v-if="authenticatedUsername">
      <UserPanel :username="authenticatedUsername" @logout="logMeOut()"></UserPanel>
      <MeetingsPage :username="authenticatedUsername"></MeetingsPage>
    </div>

    <div v-else>
      <button @click="registering = false" :class="registering ? 'button-outline' : ''">Loguję się</button>
      <button @click="registering = true" :class="!registering ? 'button-outline' : ''">Rejestruję się</button>
      <div :class="'alert alert-' + (this.isError ? 'error' : 'success')" v-if="message">{{ message }}</div>
      <LoginForm v-if="registering" @login="(user) => register(user)" button-label="Załóż konto"></LoginForm>
      <LoginForm v-else @login="(user) => logMeIn(user)"></LoginForm>
    </div>
  </div>
</template>

<script>
import "milligram";
import LoginForm from "./LoginForm";
import UserPanel from "./UserPanel";
import MeetingsPage from "./meetings/MeetingsPage";
import axios from "axios";

export default {
  components: {LoginForm, MeetingsPage, UserPanel},
  data() {
    return {
      authenticatedUsername: '',
      registering: false,
      message: '',
      isError: false,
    }
  },
  mounted() {
    const username = localStorage.getItem('username');
    const token = localStorage.getItem('token');
    if (username && token) {
      this.storeAuth(username, token);
      // if token expired or user has been deleted - logout!
      axios.get(`/api/meetings`).catch(() => this.logMeOut());
    }
  },
  methods: {
    register(user) {
      this.clearMessage();
      axios.post('/api/participants', user)
          .then(() => {
            this.success('Konto zostało założone. Możesz się zalogować.');
            this.registering = false;
          })
          .catch(error => this.failure(`Błąd przy zakładaniu konta. Kod odpowiedzi: ${error.response.status}`));
    },
    logMeIn(user) {
      this.clearMessage();
      axios.post('/api/tokens', user)
          .then((response) => {
            const token = response.data.token;
            this.storeAuth(user.login, token);
          })
          .catch(() => this.failure('Logowanie nieudane.'));
    },
    logMeOut() {
      this.authenticatedUsername = '';
      delete axios.defaults.headers.common.Authorization;
      localStorage.clear();
    },
    storeAuth(username, token) {
      this.authenticatedUsername = username;
      axios.defaults.headers.common['Authorization'] = 'Bearer ' + token;
      localStorage.setItem('username', username);
      localStorage.setItem('token', token);
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
    },
  }
}
</script>

<style>
#app {
  max-width: 1000px;
  margin: 0 auto;
}

.alert {
  padding: 10px;
  margin-bottom: 10px;
  border: 2px solid black;
}

.alert-success {
  background: lightgreen;
  border-color: green;
}

.alert-error {
  background: indianred;
  border-color: darkred;
  color: white;
}
</style>

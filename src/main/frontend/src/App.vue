<template>
  <div id="app">
    <h1>System do zapisów na zajęcia</h1>
    <div v-if="!auth">
      <login-form @login="enter($event)"></login-form>
    </div>
    <div v-show="auth">
      <h2>Witaj {{ auth }}!
      <a @click="logout()" class="float-right  button-outline button">Wyloguj</a>
      </h2>

      <meetings-page :username="auth"></meetings-page>
    </div>
  </div>
</template>

<script>
import "milligram";
import LoginForm from "./LoginForm";
import MeetingsPage from "./meetings/MeetingsPage";

export default {
  components: {LoginForm, MeetingsPage},
  data() {
    return {
      auth: ""
    };
  },
  methods: {
    enter(user) {
      this.auth = user;
    },
    logout() {
      this.auth = '';
    }
  }
};
</script>

<style>
#app {
  max-width: 1000px;
  margin: 0 auto;
}
</style>


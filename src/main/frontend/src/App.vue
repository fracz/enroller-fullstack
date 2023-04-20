<template>
  <div>
    <h1>Witaj w systemie do zapisów na zajęcia</h1>

    <div v-if="authenticatedUsername">
      <UserPanel :username="authenticatedUsername" @logout="logMeOut()"></UserPanel>
      <MeetingsPage :username="authenticatedUsername"></MeetingsPage>
    </div>

    <div v-else>
      <LoginForm @login="(user) => logMeIn(user)"></LoginForm>
    </div>
  </div>
</template>

<script>
import "milligram";
import LoginForm from "./LoginForm";
import UserPanel from "./UserPanel";
import MeetingsPage from "./meetings/MeetingsPage";

export default {
  components: {LoginForm, MeetingsPage, UserPanel},
  data() {
    return {
      authenticatedUsername: '',
    }
  },
  methods: {
    logMeIn(user) {
      this.authenticatedUsername = user.username;
    },
    logMeOut() {
      this.authenticatedUsername = '';
    }
  }
}
</script>

<style>
#app {
  max-width: 1000px;
  margin: 0 auto;
}

.logo {
  vertical-align: middle;
}
</style>

<template>
  <form @submit.prevent="enter()">
    <label>Login</label>
    <input type="text" v-model="user.login">
    <label>Hasło</label>
    <input type="password" v-model="user.password">
    <button type="submit">{{ buttonLabel || 'Zaloguj się' }}</button>
  </form>
</template>

<script>
export default {
  props: {
    buttonLabel: String
  },
  data() {
    return {
      user: {}
    };
  },
  methods: {
    enter() {
      this.$emit("login", this.user);
    }
  }
}
</script>

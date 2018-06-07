<template>
  <form @submit.prevent="enter()">
    <label>Login</label>
    <input type="text" v-model="user.login">
    <label>Hasło</label>
    <input type="password" v-model="user.password">
    <button type="submit">{{ labelOfTheButton }}</button>
  </form>
</template>

<script>
    export default {
        props: ["buttonLabel"],
        data() {
            return {
                user: {}
            };
        },
        methods: {
            enter() {
                this.$emit("submit", this.user);
            }
        },
        computed: {
            labelOfTheButton() {
                return this.buttonLabel || 'Zaloguj się';
            }
        }
    };
</script>

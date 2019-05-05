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
                user: {},
            };
        },
        methods: {
            enter() {
            	this.$emit("submit", this.user);
            },
            check(user) {
                this.clearMessage();
                this.$http.get('participants', user)
                    .then(() => {
                        this.success('Konto zostało założone. Możesz się zalogować.');
                        this.registering = false;
                    })
                    .catch(response => this.failure('Błąd przy zakładaniu konta. Kod odpowiedzi: ' + response.status));
            }

        },
        computed: {
            labelOfTheButton() {
                return this.buttonLabel || 'Zaloguj się';
            }
        }
    };
</script>

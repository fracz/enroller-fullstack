<template>
    <div>
        <h1>Witaj w systemie do zapisów na zajęcia</h1>

        <div v-if="authenticatedUsername">
            <user-panel :username="authenticatedUsername" @logout="logMeOut()"></user-panel>
            <meetings-page></meetings-page>
        </div>

        <div v-else>
            <login-form @login="logMeIn($event)"></login-form>
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
            logMeIn(username) {
                this.authenticatedUsername = username;
            },
            logMeOut() {
                this.authenticatedUsername = '';
            }
        }
    }
</script>

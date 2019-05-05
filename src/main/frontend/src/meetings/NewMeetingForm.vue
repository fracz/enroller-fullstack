<template>
 
  <div>
   <transition name="bounce">
    <form @submit.prevent="addNewMeeting()" v-if="adding">
      <h3>Dodaj nowe spotkanie</h3>
      <label>Nazwa</label>
      <input type="text" v-model="newMeeting.title">
      <label>Opis</label>
      <textarea v-model="newMeeting.description"></textarea>
      <label>Data</label>
      <input type="date" id="bday" v-model="newMeeting.date">
      <button>Dodaj</button>
      <span class="error" v-if="error">Spotkanie musi mieć nazwę!</span>
    </form>
    <button @click="adding = true" v-else>Dodaj nowe spotkanie</button>
   </transition>
  </div>

</template>

<script>
    export default {
        data() {
            return {
                newMeeting: {},
                adding: false,
                error: false
            };
        },
        methods: {
            addNewMeeting() {
                this.error = false;
                if (this.newMeeting.title) {
                    this.$emit('added', this.newMeeting);
                    this.newMeeting = {participants: []};
                    this.adding = false;
                } else {
                    this.error = true;
                }
            }
        }
    }
</script>

<style scoped>
  .error {
    color: #F00;
  }
</style>

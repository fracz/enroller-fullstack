<template>
  <form @submit.prevent="addNewMeeting()">
    <h3>Dodaj nowe spotkanie</h3>
    <label>Nazwa</label>
    <input type="text" v-model="newMeeting.title">
    <label>Opis</label>
    <textarea v-model="newMeeting.description"></textarea>
    <button>Dodaj</button>
    <span class="error" v-if="error">Spotkanie musi mieć nazwę!</span>
  </form>
</template>

<script>
export default {
  data() {
    return {
      newMeeting: { participants: [] },
      error: false,
    };
  },
  methods: {
    addNewMeeting() {
      this.error = false;
      if (this.newMeeting.title) {
        this.$emit('added', this.newMeeting);
        this.newMeeting = { participants: [] };
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

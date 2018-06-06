<template>
    <div>
        <label>Zaloguj się e-mailem</label>
        <input type="email" v-model="email">
        <button @click="enter()">{{ buttonLabel }}</button>
    </div>
</template>

<script>
export default {
  props: ["buttonLabel"],
  data() {
    return {
      email: ""
    };
  },
  methods: {
    enter() {
      this.$emit("login", this.email);
    }
  },
  mounted() {
    if (!this.buttonLabel) {
      this.buttonLabel = "Zaloguj się";
    }
  }
};
</script>

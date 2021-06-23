<template>
  <table v-if="meetings.length > 0">
    <thead>
    <tr>
      <th>Nazwa spotkania</th>
      <th>Opis</th>
      <th>Uczestnicy</th>
      <td></td>
    </tr>
    </thead>
    <tbody>
    <tr v-for="meeting in meetings" :key="meeting.name">
      <td>{{ meeting.name }}</td>
      <td>{{ meeting.description }}</td>
      <td>
        <ul v-if="meeting.participants">
          <li v-for="participant in meeting.participants" :key="participant">
            {{ participant }}
          </li>
        </ul>
      </td>
      <td style="text-align: right; min-width: 400px" v-if="meeting.participants">
        <button v-if="meeting.participants.indexOf(username) < 0"
                class="button-outline"
                @click="$emit('attend', meeting)">
          Zapisz się
        </button>
        <button v-else class="button-outline" @click="$emit('unattend', meeting)">Wypisz się</button>
        <button v-if="meeting.participants.length === 0" class="button" @click="$emit('delete', meeting)">
          Usuń puste spotkanie
        </button>
      </td>
    </tr>
    </tbody>
  </table>
</template>

<script>
    export default {
        props: ['meetings', 'username']
    }
</script>

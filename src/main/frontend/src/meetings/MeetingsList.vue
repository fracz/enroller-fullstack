<template>
  <table v-if="meetings.length > 0">
    <thead>
    <tr>
      <th>Nazwa spotkania</th>
      <th>Opis</th>
      <th>Data</th>
      <th>Uczestnicy</th>    
      <td></td>
    </tr>
    </thead>
    <tbody>
    <tr v-for="meeting in meetings" :key="meeting.name">
      <td>{{ meeting.title }}</td>
      <td>{{ meeting.description }}</td>
      <td>{{ meeting.date }}</td>
      <td>
        <ul v-if="meeting.participants">
          <li v-for="participant in meeting.participants" :key="participant">
            {{ participant.login }}
          </li>
        </ul>
      </td>
      <td style="text-align: right; min-width: 400px">
        <button v-if="!present(meeting)" class="button-outline"
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
        props: ['meetings', 'username'],
        methods: {
            present(meeting) {
                return meeting.participants.map(p => p.login).indexOf(this.username) !== -1;
            }
        }
    }
</script>

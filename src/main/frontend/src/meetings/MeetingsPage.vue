<template>
  <div>
    <NewMeetingForm @added="addNewMeeting($event)"></NewMeetingForm>
    <span v-if="meetings.length == 0">Brak zaplanowanych spotkań.</span>
    <h3 v-else>Zaplanowane zajęcia ({{ meetings.length }})</h3>
    <MeetingsList :meetings="meetings" :username="username" @attend="addMeetingParticipant($event)"
      @unattend="removeMeetingParticipant($event)" @delete="deleteMeeting($event)"></MeetingsList>
  </div>
</template>

<script>
import NewMeetingForm from "./NewMeetingForm";
import MeetingsList from "./MeetingsList";
import axios from "axios";

export default {
  components: { NewMeetingForm, MeetingsList },
  props: { username: String },
  data() {
    return {
      meetings: [],
      token: ""
    };
  },
  beforeMount() {
    axios.get(`/api/meetings`, JSON.stringify(this.username), {
      headers: {
        'Content-Type': 'application/json'
      }
    })
      .then(resp => {
        resp.data.map(meeting => {
          const objectMeeting = {"id": meeting.id, "title": meeting.title, "description": meeting.description, "participants": meeting.participants.map(participant => participant["login"])}
          this.meetings.push(objectMeeting);
        })
      })
      .catch(error => console.log(error));
  },
  methods: {
    addNewMeeting(meeting) {
      axios.post(`/api/meetings`, JSON.stringify({ title: meeting.title, description: meeting.description }), {
        headers: {
          'Content-Type': 'application/json'
        }
      })
        .then(response => this.meetings.push(response.data))
        .catch(error => console.log(error));

    },
    addMeetingParticipant(meeting) {
      axios.post(`/api/meetings/${meeting.id}/participants`, { 'login': this.username }, {
        headers: {
          'Content-Type': 'application/json'
        }
      })
      .then(resp => {
        resp.data.map(participant => {
          if (meeting.participants.indexOf(this.username) >= 0){
            // do nothing
          } else {
            meeting.participants.push(participant["login"])
          }
        }
      )})
        .catch(err => console.log(err));
    },
    removeMeetingParticipant(meeting) {
      axios.delete(`/api/meetings/${meeting.id}/participants/${this.username}`, {
        headers: {
          'Content-Type': 'application/json'
        }
      })
        .then(response => {
          meeting.participants.splice(meeting.participants.indexOf(this.username), 1);
        })
        .catch(error => console.log(error));
    },
    deleteMeeting(meeting) {
      axios.delete(`/api/meetings/${meeting.id}`, {
        headers: {
          'Content-Type': 'application/json'
        }
      })
        .then(response => this.meetings.splice(this.meetings.indexOf(response.data), 1))
        .catch(error => console.log(error));
    },
  }
}
</script>

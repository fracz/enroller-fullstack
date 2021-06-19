<template>
  <div>
    <new-meeting-form @added="addNewMeeting($event)"></new-meeting-form>

    <span v-if="meetings.length == 0">
               Brak zaplanowanych spotkań.
           </span>
    <h3 v-else>
      Zaplanowane zajęcia ({{ meetings.length }})
    </h3>

    <meetings-list :meetings="meetings"
                   :username="username"
                   @attend="addMeetingParticipant($event)"
                   @unattend="removeMeetingParticipant($event)"
                   @delete="deleteMeeting($event)"></meetings-list>
  </div>
</template>

<script>
    import NewMeetingForm from "./NewMeetingForm";
    import MeetingsList from "./MeetingsList";

    export default {
        components: {NewMeetingForm, MeetingsList},
        props: ['username'],
        data() {
            return {
                meetings: []
            };
        },
        methods: {
            addNewMeeting(meeting) {
                    this.$http.post('meetings', meeting)
                    .then((response) => {
                         this.meetings.push({id: response.body.id, name:meeting.name, description: meeting.description, date: meeting.date, participants: meeting.participants});
                    })
                    .catch(response => this.failure('Błąd dodawania spotkania, kod odpowiedzi: ' + response.status));

            },
            addMeetingParticipant(meeting) {          
             this.$http.post(`meetings/${meeting.id}/participants`,this.username)
                    .then(() => {
                         meeting.participants.push(this.username);
                    })
            },
            removeMeetingParticipant(meeting) {
                     this.$http.delete(`meetings/${meeting.id}/${this.username}`)
                    .then(() => {
                          meeting.participants.splice(meeting.participants.indexOf(this.username), 1);
                    })
            },
            deleteMeeting(meeting) {
                this.$http.delete(`meetings/${meeting.id}`).then(() => 
                this.meetings.splice(this.meetings.indexOf(meeting), 1));
            }            
        },
        mounted(){
            this.$http.get('meetings')
                    .then((response) => {
                       for(let meeting of response.body) {  
                            this.$http.get(`meetings/${meeting.id}/participants`).then((r) => {return r.body.map(obj => obj.login);})
                            .then((participants) => {
                       		        this.meetings.push({id: meeting.id, name: meeting.name, description: meeting.description, participants: participants});
                                     })          
                             }
                        });
        }

    }
</script>

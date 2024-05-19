import {useState} from "react";
import NewMeetingForm from "./NewMeetingForm";
import MeetingsList from "./MeetingsList";

export default function MeetingsPage({username}) {
    const [meetings, setMeetings] = useState([]);
    const [addingNewMeeting, setAddingNewMeeting] = useState(false);

    async function handleNewMeeting(meeting) {
        const response = await fetch('/api/meetings', {
            method: 'POST',
            body: JSON.stringify(meeting),
            headers: { 'Content-Type': 'application/json' }
        });
        if (response.ok) {
            const nextMeetings = [...meetings, meeting];
            setMeetings(nextMeetings);
            setAddingNewMeeting(false);
        }
    }

    function handleDeleteMeeting(meeting) {
        const nextMeetings = meetings.filter(m => m !== meeting);
        setMeetings(nextMeetings);
    }

    function handleSignIn(meeting) {
        const nextMeetings = meetings.map(m => {
            if (m === meeting) {
                m.participants = [...m.participants, username];
            }
            return m;
        });
        setMeetings(nextMeetings);
    }

    function handleSignOut(meeting) {
        const nextMeetings = meetings.map(m => {
            if (m === meeting) {
                m.participants = m.participants.filter(u => u !== username);
            }
            return m;
        });
        setMeetings(nextMeetings);
    }

    return (
        <div>
            <h2>Zajęcia ({meetings.length})</h2>
            {
                addingNewMeeting
                    ? <NewMeetingForm onSubmit={(meeting) => handleNewMeeting(meeting)}/>
                    : <button onClick={() => setAddingNewMeeting(true)}>Dodaj nowe spotkanie</button>
            }
            {meetings.length > 0 &&
                <MeetingsList meetings={meetings} username={username}
                              onDelete={handleDeleteMeeting}
                              onSignIn={handleSignIn}
                              onSignOut={handleSignOut}/>}
        </div>
    )
}

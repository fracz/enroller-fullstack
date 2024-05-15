import MeetingButtons from "./MeetingButtons";

export default function MeetingsList({meetings, username, onDelete, onSignOut, onSignIn}) {
    return (
        <table>
            <thead>
            <tr>
                <th>Nazwa spotkania</th>
                <th>Opis</th>
                <th>Uczestnicy</th>
                <th>Akcje</th>
            </tr>
            </thead>
            <tbody>
            {
                meetings.map((meeting, index) => <tr key={index}>
                    <td>{meeting.title}</td>
                    <td>{meeting.description}</td>
                    <td>
                        {
                            meeting.participants.length > 0
                                ? <ul>{meeting.participants.map(p => <li key={p.id}>{p.login}</li>)}</ul>
                                : <em>Brak uczestników</em>
                        }
                    </td>
                    <td>
                        <MeetingButtons meeting={meeting}
                                        username={username}
                                        onDelete={() => onDelete(meeting)}
                                        onSignIn={() => onSignIn(meeting)}
                                        onSignOut={() => onSignOut(meeting)}/>
                    </td>
                </tr>)
            }
            </tbody>
        </table>
    );
}

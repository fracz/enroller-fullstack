import MeetingsPage from "./meetings/MeetingsPage";

export default function UserPanel({username, onLogout}) {
    return <div>
        <h2>Witaj {username}!</h2>
        <button onClick={onLogout}>Wyloguj</button>
        <MeetingsPage username={username}/>
    </div>;
}

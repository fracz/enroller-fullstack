export default function MeetingButtons({username, meeting, onDelete, onSignOut, onSignIn}) {
    const isAttending = meeting.participants.map(p => p.login).includes(username);
    const isEmpty = meeting.participants.length === 0;

    return <>
        {
            isAttending
                ? <button onClick={onSignOut}>Wypisz się</button>
                : <button onClick={onSignIn}>Zapisz się</button>
        }
        {isEmpty && <button onClick={onDelete} className="button-outline">Usuń puste spotkanie</button>}
    </>
        ;
}

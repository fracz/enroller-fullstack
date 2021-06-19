package com.company.enroller.persistence;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("meetingService")
public class MeetingService {

    Session session;

    public MeetingService() {
        session = DatabaseConnector.getInstance().getSession();
    }

    public Collection<Meeting> getAll() {
        String hql = "FROM Meeting";
        Query query = session.createQuery(hql);
        return query.list();
    }


    public Meeting findById(long id) {
        return (Meeting) session.get(Meeting.class, id);
    }

    public void add(Meeting meeting) {
        Transaction transaction = this.session.beginTransaction();
        session.save(meeting);
        transaction.commit();
    }

    public void delete(Meeting meeting) {
        Transaction transaction = this.session.beginTransaction();
        session.delete(meeting);
        transaction.commit();
    }

    public void update(Meeting meeting) {
        Transaction transaction = this.session.beginTransaction();
        session.merge(meeting);
        transaction.commit();
    }

    public Participant addParticipant(Meeting meeting, Participant participant) {
        Transaction transaction = this.session.beginTransaction();
        meeting.addParticipant(participant);
        session.merge(meeting);
        transaction.commit();
        return participant;
    }

    public Meeting deleteParticipant(Meeting meeting, Participant participant) {
        Transaction transaction = this.session.beginTransaction();
        meeting.removeParticipant(participant);
        session.merge(meeting);
        transaction.commit();
        return meeting;
    }


}

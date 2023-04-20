package com.company.enroller.persistence;

import org.hibernate.Session;

public class DatabaseConnector {

    protected static DatabaseConnector instance = null;

    public static DatabaseConnector getInstance() {
        if (instance == null) {
            instance = new DatabaseConnector();
        }
        return instance;
    }

    private Session session;

    private DatabaseConnector() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    public void teardown() {
        session.close();
        HibernateUtil.shutdown();
        instance = null;
    }

    public Session getSession() {
        return session;
    }

}

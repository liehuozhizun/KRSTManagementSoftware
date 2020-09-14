package utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import repositories.Logger;

public class HibernateUtils {

    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            Logger.log("java.utils.HibernateUtils", ex.getMessage());
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError("Connection to database error!");
        }
    }

    public static Session openSession() throws Exception {
        Session session;
        try {
            session = sessionFactory.openSession();
        } catch (Exception e) {
            Logger.log("java.utils.HibernateUtils", e.getMessage());
            throw new Exception("Connection to database error!");
        }
        return session;
    }

    public static void closeSessionFactory() {
        sessionFactory.close();
    }
}

package repositories;

import domains.Log;
import org.hibernate.Session;
import utils.HibernateLogUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class Logger {

    private static Session session;

    static {
        try {
            session = HibernateLogUtils.openSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Log> findAll() {
        try {
            return session.createQuery("from Log").list();
        } catch (Exception e) {
            log("repositories:LogDatabase", e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void log(String path, String message) {
        try {
            session.getTransaction().begin();
            session.save(new Log(path, message));
            session.getTransaction().commit();
        } catch (Exception e) {
            System.exit(1);
        }
    }

    public static void closeSession() {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }
}

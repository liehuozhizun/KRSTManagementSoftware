package org.krst.app.repositories;

import org.krst.app.domains.Log;
import org.hibernate.Session;
import org.krst.app.utils.HibernateLogUtils;

import java.util.ArrayList;
import java.util.List;

public class Logger {

    public static List findAll() {
        try (Session session = HibernateLogUtils.openSession()) {
            return session.createQuery("from Log").list();
        } catch (Exception e) {
            logError("repositories: LogDatabase", e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void logInfo(String path, String message) {
        try (Session session = HibernateLogUtils.openSession()) {
            session.getTransaction().begin();
            session.save(new Log(Log.Type.INFO, path, message));
            session.getTransaction().commit();
        } catch (Exception e) {
            System.exit(1);
        }
    }

    public static void logError(String path, String message) {
        try (Session session = HibernateLogUtils.openSession()) {
            session.getTransaction().begin();
            session.save(new Log(Log.Type.ERROR, path, message));
            session.getTransaction().commit();
        } catch (Exception e) {
            System.exit(1);
        }
    }

    public static void logFetal(String path, String message) {
        try (Session session = HibernateLogUtils.openSession()) {
            session.getTransaction().begin();
            session.save(new Log(Log.Type.FETAL, path, message));
            session.getTransaction().commit();
        } catch (Exception e) {
            System.exit(1);
        }
    }

    public static void logWarn(String path, String message) {
        try (Session session = HibernateLogUtils.openSession()) {
            session.getTransaction().begin();
            session.save(new Log(Log.Type.WARN, path, message));
            session.getTransaction().commit();
        } catch (Exception e) {
            System.exit(1);
        }
    }
}

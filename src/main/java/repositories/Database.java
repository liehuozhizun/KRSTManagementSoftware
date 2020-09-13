package repositories;

import org.hibernate.Session;
import utils.HibernateUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class Database<T> {
    protected String databaseName;

    public List<T> findAll() {
        try (Session session = HibernateUtils.openSession()) {
            return session.createQuery("from "+databaseName).list();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public <T> T findById(Class c, String id) {
        try (Session session = HibernateUtils.openSession()) {
            return (T) session.load(c, id);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> T findById(Class c, Long id) {
        try (Session session = HibernateUtils.openSession()) {
            return (T) session.load(c, id);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> T save(T object) {
        try (Session session = HibernateUtils.openSession()) {
            return (T) session.save(object);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> T update(T object) {
        try (Session session = HibernateUtils.openSession()) {
            session.getTransaction().begin();
            session.update(object);
            session.getTransaction().commit();
        } catch (Exception e) {
            return null;
        }
        return object;
    }

    public <T> T delete(T object) {
        try (Session session = HibernateUtils.openSession()) {
            session.getTransaction().begin();
            session.delete(object);
            session.getTransaction().commit();
        } catch (Exception e) {
            return null;
        }
        return object;
    }

}
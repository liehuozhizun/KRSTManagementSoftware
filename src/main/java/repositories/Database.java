package repositories;

import org.hibernate.Session;
import utils.HibernateUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class Database<T> {
    protected String databaseName;

    public List<T> findAll() {
        try (Session session = HibernateUtils.openSession()) {
            return session.createQuery("from "+databaseName).list(); // Lazy initialization
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public T findById(Class c, String id) {
        try (Session session = HibernateUtils.openSession()) {
            T result = (T) session.get(c, id);
            result.toString(); // Avoid lazy initialization
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public T findById(Class c, Long id) {
        try (Session session = HibernateUtils.openSession()) {
            T result = (T) session.get(c, id);
            result.toString(); // Avoid lazy initialization
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public T save(T object) {
        try (Session session = HibernateUtils.openSession()) {
            session.getTransaction().begin();
            session.save(object);
            session.getTransaction().commit();
        } catch (Exception e) {
            return null;
        }
        return object;
    }

    public T update(T object) {
        try (Session session = HibernateUtils.openSession()) {
            session.getTransaction().begin();
            session.update(object);
            session.getTransaction().commit();
        } catch (Exception e) {
            return null;
        }
        return object;
    }

    public T delete(T object) {
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

package org.krst.app.repositories;

import org.hibernate.Session;
import org.krst.app.models.Status;
import org.krst.app.utils.HibernateUtils;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unchecked")
public abstract class Database<T> {
    protected String databaseName;

    public List<T> findAll() {
        try (Session session = HibernateUtils.openSession()) {
            return session.createQuery("from "+databaseName).list(); // Lazy initialization
        } catch (Exception e) {
            Logger.logError(this.getClass().toString(), e.getMessage());
            return new ArrayList<>();
        }
    }

    public Optional findById(Class c, String id) {
        try (Session session = HibernateUtils.openSession()) {
            T result = (T) session.get(c, id);
            result.toString(); // Avoid lazy initialization
            return Optional.ofNullable(result);
        } catch (Exception e) {
            Logger.logError(this.getClass().toString(), e.getMessage());
            return Optional.empty();
        }
    }

    public Optional findById(Class c, Long id) {
        try (Session session = HibernateUtils.openSession()) {
            T result = (T) session.get(c, id);
            result.toString(); // Avoid lazy initialization
            return Optional.ofNullable(result);
        } catch (Exception e) {
            Logger.logError(this.getClass().toString(), e.getMessage());
            return Optional.empty();
        }
    }

    public Status save(T object) {
        try (Session session = HibernateUtils.openSession()) {
            session.getTransaction().begin();
            session.save(object);
            session.getTransaction().commit();
        } catch (PersistenceException e) {
            Logger.logError(this.getClass().toString(), e.getMessage());
            return Status.CONSTRAINT_VIOLATION;
        } catch (Exception e) {
            System.out.println(e);
            Logger.logError(this.getClass().toString(), e.getMessage());
            return Status.ERROR;
        }
        return Status.SUCCESS;
    }

    public Status update(T object) {
        try (Session session = HibernateUtils.openSession()) {
            session.getTransaction().begin();
            session.update(object);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logError(this.getClass().toString(), e.getMessage());
            return Status.ERROR;
        }
        return Status.SUCCESS;
    }

    public Status delete(T object) {
        try (Session session = HibernateUtils.openSession()) {
            session.getTransaction().begin();
            session.delete(object);
            session.getTransaction().commit();
        } catch (Exception e) {
            Logger.logError(this.getClass().toString(), e.getMessage());
            return Status.ERROR;
        }
        return Status.SUCCESS;
    }

}

package org.krst.app.repositories.repositories;

import org.krst.app.domains.Visit;
import org.krst.app.domains.operations.VisitOperations;
import org.krst.app.models.Status;
import org.krst.app.repositories.Database;

import java.util.HashSet;

public interface VisitRepository {
    default <T> Status addVisit(Database<T> database, T t, Visit visit) {
        if (t instanceof VisitOperations) {
            if (((VisitOperations) t).getVisits() == null) ((VisitOperations) t).setVisits(new HashSet<>());
            ((VisitOperations) t).getVisits().add(visit);
        } else {
            return null;
        }
        return database.update(t);
    }

    default <T> Status updateVisit(Database<T> database, T t, Visit visit) {
        if (t instanceof VisitOperations) {
            if (((VisitOperations) t).getVisits() == null) ((VisitOperations) t).setVisits(new HashSet<>());
            ((VisitOperations) t).getVisits().removeIf(x -> x.getId().equals(visit.getId()));
            ((VisitOperations) t).getVisits().add(visit);
        } else {
            return null;
        }
        return database.update(t);
    }

    default <T> Status deleteVisit(Database<T> database, T t, Visit visit) {
        if (t instanceof VisitOperations) {
            if (((VisitOperations) t).getVisits() == null) return Status.SUCCESS;
            ((VisitOperations) t).getVisits().removeIf(x -> x.getId().equals(visit.getId()));
        } else {
            return null;
        }
        return database.update(t);
    }
}

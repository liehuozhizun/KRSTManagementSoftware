package org.krst.app.repositories.repositories;

import org.krst.app.domains.Visit;
import org.krst.app.domains.operations.VisitOperations;
import org.krst.app.repositories.Database;

import java.util.ArrayList;

public interface VisitRepository {
    default <T> T addVisit(Database<T> database, T t, Visit visit) {
        if (t instanceof VisitOperations) {
            if (((VisitOperations) t).getVisits() == null) ((VisitOperations) t).setVisits(new ArrayList<>());
            ((VisitOperations) t).getVisits().add(visit);
        } else {
            return null;
        }
        return database.update(t);
    }

    default <T> T updateVisit(Database<T> database, T t, Visit visit) {
        if (t instanceof VisitOperations) {
            if (((VisitOperations) t).getVisits() == null) ((VisitOperations) t).setVisits(new ArrayList<>());
            ((VisitOperations) t).getVisits().removeIf(x -> x.getId().equals(visit.getId()));
            ((VisitOperations) t).getVisits().add(visit);
        } else {
            return null;
        }
        return database.update(t);
    }

    default <T> T deleteVisit(Database<T> database, T t, Visit visit) {
        if (t instanceof VisitOperations) {
            if (((VisitOperations) t).getVisits() == null) return t;
            ((VisitOperations) t).getVisits().removeIf(x -> x.getId().equals(visit.getId()));
        } else {
            return null;
        }
        return database.update(t);
    }
}

package org.krst.app.repositories.repositories;

import org.krst.app.domains.Internship;
import org.krst.app.domains.operations.InternshipOperations;
import org.krst.app.domains.operations.VisitOperations;
import org.krst.app.models.Status;
import org.krst.app.repositories.Database;

import java.util.HashSet;

public interface InternshipRepository {
    default <T> Status addInternship(Database<T> database, T t, Internship internship) {
        if (t instanceof InternshipOperations) {
            if (((InternshipOperations) t).getInternships() == null) ((InternshipOperations) t).setInternships(new HashSet<>());
            ((InternshipOperations) t).getInternships().add(internship);
        } else {
            return null;
        }
        return database.update(t);
    }

    default <T> Status updateInternship(Database<T> database, T t, Internship internship) {
        if (t instanceof VisitOperations) {
            if (((InternshipOperations) t).getInternships() == null) ((InternshipOperations) t).setInternships(new HashSet<>());
            ((InternshipOperations) t).getInternships().removeIf(x -> x.getId() == internship.getId());
            ((InternshipOperations) t).getInternships().add(internship);
        } else {
            return null;
        }
        return database.update(t);
    }

    default <T> Status deleteInternship(Database<T> database, T t, Internship internship) {
        if (t instanceof InternshipOperations) {
            if (((InternshipOperations) t).getInternships() == null) return Status.SUCCESS;
            ((InternshipOperations) t).getInternships().removeIf(x -> x.getId() == internship.getId());
        } else {
            return null;
        }
        return database.update(t);
    }
}

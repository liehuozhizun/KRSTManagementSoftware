package repositories.repositories;

import domains.Internship;
import domains.operations.InternshipOperations;
import domains.operations.VisitOperations;
import repositories.Database;

import java.util.ArrayList;

public interface InternshipRepository {
    default <T> T addInternship(Database<T> database, T t, Internship internship) {
        if (t instanceof InternshipOperations) {
            if (((InternshipOperations) t).getInternships() == null) ((InternshipOperations) t).setInternships(new ArrayList<>());
            ((InternshipOperations) t).getInternships().add(internship);
        } else {
            return null;
        }
        return database.update(t);
    }

    default <T> T updateInternship(Database<T> database, T t, Internship internship) {
        if (t instanceof VisitOperations) {
            if (((InternshipOperations) t).getInternships() == null) ((InternshipOperations) t).setInternships(new ArrayList<>());
            ((InternshipOperations) t).getInternships().removeIf(x -> x.getId() == internship.getId());
            ((InternshipOperations) t).getInternships().add(internship);
        } else {
            return null;
        }
        return database.update(t);
    }

    default <T> T deleteInternship(Database<T> database, T t, Internship internship) {
        if (t instanceof InternshipOperations) {
            if (((InternshipOperations) t).getInternships() == null) return t;
            ((InternshipOperations) t).getInternships().removeIf(x -> x.getId() == internship.getId());
        } else {
            return null;
        }
        return database.update(t);
    }
}

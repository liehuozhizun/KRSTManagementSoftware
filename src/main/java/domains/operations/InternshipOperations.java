package domains.operations;

import domains.Internship;

import java.util.List;

public interface InternshipOperations {
    List<Internship> getInternships();
    void setInternships(List<Internship> internships);
}

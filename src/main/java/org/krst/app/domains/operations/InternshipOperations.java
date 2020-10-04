package org.krst.app.domains.operations;

import org.krst.app.domains.Internship;

import java.util.List;

public interface InternshipOperations {
    List<Internship> getInternships();
    void setInternships(List<Internship> internships);
}

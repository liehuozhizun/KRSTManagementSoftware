package org.krst.app.domains.operations;

import org.krst.app.domains.Internship;

import java.util.Set;

public interface InternshipOperations {
    Set<Internship> getInternships();
    void setInternships(Set<Internship> internships);
}

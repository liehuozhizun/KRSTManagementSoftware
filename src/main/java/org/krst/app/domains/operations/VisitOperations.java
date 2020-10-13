package org.krst.app.domains.operations;

import org.krst.app.domains.Visit;

import java.util.Set;

public interface VisitOperations {
    Set<Visit> getVisits();
    void setVisits(Set<Visit> visits);
}

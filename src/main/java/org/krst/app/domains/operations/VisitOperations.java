package org.krst.app.domains.operations;

import org.krst.app.domains.Visit;

import java.util.List;

public interface VisitOperations {
    List<Visit> getVisits();
    void setVisits(List<Visit> visits);
}

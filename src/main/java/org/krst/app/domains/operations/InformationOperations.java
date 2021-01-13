package org.krst.app.domains.operations;

import org.krst.app.domains.Relation;

import java.time.LocalDate;
import java.util.Set;

public interface InformationOperations {
    String getId();
    String getName();
    String getBaptismalName();
    LocalDate getBirthday();
    Boolean getIsGregorianCalendar();
    LocalDate getBaptismalDate();
    LocalDate getConfirmationDate();
    LocalDate getMarriageDate();
    LocalDate getDeathDate();
    Set<Relation> getRelationships();

    default String getNameAndId() {
        return getName() + " [" + getId() + "]";
    }
}

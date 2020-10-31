package org.krst.app.domains.operations;

import java.time.LocalDate;

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
}

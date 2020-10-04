package org.krst.app.domains.operations;

import java.time.LocalDate;

public interface InformationOperations {
    String getName();
    LocalDate getBirthday();
    LocalDate getBaptismalDate();
    LocalDate getConfirmationDate();
    LocalDate getMarriageDate();
    LocalDate getDeathDate();
}

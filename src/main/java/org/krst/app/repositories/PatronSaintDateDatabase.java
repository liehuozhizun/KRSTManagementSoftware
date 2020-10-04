package org.krst.app.repositories;

import org.krst.app.domains.PatronSaintDate;
import org.krst.app.utils.Constants;

public class PatronSaintDateDatabase extends Database<PatronSaintDate> {

    public PatronSaintDateDatabase() {
        databaseName = Constants.PATRON_SAINT_DATE_DATABASE;
    }

}

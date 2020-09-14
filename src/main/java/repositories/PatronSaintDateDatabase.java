package repositories;

import domains.PatronSaintDate;
import org.hibernate.Session;
import utils.Constants;
import utils.HibernateUtils;

import java.util.List;

public class PatronSaintDateDatabase extends Database<PatronSaintDate> {

    public PatronSaintDateDatabase() {
        databaseName = Constants.PATRON_SAINT_DATE_DATABASE;
    }

}

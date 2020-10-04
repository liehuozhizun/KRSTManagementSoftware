package org.krst.app.repositories;

import org.krst.app.domains.Relation;
import org.krst.app.utils.Constants;

public class RelationDatabase extends Database<Relation> {

    public RelationDatabase() {
        databaseName = Constants.RELATION_DATABASE;
    }

}
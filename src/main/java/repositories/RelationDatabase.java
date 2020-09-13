package repositories;

import domains.Relation;
import utils.Constants;

public class RelationDatabase extends Database<Relation> {

    public RelationDatabase() {
        databaseName = Constants.RELATION_DATABASE;
    }

}
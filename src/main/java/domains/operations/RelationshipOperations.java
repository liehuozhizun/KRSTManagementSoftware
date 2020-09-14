package domains.operations;

import javafx.util.Pair;
import utils.database.DatabaseType;

import java.util.Map;

public interface RelationshipOperations {
    Map<Pair<String, String>, Pair<DatabaseType, String>> getRelationships();
    void setRelationships(Map<Pair<String, String>, Pair<DatabaseType, String>> relationships);
}

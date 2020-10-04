package org.krst.app.domains.operations;

import javafx.util.Pair;
import org.krst.app.utils.database.DatabaseType;

import java.util.Map;

public interface RelationshipOperations {
    Map<Pair<String, String>, Pair<DatabaseType, String>> getRelationships();
    void setRelationships(Map<Pair<String, String>, Pair<DatabaseType, String>> relationships);
}

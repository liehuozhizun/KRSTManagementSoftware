package domains.operations;

import javafx.util.Pair;

import java.util.Map;

public interface RelationshipOperations {
    Map<Pair<String, String>, Pair<String, String>> getRelationships();
    void setRelationships(Map<Pair<String, String>, Pair<String, String>> relationships);
}

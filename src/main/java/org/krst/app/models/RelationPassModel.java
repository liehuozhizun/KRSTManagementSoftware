package org.krst.app.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.krst.app.domains.Relation;

@Data
@AllArgsConstructor
public class RelationPassModel {
    private Relation.Type AType;
    private String AId;
    private String AName;
    private Relation.Type BType;
    private String BId;
    private String BName;
    private String relationshipBtoA;
}

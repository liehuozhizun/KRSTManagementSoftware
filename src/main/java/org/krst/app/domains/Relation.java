package org.krst.app.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Relation {
    private String relation;
    private String name;
    private Type type;
    private String id;

    public enum Type {
        STUDENT,
        TEACHER,
        STAFF,
        PERSON
    }
}

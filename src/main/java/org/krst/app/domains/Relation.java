package org.krst.app.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Relation implements Cloneable {
    private String relation;
    private String name;
    private Type type;
    private String id;

    public enum Type {
        STUDENT,
        TEACHER,
        STAFF,
        PERSON;

        public String getTypeString() {
            switch (this) {
                case STUDENT:
                    return "学生";
                case TEACHER:
                    return "教师";
                case STAFF:
                    return "员工";
                case PERSON:
                    return "普通";
                default:
                    return "无该类型";
            }
        }
    }

    @Override
    public Relation clone() {
        try {
            return (Relation) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}

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
    private String relationship;
    private String name;
    @Enumerated(value = EnumType.ORDINAL)
    private Type type;
    private String id;

    public enum Type {
        STUDENT,
        TEACHER,
        STAFF,
        PERSON;

        @Override
        public String toString() {
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
                    return null;
            }
        }

        public static Type getTypeByString(String type) {
            switch (type) {
                case "学生":
                    return STUDENT;
                case "教师":
                    return TEACHER;
                case "员工":
                    return STAFF;
                case "普通":
                    return PERSON;
                default:
                    return null;
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

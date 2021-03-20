package org.krst.app.domains;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@ToString(exclude = "teachers")
@EqualsAndHashCode(exclude = "teachers")
@AllArgsConstructor
@NoArgsConstructor
public class CourseTemplate implements Cloneable {
    @Id
    private String id;
    private String name;
    private String topic;
    @OneToMany
    private Set<Teacher> teachers;

    @Override
    public CourseTemplate clone() {
        try {
            return (CourseTemplate) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public String getIdAndName() {
        return name + " [" + id + "]";
    }
}

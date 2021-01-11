package org.krst.app.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@ToString(exclude = "teachers")
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
}

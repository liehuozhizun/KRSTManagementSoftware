package org.krst.app.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseTemplate {
    @Id
    private String id;
    private String name;
    private String topic;
    @OneToMany
    private Set<Teacher> teachers;
}

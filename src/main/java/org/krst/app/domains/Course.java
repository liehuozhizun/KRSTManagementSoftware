package org.krst.app.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private String className; // 授课班级
    private CourseTemplate courseTemplate; // 授课内容（选择课程编号和课程名称）
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> offer; // 赠予
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Teacher> teachers;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Grade> grades;
}

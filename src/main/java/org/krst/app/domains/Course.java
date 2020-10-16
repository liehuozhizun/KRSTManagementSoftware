package org.krst.app.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    private String id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private String className; // 授课班级
    @OneToOne
    private CourseTemplate courseTemplate; // 课程模板
    @OneToOne
    private Teacher primaryTeacher; // 主课教师
    @OneToOne
    private Teacher secondaryTeacher; // 副课教师
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> offers; // 赠予
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Grade> grades; // 成绩

}

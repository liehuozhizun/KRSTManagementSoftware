package org.krst.app.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@ToString(exclude = {"courseTemplate","primaryTeacher","secondaryTeacher", "grades"})
@AllArgsConstructor
@NoArgsConstructor
public class Course implements Cloneable {
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
    @OneToMany
    private Set<Grade> grades; // 成绩

    @Override
    public Course clone() {
        try {
            return (Course) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}

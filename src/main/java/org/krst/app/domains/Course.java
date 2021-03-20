package org.krst.app.domains;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@ToString(exclude = {"courseTemplate","primaryTeacher","secondaryTeacher", "grades"})
@EqualsAndHashCode(exclude = {"courseTemplate","primaryTeacher","secondaryTeacher","grades"})
@AllArgsConstructor
@NoArgsConstructor
public class Course implements Cloneable {
    @Id
    private String id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private String className; // 授课班级
    @OneToOne(fetch = FetchType.LAZY)
    private CourseTemplate courseTemplate; // 课程模板
    @OneToOne(fetch = FetchType.LAZY)
    private Teacher primaryTeacher; // 主课教师
    @OneToOne(fetch = FetchType.LAZY)
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

    public String getIdAndName() {
        return className + " [" + id + "]";
    }

    public String getCourseTemplateIdAndName() {
        return courseTemplate == null ? null : courseTemplate.getIdAndName();
    }
}

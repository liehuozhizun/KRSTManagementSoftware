package org.krst.app.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String startDate;
    private String endDate;
    private String location;
    private String className; // 授课班级
    private CourseTemplate courseTemplate; // 授课内容（选择课程编号和课程名称）
    private String offer; // 赠予
    @ElementCollection
    private List<Teacher> teachers;
    @ElementCollection
    private List<Grade> grades;
}

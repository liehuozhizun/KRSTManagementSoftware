package org.krst.app.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@ToString(exclude = {"student","course"})
@AllArgsConstructor
@NoArgsConstructor
public class Grade implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private Student student;
    @OneToOne
    private Course course;
    private Integer score; // 成绩
    private String courseFeedback; // 课程评价
    private String teacherFeedback; // 教师评价


    @Override
    public Grade clone() {
        try {
            return (Grade) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}

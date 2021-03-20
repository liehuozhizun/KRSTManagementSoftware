package org.krst.app.domains;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@ToString(exclude = {"student","course"})
@EqualsAndHashCode(exclude = {"student","course"})
@AllArgsConstructor
@NoArgsConstructor
public class Grade implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Student student;
    @OneToOne(fetch = FetchType.LAZY)
    private Course course;
    private String score; // 成绩
    private String courseFeedback; // 课程评价
    private String teacherFeedback; // 教师评价
    private String offer; // 赠予

    @Override
    public Grade clone() {
        try {
            return (Grade) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}

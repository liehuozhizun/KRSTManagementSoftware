package org.krst.app.domains;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@ToString(exclude = {"staff","visits","internships","relationships","grades"})
@EqualsAndHashCode(exclude = {"grades","attribute","staff","visits","internships","relationships"})
@AllArgsConstructor
@NoArgsConstructor
public class Student implements InformationOperations {
    @Id
    private String id;
    private String name;
    private String baptismalName;
    private String gender;
    private LocalDate birthday;
    private Boolean isGregorianCalendar;
    private LocalDate baptismalDate;
    private LocalDate confirmationDate;
    private LocalDate marriageDate;
    private LocalDate deathDate;
    @OneToOne(fetch = FetchType.LAZY)
    private Attribute attribute;
    private String phone;
    private String altPhone;
    private String Address;
    private String experience;
    private String talent;
    private String resource;
    private String education;
    @OneToOne(fetch = FetchType.LAZY)
    private Staff staff;
    @ElementCollection
    private Set<Visit> visits; // 探访记录
    @ElementCollection
    private Set<Internship> internships; // 实践记录
    @ElementCollection
    private Set<Relation> relationships; // 亲属关系 <关系, 姓名, 人员类型, 人员id>
    @OneToMany
    private Set<Grade> grades;
}

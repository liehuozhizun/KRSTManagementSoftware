package org.krst.app.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.krst.app.domains.operations.InformationOperations;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@ToString(exclude = {"visits","internships","evaluations","relationships"})
@AllArgsConstructor
@NoArgsConstructor
public class Staff implements InformationOperations {
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
    private String title;
    private String responsibility;
    private String phone;
    private String altPhone;
    private String address;
    private String education;
    private String experience;
    private String talent;
    private String resource;
    @ElementCollection
    private Set<Visit> visits; // 探访记录
    @ElementCollection
    private Set<Internship> internships; // 服侍记录
    @ElementCollection
    private Set<Evaluation> evaluations; // 员工评定
    @ElementCollection
    private Set<Relation> relationships; // 亲属关系 <关系, 姓名, 人员类型, 人员id>
}

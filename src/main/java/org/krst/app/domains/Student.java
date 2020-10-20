package org.krst.app.domains;

import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.krst.app.domains.operations.InformationOperations;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Entity
@Data
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
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Attribute attribute;
    private String phone;
    private String altPhone;
    private String Address;
    private String experience;
    private String talent;
    private String resource;
    @OneToOne
    private Staff staff;
    @ElementCollection
    private Set<Visit> visits; // 探访记录
    @ElementCollection
    private Set<Internship> internships; // 实践记录
    @ElementCollection
    private Map<String, Pair<PersonType, String>> relationships; // 亲属关系 <<关系, 姓名>, <所属表, Id>>
    @OneToMany
    private Set<Grade> grades;
}

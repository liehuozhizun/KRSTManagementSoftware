package org.krst.app.domains;

import org.krst.app.domains.operations.InformationOperations;
import org.krst.app.domains.operations.InternshipOperations;
import org.krst.app.domains.operations.RelationshipOperations;
import org.krst.app.domains.operations.VisitOperations;
import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.krst.app.utils.database.DatabaseType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student implements VisitOperations, RelationshipOperations, InformationOperations, InternshipOperations {
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
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Attribute attribute;
    private String phone;
    private String altPhone;
    private String Address;
    private String experience;
    private String talent;
    private String resource;
    private String staffId;
    private String staffName;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Visit> visits; // 探访记录
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Internship> internships; // 实习记录
    @ElementCollection(fetch = FetchType.EAGER)
    private Map<Pair<String, String>, Pair<DatabaseType, String>> relationships; // 亲属关系 <<关系, 姓名>, <所属表, Id>>
}

package org.krst.app.domains;

import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.krst.app.domains.operations.InformationOperations;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Relation implements InformationOperations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String baptismalName;
    private String gender;
    private LocalDate birthday;
    private Boolean isGregorianCalendar;
    private LocalDate baptismalDate;
    private LocalDate confirmationDate;
    private LocalDate marriageDate;
    private LocalDate deathDate;
    private String phone;
    private String address;
    private String job;
    private String talent;
    private String resources;
    @ElementCollection(fetch = FetchType.EAGER)
    private Map<Pair<String, String>, Pair<PersonType, String>> relationships; // 亲属关系 <<关系, 姓名>, <所属表, Id>>
}

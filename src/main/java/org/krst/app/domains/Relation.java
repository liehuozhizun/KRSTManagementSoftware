package org.krst.app.domains;

import org.krst.app.domains.operations.InformationOperations;
import org.krst.app.domains.operations.RelationshipOperations;
import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.krst.app.utils.database.DatabaseType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Map;

@Entity
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Relation implements RelationshipOperations, InformationOperations {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String baptismalName;
    private String gender;
    private LocalDate birthday;
    @Type(type = "yes_no")
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
    @ElementCollection
    private Map<Pair<String, String>, Pair<DatabaseType, String>> relationships; // 亲属关系 <<关系, 姓名>, <所属表, Id>>
}

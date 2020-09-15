package domains;

import domains.operations.InformationOperations;
import domains.operations.InternshipOperations;
import domains.operations.RelationshipOperations;
import domains.operations.VisitOperations;
import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import utils.database.DatabaseType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    @Type(type = "yes_no")
    private Boolean isGregorianCalendar;
    private LocalDate baptismalDate;
    private LocalDate confirmationDate;
    private LocalDate marriageDate;
    private LocalDate deathDate;
    private String attribute;
    private String leader;
    private String leaderPhone;
    private String phone;
    private String altPhone;
    private String Address;
    private String experience;
    private String talent;
    private String resource;
    private String staffId;
    private String staffName;
    @ElementCollection
    private List<Visit> visits; // 探访记录
    @ElementCollection
    private List<Internship> internships; // 实习记录
    @ElementCollection
    private Map<Pair<String, String>, Pair<DatabaseType, String>> relationships; // 亲属关系 <<关系, 姓名>, <所属表, Id>>
}

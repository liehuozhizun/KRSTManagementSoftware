package domains;

import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;
import java.util.Map;

@Entity
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Staff {
    @Id
    private String id;
    private String name;
    private String baptismalName;
    private String gender;
    private String birthday;
    private String baptismalDate;
    private String confirmationDate;
    private String marriageDate;
    private String deathDate;
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
    private List<Evaluation> evaluations;
    @ElementCollection
    private Map<Pair<String, String>, Pair<String, String>> relationships; // 亲属关系 <<关系, 姓名>, <所属表, Id>>
}

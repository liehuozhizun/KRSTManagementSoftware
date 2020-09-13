package domains;

import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Map;

@Entity
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Relation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String baptismalName;
    private String gender;
    private String birthday;
    private String baptismalDate;
    private String confirmationDate;
    private String marriageDate;
    private String deathDate;
    private String phone;
    private String address;
    private String job;
    private String talent;
    private String resources;
    @ElementCollection
    private Map<Pair<String, String>, Pair<String, String>> relationships; // 亲属关系 <<关系, 姓名>, <所属表, Id>>
}

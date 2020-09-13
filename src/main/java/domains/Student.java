package domains;

import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
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
    private Map<Pair<String, String>, Pair<String, String>> relationships; // 亲属关系 <<关系, 姓名>, <所属表, Id>>
}

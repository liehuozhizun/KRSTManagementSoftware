package org.krst.app.domains;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@ToString(exclude = "relationships")
@EqualsAndHashCode(exclude = "relationships")
@AllArgsConstructor
@NoArgsConstructor
public class Person implements InformationOperations {
    @Id
    private String id;
    private String name;
    private String baptismalName;
    private String gender;
    private LocalDate birthday;
    private Boolean isGregorianCalendar;
    private String phone;
    private String altPhone;
    private String address;
    private String job;
    @ElementCollection
    private Set<Relation> relationships; // 亲属关系 <关系, 姓名, 人员类型, 人员id>

    @Override
    public LocalDate getBaptismalDate() {
        return null;
    }

    @Override
    public LocalDate getConfirmationDate() {
        return null;
    }

    @Override
    public LocalDate getMarriageDate() {
        return null;
    }

    @Override
    public LocalDate getDeathDate() {
        return null;
    }
}

package domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Internship {
    @Id
    private long id;
    private String startDate;
    private String endDate;
    private String purpose;
    private String summary;
    private String comment;
}

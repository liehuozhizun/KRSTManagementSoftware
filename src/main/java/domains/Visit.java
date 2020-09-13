package domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Visit {
    @Id
    private long id;
    private String Date;
    @ElementCollection
    private List<Staff> visitors;
    private String content;
    private String summary;
    private String comment;
}

package org.krst.app.domains;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Embeddable
@Data
@EqualsAndHashCode(exclude = "visitors")
@ToString(exclude = "visitors")
@AllArgsConstructor
@NoArgsConstructor
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate date;
    @OneToMany
    private List<Staff> visitors;
    private String content;
    private String summary;
    private String comment;
}

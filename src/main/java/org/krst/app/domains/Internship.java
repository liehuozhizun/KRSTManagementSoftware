package org.krst.app.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Internship {
    private LocalDate startDate;
    private LocalDate endDate;
    private String purpose;
    private String summary;
    private String comment;
}

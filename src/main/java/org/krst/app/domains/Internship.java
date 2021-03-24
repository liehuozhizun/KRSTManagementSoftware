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
public class Internship implements Cloneable {
    private LocalDate startDate;
    private LocalDate endDate;
    private String purpose;
    private String summary;
    private String comment;

    @Override
    public Internship clone() {
        try {
            return (Internship) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}

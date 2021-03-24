package org.krst.app.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Evaluation implements Cloneable {
    private String year;
    private String title;
    private String responsibility;
    private String comment;

    @Override
    public Evaluation clone() {
        try {
            return (Evaluation) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return this;
    }
}

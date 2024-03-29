package org.krst.app.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.krst.app.utils.RemainderEventType;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Remainder implements Comparable {
    private LocalDate date;
    private RemainderEventType event;
    private String message;

    @Override
    public int compareTo(Object o) {
        return this.date.compareTo(((Remainder)o).getDate());
    }
}

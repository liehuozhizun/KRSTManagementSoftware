package org.krst.app.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.krst.app.utils.RemainderEventType;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Remainder {
    private LocalDate date;
    private RemainderEventType event;
    private String message;
}

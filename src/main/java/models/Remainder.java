package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import utils.RemainderEventType;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Remainder {
    private LocalDate date;
    private RemainderEventType event;
    private String message;
}

package org.krst.app.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportExportFailure {
    private Integer rowNumber;
    private String id;
    private String name;
    private String error;
}

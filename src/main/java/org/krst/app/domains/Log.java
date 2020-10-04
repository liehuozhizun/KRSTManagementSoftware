package org.krst.app.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.krst.app.utils.CommonUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Type type;
    private String time;
    private String path;
    private String message;

    public Log (Type type, String path, String message) {
        this(null, type, CommonUtils.getCurrentTime(), path, message);
    }

    public enum Type {
        INFO,
        ERROR,
        FETAL,
        WARN
    }
}
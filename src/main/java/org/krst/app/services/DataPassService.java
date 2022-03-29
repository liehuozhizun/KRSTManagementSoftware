package org.krst.app.services;

import org.springframework.stereotype.Service;

@Service
public class DataPassService {
    private Object value;
    private Boolean ready = false;

    public Object getValue() {
        if (!ready) return null;
        ready = false;
        return value;
    }

    public void setValue(Object object) {
        value = object;
        ready = true;
    }

    public boolean isReady() {
        return ready;
    }
}

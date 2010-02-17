package com.blackbox.system;

import com.blackbox.foundation.BBPersistentObject;
import org.joda.time.DateTime;

public class DbMetaData extends BBPersistentObject {

    private String key;

    public DbMetaData() {
    }

    public DbMetaData(String key) {
        this.key = key;
        created = new DateTime();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}

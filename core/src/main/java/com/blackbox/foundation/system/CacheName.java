package com.blackbox.foundation.system;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CacheName {

    private String name;

    public CacheName() {
    }

    public CacheName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

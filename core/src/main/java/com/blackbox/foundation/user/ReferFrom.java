/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blackbox.foundation.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ReferFrom implements Serializable {
    public static enum ReferFromType {
        Web,
        Print,
        Friend,
        Other,
    }

    private String description;
    private ReferFromType referFromType;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ReferFromType getReferFromType() {
        return referFromType;
    }

    public void setReferFromType(ReferFromType referFromType) {
        this.referFromType = referFromType;
    }
}

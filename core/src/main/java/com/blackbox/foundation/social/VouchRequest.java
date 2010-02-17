package com.blackbox.foundation.social;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class VouchRequest implements Serializable {

    private Vouch vouch;
    private boolean permanent;

    public VouchRequest() {
    }

    public VouchRequest(Vouch vouch, boolean permanent) {
        this.vouch = vouch;
        this.permanent = permanent;
    }

    public Vouch getVouch() {
        return vouch;
    }

    public void setVouch(Vouch vouch) {
        this.vouch = vouch;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }
}

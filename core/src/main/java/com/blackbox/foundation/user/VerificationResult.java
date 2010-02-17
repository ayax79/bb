package com.blackbox.foundation.user;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class VerificationResult implements Serializable {

    private boolean verified;

    public VerificationResult() {
    }

    public VerificationResult(boolean verified) {
        this.verified = verified;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}

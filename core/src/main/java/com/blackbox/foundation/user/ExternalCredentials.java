package com.blackbox.foundation.user;

import com.blackbox.foundation.BBPersistentObject;
import com.blackbox.foundation.EntityReference;

import static org.yestech.lib.crypto.AesUtils.decrypt;
import static org.yestech.lib.crypto.AesUtils.encrypt;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class ExternalCredentials extends BBPersistentObject implements Serializable {

    private static final long serialVersionUID = -6750260733875151262L;

    private static final String TWITTER_USERNAME_KEY = "SN36nxYma51Vj8KJcFltjA==";
    private static final String TWITTER_PASSWORD_KEY = "/y4wI7/i/TQakj/dYMVdfg==";
    private static final String FACEBOOK_USERNAME_KEY = "hGBw9j4zcreosMqz1liPOg==";
    private static final String FACEBOOK_PASSWORD_KEY = "S38bANfnv5F6VL8gZyLJng==";
    private static final String FACEBOOK_EXTERNAL_KEY_KEY = "bOGKhiN8F90hcC9oM74d7A==";


    public static enum CredentialType {
        TWITTER,
        FACEBOOK
    }

    private String username;
    private String password;
    private CredentialType type;
    private EntityReference owner;
    private String externalKey;

    // cheating a bit here to get this working then we'll look at generalizing it...
    private FacebookCredentials facebookCredentials;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CredentialType getType() {
        return type;
    }

    public void setType(CredentialType type) {
        this.type = type;
    }

    public EntityReference getOwner() {
        return owner;
    }

    public void setOwner(EntityReference owner) {
        this.owner = owner;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getExternalKey() {
        return externalKey;
    }

    public void setExternalKey(String externalKey) {
        this.externalKey = externalKey;
    }

    public static ExternalCredentials buildExternalCredentials(CredentialType type, EntityReference owner, String username, String password) {
        ExternalCredentials credentials = new ExternalCredentials();
        credentials.setOwner(owner);
        credentials.setType(type);
        String key = passwordBuildKey(credentials);
        credentials.setPassword(encrypt(key, password));
        key = usernameBuildKey(credentials);
        credentials.setUsername(encrypt(key, username));
        return credentials;
    }

    public static ExternalCredentials buildExternalCredentials(CredentialType type, EntityReference owner, String externalKey) {
        ExternalCredentials credentials = new ExternalCredentials();
        credentials.setType(type);
        String key = externalKeyBuildKey(credentials);
        credentials.setExternalKey(encrypt(key, externalKey));
        credentials.setOwner(owner);
        return credentials;
    }

    public static ExternalCredentials buildFacebookExternalCredentials(EntityReference owner, FacebookCredentials facebookCredentials) {
        ExternalCredentials credentials = new ExternalCredentials();
        credentials.setType(CredentialType.FACEBOOK);
        credentials.facebookCredentials = facebookCredentials;
        credentials.setOwner(owner);
        return credentials;
    }

    private static String passwordBuildKey(ExternalCredentials cred) {
        if (cred.getType() == CredentialType.TWITTER) {
            return TWITTER_PASSWORD_KEY;
        } else if (cred.getType() == CredentialType.FACEBOOK) {
            return FACEBOOK_PASSWORD_KEY;
        }
        throw new IllegalArgumentException("no credential type specified");
    }

    private static String usernameBuildKey(ExternalCredentials cred) {
        if (cred.getType() == CredentialType.TWITTER) {
            return TWITTER_USERNAME_KEY;
        } else if (cred.getType() == CredentialType.FACEBOOK) {
            return FACEBOOK_USERNAME_KEY;
        }
        throw new IllegalArgumentException("no credential type specified");
    }

    private static String externalKeyBuildKey(ExternalCredentials cred) {
        if (cred.getType() == CredentialType.FACEBOOK) {
            return FACEBOOK_EXTERNAL_KEY_KEY;
        }
        throw new IllegalArgumentException("no credential type specified");
    }

    public String decryptPassword() {
        String key = passwordBuildKey(this);
        return decrypt(key, password);
    }

    public String decryptUsername() {
        String key = usernameBuildKey(this);
        return decrypt(key, username);
    }

    public String decryptExternalKey() {
        String key = externalKeyBuildKey(this);
        return decrypt(key, externalKey);
    }

    public FacebookCredentials getFacebookCredentials() {
        return facebookCredentials;
    }
}

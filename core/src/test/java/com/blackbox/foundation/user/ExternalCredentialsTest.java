package com.blackbox.foundation.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import org.junit.Test;

public class ExternalCredentialsTest {

    @Test
    public void testInitializeAndDecrypt() {
        String password = "fuckyoutoo";
        String username = "bobbymcgofuckyourself";
        ExternalCredentials ec = ExternalCredentials.buildExternalCredentials(
                ExternalCredentials.CredentialType.TWITTER, User.createUser().toEntityReference(), username, password);

        assertNotSame(username, ec.getUsername());
        assertNotSame(password, ec.getPassword());
        assertEquals(username, ec.decryptUsername());
        assertEquals(password, ec.decryptPassword());


        password = "asfv32vat3233avzss";
        username = "2f8as3s5awstrsa";
        ec = ExternalCredentials.buildExternalCredentials(
                ExternalCredentials.CredentialType.TWITTER, User.createUser().toEntityReference(), username, password);

        assertNotSame(username, ec.getUsername());
        assertNotSame(password, ec.getPassword());
        assertEquals(username, ec.decryptUsername());
        assertEquals(password, ec.decryptPassword());


        password = "2q389taawtawtat3tatwa3wstawx";
        username = "2q38azs;5tlia5usli5";
        ec = ExternalCredentials.buildExternalCredentials(
                ExternalCredentials.CredentialType.TWITTER, User.createUser().toEntityReference(), username, password);

        assertNotSame(username, ec.getUsername());
        assertNotSame(password, ec.getPassword());
        assertEquals(username, ec.decryptUsername());
        assertEquals(password, ec.decryptPassword());

        password = "asfv32vat3233avzss";
        username = "2f8as3s5awstrsa";
        ec = ExternalCredentials.buildExternalCredentials(
                ExternalCredentials.CredentialType.FACEBOOK, User.createUser().toEntityReference(), username, password);

        assertNotSame(username, ec.getUsername());
        assertNotSame(password, ec.getPassword());
        assertEquals(username, ec.decryptUsername());
        assertEquals(password, ec.decryptPassword());

    }

}

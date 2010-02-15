package com.blackbox.server.system.email;

public interface SimpleEmailDelivery {


    /**
     * Send an email with the specified email definition.
     *
     * @param definition The email definition.
     */
    public void send(EmailDefinition definition);

}

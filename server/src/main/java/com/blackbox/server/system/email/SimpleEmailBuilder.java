package com.blackbox.server.system.email;

import java.util.HashMap;
import java.util.Map;

public class SimpleEmailBuilder {

    public class NameEmailPair {
        String name;
        String email;

        public NameEmailPair(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }

    private NameEmailPair sender;
    private NameEmailPair recipient;
    private String velocityTextTemplate;
    private String velocityHtmlTemplate;
    private String subject;
    private Map<String, Object> mapParams = new HashMap<String, Object>();

    public SimpleEmailBuilder setSender(String name, String email) {
        sender = new NameEmailPair(name, email);
        return this;
    }

    public SimpleEmailBuilder setRecipient(String name, String email) {
        recipient = new NameEmailPair(name, email);
        return this;
    }

    public SimpleEmailBuilder setSubject(String name, String... params) {
        if (params != null && params.length > 0) {
            subject = String.format(name, params);
        }
        else {
            subject = name;
        }
        return this;
    }

    public SimpleEmailBuilder setHtmlTemplate(String templatePath) {
        velocityHtmlTemplate = templatePath;
        return this;
    }

    public SimpleEmailBuilder setTextTemplate(String templatePath) {
        velocityTextTemplate = templatePath;
        return this;
    }

    public SimpleEmailBuilder addTemplateVariable(String key, Object value) {
        mapParams.put(key, value);
        return this;
    }

    public NameEmailPair getSender() {
        return sender;
    }

    public NameEmailPair getRecipient() {
        return recipient;
    }

    public String getVelocityTextTemplate() {
        return velocityTextTemplate;
    }

    public String getVelocityHtmlTemplate() {
        return velocityHtmlTemplate;
    }

    public String getSubject() {
        return subject;
    }

    public Map<String, Object> getMapParams() {
        return mapParams;
    }
}

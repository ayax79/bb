package com.blackbox.server.system.email;

/**
 * Defines a simple DSL to use in constructing an email.
 *
 */
public class EmailDefinition {

    private SimpleEmailBuilder builder = new SimpleEmailBuilder();

    public void withSender(String name, String email) {
        builder.setSender(name, email);
    }

    public void withRecipient(String name, String email) {
        builder.setRecipient(name, email);
    }

    public void withSubject(String subject, String... params) {
        builder.setSubject(subject, params);

    }

    public void withHtmlTemplate(String template) {
        builder.setHtmlTemplate(template);
    }

    public void withTextTemplate(String template) {
        builder.setTextTemplate(template);
    }

    public void withTemplateVariable(String key, Object value) {
        builder.addTemplateVariable(key, value);
    }

    public SimpleEmailBuilder getBuilder() {
        return builder;
    }
}

package com.blackbox.server.system.email;

import com.blackbox.foundation.Utils;
import com.blackbox.server.util.VelocityUtil;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.yestech.notify.deliver.EmailDelivery;
import org.yestech.notify.objectmodel.IMessage;
import org.yestech.notify.objectmodel.INotification;
import org.yestech.notify.objectmodel.IRecipient;
import org.yestech.notify.objectmodel.ISender;
import org.yestech.notify.template.ITemplateLanguage;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * @author A.J. Wright
 */
public class RemoteEmailDelivery extends EmailDelivery implements SimpleEmailDelivery {

    private static final Logger logger = LoggerFactory.getLogger(RemoteEmailDelivery.class);

    private ISender sender;
    private String presentationUrl;
    private ExecutorService service;

    @Required
    public void setDefaultSender(ISender sender) {
        this.sender = sender;
    }

    @Required
    public void setPresentationUrl(String presentationUrl) {
        this.presentationUrl = presentationUrl;
    }

    public void init() {
        service = Executors.newCachedThreadPool();
    }

    public void destroy() {
        service.shutdownNow();
    }

    @Override
    protected void sendMessage(INotification notification, IRecipient recipient) throws EmailException {
        ISender sender = notification.getSender();
        IMessage message = notification.getMessage();

        try {

            PostMethod post = new PostMethod(getEmailHost());
            post.addParameter("from_email", isNotBlank(sender.getReplyAddress()) ? sender.getReplyAddress() : sender.getEmailAddress());
            post.addParameter("from_name", sender.getDisplayName());
            post.addParameter("subject", message.getSubject());
            ITemplateLanguage template = notification.getTemplate();
            String appliedMessage = template.apply(notification.getMessage());
            post.addParameter("textbody", appliedMessage);
            post.addParameter("to_email", recipient.getEmailAddress());
            post.addParameter("to_name", recipient.getDisplayName());

            send(post);
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new EmailException(e);
        }
    }

    /*
       to_name
       to_email
       from_name
       from_email
       subject
       htmlbody
       textbody
     */

    @Override
    public void send(final EmailDefinition definition) {
        service.submit(new Runnable() {
            @Override
            public void run() {
                SimpleEmailBuilder builder = definition.getBuilder();

                try {

                    Map<String, Object> params = builder.getMapParams();
                    setDefaults(params);
                    String htmlTemplate = VelocityUtil.transform(builder.getVelocityHtmlTemplate(), params);
                    String textTemplate = VelocityUtil.transform(builder.getVelocityTextTemplate(), params);

                    PostMethod post = new PostMethod(getEmailHost());
                    if (builder.getSender() != null) {
                        post.addParameter("from_email", builder.getSender().getEmail());
                        post.addParameter("from_name", builder.getSender().getName());
                    } else {
                        post.addParameter("from_email", sender.getEmailAddress());
                        post.addParameter("from_name", sender.getDisplayName());
                    }

                    post.addParameter("subject", builder.getSubject());

                    post.addParameter("textbody", textTemplate);
                    post.addParameter("htmlbody", htmlTemplate);
                    post.addParameter("to_email", builder.getRecipient().getEmail());
                    if (builder.getRecipient().getName() != null) {
                        post.addParameter("to_name", builder.getRecipient().getName());
                    } else {
                        post.addParameter("to_name", builder.getRecipient().getEmail());
                    }
                    send(post);
                }
                catch (Exception e) {
                    Utils.logWrapAndThrow("Unable to send mail", logger, e);
                }

            }
        });

    }

    protected void send(PostMethod post) throws IOException {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials(getAuthenticatorUserName(), getAuthenticatorPassword());
        client.getState().setCredentials(new AuthScope("mailer.blackboxrepublic.com", 80, "mailer.blackboxrepublic.com"), defaultcreds);
        client.executeMethod(post);
        String result = post.getResponseBodyAsString().trim();
        if (result == null || !"1".equals(result)) {
            logger.warn("Remote mailer did not send success result back. : " + result);
        }
    }

    protected void setDefaults(Map<String, Object> params) {
        params.put("presentation_url", presentationUrl);
    }

}

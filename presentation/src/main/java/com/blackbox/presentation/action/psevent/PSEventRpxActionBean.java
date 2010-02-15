package com.blackbox.presentation.action.psevent;

import java.util.Collection;


import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.integration.spring.SpringBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.rpx.RPXClient;
import org.yestech.rpx.objectmodel.AuthInfoResponse;
import org.yestech.rpx.objectmodel.Contact;
import org.yestech.rpx.objectmodel.GetContactsResponse;

public class PSEventRpxActionBean extends PSBaseEventActionBean {

    final private static Logger logger = LoggerFactory.getLogger(PSEventRpxActionBean.class);
    @SpringBean("rpxClient")
    private RPXClient rpxClient;

    private String token;
    private Collection<Contact> contacts;

    @DefaultHandler
    public Resolution begin() {
        logger.info("calling begin");
        return new ForwardResolution("/WEB-INF/jsp/include/ps_event/eventrpxtest.jsp");
    }

    public Resolution test() {
        String test = "wait";
        if (getContext().getRequest().getSession().getAttribute("tempImportReceiver") != null) {
            test = "finish";
        }
        return new StreamingResolution("text", test);
    }

    public Resolution handleRpx() {
        logger.info("calling handleRpx");
        logger.info("token is:" + token);
        try {
            AuthInfoResponse resp = rpxClient.authInfo(token, true);
            String providerId = resp.getProfile().getIdentifier();
            logger.info("providerId is:" + providerId);

            GetContactsResponse response = rpxClient.getContacts(providerId);
            contacts = response.getEntry();
            logger.info("contacts:" + contacts.toString());
            getContext().getRequest().getSession().setAttribute("tempImportReceiver", contacts);
        }
        catch (Exception ex) {
            logger.error("get contacts exception:", ex);
        }
        return new ForwardResolution("/WEB-INF/jsp/include/ps_event/eventrpxres.jsp");

    }

    public void setToken(String token) {
        this.token = token;
    }

    public Collection<Contact> getContacts() {
        return contacts;
    }
    @Override
    public boolean isHasIntro() {
        return false;
    }

}

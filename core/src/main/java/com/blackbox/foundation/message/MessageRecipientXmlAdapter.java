package com.blackbox.foundation.message;

import com.blackbox.foundation.activity.IRecipient;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author A.J. Wright
 */
public class MessageRecipientXmlAdapter extends XmlAdapter<MessageRecipient, IRecipient> {

    @Override
    public IRecipient unmarshal(MessageRecipient v) throws Exception {
        return v;
    }

    @Override
    public MessageRecipient marshal(IRecipient v) throws Exception {
        return (MessageRecipient) v;
    }
}

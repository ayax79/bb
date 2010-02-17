package com.blackbox.foundation.media;

import com.blackbox.foundation.activity.IRecipient;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author A.J. Wright
 */
public class MediaRecipientXmlAdapter extends XmlAdapter<MediaRecipient, IRecipient> {
    @Override
    public IRecipient unmarshal(MediaRecipient v) throws Exception {
        return v;
    }

    @Override
    public MediaRecipient marshal(IRecipient v) throws Exception {
        return (MediaRecipient) v;
    }
}

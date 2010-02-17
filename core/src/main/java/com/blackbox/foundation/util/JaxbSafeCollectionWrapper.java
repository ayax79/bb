package com.blackbox.foundation.util;

import com.blackbox.foundation.activity.Activity;
import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.foundation.message.Message;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.Serializable;
import java.util.Collection;

/**
 * I wrote this class simply because jaxb sucks. And so does rest easy half the time.
 * <p/>
 * The problem is when returning a collection from resteasy that has an interface in it, jaxb fucks up.
 * This is an attempt to fix its shittiness in a hacky and haphazard way.
 * <p/>
 * Make sure you add the classes you need to wrap to the XmlSee also
 */
@XmlRootElement
@XmlSeeAlso({MediaMetaData.class, Message.class, Activity.class})
public class JaxbSafeCollectionWrapper<C extends Collection> implements Serializable {

    private C collection;
    private static final long serialVersionUID = -2366590781268679934L;

    public JaxbSafeCollectionWrapper() {
    }

    public JaxbSafeCollectionWrapper(C collection) {
        this.collection = collection;
    }

    public C getCollection() {
        return collection;
    }

    public void setCollection(C collection) {
        this.collection = collection;
    }
}

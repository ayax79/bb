package com.blackbox.message;

import com.blackbox.activity.ActivityProfile;
import com.blackbox.activity.ActivityReference;
import com.blackbox.social.NetworkTypeEnum;
import org.joda.time.DateTime;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

public class MessageUnitTest {


    @Test
    @Ignore
    public void cloneMessageTest() {

        Message m = Message.createMessage();
//        m.setAccessControlList(AccessControlList.createAccessControlList());
        m.setArtifactIdentifier("lskdfjlskdfjfeq2");
        m.setArtifactMetaData(MessageMetaData.createMessageMetaData());
        m.setBody("7h9uyrkhgtfjhg");
        m.setCreated(new DateTime(789876987L));
        m.setGuid("uyo7vtovybhi7tkj");
        m.setModified(new DateTime(7968769876345L));
        m.setParentActivity(ActivityReference.createActivityReference(Message.createMessage()));
        m.setPostDate(new DateTime(8976875867543L));
        m.setRecipientDepth(NetworkTypeEnum.FRIENDS);
        m.setSenderProfile(new ActivityProfile("fsdlkjsdf", "fdav3refd"));


        Message m2 = Message.cloneMessage(m);
        assertNotNull(m2);
        assertSame(m.getGuid(), m2.getGuid());
//        assertNotSame(m.getAccessControlList(), m2.getAccessControlList());
        assertNull(m2.getCreated());
        assertNull(m2.getModified());
        assertNull(m2.getVersion());
        assertEquals(m.getBody(), m2.getBody());
        assertEquals(m.getActivityType(), m2.getActivityType());
        assertEquals(m.getFormattedPostDate(), m2.getFormattedPostDate());
        assertNotSame(m.getArtifactMetaData(), m2.getArtifactMetaData());
        assertEquals(m.getPostDate(), m2.getPostDate());

    }


}

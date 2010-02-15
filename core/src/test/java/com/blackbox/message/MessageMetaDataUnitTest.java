package com.blackbox.message;

import com.blackbox.activity.ActivityProfile;
import com.blackbox.user.User;
import org.joda.time.DateTime;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import org.yestech.publish.objectmodel.ArtifactType;


public class MessageMetaDataUnitTest {

    @Test
    @Ignore
    public void testCloneMessageMetaDate() {
        MessageMetaData mmd = new MessageMetaData();
        mmd.setArtifactMetaDataIdentifier("sdlfkjsldkfjacs");
        mmd.setArtifactOwner(User.createUser().toEntityReference());
        mmd.setArtifactType(ArtifactType.VIDEO);
        mmd.setMessageType(new MessageType("sdfkljfdls", "dfscew"));
        mmd.getRecipients().add(new MessageRecipient(User.createUser().toEntityReference(), Message.createMessage().getGuid()));
        mmd.setSenderProfile(new ActivityProfile("vew2q4wre", "vqp389rdzx"));
        mmd.setVersion(3223L);
        mmd.setCreated(new DateTime(32929032390L));
        mmd.setModified(new DateTime(14371654L));

        MessageMetaData mmd2 = MessageMetaData.cloneMessageMetaData(mmd);
        assertNotSame(mmd, mmd2);
        assertSame(mmd.getGuid(), mmd2.getGuid());
        assertSame(mmd.getArtifactMetaDataIdentifier(), mmd2.getArtifactMetaDataIdentifier());
        assertNotNull(mmd.getGuid());
        assertNull(mmd2.getVersion());
        assertNull(mmd2.getCreated());
        assertNull(mmd2.getModified());
        assertEquals(mmd.getArtifactOwner(), mmd2.getArtifactOwner());
        assertEquals(mmd.getArtifactType(), mmd2.getArtifactType());
        assertEquals(mmd.getMessageType(), mmd2.getMessageType());
        assertEquals(mmd.getRecipients(), mmd2.getRecipients());
        assertEquals(mmd.getSenderProfile(), mmd2.getSenderProfile());
    }

}

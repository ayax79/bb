package com.blackbox.server.gifting;

import com.blackbox.server.BaseIntegrationTest;
import com.blackbox.server.media.IMediaDao;
import com.blackbox.server.user.IUserDao;
import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.foundation.media.MediaRecipient;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.social.NetworkTypeEnum;
import com.blackbox.gifting.GiftLayout;
import com.blackbox.foundation.activity.IActivity;
import org.junit.Test;
import org.yestech.publish.objectmodel.ArtifactType;

import javax.annotation.Resource;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author A.J. Wright
 */
public class GiftingDaoIntegrationTest extends BaseIntegrationTest {

    @Resource
    IGiftingDao giftingDao;

    @Resource
    IUserDao userDao;

    @Resource
    IMediaDao mediaDao;

    @Test
    public void testCrud() {
        User aj = userDao.loadUserByUsername("aj");

        MediaMetaData mmd = MediaMetaData.createMediaMetaData();
        mmd.setArtifactOwner(aj.toEntityReference());
        mmd.setComment("slkdjfjlkfds");
        mmd.setLocation("d;alkdsjf;aldfjk");
        mmd.setRecipientDepth(NetworkTypeEnum.FRIENDS);
        mmd.getRecipients().add(new MediaRecipient(aj.toEntityReference(), mmd));
        mmd.setArtifactType(ArtifactType.IMAGE);
        mmd.setFileName("sldkfjsldfjk");
        mmd.setMimeType("sdlfkjsdf");
        mmd.setVirtualGift(true);
        mmd.setAcknowledged(true);
        mediaDao.save(mmd);

        GiftLayout gl = new GiftLayout();
        gl.setActivityReference(mmd.toEntityReference());
        gl.setBody("asdkfljasd;fljkdfs;");
        gl.setFrame("sdlfkjsdflk");
        gl.setIconLocation("sdkjflalsdf");
        gl.setLocation("sdfdf");
        gl.setShelf(GiftLayout.Shelf.upper);
        giftingDao.save(gl);
        assertNotNull(gl.getGuid());

        gl.setLocation("sdflakjdsf;ljk");
        giftingDao.save(gl);

        List<GiftLayout> list = giftingDao.loadGiftLayout(aj.getGuid());
        assertNotNull(list);
        assertFalse(list.isEmpty());
        gl = list.get(0);
        assertNotNull(gl);

        GiftLayout result = giftingDao.loadGiftLayoutByGuid(gl.getGuid());
        assertNotNull(result);
        assertNotNull(result.getLocation());

        List<IActivity> list2 = giftingDao.loadVirtualGiftsForRecipient(aj.getGuid(), true);
        assertNotNull(list2);
        assertFalse(list2.isEmpty());

        list2 = giftingDao.loadVirtualGiftsForSender(aj.getGuid());
        assertNotNull(list2);
        assertFalse(list2.isEmpty());
    }

    @Test
    public void countVirtualGiftsForRecipientTest() {
        User aj = userDao.loadUserByUsername("aj");
        int i = giftingDao.countVirtualGiftsForRecipient(aj.getGuid(), true);
        // kind of lame test, but basically just making sure the queries aren't hosed.
        assertTrue(i > 0);
    }

}

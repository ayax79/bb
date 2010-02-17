package com.blackbox.server.gifting;

import com.blackbox.foundation.Utils;
import com.blackbox.gifting.IGiftingManager;
import com.blackbox.gifting.GiftLayout;
import com.blackbox.foundation.media.IMediaManager;
import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.foundation.media.MediaPublish;
import com.blackbox.server.media.event.VirtualGiftAcceptedEvent;
import com.blackbox.server.media.IMediaDao;
import com.blackbox.server.message.IMessageDao;
import com.blackbox.foundation.util.PaginationUtil;
import com.blackbox.server.gifting.event.CreateGiftEvent;
import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.message.IMessageManager;
import com.blackbox.foundation.activity.IActivity;
import com.blackbox.foundation.social.NetworkTypeEnum;
import com.blackbox.foundation.user.PaginationResults;
import com.blackbox.foundation.util.JaxbSafeCollectionWrapper;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yestech.event.multicaster.BaseServiceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Service("giftingManager")
public class GiftingManager extends BaseServiceContainer implements IGiftingManager {

    private static final Logger log = LoggerFactory.getLogger(BaseServiceContainer.class);

    @Resource
    IGiftingDao giftingDao;

    @Resource
    IMediaManager mediaManager;

    @Resource
    IMessageManager messageManager;

    @Resource
    IMediaDao mediaDao;

    @Resource
    IMessageDao messageDao;

    @Override
    public MediaMetaData sendMediaVirtualGift(MediaPublish<MediaMetaData> publish) throws IOException {
        MediaMetaData mmd = publish.getMediaMetaData();
        if (mmd.getGuid() == null) Utils.applyGuid(mmd);
        mmd.setPostDate(new DateTime());
        mmd.setAcknowledged(false);
        mmd.setVirtualGift(true);
        mmd.setRecipientDepth(NetworkTypeEnum.ALL_MEMBERS);
        assert mmd.getRecipients() != null && !mmd.getRecipients().isEmpty();
        mediaManager.publishMedia(publish);
        getEventMulticaster().process(new CreateGiftEvent(mmd));
        return mmd;
    }

    @Override
    public Message sendMessageVirtualGift(Message m) {
        if (m.getGuid() == null) Utils.applyGuid(m);
        m.setPostDate(new DateTime());
        m.setAcknowledged(false);
        m.setVirtualGift(true);
        m.setRecipientDepth(NetworkTypeEnum.ALL_MEMBERS);
        messageManager.publish(m);
        getEventMulticaster().process(new CreateGiftEvent(m));
        return m;
    }

    @Override
    @Transactional
    public void acceptVirtualGift(String virtualGiftGuid) {
        MediaMetaData data = mediaManager.loadMediaMetaDataByGuid(virtualGiftGuid);
        if (data != null) {
            data.setAcknowledged(true);
            mediaManager.save(data);
            GiftLayout layout = new GiftLayout();
            layout.setActivityReference(data.toEntityReference());
            giftingDao.save(layout);
            getEventMulticaster().process(new VirtualGiftAcceptedEvent(data));
        }
        else {
            Message message = messageManager.loadMessageByGuid(virtualGiftGuid);
            if (message != null) {
                message.setAcknowledged(true);
                messageManager.save(message);
                GiftLayout layout = new GiftLayout();
                layout.setActivityReference(message.toEntityReference());
                giftingDao.save(layout);
                getEventMulticaster().process(new VirtualGiftAcceptedEvent(message));
            }
            else {
                log.warn("No virtual gift with id " + virtualGiftGuid + " exists");
            }
        }
    }

    @Transactional
    @Override
    public void rejectVirtualGift(String virtualGiftGuid) {
        MediaMetaData data = mediaManager.loadMediaMetaDataByGuid(virtualGiftGuid);
        if (data != null) {
            mediaDao.delete(data);
            giftingDao.deleteGiftLayoutByActivityGuid(data.getGuid());
        }
        else {
            Message message = messageManager.loadMessageByGuid(virtualGiftGuid);
            if (message != null) {
                messageDao.delete(message);
                giftingDao.deleteGiftLayoutByActivityGuid(message.getGuid());
            }
            else {
                log.warn("No virtual gift with id " + virtualGiftGuid + " exists");
            }
        }
    }

    @Override
    @Transactional
    public void updateGiftLayout(List<GiftLayout> layout) {
        for (GiftLayout gl : layout) {
            GiftLayout original = giftingDao.loadGiftLayoutByGuid(gl.getGuid());
            original.setSize(gl.getSize());
            original.setX(gl.getX());
            original.setY(gl.getY());
            original.setZ(gl.getZ());
            original.setShelf(gl.getShelf());
            giftingDao.save(original);
        }
    }


    public void disableVirtualGift(String guid) {
        GiftLayout giftLayout = giftingDao.loadGiftLayoutByGuid(guid);
        if (giftLayout != null) {
            giftLayout.setActive(false);
            giftingDao.save(giftLayout);
        }
    }

    @Override
    public JaxbSafeCollectionWrapper<List<IActivity>> loadVirtualGiftsReceived(String guid, boolean accepted) {
        return new JaxbSafeCollectionWrapper<List<IActivity>>(giftingDao.loadVirtualGiftsForRecipient(guid, accepted));
    }

    public PaginationResults<IActivity> loadVirtualGiftsReceivedInBounds(String guid, int startIndex, int numResults) {
        List<IActivity> gifts = giftingDao.loadVirtualGiftsForRecipient(guid, true);
        // no real pagination right now, will implement later
        return PaginationUtil.buildPaginationResults(gifts, startIndex, numResults);
    }

    @Override
    public JaxbSafeCollectionWrapper<List<IActivity>> loadVirtualGiftsSent(String guid) {
        return new JaxbSafeCollectionWrapper<List<IActivity>>(giftingDao.loadVirtualGiftsForSender(guid));
    }


    @Override
    public List<GiftLayout> loadGiftLayout(String guid) {
        return giftingDao.loadGiftLayout(guid);
    }

}

package com.blackbox.gifting;

import com.blackbox.activity.IActivity;
import com.blackbox.media.MediaMetaData;
import com.blackbox.media.MediaPublish;
import com.blackbox.message.Message;
import com.blackbox.user.PaginationResults;
import com.blackbox.util.JaxbSafeCollectionWrapper;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;


public interface IGiftingManager {

    MediaMetaData sendMediaVirtualGift(MediaPublish<MediaMetaData> publish) throws IOException;

    @Transactional
    void acceptVirtualGift(String virtualGiftGuid);

    @Transactional
    void rejectVirtualGift(String virtualGiftGuid);

    JaxbSafeCollectionWrapper<List<IActivity>> loadVirtualGiftsReceived(String guid, boolean accepted);

    JaxbSafeCollectionWrapper<List<IActivity>> loadVirtualGiftsSent(String guid);

    Message sendMessageVirtualGift(Message m);

    List<GiftLayout> loadGiftLayout(String guid);

    void updateGiftLayout(List<GiftLayout> layout);

    PaginationResults<IActivity> loadVirtualGiftsReceivedInBounds(String guid, int startIndex, int numResults);

    void disableVirtualGift(String guid);
}

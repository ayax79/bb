package com.blackbox.foundation.gifting;

import com.blackbox.foundation.activity.IActivity;
import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.foundation.media.MediaPublish;
import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.user.PaginationResults;
import com.blackbox.foundation.util.JaxbSafeCollectionWrapper;
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

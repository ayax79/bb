package com.blackbox.server.gifting;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.blackbox.foundation.activity.IActivity;
import com.blackbox.foundation.gifting.GiftLayout;

public interface IGiftingDao {

    int countVirtualGiftsForRecipient(String guid, boolean accepted);

    List<IActivity> loadVirtualGiftsForRecipient(String guid, boolean accepted);

    List<IActivity> loadVirtualGiftsForSender(String guid);

    @Transactional
    void save(GiftLayout layout);

    @Transactional
    void delete(GiftLayout layout);

    List<GiftLayout> loadGiftLayout(String userGuid);

    GiftLayout loadGiftLayoutByGuid(String guid);

    @Transactional
    void deleteGiftLayoutByActivityGuid(String activityGuid);
}

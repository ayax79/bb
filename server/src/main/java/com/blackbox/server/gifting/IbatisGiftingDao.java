package com.blackbox.server.gifting;

import com.blackbox.activity.IActivity;
import com.blackbox.gifting.GiftLayout;
import com.blackbox.server.util.PersistenceUtil;
import org.springframework.orm.ibatis3.SqlSessionOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yestech.publish.objectmodel.ArtifactType;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository("giftingDao")
public class IbatisGiftingDao implements IGiftingDao {

    @Resource
    SqlSessionOperations template;

    @SuppressWarnings({"unchecked"})
    @Override
    public List<IActivity> loadVirtualGiftsForRecipient(String guid, boolean accepted) {

        HashMap<String, Object> params = new HashMap<String, Object>(2);
        params.put("guid", guid);
        params.put("acknowledged", accepted);
        List messages = template.selectList("VirtualGift.loadMessageVirtualGiftForRecipient", params);
        List media = template.selectList("VirtualGift.loadMediaVirtualGiftForRecipient", params);
        List<IActivity> union = new ArrayList<IActivity>(media.size() + messages.size());
        union.addAll(media);
        union.addAll(messages);
        return union;
    }

    @Override
    public int countVirtualGiftsForRecipient(final String guid, final boolean accepted) {
        HashMap<String, Object> params = new HashMap<String, Object>(2);
        params.put("guid", guid);
        params.put("acknowledged", accepted);

        int messageCount = (Integer) template.selectOne("VirtualGift.countMessageGiftsForRecipient", params);
        int mediaCount = (Integer) template.selectOne("VirtualGift.countMediaGiftsForRecipient", params);
        return messageCount + mediaCount;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<IActivity> loadVirtualGiftsForSender(String guid) {
        HashMap<String, Object> params = new HashMap<String, Object>(2);
        params.put("guid", guid);
        params.put("acknowledged", true);

        List media = template.selectList("VirtualGift.loadMediaVirtualGiftForOwner", params);
        List messages = template.selectList("VirtualGift.loadMessageVirtualGiftForOwner", params);
        List<IActivity> union = new ArrayList<IActivity>(media.size() + messages.size());
        union.addAll(media);
        union.addAll(messages);
        return union;
    }

    @Override
    @Transactional
    public void save(GiftLayout layout) {
        PersistenceUtil.insertOrUpdate(layout, template, "GiftLayout.insert", "GiftLayout.update");
    }

    @Override
    @Transactional
    public void delete(GiftLayout layout) {
        template.delete("GiftLayout.delete", layout.getGuid());
    }

    @SuppressWarnings({"unchecked"})
    public List<GiftLayout> loadGiftLayout(final String userGuid) {
        HashMap<String,Object> params = new HashMap<String, Object>(2);
        params.put("acknowledged", true);
        params.put("guid", userGuid);
        return template.selectList("GiftLayout.loadVirtualGiftForRecipient", params);
    }


    public GiftLayout loadGiftLayoutByGuid(String guid) {
        return (GiftLayout) template.selectOne("GiftLayout.load", guid);
    }

    @Transactional
    public void deleteGiftLayoutByActivityGuid(String activityGuid) {
        template.delete("GiftLayout.deleteByActivityGuid", activityGuid);
    }

}

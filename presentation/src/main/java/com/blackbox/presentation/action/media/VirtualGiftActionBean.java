package com.blackbox.presentation.action.media;

import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.activity.IActivity;
import com.blackbox.foundation.gifting.GiftLayout;
import com.blackbox.foundation.gifting.IGiftingManager;
import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.foundation.media.MediaPublish;
import com.blackbox.foundation.media.MediaRecipient;
import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.message.MessageRecipient;
import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.presentation.action.util.JSONUtil;
import com.blackbox.foundation.user.User;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.util.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.blackbox.foundation.EntityType.USER;
import static com.blackbox.presentation.action.media.VirtualGiftActionBean.FilterType.RECEIVED;
import static com.blackbox.presentation.action.media.VirtualGiftActionBean.FilterType.SENT;
import static com.blackbox.presentation.action.util.JSONUtil.toJSON;
import static com.blackbox.presentation.action.util.PresentationUtil.*;
import static com.blackbox.presentation.action.util.PrivacyUtils.isAllowedToGift;

/**
 * @author A.J. Wright
 */
public class VirtualGiftActionBean extends BaseBlackBoxActionBean {

    public static enum FilterType {
        SENT,
        RECEIVED
    }

    @SpringBean("giftingManager")
    IGiftingManager giftingManager;

    private FileBean fileData;
    private String messageBody;
    private String guid;
    private FilterType filter = RECEIVED;
    private List<IActivity> gifts;
    private String base64;
    private JSONArray json;
    private String giftGuid;

    public Resolution sendMedia() throws JSONException, IOException {
        if (!isAllowedToGift(getCurrentUser(), guid)) {
            return createResolutionPrivacyError(getContext());
        }

        MediaMetaData giftMeta = MediaMetaData.createMediaMetaData();
        giftMeta.getRecipients().add(new MediaRecipient(new EntityReference(USER, guid), giftMeta));

        User user = getCurrentUser();
        populateMediaMetaData(giftMeta, fileData, user, messageBody);
        giftingManager.sendMediaVirtualGift(new MediaPublish<MediaMetaData>(giftMeta, buildByteArray(fileData)));
        fileData.delete();

        if (getView() == ViewType.json) {
            return createResolutionWithJson(getContext(), toJSON(giftMeta));
        }

        return new ForwardResolution("todo");
    }

    public Resolution sendBase64() throws JSONException, IOException {
        User currentUser = getCurrentUser();
        assert currentUser != null;

        if (!isAllowedToGift(getCurrentUser(), guid)) {
            return createResolutionPrivacyError(getContext());
        }

        MediaMetaData giftMeta = MediaMetaData.createMediaMetaData();
        giftMeta.getRecipients().add(new MediaRecipient(new EntityReference(USER, guid), giftMeta));
        populateMetaData(giftMeta, getCurrentUser(), messageBody);

        byte[] bytes = Base64.decode(base64);
        giftMeta.setSize(bytes.length / 1024L);
        //set parent
        giftMeta = giftingManager.sendMediaVirtualGift(new MediaPublish<MediaMetaData>(giftMeta, bytes));

        if (getView() == ViewType.json) {
            return createResolutionWithJson(getContext(), toJSON(giftMeta));
        }

        return new ForwardResolution("todo");
    }

    public Resolution sendMessage() throws JSONException, IOException {
        Message message = Message.createMessage();

        if (!isAllowedToGift(getCurrentUser(), guid)) {
            return createResolutionPrivacyError(getContext());
        }

        message.getRecipients().add(new MessageRecipient(new EntityReference(USER, guid), message.getGuid()));
        message.getArtifactMetaData().setArtifactOwner(getCurrentUser().toEntityReference());
        message.setBody(messageBody);
        giftingManager.sendMessageVirtualGift(message);

        if (getView() == ViewType.json) {
            return createResolutionWithJson(getContext(), toJSON(message));
        }

        return new ForwardResolution("todo");
    }

    @DontValidate
    public Resolution loadJson() throws JSONException {
        List<GiftLayout> list = giftingManager.loadGiftLayout(guid);

        if (list != null && !list.isEmpty()) {
            JSONArray array = new JSONArray();

            for (GiftLayout giftLayout : list) {
                array.put(JSONUtil.toJSON(giftLayout));
            }

            return createResolutionWithJsonArray(getContext(), array);
        } else {
            return createResolutionWithText(getContext(), "[]");
        }
    }

    @DefaultHandler
    public Resolution load() {

        User user = getCurrentUser();
        if (filter == RECEIVED) {
            gifts = giftingManager.loadVirtualGiftsReceived(user.getGuid(), true).getCollection();
        } else if (filter == SENT) {
            gifts = giftingManager.loadVirtualGiftsSent(user.getGuid()).getCollection();
        }

        return new ForwardResolution("todo");
    }

    public Resolution disable() {
        giftingManager.disableVirtualGift(giftGuid);
        return createResolutionWithText(getContext(), "success=true");
    }


    public Resolution saveLayout() throws JSONException {
        if (getCurrentUser().getGuid().equals(guid)) {
            ArrayList<GiftLayout> list = new ArrayList<GiftLayout>(json.length());
            if (json != null) {
                for (int i = 0, size = json.length(); i < size; i++) {
                    JSONObject json = this.json.getJSONObject(i);
                    GiftLayout giftLayout = JSONUtil.giftLayoutFromJSON(json);
                    list.add(giftLayout);
                }
                giftingManager.updateGiftLayout(list);
            }
        }

        return createResolutionWithText(getContext(), "success=true");
    }

    public List<IActivity> getGifts() {
        return gifts;
    }

    public void setFileData(FileBean fileData) {
        this.fileData = fileData;
    }

    public FileBean getFileData() {
        return fileData;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public FilterType getFilter() {
        return filter;
    }

    public void setFilter(FilterType filter) {
        this.filter = filter;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public JSONArray getJson() {
        return json;
    }

    public void setJson(JSONArray json) {
        this.json = json;
    }

    public String getGiftGuid() {
        return giftGuid;
    }

    public void setGiftGuid(String giftGuid) {
        this.giftGuid = giftGuid;
    }
}

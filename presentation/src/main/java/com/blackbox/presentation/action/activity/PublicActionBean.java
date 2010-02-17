package com.blackbox.presentation.action.activity;

import com.blackbox.foundation.activity.IActivityManager;
import com.blackbox.foundation.activity.IActivityThread;
import com.blackbox.foundation.message.IMessageManager;
import com.blackbox.foundation.message.Message;
import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.foundation.user.IUserManager;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.yestech.cache.ICacheManager;

import java.util.Collection;
import java.util.List;

import static com.blackbox.presentation.action.util.PresentationUtil.createJSONResolutionWithText;

@UrlBinding("/public/{$event}/{identifier}")
public class PublicActionBean extends BaseBlackBoxActionBean {

    public static final String ACTIVITY_KEY = "public-activity";

    @SpringBean("activityManager")
    IActivityManager activityManager;
    @SpringBean("messageManager")
    IMessageManager messageManager;
    @SpringBean("publicCache")
    ICacheManager publicCache;
    @SpringBean("userManager")
    IUserManager userManager;

    private String identifier;
    private IActivityThread activityThread;


    @SuppressWarnings({"unchecked"})
    @DefaultHandler
    public Resolution threads() throws JSONException {

        if (getView() == ViewType.json) {
            String json = (String) publicCache.get(ACTIVITY_KEY);

            if (json == null) {
                JSONArray array = new JSONArray();

                List<Message> list = messageManager.loadRecentPublicParentMessages();
                for (Message message : list) {
                    JSONObject jo = new JSONObject();
                    jo.put("body", message.getBody());
                    jo.put("username", message.getArtifactMetaData().getSenderProfile().getSenderDisplayName());
                    jo.put("avatarUrl", message.getArtifactMetaData().getSenderProfile().getSenderAvatarImage());
                    jo.put("postDate", message.getPostDate());
                    array.put(jo);
                }

                json = array.toString();
                publicCache.put(ACTIVITY_KEY, json);
            }
            return createJSONResolutionWithText(getContext(), json);
        } else {
            Collection<IActivityThread> threads = activityManager.loadPublicActivityThreads(0, 25);
            return new ForwardResolution("todo");
        }

    }

    public Resolution thread() {
        activityThread = activityManager.loadActivityThreadByParentGuid(identifier);
        return new ForwardResolution("todo");
    }


    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public IActivityThread getActivityThread() {
        return activityThread;
    }

    @Override
    public boolean isHasIntro() {
        return false;
    }
}

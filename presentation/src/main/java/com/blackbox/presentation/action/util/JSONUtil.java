/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */
package com.blackbox.presentation.action.util;

import com.blackbox.foundation.BaseEntity;
import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.EntityType;
import com.blackbox.foundation.IEntity;
import com.blackbox.foundation.util.NameInfo;
import com.blackbox.foundation.activity.*;
import com.blackbox.foundation.bookmark.Bookmark;
import com.blackbox.gifting.GiftLayout;
import com.blackbox.foundation.media.AvatarImage;
import com.blackbox.foundation.media.CorkboardImage;
import com.blackbox.foundation.media.MediaLibrary;
import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.message.MessageMetaData;
import com.blackbox.foundation.occasion.Occasion;
import static com.blackbox.presentation.action.util.JspFunctions.displayName;
import static com.blackbox.presentation.action.util.PresentationUtil.getAvatarImage;
import com.blackbox.foundation.search.WordFrequency;
import com.blackbox.foundation.social.Category;
import com.blackbox.foundation.social.Ignore;
import com.blackbox.foundation.user.Profile;
import com.blackbox.foundation.user.User;
import static com.google.common.collect.Lists.newArrayList;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.publish.objectmodel.ArtifactType;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Utility for converted objects into json strings.
 *
 * @author A.J. Wright
 */
public final class JSONUtil {

    final private static Logger logger = LoggerFactory.getLogger(JSONUtil.class);

    private JSONUtil() {
    }

    public static JSONArray toJSON(List<WordFrequency> list) throws JSONException {
        JSONArray array = new JSONArray();
        if (list != null && !list.isEmpty()) {
            for (WordFrequency wordFrequency : list) {
                array.put(toJSON(wordFrequency));
            }
        }
        return array;
    }

    public static JSONObject toJSON(WordFrequency wf) throws JSONException {
        if (wf == null) return null;
        JSONObject jo = new JSONObject();
        jo.put("frequency", wf.getFrequency());
        jo.put("word", wf.getWord());
        return jo;
    }

    public static JSONObject toJSON(IActivity activity) throws JSONException {
        if (activity == null) {
            return null;
        }
        JSONObject object = new JSONObject();
        if (activity instanceof Message) {
            object = toJSON((Message) activity);
        } else if (activity instanceof MediaMetaData) {
            object = toJSON((MediaMetaData) activity);
        } else if (activity instanceof Activity) {
            object = toJSON((Activity) activity);
        }
        return object;
    }

    public static JSONObject toJSON(Activity metaData) throws JSONException {
        if (metaData == null) {
            return null;
        }
        JSONObject object = new JSONObject();
        populateMediaMetaData(metaData, object);
        return object;
    }

    private static void populateMediaMetaData(Activity metaData, JSONObject object) throws JSONException {
        object.put("guid", metaData.getGuid());
        object.put("ownerType", metaData.getOwnerType().name());
        object.put("location", metaData.getLocation());
        object.put("thumbnailLocation", metaData.getThumbnailLocation());
        if (metaData.getParentActivity() != null) {
            object.put("parentActivity", toJSON(metaData.getParentActivity()));
        }
        object.put("owner", toJSON(metaData.getArtifactOwner()));
        object.put("filename", metaData.getFileName());
        object.put("mimeType", metaData.getMimeType());
        object.put("passwordProtect", metaData.isPasswordProtect());
        object.put("acknowledged", metaData.isAcknowledged());
        object.put("virtualGift", metaData.isVirtualGift());

        object.put("body", metaData.getComment());
        populateActivity(object, metaData);
        object.put("size", metaData.getSize());
    }

    public static JSONObject toJSON(MediaMetaData metaData) throws JSONException {
        if (metaData == null) {
            return null;
        }
        JSONObject object = new JSONObject();
        populateMediaMetaData(metaData, object);
        return object;
    }

    private static void populateMediaMetaData(MediaMetaData metaData, JSONObject object) throws JSONException {
        object.put("guid", metaData.getGuid());
        object.put("ownerType", metaData.getOwnerType().name());
        object.put("location", metaData.getLocation());
        object.put("thumbnailLocation", metaData.getThumbnailLocation());
        if (metaData.getParentActivity() != null) {
            object.put("parentActivity", toJSON(metaData.getParentActivity()));
        }
        object.put("owner", toJSON(metaData.getArtifactOwner()));
        object.put("filename", metaData.getFileName());
        object.put("mimeType", metaData.getMimeType());
        object.put("profile", metaData.isProfile());
        object.put("passwordProtect", metaData.isPasswordProtect());
        object.put("acknowledged", metaData.isAcknowledged());
        object.put("virtualGift", metaData.isVirtualGift());

        object.put("body", metaData.getComment());
        populateActivity(object, metaData);
        object.put("size", metaData.getSize());
    }

    public static JSONObject toJSON(List<MediaLibrary> mediaLibList) throws JSONException {

        if (mediaLibList == null) {
            return null;
        }
        JSONObject object = new JSONObject();
        JSONArray list = new JSONArray();
        object.put("list", list);

        JSONArray firstLibCnt = new JSONArray();
        object.put("firstLibCnt", firstLibCnt);

        logger.info("mediaLibList size is:" + mediaLibList.size());
        boolean addedFirstLib = false;
        for (MediaLibrary medlib : mediaLibList) {
            list.put(toJSON(medlib));

            List<MediaMetaData> mediaList = medlib.getMedia();
            logger.info("media in lib size:" + mediaList.size());
            if (mediaList != null && mediaList.size() > 0 && addedFirstLib == false) {
                for (MediaMetaData media : mediaList) {
                    firstLibCnt.put(toJSON(media));
                    logger.info("media is:" + media.toString());
                }
                addedFirstLib = true;
            }
        }
        return object;
    }

    public static JSONObject toJSON(MediaLibrary medlib) throws JSONException {
        if (medlib == null) {
            return null;
        }
        JSONObject object = new JSONObject();
        object.put("guid", medlib.getGuid());
        object.put("name", medlib.getName());
        object.put("created", medlib.getCreated().toString("MM/dd/yyyy"));
        object.put("desc", medlib.getDescription());

        return object;
    }

    public static JSONObject toJSON(Profile profile) throws JSONException {
        JSONObject json = new JSONObject();
        if (profile != null) {
            if (profile.getBirthday() != null) {
                json.put("birthday", profile.getBirthday().toString("MM/dd/yyyy"));
            }
            if (profile.getLocation() != null) {
                json.put("location", profile.getLocation());
            }
            if (profile.getSex() != null) {
                json.put("sex", profile.getSex());
            }
            json.put("acceptsGifts", profile.isAcceptsGifts());
            if (profile.getLookingFor() != null) {
                JSONObject lookingFor=new JSONObject();
                lookingFor.put("date", profile.getLookingFor().isDates());
                lookingFor.put("hookup", profile.getLookingFor().isHookup());
                lookingFor.put("friends", profile.getLookingFor().isFriends());
                lookingFor.put("love", profile.getLookingFor().isLove());
                lookingFor.put("donkeySex", profile.getLookingFor().isDonkeySex());
                lookingFor.put("snuggling", profile.getLookingFor().isSnuggling());
                json.put("lookingFor", lookingFor);
            }
            if (profile.getPoliticalViews() != null) {
                json.put("politicalViews", profile.getPoliticalViews());
            }
            if (profile.getReligiousViews() != null) {
                json.put("religiousViews", profile.getReligiousViews());
            }
            if (profile.getWebsite() != null) {
                json.put("website", profile.getWebsite());
            }
            if (profile.getBodyMods() != null) {
                json.put("bodyMods", profile.getBodyMods());
            }
            if (profile.getMostly() != null) {
                json.put("mostly", profile.getMostly());
            }
            if (profile.getProfileImgUrl() != null) {
                json.put("imageUrl", profile.getProfileImgUrl());
            }
            if (profile.getProfileImgUrl() != null) {
                json.put("imageUrl", profile.getProfileImgUrl());
            }
        }
        return json;
    }

    public static JSONObject toJSON(Message message) throws JSONException {
        if (message == null) {
            return null;
        }
        JSONObject object = new JSONObject();
        object.put("guid", message.getGuid());
        object.put("ownerType", message.getOwnerType().name());
        object.put("body", message.getBody());
        object.put("acknowledged", message.isAcknowledged());
        object.put("virtualGift", message.isVirtualGift());
        MessageMetaData messageMetaData = message.getArtifactMetaData();

        JSONObject owner = toJSON(messageMetaData.getArtifactOwner());
        if (owner != null) {
            //TODO update with Owner
//            owner.put("ownerImageUrl", messageMetaData.getOwnerImageUrl());
//            owner.put("ownerName", messageMetaData.getOwnerName());
            object.put("owner", owner);
        }
        if (messageMetaData.getMessageType() != null) {
            object.put("messageType", messageMetaData.getMessageType().getCustomDefinedType());
        }
        if (message.getParentActivity() != null) {
            object.put("parentActivity", toJSON(message.getParentActivity()));
        }
        populateActivity(object, message);
        return object;
    }

    public static JSONObject toJSON(IActivityThread<IActivity> thread) throws JSONException {
        if (thread == null) {
            return null;
        }
        JSONObject object = toJSON(thread.getParent());
        JSONArray children = new JSONArray();
        object.put("children", children);
        for (IActivity message : thread.getChildren()) {
            children.put(toJSON(message));
        }
        return object;
    }

    public static JSONObject toJSON(BaseEntity baseEntity) throws JSONException {
        if (baseEntity == null) {
            return null;
        }
        JSONObject object = new JSONObject();
        populate(object, baseEntity);
        return object;
    }

    public static JSONObject fromJSON(String json) throws JSONException {
        return new JSONObject(json);
    }

    public static List<Category> toCategories(String json) throws JSONException {
        List<Category> categories = newArrayList();
        JSONObject object = fromJSON(json);
        JSONArray jsonArray = object.getJSONArray("categories");
        for (int i = 0; i < jsonArray.length(); i++) {
            String categoryName = jsonArray.getString(i);
            Category category = new Category();
            category.setName(categoryName);
            categories.add(category);
        }
        return categories;
    }

    public static JSONObject toJSON(User user) throws JSONException {
        if (user == null) {
            return null;
        }
        JSONObject object = new JSONObject();
        populate(object, user);
        object.put("name", displayName(user));
        object.put("username", user.getUsername());
        object.put("email", user.getEmail());
        object.put("status", user.getStatus());
        object.put("type", user.getType().name());
        return object;
    }

    public static JSONObject toJSON(Occasion occasion) throws JSONException {
        if (occasion == null) {
            return null;
        }
        JSONObject object = new JSONObject();
        populate(object, occasion);
        object.put("location", occasion.getLocation());
        object.put("description", occasion.getDescription());
        object.put("level", occasion.getOccasionLevel());
        final DateTime dateTime = occasion.getEventTime();
        if (dateTime != null) {
            object.put("eventDate", dateTime.toString(ISODateTimeFormat.dateTimeNoMillis()));
        }
        populateActivity(object, occasion);
        if (occasion.getOwner() != null) {
            object.put("owner", toJSON(occasion.getOwner()));
        }
        return object;
    }

    public static JSONObject toJSON(Bookmark bookmark) throws JSONException {
        if (bookmark == null) {
            return null;
        }
        JSONObject object = new JSONObject();
        object.put("owner", toJSON(bookmark.getOwner()));
        object.put("target", toJSON(bookmark.getTarget()));
        return object;
    }

    public static JSONObject toJSON(EntityReference entityReference) throws JSONException {
        if (entityReference == null) {
            return null;
        }

        JSONObject object = new JSONObject();
        object.put("guid", entityReference.getGuid());
        if (entityReference.getOwnerType() != null) {
            object.put("ownerType", entityReference.getOwnerType().name());
        }
        return object;
    }

    public static JSONObject toJSON(ActivityReference activityReference) throws JSONException {
        if (activityReference == null) {
            return null;
        }

        JSONObject object = new JSONObject();
        object.put("guid", activityReference.getGuid());
        EntityType ownerType = activityReference.getOwnerType();
        if (ownerType != null) {
            object.put("ownerType", ownerType.name());
        }
        ActivityTypeEnum activityType = activityReference.getActivityType();
        if (activityType != null) {
            object.put("activityType", activityType.name());
        }
        final DateTime dateTime = activityReference.getPostDate();
        if (dateTime != null) {
            object.put("postDate", dateTime.toString(ISODateTimeFormat.dateTimeNoMillis()));
        }
        return object;
    }

    public static JSONObject toJSON(Ignore ignore) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("target", toJSON(ignore.getTarget()));
        object.put("ignorer", toJSON(ignore.getIgnorer()));
        object.put("type", ignore.getType().name());
        return object;
    }

    protected static void populate(JSONObject object, BaseEntity baseEntity) throws JSONException {
        object.put("guid", baseEntity.getGuid());
        object.put("name", baseEntity.getName());
        object.put("ownerType", baseEntity.getOwnerType().name());
        if (baseEntity.getStatus() != null) {
            object.put("status", baseEntity.getStatus().name());
        }
    }

    protected static void populateActivity(JSONObject object, IActivity activity) throws JSONException {
        if (activity.getRecipients() != null) {
            JSONArray array = new JSONArray();

            for (IRecipient recipient : activity.getRecipients()) {
                array.put(toJSON(recipient.getRecipient()));
            }

            object.put("recipients", array);
        }
        if (activity.getRecipientDepth() != null) {
            object.put("recipientDepth", activity.getRecipientDepth().name());
        }
        if (activity.getPostDate() != null) {
            object.put("postDate", activity.getFormattedPostDate());

			//TODO: Need to get locale from the correct context, but not sure how to do that
			object.put("prettyDate", activity.getPostDate().toString("MMMM d", Locale.US));
        }
        if (activity.getSender() != null) {
            object.put("sender", toJSON(activity.getSender()));
        }
        object.put("activityType", activity.getActivityType().name());


        IEntity artifactOwner = null;
        if (activity instanceof Message) {
            Message m = (Message) activity;
            artifactOwner = m.getArtifactMetaData().getArtifactOwner();
        }
        else if (activity instanceof MediaMetaData) {
            MediaMetaData m = (MediaMetaData) activity;
            artifactOwner = m.getArtifactOwner();
        }
        else if (activity instanceof Occasion) {
            Occasion o = (Occasion) activity;
            artifactOwner = o.getOwner();
        }

        if (artifactOwner != null) {
            JSONObject senderProfile = new JSONObject();

            AvatarImage image = getAvatarImage(artifactOwner.toEntityReference());
            if (image != null) senderProfile.put("senderProfileImage", image.getImageLink());
            NameInfo nameInfo = PresentationUtil.getNameInfo(artifactOwner);
            if (nameInfo != null) {
                senderProfile.put("senderDisplayName", nameInfo.getDisplayName());
                senderProfile.put("senderDisplayUsername", nameInfo.getUsername());
                object.put("senderProfile", senderProfile);
            }
        }

    }

    public static JSONObject toJSON(ActivityProfile activityProfile) throws JSONException {
        if (activityProfile == null) {
            return null;
        }
        JSONObject object = new JSONObject();
        object.put("senderProfileImage", activityProfile.getSenderAvatarImage());
        object.put("senderDisplayName", activityProfile.getSenderDisplayName());
        return object;
    }

    public static JSONObject toJSON(CorkboardImage ci) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("guid", ci.getGuid());
        jo.put("location", ci.getLocation());
        jo.put("x", ci.getX());
        jo.put("y", ci.getY());
        jo.put("z", ci.getZ());
        jo.put("rotation", ci.getRotation());
        jo.put("scale", String.valueOf(ci.getScale()));
        jo.put("fileName", ci.getFileName());
        return jo;
    }

    public static JSONObject toJSON(GiftLayout layout) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("guid", layout.getGuid());
        jo.put("description", layout.getBody());
        jo.put("location", layout.getLocation());
        jo.put("frame", layout.getFrame());

        if (layout.getType() == ArtifactType.IMAGE) {
            jo.put("iconLocation", photosUrl());
        } else if (layout.getType() == ArtifactType.VIDEO) {
            jo.put("iconLocation", videoUrl());
        } else if (layout.getType() == ArtifactType.TEXT) {
            jo.put("iconLocation", textUrl());
        }

        if (layout.getSize() != null) {
            jo.put("size", layout.getSize().name().toLowerCase());
        }
        if (layout.getType() != null) {
            jo.put("type", layout.getType().name().toLowerCase());
        }
        jo.put("x", layout.getX());
        jo.put("y", layout.getY());
        jo.put("z", layout.getZ());
        jo.put("shelf", layout.getShelf().name());
        return jo;
    }

    public static GiftLayout giftLayoutFromJSON(JSONObject jo) {
        GiftLayout gl = new GiftLayout();
        gl.setGuid(jsonString(jo, "guid"));
        gl.setSize(GiftLayout.GiftLayoutSize.valueOf(jsonString(jo, "size")));
        gl.setX(jsonInt(jo, "x"));
        gl.setY(jsonInt(jo, "y"));
        gl.setZ(jsonInt(jo, "z"));
        gl.setActive(jsonBoolean(jo, "active"));
        gl.setShelf(GiftLayout.Shelf.valueOf(jsonString(jo, "shelf")));
        return gl;
    }

    public static boolean jsonBoolean(JSONObject jo, String property) {
        try {
            if (jo != null) return jo.getBoolean(property);
        } catch (JSONException e) {
            // ignore
        }
        return false;
    }

    public static String jsonString(JSONObject jo, String property) {
        try {
            if (jo != null) return jo.getString(property);
        } catch (JSONException e) {
            // ignore the exception
        }
        return null;
    }

    public static JSONArray jsonArray(JSONObject jo, String property) {
        try {
            if (jo != null) return jo.getJSONArray(property);
        } catch (JSONException e) {
            // ignore the exception
        }
        return null;
    }

    public static JSONObject jsonObject(JSONObject jo, String property) {
        try {
            if (jo != null) return jo.getJSONObject(property);
        } catch (JSONException e) {
            // ignore the exception
        }
        return null;
    }

    public static int jsonInt(JSONObject jo, String property) {
        try {
            if (jo != null) return jo.getInt(property);
        } catch (JSONException e) {
            // ignore the exception
        }
        return 0;
    }

    public static JSONObject jsonObject(JSONArray array, int index) {
        try {
            if (array != null) return array.getJSONObject(index);
        } catch (JSONException e) {
            // ignore the exception
        }
        return null;
    }

    public static String jsonString(JSONArray array, int index) {
        try {
            if (array != null) return array.getString(index);
        } catch (JSONException e) {
            // ignore the exception
        }
        return null;
    }

    public static double jsonDouble(JSONObject jo, String prop) {
        try {
            if (jo != null) return jo.getDouble(prop);
        }
        catch (JSONException e) {
            // ignore
        }
        return 0;
    }

    public static class JsonIterator implements Iterator<JSONObject> {

        private JSONArray array;
        int count = 0;

        public JsonIterator(JSONArray array) {
            this.array = array;
        }

        @Override
        public boolean hasNext() {
            return count < array.length();
        }

        @Override
        public JSONObject next() {
            try {
                return array.getJSONObject(count);
            } catch (JSONException e) {
                logger.error(e.getMessage(), e);
            }
            return null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static String photosUrl() {
        return new StringBuilder(PresentationUtil.getProperty("cdn.url"))
                .append("/library/images/giftshelf/photos.png")
                .toString();
    }

    public static String videoUrl() {
        return new StringBuilder(PresentationUtil.getProperty("cdn.url"))
                .append("/library/images/giftshelf/video.png")
                .toString();
    }

    public static String textUrl() {
        return new StringBuilder(PresentationUtil.getProperty("cdn.url"))
                .append("/library/images/giftshelf/text.png")
                .toString();
    }


}

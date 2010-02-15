/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.activity;

import com.blackbox.EntityReference;
import com.blackbox.EntityType;
import com.blackbox.media.MediaMetaData;
import com.blackbox.message.Message;
import com.blackbox.occasion.ActivityOccasion;
import com.blackbox.occasion.Occasion;
import com.blackbox.occasion.OccasionType;
import com.blackbox.social.NetworkTypeEnum;
import org.apache.commons.beanutils.BeanUtils;
import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.yestech.publish.objectmodel.ArtifactType;
import org.yestech.publish.objectmodel.DefaultFileArtifact;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import static org.yestech.lib.crypto.MessageDigestUtils.sha1Hash;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@SuppressWarnings("unchecked")
public class ActivityFactory {

    /**
     * creates an initialized message with a guid.
     *
     * @return an Message
     */
    public static Message createMessage() {
        Message message = new Message();
        final String key = sha1Hash(UUID.randomUUID().toString());
        message.setGuid(key);
        message.setPostDate(new DateTime(ISOChronology.getInstanceUTC()));
        return message;
    }

    /**
     * creates an initialized occassion with a guid.
     *
     * @return an Occasion
     */
    public static Occasion createOccasion() {
        Occasion occasion = new Occasion();
        final String key = sha1Hash(UUID.randomUUID().toString());
        occasion.setGuid(key);
        occasion.setPostDate(new DateTime(ISOChronology.getInstanceUTC()));
        return occasion;
    }

    /**
     * creates an initialized mediaMataData with a guid.
     *
     * @return an MediaMetaData
     */
    public static MediaMetaData createMedia() {
        MediaMetaData mediaMetaData = new MediaMetaData();
        final String key = sha1Hash(UUID.randomUUID().toString());
        mediaMetaData.setGuid(key);
        mediaMetaData.setPostDate(new DateTime(ISOChronology.getInstanceUTC()));
        return mediaMetaData;
    }

    /**
     * creates an initialized activity with a guid.
     *
     * @return an IActivity
     */
    public static <K extends IActivity> K create(ActivityTypeEnum type) {
        IActivity activity = null;
        if (ActivityTypeEnum.MEDIA.equals(type)) {
            activity = createMedia();
        } else if (ActivityTypeEnum.MESSAGE.equals(type)) {
            activity = createMessage();
        }
        if (ActivityTypeEnum.OCCASION.equals(type)) {
            activity = createOccasion();
        }
        return (K) activity;
    }

    /**
     * create a Thread
     *
     * @return created Thread
     */
    public static ActivityThread create() {
        return new ActivityThread();
    }

    /**
     * create a Thread
     *
     * @param parent Parent Activity
     * @return created Thread
     */
    public static ActivityThread create(IActivity parent) {
        ActivityThread thread = create();
        thread.setParent(parent);
        return thread;
    }

    public static Activity transform(Object camelBody) {
        Activity activity = null;
        if (camelBody instanceof Message) {
            Message message = (Message) camelBody;
            activity = createActivity();
            activity.setGuid(message.getGuid());
            activity.setRecipientDepth(message.getRecipientDepth());
            activity.setComment(message.getBody());
            activity.setArtifactOwner(message.getArtifactMetaData().getArtifactOwner());
            activity.setParentActivity(message.getParentActivity());
            activity.setPostDate(message.getPostDate());
            activity.setPublishToTwitter(message.isPublishToTwitter());
            activity.setExternalCredentials(message.getCreds());
            activity.setActivityType(message.getActivityType());
            activity.setArtifactType(message.getArtifactMetaData().getArtifactType());
            activity.setSenderProfile(message.getSenderProfile());
            activity.setOwnerType(message.getOwnerType());
            activity.setAcknowledged(message.isAcknowledged());
            activity.setVirtualGift(message.isVirtualGift());
        } else if (camelBody instanceof DefaultFileArtifact) {
            DefaultFileArtifact fileMetaData = (DefaultFileArtifact) camelBody;
            MediaMetaData mediaMetaData = (MediaMetaData) fileMetaData.getArtifactMetaData();
            activity = createMediaMetaData(activity, mediaMetaData);
        } else if (camelBody instanceof MediaMetaData) {
            MediaMetaData mediaMetaData = (MediaMetaData) camelBody;
            activity = createMediaMetaData(activity, mediaMetaData);
        } else {
            activity = (Activity) camelBody;
        }
        return activity;
    }

    private static Activity createMediaMetaData(Activity activity, MediaMetaData mediaMetaData) {
        if (!mediaMetaData.isLibrary() && !mediaMetaData.isProfile()) {
            activity = createActivity();
            activity.setGuid(mediaMetaData.getGuid());
            activity.setRecipientDepth(mediaMetaData.getRecipientDepth());
            activity.setArtifactOwner(mediaMetaData.getArtifactOwner());
            activity.setParentActivity(mediaMetaData.getParentActivity());
            activity.setPostDate(mediaMetaData.getPostDate());
            activity.setActivityType(mediaMetaData.getActivityType());
            activity.setArtifactType(mediaMetaData.getArtifactType());
            activity.setSenderProfile(mediaMetaData.getSenderProfile());
            activity.setOwnerType(mediaMetaData.getOwnerType());
            activity.setFileName(mediaMetaData.getFileName());
            activity.setMimeType(mediaMetaData.getMimeType());
            activity.setSize(mediaMetaData.getSize());
            activity.setComment(mediaMetaData.getComment());
            activity.setLocation(mediaMetaData.getLocation());
            activity.setAcknowledged(mediaMetaData.isAcknowledged());
            activity.setAssetId(mediaMetaData.getAssetId());
            activity.setAvatar(mediaMetaData.isAvatar());
            activity.setEpisodeId(mediaMetaData.getEpisodeId());
            activity.setLoosePic(mediaMetaData.isLoosePic());
            activity.setPasswordProtect(mediaMetaData.isPasswordProtect());
            activity.setVirtualGift(mediaMetaData.isVirtualGift());
            activity.setThumbnailLocation(mediaMetaData.getThumbnailLocation());
        }
        return activity;
    }


    public static IActivity clone(IActivity activity) {
        try {
            return (IActivity) BeanUtils.cloneBean(activity);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static Activity toActivity(Occasion occasion) {
        if (occasion == null) {
            return null;
        }
        Activity activity = createActivity();
        activity.setGuid(occasion.getGuid());
        activity.setActivityType(ActivityTypeEnum.OCCASION);
        activity.setArtifactType(ArtifactType.TEXT);
        activity.setAcknowledged(false);
        activity.setSenderProfile(occasion.getSenderProfile());
        activity.setLocation(occasion.getLocation());
        activity.setOwnerType(EntityType.OCCASION);
        activity.setComment(occasion.getDescription());
        activity.setPostDate(occasion.getPostDate());
        activity.setArtifactOwner(EntityReference.createEntityReference(occasion.getOwner()));
        ActivityOccasion activityOccasion = new ActivityOccasion();
        activityOccasion.setAddress(occasion.getAddress());
        activityOccasion.setAvatarLocation(occasion.getAvatarLocation());
        activityOccasion.setDescription(occasion.getDescription());
        activityOccasion.setEventEndTime(occasion.getEventEndTime());
        activityOccasion.setEventTime(occasion.getEventTime());
        activityOccasion.setEventUrl(occasion.getEventUrl());
        activityOccasion.setGuid(occasion.getGuid());
        activityOccasion.setHostBy(occasion.getHostBy());
        activityOccasion.setOccasionType(occasion.getOccasionType());
        activity.setOccasion(activityOccasion);

        if (occasion.getLayout() != null && occasion.getLayout().getTransiantImage() != null) {
            activity.setThumbnailLocation(occasion.getLayout().getTransiantImage().getThumbnailLocation());
        }
        if (OccasionType.OPEN.equals(occasion.getOccasionType())) {
            activity.setRecipientDepth(NetworkTypeEnum.WORLD);
        } else {
            activity.setRecipientDepth(NetworkTypeEnum.FOLLOWERS);
        }
        return activity;
    }

    public static Activity createActivity() {
        return new Activity();
    }
}

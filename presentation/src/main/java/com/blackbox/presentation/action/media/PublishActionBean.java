/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.presentation.action.media;

import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.EntityType;
import com.blackbox.foundation.activity.*;
import com.blackbox.foundation.media.IMediaManager;
import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.foundation.media.MediaPublish;
import com.blackbox.foundation.media.MediaRecipient;
import com.blackbox.foundation.message.IMessageManager;
import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.message.MessageRecipient;
import com.blackbox.foundation.message.PrePublicationUtil;
import com.blackbox.foundation.social.NetworkTypeEnum;
import com.blackbox.foundation.user.ExternalCredentials;
import com.blackbox.foundation.user.IUser;
import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.User;
import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.presentation.action.util.PresentationUtil;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.util.Base64;
import net.sourceforge.stripes.validation.Validate;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.cache.ICacheManager;
import org.yestech.publish.objectmodel.ArtifactType;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collection;

import static com.blackbox.foundation.user.ExternalCredentials.CredentialType.TWITTER;
import static com.blackbox.presentation.action.BaseBlackBoxActionBean.ViewType.json;
import static com.blackbox.presentation.action.util.JSONUtil.toJSON;
import static com.blackbox.presentation.action.util.PresentationUtil.*;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class PublishActionBean extends BaseBlackBoxActionBean {
    final private static Logger logger = LoggerFactory.getLogger(PublishActionBean.class);

    @SpringBean("mediaManager")
    IMediaManager mediaManager;

    @SpringBean("messageManager")
    IMessageManager messageManager;

    @SpringBean("userManager")
    IUserManager userManager;

    @SpringBean("activityManager")
    IActivityManager activityManager;

    @SpringBean("prePublishedMessageCache")
    ICacheManager<String, Collection<Message>> prePublishedMessageCache;

    private FileBean fileData;

    @Validate(required = true, on = "publishMessage")
    private String messageBody;

    private String twitterMessageBody;

    @Validate(required = true, on = "publishBase64Media")
    private String base64;

    @Validate(required = true, on = {"publishMedia", "publishMessage", "publishBase64Media"})
    private NetworkTypeEnum recipientDepth;

    private EntityReference parent;

    private EntityReference recipient;

    private MediaMetaData mediaMetaData;

    private boolean publishToFacebook;
    private String facebookSessionId;

    private boolean publishToTwitter;
    private boolean twitterRemember;

    private String twitterUsername;
    private String twitterPassword;

    @Validate(required = true, on = {"deleteMedia", "deleteMessage"})
    private String guid;

    @Before
    public void hacks() {
        // for some reason in dev, the enum wasn't getting translated into the object properly
        if (parent != null && parent.getOwnerType() == null) {
            String ownerTypeString = getContext().getRequest().getParameter("parent.ownerType");
            if (ownerTypeString != null) {
                parent.setOwnerType(EntityType.valueOf(ownerTypeString));

            }
        }

        if (recipient != null && recipient.getOwnerType() == null) {
            recipient.setOwnerType(EntityType.USER);
        }
    }

    @DefaultHandler
    public Resolution publishMessage() throws JSONException {

        User currentUser = getCurrentUser();

        Message message = Message.createMessage();
        message.setRecipientDepth(recipientDepth);
        message.setBody(messageBody);
        message.getArtifactMetaData().setArtifactOwner(currentUser.toEntityReference());
        ActivityReference parentReference = ActivityReference.createActivityReference(parent);
        message.setParentActivity(parentReference);
        message.getArtifactMetaData().getRecipients().add(new MessageRecipient(recipient, message.getGuid()));
        ActivityProfile profile = new ActivityProfile();
        profile.setSenderDisplayName(currentUser.getUsername());
        profile.setSenderAvatarImage(getAvatarImage(currentUser.toEntityReference()).getImageLink());
        message.setSenderProfile(profile);
        message.getArtifactMetaData().setArtifactOwnerObject(getCurrentUser());
//        message.getArtifactMetaData().getSenderProfile().setSenderDisplayName(displayName(getCurrentUser()));
        message.setPublishToTwitter(publishToTwitter);
        handleTwitterPublication(currentUser, message);

        message.setPublishToFacebook(publishToFacebook);
        handleFacebookPublication(currentUser, message);

        PrePublicationUtil.prePublish(message, prePublishedMessageCache);
        messageManager.publish(message);

        if (getView() == json) {
            return createResolutionWithJson(getContext(), toJSON(message));
        }
        return new ForwardResolution("/ajax/foo");
    }

    /**
     * @precondition message.setPublishToTwitter should have been called!
     */
    private void handleTwitterPublication(User currentUser, Message message) {
        if (!message.isPublishToTwitter()) {
            return;
        }
        message.setShortBody(twitterMessageBody);
        if (twitterPassword != null && twitterUsername != null) {
            ExternalCredentials cred = ExternalCredentials.buildExternalCredentials(TWITTER, currentUser.toEntityReference(),
                    twitterUsername, twitterPassword);
            message.setCreds(cred);

            if (twitterRemember) {
                userManager.saveExternalCredential(cred);
            }
        }
    }

    /**
     * @precondition message.handleFacebookPublication should have been called!
     */
    private void handleFacebookPublication(User currentUser, Message message) {
        if (!message.isPublishToFacebook()) {
            return;
        }

        logger.warn(MessageFormat.format("Facebook publication not supported. Sorry current user {0}", currentUser));
    }

    public Resolution publishMedia() throws JSONException, IOException {
        User currentUser = getCurrentUser();
        assert currentUser != null;

        try {
            mediaMetaData = PresentationUtil.buildMediaMetaData(fileData, currentUser, messageBody);
            mediaMetaData.setRecipientDepth(recipientDepth);
            mediaMetaData.getRecipients().add(new MediaRecipient(recipient, mediaMetaData));
            //set parent
            mediaMetaData.setParentActivity(ActivityReference.createActivityReference(parent));
            mediaMetaData = mediaManager.publishMedia(new MediaPublish<MediaMetaData>(mediaMetaData, buildByteArray(fileData)));
            getContext().getResponse().setHeader("Stripes-Success", "true");
            fileData.delete();

            if (getView() == json) {
                return createResolutionWithJson(getContext(), toJSON(mediaMetaData));
            }

            return new ForwardResolution("/ajax/dash/publishMediaResponse.jspf");
        }
        catch (IOException e) {
            logger.error("error saving upload", e);
            throw e;
        }
    }

    public Resolution publishOccasion() throws JSONException, IOException {
        User currentUser = getCurrentUser();
        assert currentUser != null;


        Activity activity = ActivityFactory.createActivity();
        activity.setRecipientDepth(recipientDepth);
        activity.setOwnerType(EntityType.OCCASION);
        activity.setActivityType(ActivityTypeEnum.OCCASION);
        activity.setArtifactType(ArtifactType.TEXT);
        activity.setComment(messageBody);
        activity.setArtifactOwner(currentUser.toEntityReference());
        ActivityReference parentReference = ActivityReference.createActivityReference(parent);
        activity.setParentActivity(parentReference);
        activityManager.publish(activity);
//        activity.getSenderProfile().setSenderDisplayName(displayName(getCurrentUser()));
        if (getView() == json) {
            return createResolutionWithJson(getContext(), toJSON(activity));
        }
        return new ForwardResolution("/ajax/foo");
    }

    public Resolution publishBase64Media() throws JSONException, IOException {

        mediaMetaData = buildMetaData(getCurrentUser(), messageBody);

        byte[] bytes = Base64.decode(base64);
        mediaMetaData.setSize(bytes.length / 1024L);
        mediaMetaData.setRecipientDepth(recipientDepth);
        //set parent
        mediaMetaData.setParentActivity(ActivityReference.createActivityReference(parent));
        mediaMetaData = mediaManager.publishMedia(new MediaPublish<MediaMetaData>(mediaMetaData, bytes));

        if (getView() == json) {
            return createResolutionWithJson(getContext(), toJSON(mediaMetaData));
        }

        return new ForwardResolution("/ajax/foo");
    }

    public Resolution deleteMessage() {
        PrePublicationUtil.flushMessage(guid, getCurrentUser(), prePublishedMessageCache);
        messageManager.deleteMessage(guid, getCurrentUser().getGuid());

        if (getView() == json) {
            return createResolutionWithText(getContext(), "success");
        }
        return new ForwardResolution("todo");
    }

    public Resolution deleteMedia() {
        mediaManager.deleteMediaMetaData(guid);

        if (getView() == json) {
            return createResolutionWithText(getContext(), "success");
        }
        return new ForwardResolution("todo");
    }

    @DontValidate
    public Resolution twitterCreds() {
        ExternalCredentials cred = userManager.loadExternalCredentialsForUserAndCredType(getCurrentUser().getGuid(),
                ExternalCredentials.CredentialType.TWITTER);

        if (cred != null) {
            JSONObject object = new JSONObject();
            try {
                object.put("username", cred.decryptUsername());
                object.put("password", cred.decryptPassword());
                return createResolutionWithJson(getContext(), object);

            } catch (JSONException e) {
                return new ErrorResolution(500, e.getMessage());
            }

        }

        return new ErrorResolution(404, "no twitter credentials");
    }


    protected MediaMetaData buildMetaData(IUser user, String comment) {

        MediaMetaData metaData = MediaMetaData.createMediaMetaData();
        populateMetaData(metaData, user, comment);
        return metaData;
    }

    public void setFileData(FileBean fileData) {
        this.fileData = fileData;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public NetworkTypeEnum getRecipientDepth() {
        return recipientDepth;
    }

    public void setRecipientDepth(NetworkTypeEnum recipientDepth) {
        this.recipientDepth = recipientDepth;
    }

    public EntityReference getParent() {
        return parent;
    }

    public void setParent(EntityReference parent) {
        this.parent = parent;
    }

    public EntityReference getRecipient() {
        return recipient;
    }

    public void setRecipient(EntityReference recipient) {
        this.recipient = recipient;
    }

    public FileBean getFileData() {
        return fileData;
    }

    public MediaMetaData getMediaMetaData() {
        return mediaMetaData;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public void setMediaMetaData(MediaMetaData mediaMetaData) {
        this.mediaMetaData = mediaMetaData;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public boolean isPublishToFacebook() {
        return publishToFacebook;
    }

    public void setPublishToFacebook(boolean publishToFacebook) {
        this.publishToFacebook = publishToFacebook;
    }

    public String getFacebookSessionId() {
        return facebookSessionId;
    }

    public void setFacebookSessionId(String facebookSessionId) {
        this.facebookSessionId = facebookSessionId;
    }

    public boolean isPublishToTwitter() {
        return publishToTwitter;
    }

    public void setPublishToTwitter(boolean publishToTwitter) {
        this.publishToTwitter = publishToTwitter;
    }

    public String getTwitterUsername() {
        return twitterUsername;
    }

    public void setTwitterUsername(String twitterUsername) {
        this.twitterUsername = twitterUsername;
    }

    public String getTwitterPassword() {
        return twitterPassword;
    }

    public void setTwitterPassword(String twitterPassword) {
        this.twitterPassword = twitterPassword;
    }

    public String getTwitterMessageBody() {
        return twitterMessageBody;
    }

    public void setTwitterMessageBody(String twitterMessageBody) {
        this.twitterMessageBody = twitterMessageBody;
    }

    public boolean isTwitterRemember() {
        return twitterRemember;
    }

    public void setTwitterRemember(boolean twitterRemember) {
        this.twitterRemember = twitterRemember;
    }
}

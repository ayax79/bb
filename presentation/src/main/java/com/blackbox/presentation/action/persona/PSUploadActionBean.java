/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */
package com.blackbox.presentation.action.persona;

import com.blackbox.foundation.Status;
import com.blackbox.foundation.Utils;
import com.blackbox.foundation.activity.ActivityProfile;
import com.blackbox.foundation.media.*;
import com.blackbox.foundation.media.MediaLibrary.MediaLibraryType;
import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.presentation.action.media.SessionImageActionBean;
import com.blackbox.presentation.action.util.AvatarCacheKey;
import com.blackbox.presentation.action.util.MediaUtil;
import com.blackbox.foundation.social.NetworkTypeEnum;
import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.User;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.cache.ICacheManager;
import org.yestech.publish.objectmodel.ArtifactType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.blackbox.foundation.EntityReference.createEntityReference;
import static com.blackbox.foundation.IBlackBoxConstants.BUFFER_SIZE;
import static com.blackbox.foundation.Utils.getCurrentDateTime;
import static com.blackbox.presentation.action.util.PresentationUtil.createResolutionWithJson;
import static com.blackbox.presentation.action.util.PresentationUtil.createResolutionWithText;
import static org.apache.commons.io.FileUtils.deleteDirectory;
import static org.apache.commons.io.FileUtils.openInputStream;

@UrlBinding("/action/ajax/psupload")
public class PSUploadActionBean extends BaseBlackBoxActionBean {

    private static final String MEDIA_LIB = "com.blackbox.presentation.action.MEDIA_LIB";
    private static final String MEDIA_LIB_ACTION = "com.blackbox.presentation.action.MEDIA_LIB_ACTION";
    private static final String MEDIA_LIB_ACTION_ADD = "com.blackbox.presentation.action.MEDIA_LIB_ACTION_ADD";
    private static final String ALBUM_PERMISSION = "com.blackbox.presentation.action.ALBUM_PERMISSION";
    private static final String SINGLE_PIC_PERMISSION = "com.blackbox.presentation.action.SINGLE_PIC_PERMISSION";

    final private static Logger _logger = LoggerFactory.getLogger(PSUploadActionBean.class);

    @SpringBean("avatarCache")
    ICacheManager<AvatarCacheKey, AvatarImage> avatarCache;
    @SpringBean("userManager")
    IUserManager userManager;
    @SpringBean("mediaManager")
    IMediaManager mediaManager;
    @SpringBean("uploadTempDir")
    File uploadTempImageDir;  //pulled from pom.xml
    @SpringBean("personaHelper")
    PersonaHelper personaHelper;


    private String existAlbumName;
    private String albumName;
    private String albumDesc;
    private String videoName;
    private String videoDesc;
    private List<MediaLibrary> mediaLibList;
    List<String> mediaLibCoverList;
    private MediaLibrary showingLib;
    public FileBean fileData;
    private int imageX;
    private int imageY;
    private int imageHeight;
    private int imageWidth;
    private int avatarX;
    private int avatarY;
    private int avatarW;
    private int avatarH;
    private boolean owner;
    private String albumGuid;

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    @DefaultHandler
    public Resolution defaultResolution() {
        _logger.warn("Default Resolution called.");
        return null;
    }

    @DontValidate
    public Resolution startPicture() {
        _logger.info("startMediaLib:" + albumName + "; desc:" + albumDesc + "; existAlbumName:" + existAlbumName);
        MediaLibrary ml = null;
        String permission = getContext().getRequest().getParameter("permission");
        if (permission == null) {
            permission = "WORLD";
        }
        getSession().setAttribute(ALBUM_PERMISSION, permission);
        if (existAlbumName != null && existAlbumName.trim().length() > 0 && !"-1".equals(existAlbumName)) {
            List<MediaLibrary> totalMediaLibList = mediaManager.loadMediaLibrariesForOwner(getCurrentUser().toEntityReference());
            ml = personaHelper.findMediaLibWithName(totalMediaLibList, existAlbumName, MediaLibraryType.IMAGE);
            if (ml != null) {
                ml.setNetworkTypeEnum(NetworkTypeEnum.valueOf(permission));
                getSession().setAttribute(MEDIA_LIB, ml);
                getSession().setAttribute(MEDIA_LIB_ACTION, MEDIA_LIB_ACTION_ADD);
            }
        } else {
            ml = MediaLibrary.createMediaLibrary();
            ml.setNetworkTypeEnum(NetworkTypeEnum.valueOf(permission));
            ml.setMedia(new ArrayList<MediaMetaData>());
            ml.setName(albumName);
            ml.setDescription(albumDesc);
            ml.setCreated(getCurrentDateTime());
            ml.setOwner(getCurrentUser().toEntityReference());
            ml.setStatus(Status.ENABLED);
            ml.setType(MediaLibraryType.IMAGE);
            ml.setVersion(null);
            Utils.applyGuid(ml);
            getSession().setAttribute(MEDIA_LIB, ml);
        }

        _logger.info("media lib guid:" + ml.getGuid());
        return createResolutionWithText(getContext(), "start");
    }

    public Resolution addPictureToAlbum() {
        _logger.info("calling uploadPicture");
        MediaLibrary ml = (MediaLibrary) getContext().getRequest().getSession().getAttribute(MEDIA_LIB);
        String permission = (String) getSession().getAttribute(ALBUM_PERMISSION);
        buildMetaData(ArtifactType.IMAGE, ml, permission, true);
        return createResolutionWithText(getContext(), "doing");
    }

    @DontValidate
    public Resolution uploadPicture() {
        _logger.info("calling uploadPicture");
        MediaLibrary ml = (MediaLibrary) getContext().getRequest().getSession().getAttribute(MEDIA_LIB);
        String permission = (String) getSession().getAttribute(ALBUM_PERMISSION);
        buildMetaData(ArtifactType.IMAGE, ml, permission, true);
        return createResolutionWithText(getContext(), "doing");
    }

    @DontValidate
    public Resolution uploadSinglePicture() {
        _logger.info("calling upload Single Picture");
        String permission = (String) getSession().getAttribute(SINGLE_PIC_PERMISSION);
        buildMetaData(ArtifactType.IMAGE, null, permission, false);
        return createResolutionWithText(getContext(), "doing");
    }

    @DontValidate
    public Resolution uploadPictureComplete() throws JSONException {
        _logger.info("calling uploadPicture complete");
        MediaLibrary ml = (MediaLibrary) getSession().getAttribute(MEDIA_LIB);
        if (ml != null) {
            //mediaManager.saveMediaLibrary(ml);  // where is this from? save is done in saveMediaLib
        } else {
            _logger.warn("No MediaLibrary was found in the flash scope.");
        }
        saveMediaLib();
        List<MediaLibrary> totalMediaLibList = mediaManager.loadMediaLibrariesForOwner(getCurrentUser().toEntityReference());
        List<MediaLibrary> mediaLibList = personaHelper.getMediaLibListByType(totalMediaLibList, MediaLibraryType.IMAGE);
        mediaLibCoverList = personaHelper.getMediaLibCoverList(mediaLibList);

        // This has to be set as a request attribute since it is set by more than one action
        getContext().getRequest().setAttribute("mediaLibList", mediaLibList);

        showingLib = personaHelper.getShowingLib(mediaLibList, 0);
        getSession().removeAttribute(MEDIA_LIB);
        getSession().removeAttribute(ALBUM_PERMISSION);
        setOwner(true);

        JSONObject object = new JSONObject();
        object.put("guid", ml.getGuid());

        return createResolutionWithJson(getContext(), object);
    }

    @DontValidate
    public Resolution singlePicPermissionOption() {
        _logger.info("calling singlePicPermissionOption");
        //HttpRequestUtil.logparms(getContext().getRequest());
        HttpServletRequest req = getContext().getRequest();
        if (req.getParameter("permissionVal") != null) {
            req.getSession().setAttribute(SINGLE_PIC_PERMISSION, req.getParameter("permissionVal"));
        }
        return new StreamingResolution("text", new StringReader("success"));
    }

    @DontValidate
    public Resolution uploadProfileImage() throws Exception {
        _logger.info("calling uploadProfileImage");
        try {
            personaHelper.resetSessionImage(getContext().getRequest().getSession(), fileData);
        } catch (IOException ex) {
            _logger.error(ex.getMessage());
        }
        getContext().getResponse().setHeader("Stripes-Success", "true");
        return new StreamingResolution("text", new StringReader("success"));
    }

    @DontValidate
    public Resolution cropImage() {
        MediaMetaData profileImage = mediaManager.loadProfileMetaMediaData(getCurrentUser().toEntityReference());
        MediaMetaData avatarImage = mediaManager.loadAvatarMetaMediaData(getCurrentUser().toEntityReference());
        final String uploadFullPath = getUploadTempImageDir().getAbsolutePath() + File.separator + Utils.generateGuid();

        fileData = (FileBean) getSession().getAttribute(SessionImageActionBean.SESSION_IMAGE_PARAM);
        String location = null;
        String avatarLocation = null;
        if (profileImage != null) {
            mediaManager.deleteMediaMetaData(profileImage.getGuid());
        }
        if (avatarImage != null) {
            mediaManager.deleteMediaMetaData(avatarImage.getGuid());
        }
        String fileName = fileData.getFileName().replace(" ", "_");
        profileImage = MediaUtil.prepareBuildMetaData(ArtifactType.IMAGE, fileName, false, fileData);
        avatarImage = MediaUtil.prepareBuildMetaData(ArtifactType.IMAGE, "avatar_" + fileName, false, fileData);
        profileImage.setArtifactOwner(createEntityReference(getCurrentUser()));
        avatarImage.setArtifactOwner(createEntityReference(getCurrentUser()));
        avatarImage.setProfile(false);
        avatarImage.setAvatar(true);
        profileImage.setProfile(true);
        avatarImage.setRecipientDepth(NetworkTypeEnum.SELF);
        profileImage.setRecipientDepth(NetworkTypeEnum.SELF);
        avatarImage.getRecipients().add(new MediaRecipient(createEntityReference(getCurrentUser()), avatarImage));
        profileImage.getRecipients().add(new MediaRecipient(createEntityReference(getCurrentUser()), profileImage));
        avatarImage.setSenderProfile(new ActivityProfile());
        profileImage.setSenderProfile(new ActivityProfile());
        File imageFile = null;
        File avatarFile = null;
        InputStream image = null;
        InputStream avatarImageStream = null;
        try {
            //crop profile image
            if (!(imageX == 0 && imageY == 0 && imageWidth == 0 && imageHeight == 0)) {
                imageFile = personaHelper.getCroppedImage(fileData, uploadFullPath, imageX, imageY, imageWidth, imageHeight);
                image = new BufferedInputStream(openInputStream(imageFile), BUFFER_SIZE);
            } else {
                image = fileData.getInputStream();
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            IOUtils.copy(image, out);
            byte[] bytes = out.toByteArray();
            location = mediaManager.publishMedia(new MediaPublish(profileImage, bytes)).getLocation();
            _logger.info("profileImage:" + location);
            //crop avatar image
            if (!(avatarX == 0 && avatarY == 0 && avatarW == 0 && avatarY == 0)) {
                avatarFile = personaHelper.getCroppedImage(fileData, uploadFullPath, avatarX, avatarY, avatarW, avatarH);
                avatarImageStream = new BufferedInputStream(openInputStream(avatarFile), BUFFER_SIZE);
            } else {
                avatarImageStream = fileData.getInputStream();
            }
            avatarLocation = mediaManager.publishMedia(new MediaPublish(avatarImage, bytes)).getLocation();
            _logger.info("avatarImage:" + avatarLocation);
        } catch (Exception e) {
            _logger.error("error", e);
        } finally {
            try {
                deleteDirectory(new File(uploadFullPath));
            } catch (Exception e) {
                _logger.error("error cleaning up: " + uploadFullPath);
            }
            FileBean old = (FileBean) getSession().getAttribute(SessionImageActionBean.SESSION_IMAGE_PARAM);
            if (old != null) {
                try {
                    old.delete();
                }
                catch (Exception e) {
                    _logger.error("error cleaning up", e);
                }
            }
            getSession().removeAttribute(SessionImageActionBean.SESSION_IMAGE_PARAM);
        }
        User currentUser = getContext().getUser();
        currentUser.getProfile().setProfileImgUrl(location);
        currentUser.getProfile().setAvatarUrl(avatarLocation);
        avatarCache.flushAll();

        User tempU = userManager.loadUserByGuid(currentUser.getGuid());
        tempU.getProfile().setProfileImgUrl(location);
        tempU.getProfile().setAvatarUrl(avatarLocation);
        _logger.info("before save: imgUrl :" + location + "; avatar:" + avatarLocation);
        userManager.save(tempU);
        //User newTmpU = userManager.loadUserByGuid(getContext().getUser().getGuid());
        //_logger.info("after save: imgUrl :" + newTmpU.getProfile().getProfileImgUrl() + "; avatar:" + newTmpU.getProfile().getAvatarUrl());

        personaHelper.flushPersonaPageCache(getContext());
        return createResolutionWithText(getContext(), location);
    }

    protected void buildMetaData(ArtifactType type, MediaLibrary mediaLib, String permission, boolean forLib) {
        _logger.info("calling buildMetaData:" + fileData.getContentType() + "; Name:" + fileData.getFileName() + "; size:" + fileData.getSize());
        String fileName = fileData.getFileName().replace(" ", "_");
        MediaMetaData metaData = MediaUtil.prepareBuildMetaData(type, fileName, true, fileData);
        if (mediaLib == null && forLib == false) {
            metaData.setLibrary(false);
            metaData.setLoosePic(true);
        }
        metaData.setArtifactOwner(createEntityReference(getCurrentUser()));
        if (permission != null) {
            try {
                metaData.setRecipientDepth(NetworkTypeEnum.valueOf(permission));
            } catch (Exception e) {
                _logger.error("error at assign permission to media", e);
            }
        } else {
            metaData.setRecipientDepth(NetworkTypeEnum.WORLD);
        }
        MediaLibrary ml = (MediaLibrary) getSession().getAttribute(MEDIA_LIB);

        try {
            metaData.setAspect(MediaUtil.getAspect(fileData.getInputStream()));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            IOUtils.copy(fileData.getInputStream(), out);
            metaData = mediaManager.publishMedia(new MediaPublish(metaData, out.toByteArray()));
            if (mediaLib != null) {
                ml.getMedia().add(metaData);
            }
        } catch (Exception ex) {
            _logger.error("mediaMgr publish throws Exception", ex);
        }
        personaHelper.flushPersonaPageCache(getContext());
    }

    private void saveMediaLib() {
        _logger.info("save media lib");
        MediaLibrary ml = (MediaLibrary) getSession().getAttribute(MEDIA_LIB);
        mediaManager.saveMediaLibrary(ml);
        personaHelper.flushPersonaPageCache(getContext());
        clearMediaSession();
    }

    protected HttpSession getSession() {
        return getContext().getRequest().getSession();
    }

    private void clearMediaSession() {
        HttpSession ses = getContext().getRequest().getSession();
        ses.removeAttribute(MEDIA_LIB);
    }

    public IMediaManager getMediaManager() {
        return mediaManager;
    }

    public void setMediaManager(IMediaManager mediaManager) {
        this.mediaManager = mediaManager;
    }

    public String getAlbumGuid() {
        return albumGuid;
    }

    public void setAlbumGuid(String albumGuid) {
        this.albumGuid = albumGuid;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumDesc() {
        return albumDesc;
    }

    public void setAlbumDesc(String albumDesc) {
        this.albumDesc = albumDesc;
    }

    public void setFileData(FileBean fileData) {
        this.fileData = fileData;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoDesc() {
        return videoDesc;
    }

    public void setVideoDesc(String videoDesc) {
        this.videoDesc = videoDesc;
    }

    public MediaLibrary getShowingLib() {
        return showingLib;
    }

    public File getUploadTempImageDir() {
        return uploadTempImageDir;
    }

    public void setImageX(int imageX) {
        this.imageX = imageX;
    }

    public void setImageY(int imageY) {
        this.imageY = imageY;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setUploadTempImageDir(File uploadTempImageDir) {
        this.uploadTempImageDir = uploadTempImageDir;
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }

    public IMediaManager getContentManager() {
        return mediaManager;
    }

    public void setContentManager(IMediaManager mediaManager) {
        this.mediaManager = mediaManager;
    }

    public List<String> getMediaLibCoverList() {
        return mediaLibCoverList;
    }

    public void setMediaLibCoverList(List<String> mediaLibCoverList) {
        this.mediaLibCoverList = mediaLibCoverList;
    }

    public List<MediaLibrary> getMediaLibList() {
        return mediaLibList;
    }

    public void setMediaLibList(List<MediaLibrary> mediaLibList) {
        this.mediaLibList = mediaLibList;
    }

    public String getExistAlbumName() {
        return existAlbumName;
    }

    public void setExistAlbumName(String existAlbumName) {
        this.existAlbumName = existAlbumName;
    }

    public int getAvatarH() {
        return avatarH;
    }

    public void setAvatarH(int avatarH) {
        this.avatarH = avatarH;
    }

    public int getAvatarW() {
        return avatarW;
    }

    public void setAvatarW(int avatarW) {
        this.avatarW = avatarW;
    }

    public int getAvatarX() {
        return avatarX;
    }

    public void setAvatarX(int avatarX) {
        this.avatarX = avatarX;
    }

    public int getAvatarY() {
        return avatarY;
    }

    public void setAvatarY(int avatarY) {
        this.avatarY = avatarY;
    }
}

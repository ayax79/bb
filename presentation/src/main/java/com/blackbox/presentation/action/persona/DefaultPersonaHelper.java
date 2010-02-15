/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */
package com.blackbox.presentation.action.persona;

import com.blackbox.EntityReference;
import com.blackbox.Status;
import com.blackbox.Utils;
import com.blackbox.activity.ActivityFactory;
import com.blackbox.media.IMediaManager;
import com.blackbox.media.MediaLibrary;
import com.blackbox.media.MediaLibrary.MediaLibraryType;
import com.blackbox.media.MediaMetaData;
import com.blackbox.media.MediaPublish;
import com.blackbox.presentation.action.media.SessionImageActionBean;
import com.blackbox.presentation.action.util.PresentationUtil;
import com.blackbox.presentation.extension.BlackBoxContext;
import com.blackbox.social.NetworkTypeEnum;
import com.blackbox.user.IUser;
import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.web.ServletCacheAdministrator;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.FileBean;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yestech.publish.objectmodel.ArtifactType;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.blackbox.IBlackBoxConstants.BUFFER_SIZE;
import static com.blackbox.Utils.getCurrentDateTime;
import static org.apache.commons.io.FileUtils.openOutputStream;
import static org.apache.commons.io.FilenameUtils.getName;
import static org.yestech.multimedia.image.jdk.ImageUtil.crop;

@Component("personaHelper")
public class DefaultPersonaHelper implements PersonaHelper {

    private final static Logger _logger = LoggerFactory.getLogger(DefaultPersonaHelper.class);

    @Override
    public List<MediaLibrary> getMediaLibListByType(List<MediaLibrary> totalMediaLibList, MediaLibraryType type) {

        _logger.info("load media libListBy UserType:" + type);

        if (totalMediaLibList == null) {
            return null;
        }

        List<MediaLibrary> rtnList = new ArrayList<MediaLibrary>();
        for (MediaLibrary medlib : totalMediaLibList) {
            if (type.equals(medlib.getType())) {
                rtnList.add(medlib);
            }
        }
        return rtnList;
    }

    @Override
    public MediaLibrary getCorkboardLib(List<MediaLibrary> totalMediaLibList) {
        List<MediaLibrary> corkboardList = getMediaLibListByType(totalMediaLibList, MediaLibraryType.CORK_BOARD);
        if (corkboardList == null || corkboardList.size() == 0) {
            return null;
        }

        return corkboardList.get(0);
    }

    @Override
    public List<String> getMediaLibCoverList(List<MediaLibrary> mediaLibList) {
        List<String> rtnList = new ArrayList<String>();
        for (MediaLibrary medlib : mediaLibList) {

            if (medlib.getMedia() == null || medlib.getMedia().size() == 0 || medlib.getMedia().get(0).getLocation() == null || medlib.getMedia().get(0).getLocation().trim().length() == 0) {
                rtnList.add("/library/images/persona/pic7.png");
            } else {
                for (int i = 0; i < medlib.getMedia().size(); i++) {
                    MediaMetaData mmd = medlib.getMedia().get(i);
                    //_logger.info("name:" + medlib.getName() + "; index:" + i + "; location:" + mmd.getLocation());
                }
                rtnList.add(medlib.getMedia().get(0).getLocation());
            }
        }
        for (String cover : rtnList) {
            _logger.info("cover list is:" + cover);
        }
        return rtnList;
    }

    @Override
    public MediaLibrary getShowingLib(List<MediaLibrary> mediaLibList, int index) {
        if (index == -1) {
            index = mediaLibList.size() - 1;
        }
        if (mediaLibList.size() == 0) {
            _logger.warn("mediaLibList size is 0");
            return null;
        }
        MediaLibrary ml = mediaLibList.get(index);
        _logger.info("index:" + index + "; size:" + (ml.getMedia() != null ? ml.getMedia().size() : 0));
        return ml;
    }

    @Override
    public File getCroppedImage(FileBean fileBean, String uploadFullPath, int imageX, int imageY, int imageWidth, int imageHeight) throws IOException {
        String fileName = StringUtils.replace(getName(fileBean.getFileName()), " ", "_");
        File dir = new File(uploadFullPath);
        if (!dir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        }
        String croppedImageFile = uploadFullPath + File.separator + Utils.generateGuid() + fileName;
        OutputStream out = new BufferedOutputStream(openOutputStream(new File(croppedImageFile)), BUFFER_SIZE);
        InputStream in = fileBean.getInputStream();
        String contentType = fileName.substring(fileName.indexOf('.') + 1);
        crop(contentType, in, out, imageX, imageY, imageWidth, imageHeight);
        out.flush();
        out.close();
        in.close();
        //fileBean.delete();
        File croppedImage = new File(croppedImageFile);
        return croppedImage;
    }

    @Override
    public MediaLibrary createDefaultCorkboard(IUser user, IMediaManager mediaManager, boolean isOwner) {
        MediaLibrary corkboard = MediaLibrary.createMediaLibrary();
        List<MediaMetaData> mediaList = new ArrayList<MediaMetaData>(9);

        for (int i = 0; i < 9; i++) {
            StringBuilder picBuilder = new StringBuilder();
            picBuilder.append("/library/images/persona/cor1_pic").append(i + 1).append(".png");

            MediaMetaData metaData = MediaMetaData.createMediaMetaData();
            metaData.setLibrary(true);
            metaData.setArtifactType(ArtifactType.IMAGE);
            metaData.setArtifactOwner(EntityReference.createEntityReference(user));
            metaData.setFileName(picBuilder.toString());
            metaData.setMimeType("image/png");
            metaData.setSize(100);
            metaData.setProfile(false);
            metaData.setRecipientDepth(NetworkTypeEnum.SELF);
            metaData.setLocation(picBuilder.toString());
            if (isOwner) {
                metaData = mediaManager.save(metaData);
            }
            mediaList.add(metaData);
        }
        corkboard.setMedia(mediaList);
        corkboard.setName(CORKBOARDNAME);
        corkboard.setDescription("The Default Corkboard");
        corkboard.setCreated(new DateTime());
        corkboard.setOwner(user.toEntityReference());
        corkboard.setStatus(Status.ENABLED);
        corkboard.setType(MediaLibraryType.CORK_BOARD);
        if (isOwner) {
            corkboard = mediaManager.saveMediaLibrary(corkboard);
        }

        return corkboard;
    }

    @Override
    public InputStream getLocalMediaStream(String location) {
        InputStream fileStream = null;
        try {
            _logger.info("location is:" + location);
            fileStream = new FileInputStream(new File(location));
        } catch (FileNotFoundException ex) {
            _logger.error("not found image file from " + location);
        }
        return fileStream;
    }

    @Override
    public MediaLibrary saveVideo(FileBean fileData, IUser curUser, IMediaManager mediaManager, String name, String desc) {
        _logger.info("saveVideo:" + name + "; desc:" + desc);
        MediaLibrary ml = MediaLibrary.createMediaLibrary();
        ml.setMedia(new ArrayList<MediaMetaData>());
        ml.setName(name);
        ml.setDescription(desc);
        ml.setCreated(getCurrentDateTime());
        ml.setOwner(curUser.toEntityReference());
        ml.setStatus(Status.ENABLED);
        ml.setType(MediaLibraryType.VIDEO);
        _logger.info(fileData.getFileName());
        _logger.info("now create metaData:" + fileData.getContentType() + "; Name:" + fileData.getFileName() + "; size:" + fileData.getSize());
        MediaMetaData metaData = ActivityFactory.createMedia();
        metaData.setLibrary(true);
        metaData.setArtifactOwner(EntityReference.createEntityReference(curUser));
        metaData.setMimeType(getMediaContentType(fileData));
        metaData.setArtifactType(ArtifactType.VIDEO);
        metaData.setFileName(fileData.getFileName().replace(" ", "_"));
        metaData.setMimeType(getMediaContentType(fileData));
        metaData.setSize(fileData.getSize());
        metaData.setRecipientDepth(NetworkTypeEnum.SELF);

        try {
            metaData = mediaManager.publishMedia(new MediaPublish(metaData, PresentationUtil.buildByteArray(fileData)));
            mediaManager.saveMediaLibrary(ml);
        } catch (Exception ex) {
            _logger.error("exception:", ex);
        }
        _logger.info("done on saveVideo");
        return ml;
    }

    @Override
    public MediaLibrary findMediaLibWithName(List<MediaLibrary> totalMediaLibList, String libName, MediaLibraryType type) {
        for (MediaLibrary ml : totalMediaLibList) {
            if (libName.equals(ml.getName()) && type == ml.getType()) {
                return ml;
            }
        }
        return null;

    }

    @Override
    public String getMediaContentType(FileBean fileData) {
        String rtnType = "image/jpg";
        String ext = fileData.getFileName().substring(fileData.getFileName().lastIndexOf(".") + 1);
        if ("png".equalsIgnoreCase(ext)) {
            rtnType = "image/png";
        } else if ("gif".equalsIgnoreCase(ext)) {
            rtnType = "image/gif";
        } else if ("jpeg".equalsIgnoreCase(ext) || "jpg".equalsIgnoreCase(ext)) {
            rtnType = "image/jpeg";
        } else if ("png".equalsIgnoreCase(ext)) {
            rtnType = "image/png";
        } else if ("mp4".equalsIgnoreCase(ext)) {
            rtnType = "video/mp4";
        } else if ("mpg".equalsIgnoreCase(ext) || "mpeg".equalsIgnoreCase(ext)) {
            rtnType = "video/mpeg";
        } else if ("mov".equalsIgnoreCase(ext)) {
            rtnType = "video/quicktime";
        } else {
            _logger.error("unknown type:" + fileData.getFileName() + "; type:" + ext);
        }
        return rtnType;
    }

    @Override
    public void resetSessionImage(HttpSession session, FileBean fileData) throws Exception {
        FileBean old = (FileBean) session.getAttribute(SessionImageActionBean.SESSION_IMAGE_PARAM);
        if (old != null) {
            old.delete();
        }
        session.setAttribute(SessionImageActionBean.SESSION_IMAGE_PARAM, fileData);
    }


    @Override
    public void flushPersonaPageCache(ActionBeanContext context) {
        HttpServletRequest request = context.getRequest();
        ServletContext servletContext = request.getSession().getServletContext();

        Cache cache = ServletCacheAdministrator.getInstance(servletContext).getCache(request, PageContext.SESSION_SCOPE);
        cache.flushGroup("persona-header");
        cache.flushGroup("persona-body");
    }

    @Override
    public boolean containsPersonaPageCache(BlackBoxContext context, String username) {
        HttpServletRequest request = context.getRequest();
        ServletContext servletContext = request.getSession().getServletContext();
        String key = "persona-" + username;
        ServletCacheAdministrator cacheAdministrator = ServletCacheAdministrator.getInstance(servletContext);
        key = cacheAdministrator.generateEntryKey(key, request, PageContext.SESSION_SCOPE, null, null);
        Cache cache = cacheAdministrator.getCache(request, PageContext.SESSION_SCOPE);

        try {

            if (request.getParameter("refresh") != null || username.equals(context.getUser().getUsername())) {
                flushPersonaPageCache(context);
                return false;
            }

            Object value = cache.getFromCache(key, 300) != null;
            return value != null;
        } catch (NeedsRefreshException e) {
            flushPersonaPageCache(context);
            return false;
        }
        finally {
            cache.cancelUpdate(key);
        }
    }

}

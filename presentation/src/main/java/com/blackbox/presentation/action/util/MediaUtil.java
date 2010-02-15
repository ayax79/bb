package com.blackbox.presentation.action.util;

import com.blackbox.EntityReference;
import com.blackbox.Utils;
import com.blackbox.activity.ActivityFactory;
import com.blackbox.media.IMediaManager;
import com.blackbox.media.MediaLibrary;
import com.blackbox.media.MediaLibrary.MediaLibraryType;
import com.blackbox.media.MediaMetaData;
import com.blackbox.media.MediaPublish;
import com.blackbox.social.NetworkTypeEnum;
import com.blackbox.user.User;
import net.sourceforge.stripes.action.FileBean;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.publish.objectmodel.ArtifactType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.blackbox.IBlackBoxConstants.BUFFER_SIZE;
import static com.blackbox.presentation.action.util.JspFunctions.isBlocked;
import static com.blackbox.presentation.action.util.JspFunctions.isFriend;
import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.io.FileUtils.openOutputStream;
import static org.apache.commons.io.FilenameUtils.getName;
import static org.yestech.multimedia.image.jdk.ImageUtil.crop;


public class MediaUtil {
    final private static Logger _logger = LoggerFactory.getLogger(MediaUtil.class);

    public static List<MediaLibrary> getMediaLibListByType(List<MediaLibrary> totalMediaLibList, MediaLibraryType type) {
        if (totalMediaLibList == null) {
            return null;
        }
        List<MediaLibrary> rtnList = new ArrayList<MediaLibrary>();
        for (MediaLibrary medlib : totalMediaLibList) {
            if (medlib.getMedia() == null || medlib.getMedia().size() == 0) {
                continue;   //get rid of empty lib 
            }
            if (type.equals(medlib.getType())) {
                rtnList.add(medlib);
            }
        }
        return rtnList;
    }

    public static MediaLibrary getCorkboardLib(List<MediaLibrary> totalMediaLibList) {
        List<MediaLibrary> corkboardList = getMediaLibListByType(totalMediaLibList, MediaLibraryType.CORK_BOARD);
        if (corkboardList == null || corkboardList.size() == 0) {
            return null;
        }

        return corkboardList.get(0);
    }

    public static List<String> getMediaLibCoverList(List<MediaLibrary> mediaLibList) {
        List<String> rtnList = new ArrayList<String>();
        for (MediaLibrary medlib : mediaLibList) {

            if (medlib.isHide()) {
                rtnList.add("/library/images/private-logo.png");
            }
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

    public static MediaLibrary getShowingLib(List<MediaLibrary> mediaLibList, int index) {
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

    public static File getCroppedImage(FileBean fileBean, String uploadFullPath, int imageX, int imageY, int imageWidth, int imageHeight) throws IOException {
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
        File croppedImage = new File(croppedImageFile);
        return croppedImage;
    }

    //for event
    public static File getCroppedImage(String name, InputStream in, String uploadFullPath, int imageX, int imageY, int imageWidth, int imageHeight) throws IOException {
        String fileName = StringUtils.remove(name, "_");
        File dir = new File(uploadFullPath);
        if (!dir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        }
        String croppedImageFile = uploadFullPath + File.separator + Utils.generateGuid() + fileName;
        OutputStream out = new BufferedOutputStream(openOutputStream(new File(croppedImageFile)), BUFFER_SIZE);
        String contentType = StringUtils.remove(fileName.substring(fileName.indexOf('.') + 1), "_");
        crop(contentType, in, out, imageX, imageY, imageWidth, imageHeight);
        out.flush();
        out.close();
        File croppedImage = new File(croppedImageFile);
        return croppedImage;
    }

    public static String getMediaContentType(String fileName) {
        String rtnType = "image/jpg";
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
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
            _logger.error("unknown type:" + fileName + "; type:" + ext);
        }
        return rtnType;

    }

    public static String getMediaContentType(FileBean fileData) {
        String fileName = fileData.getFileName();
        return getMediaContentType(fileName);
    }

    public static MediaMetaData prepareBuildMetaData(ArtifactType type, String name, boolean library, FileBean fileData) {
        MediaMetaData metaData = ActivityFactory.createMedia();
        if (library) {
            metaData.setLibrary(true);
        } else {
            metaData.setLibrary(false);
        }
        metaData.setMimeType(getMediaContentType(fileData));
        metaData.setArtifactType(type);
        metaData.setFileName(name);
        metaData.setSize(fileData.getSize());
        return metaData;
    }

    public static MediaMetaData buildMetaData(IMediaManager mediaManager, FileBean fileData, EntityReference er, ArtifactType type, boolean isPartOfLib) {
        MediaMetaData metaData = prepareBuildMetaData(type, fileData.getFileName().replace(" ", "_"), isPartOfLib, fileData);
        metaData.setArtifactOwner(EntityReference.createEntityReference(er));
        metaData.setRecipientDepth(NetworkTypeEnum.SELF);
        try {
            metaData.setAspect(MediaUtil.getAspect(fileData.getInputStream()));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            IOUtils.copy(fileData.getInputStream(), out);
            metaData = mediaManager.publishMedia(new MediaPublish(metaData, out.toByteArray()));
            return metaData;
        } catch (Exception ex) {
            _logger.error("mediaMgr publish throws Exception", ex);
            return null;
        }
    }

    public static List<MediaLibrary> getMediaLibListBasedOnNetwork(List<MediaLibrary> mediaLibList, User libraryOwner, User viewer) {
        List<MediaLibrary> libraryCollection = newArrayList();
        if (libraryOwner.getGuid().equals(viewer.getGuid())) {
            return mediaLibList;
        } else if (!isBlocked(libraryOwner)) {
            for (MediaLibrary mediaLibrary : mediaLibList) {
                NetworkTypeEnum recipientDepth = mediaLibrary.getNetworkTypeEnum();

                if (isFriend(libraryOwner) && NetworkTypeEnum.FRIENDS.equals(recipientDepth)) {
                    updateMediaLibBasedOnNetwork(mediaLibrary, libraryOwner, viewer);
                    libraryCollection.add(mediaLibrary);
                } else if (viewer != null && NetworkTypeEnum.ALL_MEMBERS.equals(recipientDepth)) {
                    updateMediaLibBasedOnNetwork(mediaLibrary, libraryOwner, viewer);
                    libraryCollection.add(mediaLibrary);
                } else if (NetworkTypeEnum.WORLD.equals(recipientDepth)) {
                    updateMediaLibBasedOnNetwork(mediaLibrary, libraryOwner, viewer);
                    libraryCollection.add(mediaLibrary);
                }
            }
        }

        return libraryCollection;
    }

    public static void updateMediaLibBasedOnNetwork(MediaLibrary currentAlbum, User libraryOwner, User viewer) {
        if (!libraryOwner.getGuid().equals(viewer.getGuid())) {
            List<MediaMetaData> scrubbedList = newArrayList();
            List<MediaMetaData> mediaMetaDataList = currentAlbum.getMedia();
            if (mediaMetaDataList != null) {
                for (MediaMetaData mediaMetaData : mediaMetaDataList) {
                    NetworkTypeEnum recipientDepth = mediaMetaData.getRecipientDepth();

                    if (isFriend(libraryOwner) && NetworkTypeEnum.FRIENDS.equals(recipientDepth)) {
                        scrubbedList.add(mediaMetaData);
                    } else if (viewer != null && NetworkTypeEnum.ALL_MEMBERS.equals(recipientDepth)) {
                        scrubbedList.add(mediaMetaData);
                    } else if (NetworkTypeEnum.WORLD.equals(recipientDepth)) {
                        scrubbedList.add(mediaMetaData);
                    }
                }
                currentAlbum.setMedia(scrubbedList);
            }
        }
    }

    /*public static MediaLibrary createFakePrivMediaLib(MediaLibrary realLib) {
        realLib.setIsFake(true);
        return realLib;
        MediaLibrary fakeLib = MediaLibrary.saveMediaLibrary();
        fakeLib.setGuid("-1");
        fakeLib.setCreated(realLib.getCreated());
        fakeLib.setName(realLib.getName());
        fakeLib.setDescription(realLib.getDescription());
        fakeLib.setMedia(new ArrayList<MediaMetaData>());
        fakeLib.setIsFake(true);
        return fakeLib;
    }
    */

    public static MediaMetaData buildMetaDataMock(IMediaManager mediaManager, String fileLoc, EntityReference er, ArtifactType type, boolean isPartOfLib) {
        MediaMetaData metaData = ActivityFactory.createMedia();
        metaData.setArtifactOwner(er);
        metaData.setLibrary(isPartOfLib);
        metaData.setProfile(false);
        metaData.setArtifactType(type);
        metaData.setMimeType(MediaUtil.getMediaContentType(fileLoc));
        metaData.setSize(100);
        String fileName = fileLoc;
        if (fileLoc.lastIndexOf("/") >= 0) {
            fileName = fileLoc.substring(fileLoc.lastIndexOf("/"));
        }
        metaData.setFileName(fileName.replace(" ", "_"));
        metaData.setLocation(fileLoc);
        metaData.setRecipientDepth(NetworkTypeEnum.WORLD);
        metaData = mediaManager.save(metaData);
        return metaData;
    }

    public static double getAspect(InputStream in) {
        double rtnVal = 1.0;
        try {
            BufferedImage bufferedImage = ImageIO.read(in);
            int height = bufferedImage.getHeight();
            int width = bufferedImage.getWidth();
            rtnVal = (1.0 * width) / height;
        }
        catch (Exception ex) {
            _logger.info("getAspect failed:", ex);
        }
        return rtnVal;
    }
}

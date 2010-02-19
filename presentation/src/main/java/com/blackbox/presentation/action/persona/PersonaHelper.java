package com.blackbox.presentation.action.persona;

import com.blackbox.foundation.media.IMediaManager;
import com.blackbox.foundation.media.MediaLibrary;
import com.blackbox.presentation.extension.BlackBoxContext;
import com.blackbox.foundation.user.IUser;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.FileBean;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface PersonaHelper {
    String CORKBOARDNAME = "CORKBOARD";

    List<MediaLibrary> getMediaLibListByType(List<MediaLibrary> totalMediaLibList, MediaLibrary.MediaLibraryType type);

    MediaLibrary getCorkboardLib(List<MediaLibrary> totalMediaLibList);

    List<String> getMediaLibCoverList(List<MediaLibrary> mediaLibList);

    MediaLibrary getShowingLib(List<MediaLibrary> mediaLibList, int index);

    File getCroppedImage(FileBean fileBean, String uploadFullPath, int imageX, int imageY, int imageWidth, int imageHeight) throws IOException;

    MediaLibrary createDefaultCorkboard(IUser user, IMediaManager mediaManager, boolean isOwner);

    InputStream getLocalMediaStream(String location);

    MediaLibrary saveVideo(FileBean fileData, IUser curUser, IMediaManager mediaManager, String name, String desc);

    MediaLibrary findMediaLibWithName(List<MediaLibrary> totalMediaLibList, String libName, MediaLibrary.MediaLibraryType type);

    String getMediaContentType(FileBean fileData);

    void resetSessionImage(HttpSession session, FileBean fileData) throws Exception;

}

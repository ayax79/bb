/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.service.media;

import com.blackbox.EntityReference;
import com.blackbox.activity.ActivityFactory;
import com.blackbox.media.IMediaManager;
import com.blackbox.media.MediaMetaData;
import com.blackbox.media.MediaPublish;
import com.blackbox.user.User;
import com.blackbox.util.InputStreamDataSource;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.yestech.lib.crypto.MessageDigestUtils;
import org.yestech.publish.objectmodel.ArtifactType;

import javax.activation.DataSource;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import java.io.*;
import java.util.List;
import java.util.UUID;

import static com.blackbox.IBlackBoxConstants.BUFFER_SIZE;
import static org.apache.commons.fileupload.servlet.ServletFileUpload.isMultipartContent;
import static org.apache.commons.io.FilenameUtils.getName;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@Service
public class MediaPublishService implements IMediaPublishService {
    final private static Logger logger = LoggerFactory.getLogger(MediaPublishService.class);

    private File fileStoreDirectory;
    private IMediaManager mediaManager;
    private int maxMemoryFileSize = 0;

    public int getMaxMemoryFileSize() {
        return maxMemoryFileSize;
    }

    public void setMaxMemoryFileSize(int maxMemoryFileSize) {
        this.maxMemoryFileSize = maxMemoryFileSize;
    }

    public IMediaManager getMediaManager() {
        return mediaManager;
    }

    @Resource(name = "mediaManager")
    public void setMediaManager(IMediaManager mediaManager) {
        this.mediaManager = mediaManager;
    }

    public File getFileStoreDirectory() {
        return fileStoreDirectory;
    }

    public void setFileStoreDirectory(File fileStoreDirectory) {
        this.fileStoreDirectory = fileStoreDirectory;
    }

    @Override
    public String image(@PathParam("session") String session, @PathParam("guid") String guid, @Context HttpServletRequest request) {
        return processUpload(request, ArtifactType.IMAGE);
    }

    @SuppressWarnings("unchecked")
    protected String processUpload(HttpServletRequest request, ArtifactType artifactType) {
        String result = "";
        if (isUpload(request)) {
            if (!fileStoreDirectory.exists()) {
                fileStoreDirectory.mkdirs();
            }
            User user = (User) SecurityUtils.getSubject().getPrincipal();

            // Create a factory for disk-based file items
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // Set factory constraints
            factory.setSizeThreshold(maxMemoryFileSize);
            factory.setRepository(fileStoreDirectory);

            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);

            // Parse the request
            try {
                List<FileItem> items = upload.parseRequest(request);
                for (FileItem item : items) {
                    MediaMetaData metaData = ActivityFactory.createMedia();
                    metaData.setArtifactOwner(EntityReference.createEntityReference(user));
                    String fileName = StringUtils.replace(getName(item.getName()), " ", "_");
                    metaData.setFileName(fileName);
                    metaData.setArtifactType(artifactType);
                    metaData.setSize(item.getSize());
                    metaData.setMimeType(item.getContentType());
                    InputStream image = new BufferedInputStream(item.getInputStream(), BUFFER_SIZE);
                    DataSource ds = new InputStreamDataSource(fileName, item.getContentType(), image);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    IOUtils.copy(item.getInputStream(), out);

                    mediaManager.publishMedia(new MediaPublish(metaData, out.toByteArray()));
                }
                result = generateUniqueId();
            } catch (FileUploadException e) {
                logger.error("error during upload...", e);
                result = "error";
            } catch (IOException e) {
                logger.error("error during upload...", e);
                result = "error";
            }
        }
        return result;
    }

    private boolean isUpload(HttpServletRequest request) {
        return isMultipartContent(request);
    }

    private String generateUniqueId() {
        return MessageDigestUtils.sha1Hash(UUID.randomUUID().toString());
    }

    @Override
    public String video(@PathParam("session") String session, @PathParam("guid") String guid, @Context HttpServletRequest request) {
        return processUpload(request, ArtifactType.VIDEO);
    }

    @Override
    public String audio(@PathParam("session") String session, @PathParam("guid") String guid, @Context HttpServletRequest request) {
        return processUpload(request, ArtifactType.AUDIO);
    }
}

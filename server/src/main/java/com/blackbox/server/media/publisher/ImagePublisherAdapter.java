package com.blackbox.server.media.publisher;

import com.blackbox.media.MediaMetaData;
import static com.blackbox.server.util.MediaPublishUtil.buildThumbnailUniqueName;
import static org.apache.commons.io.FileUtils.deleteQuietly;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.lib.util.Pair;
import static org.yestech.multimedia.image.jdk.ImageUtil.scale;
import static org.yestech.multimedia.image.jdk.ImageUtil.determineFormat;
import org.yestech.multimedia.image.jdk.ImageUtil;
import org.yestech.publish.objectmodel.ArtifactType;
import org.yestech.publish.objectmodel.DefaultFileArtifact;
import org.yestech.publish.objectmodel.IFileArtifact;
import org.yestech.publish.objectmodel.ProducerArtifactType;
import org.yestech.publish.publisher.IPublisher;

import java.io.*;

/**
 * @author A.J. Wright
 */
@ProducerArtifactType(type = ArtifactType.IMAGE)
public class ImagePublisherAdapter implements IPublisher<IFileArtifact<MediaMetaData, String>> {

    private static final Logger logger = LoggerFactory.getLogger(ImagePublisherAdapter.class);

    private IPublisher<IFileArtifact> delegate;
    private int maxSize;
    private int maxThumbnailSize;
    private File tempdir;

    @Override
    public void publish(IFileArtifact<MediaMetaData, String> fileArtifact) {

        File file = null;
        try {

            file = File.createTempFile("original", ".tmp", tempdir);
            IOUtils.copy(fileArtifact.getStream(), new FileOutputStream(file));

            File scaledImage = File.createTempFile("scaled", ".tmp", tempdir);
            File thumbNail = File.createTempFile("thumb", ".tmp", tempdir);

            //scale image
            String fileName = fileArtifact.getArtifactMetaData().getFileName();
            String formatName = determineFormat(fileName, "png");
            scale(file, scaledImage, maxSize, formatName);
            scale(file, thumbNail, maxThumbnailSize, formatName);

            fileArtifact.setFile(scaledImage);    
            fileArtifact.setStream(new BufferedInputStream(new FileInputStream(scaledImage)));

            DefaultFileArtifact<MediaMetaData, String> thumbnailArtifact = new DefaultFileArtifact<MediaMetaData, String>();
            thumbnailArtifact.setFile(thumbNail);
            thumbnailArtifact.setStream(new BufferedInputStream(new FileInputStream(thumbNail)));
            thumbnailArtifact.setArtifactMetaData(MediaMetaData.cloneMediaMetaData(fileArtifact.getArtifactMetaData()));
            thumbnailArtifact.setUniqueNames(buildThumbnailUniqueNames(fileArtifact.getArtifactMetaData().getUniqueNames()));
            thumbnailArtifact.getArtifactMetaData().setUniqueNames(thumbnailArtifact.getUniqueNames());

            delegate.publish(fileArtifact);
            delegate.publish(thumbnailArtifact);

        }
        catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        finally {
            if (file != null) deleteQuietly(file);
        }
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void setMaxThumbnailSize(int maxThumbnailSize) {
        this.maxThumbnailSize = maxThumbnailSize;
    }

    public void setDelegate(IPublisher<IFileArtifact> delegate) {
        this.delegate = delegate;
    }

    public void setTempdir(File tempdir) {
        this.tempdir = tempdir;
    }

    protected Pair<String, String> buildThumbnailUniqueNames(Pair<String, String> old) {
        String first = old.getFirst();
        String second = old.getSecond();

        return new Pair<String, String>(first, buildThumbnailUniqueName(second));
    }

}

package com.blackbox.server.occasion.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.annotation.AfterReturning;
import com.blackbox.occasion.Occasion;
import com.blackbox.occasion.OccasionLayout;
import com.blackbox.media.IMediaManager;
import com.blackbox.media.MediaMetaData;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author A.J. Wright
 */
@Aspect
public class PopulateOccasionTransientsAspect {

    @Resource
    private IMediaManager mediaManager;


    @Pointcut("execution(public com.blackbox.occasion.Occasion com.blackbox.occasion.IOccasionManager.load*(..)) || " +
            "execution(public com.blackbox.occasion.Occasion com.blackbox.occasion.IOccasionManager.updateOccasion(..))")
    private void returnsOccasionPointcut() {
    }

    @Pointcut("execution(public java.util.List com.blackbox.occasion.IOccasionManager.*(..))")
    private void returnsOccasionList() {
    }

    @AfterReturning(pointcut = "returnsOccasionList()", returning = "list")
    public void handleOccasionList(List<Occasion> list) {

        if (list != null && !list.isEmpty()) {
            for (Occasion occasion : list) {
                handleOccasion(occasion);
            }
        }
    }

    @AfterReturning(pointcut = "returnsOccasionPointcut()", returning = "occasion")
    public void handleOccasion(Occasion occasion) {
        if (occasion == null) return;
        OccasionLayout layout = occasion.getLayout();
        if (layout == null) return;

        String guid = layout.getImageGuid();
        if (guid != null && layout.getTransiantImage() == null) {
            MediaMetaData md = mediaManager.loadMediaMetaDataByGuid(guid);
            layout.setTransiantImage(md);
        }

        guid = layout.getVideoGuid();
        if (guid != null && layout.getTransiantVideo() == null) {
            MediaMetaData md = mediaManager.loadMediaMetaDataByGuid(guid);
            layout.setTransiantVideo(md);
        }
    }


    public void setMediaManager(IMediaManager mediaManager) {
        this.mediaManager = mediaManager;
    }
}

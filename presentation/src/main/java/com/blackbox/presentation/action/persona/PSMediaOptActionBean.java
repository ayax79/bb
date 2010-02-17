/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blackbox.presentation.action.persona;

import com.blackbox.foundation.media.IMediaManager;
import com.blackbox.foundation.media.MediaLibrary;
import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.foundation.social.NetworkTypeEnum;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.blackbox.presentation.action.util.PresentationUtil.createResolutionWithText;

public class PSMediaOptActionBean extends BasePersonaActionBean{

    final private static Logger _logger = LoggerFactory.getLogger(PSMediaOptActionBean.class);
    String mediaGuid;
    String libGuid;
    String option;

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
    @SpringBean("mediaManager")
    private IMediaManager mediaManager;

    public String getLibGuid() {
        return libGuid;
    }

    public void setLibGuid(String libGuid) {
        this.libGuid = libGuid;
    }

    public String getMediaGuid() {
        return mediaGuid;
    }

    public void setMediaGuid(String mediaGuid) {
        this.mediaGuid = mediaGuid;
    }

    public void setMediaManager(IMediaManager mediaManager) {
        this.mediaManager = mediaManager;
    }

    @DontValidate
    @DefaultHandler                                                          
    public Resolution begin() {
        _logger.info("calling begin");
        return createResolutionWithText(getContext(), "success");
    }

    public Resolution changeMediaOption() {
        _logger.info("calling changePicOpt");
        MediaMetaData md = mediaManager.loadMediaMetaDataByGuid(mediaGuid);
        md.setRecipientDepth(NetworkTypeEnum.valueOf(option));
         mediaManager.saveLibraryImage(md);
        return createResolutionWithText(getContext(), "success");
    }
    public Resolution getMediaOption(){
        _logger.info("calling get");
        MediaMetaData md = mediaManager.loadMediaMetaDataByGuid(mediaGuid);
        NetworkTypeEnum depth=md.getRecipientDepth();
        return createResolutionWithText(getContext(), md.getRecipientDepth().toString());
    }
    public Resolution deleteMediaFromLib() {
        _logger.info("calling deletePic");
        MediaLibrary ml = mediaManager.loadMediaLibraryByGuid(libGuid);
        for (MediaMetaData md : ml.getMedia()) {
            if (md.getGuid().equals(mediaGuid)) {
                ml.getMedia().remove(md);
                break;
            }
        }
        mediaManager.saveMediaLibrary(ml);
        return createResolutionWithText(getContext(), "success");
    }
}

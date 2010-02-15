/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blackbox.presentation.action.persona;

import com.blackbox.media.IMediaManager;
import com.blackbox.media.MediaLibrary;
import com.blackbox.social.NetworkTypeEnum;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.blackbox.presentation.action.util.PresentationUtil.createResolutionWithText;

public class MediaLibraryPrivacyActionBean extends BasePersonaActionBean {

    final private static Logger logger = LoggerFactory.getLogger(MediaLibraryPrivacyActionBean.class);
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

    public void setMediaManager(IMediaManager mediaManager) {
        this.mediaManager = mediaManager;
    }

    @DontValidate
    @DefaultHandler
    public Resolution begin() {
        logger.info("calling begin");
        return createResolutionWithText(getContext(), "success");
    }

    public Resolution changeMediaOption() {
        logger.info("calling changePicOpt");
        MediaLibrary ml = mediaManager.loadMediaLibraryByGuid(libGuid);
        ml.setNetworkTypeEnum(NetworkTypeEnum.valueOf(option));
        mediaManager.saveMediaLibrary(ml);
        return createResolutionWithText(getContext(), "success");
    }

    public Resolution getMediaOption() {
        logger.info("calling get");
        MediaLibrary ml = mediaManager.loadMediaLibraryByGuid(libGuid);
        NetworkTypeEnum depth = ml.getNetworkTypeEnum();
        return createResolutionWithText(getContext(), depth.toString());
    }
}
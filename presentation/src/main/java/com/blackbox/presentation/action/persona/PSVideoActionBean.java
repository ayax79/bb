/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */
package com.blackbox.presentation.action.persona;

import com.blackbox.media.IMediaManager;
import com.blackbox.media.MediaLibrary;
import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;


public class PSVideoActionBean extends BaseBlackBoxActionBean {

    @SpringBean("mediaManager")
    private IMediaManager mediaManager;

    @SpringBean("personaHelper") protected PersonaHelper personaHelper;

    private String videoName;
    private String videoDesc;
    public FileBean fileData;
    final private static Logger _logger = LoggerFactory.getLogger(PSVideoActionBean.class);

    @DontValidate
    public Resolution uploadVideo() {
        MediaLibrary videoLib = personaHelper.saveVideo(fileData, getCurrentUser(), mediaManager, videoName, videoDesc);
        String size = new Integer(videoLib.getMedia().size()).toString();
        _logger.info(size);
        return new StreamingResolution("text", new StringReader("test"));
    }
}

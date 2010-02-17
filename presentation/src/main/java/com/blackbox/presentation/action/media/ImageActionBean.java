/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.presentation.action.media;

import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.foundation.media.IMediaManager;
import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class ImageActionBean extends BaseBlackBoxActionBean
{
    private static final Logger log = LoggerFactory.getLogger(ImageActionBean.class);


    @SpringBean("mediaManager") private IMediaManager mediaService;
    @Validate(required = true) private String imageGuid;


    public void setContentService(IMediaManager mediaService)
    {
        this.mediaService = mediaService;
    }

    protected IMediaManager getContentService()
    {
        return mediaService;
    }

    public void setImageGuid(String imageGuid)
    {
        this.imageGuid = imageGuid;
    }

    public Resolution display()
    {
        MediaMetaData metaData = getContentMetaData();

        if (metaData == null)
        {
            throw new IllegalStateException("image does not exist");
        }

//        byte[] bytes = mediaManager.loadContentById(metaData.getIdentifier());
//        InputStream in = new ByteArrayInputStream(bytes);
//
//        return new StreamingResolution(metaData.getMimeType(), in).setFilename(metaData.getFileName());
        return new ForwardResolution(metaData.getLocation());
    }

    protected MediaMetaData getContentMetaData()
    {
        return mediaService.loadMediaMetaDataByGuid(imageGuid);
    }
}

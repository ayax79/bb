/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.media.listener;

import com.blackbox.foundation.exception.MediaStoreException;
import com.blackbox.server.media.event.LoadContentEvent;
import com.blackbox.server.BaseBlackboxListener;
import org.apache.commons.io.IOUtils;
import static org.apache.commons.io.FileUtils.openInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;

import java.io.*;

/**
 * @author A.J. Wright
 */
@ListenedEvents(LoadContentEvent.class)
public class LoadContentListener extends BaseBlackboxListener<LoadContentEvent, byte[]>
{
    private static final Logger log = LoggerFactory.getLogger(LoadContentListener.class);

    private File fileStoreDirectory;

    @Override
    public void handle(LoadContentEvent loadContentEvent, ResultReference<byte[]> reference)
    {

        File file = new File(fileStoreDirectory, String.valueOf(loadContentEvent.getType()));
        assert file.exists();

        try
        {
            InputStream in = openInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            IOUtils.copy(in, out);
            in.close();
            out.close();


            reference.setResult(out.toByteArray());

        }
        catch (IOException e)
        {
            log.error(e.getMessage(), e);
            throw new MediaStoreException(e);
        }
    }

    @Required
    public void setFileStoreDirectory(File fileStoreDirectory)
    {
        this.fileStoreDirectory = fileStoreDirectory;
    }
}

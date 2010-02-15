/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.media.listener;

import com.blackbox.server.media.event.LoadContentEvent;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import static org.apache.commons.io.FileUtils.openOutputStream;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.yestech.event.ResultReference;

import java.io.*;
import static java.lang.String.valueOf;

/**
 *
 *
 */
public class LoadContentListenerUnitTest
{

    File tempDir;
    long testId = 122113L;

    @Before
    public void setupTempDir() throws IOException
    {
        // hack to create a temporary dir
        tempDir = File.createTempFile("tmpdir", "");
        assertTrue(tempDir.delete());
        assertTrue(tempDir.mkdirs());

        InputStream in = getClass().getResourceAsStream("/daisy.jpg");
        assertNotNull(in);
        File file = new File(tempDir, valueOf(testId));
        assertTrue(file.createNewFile());
        OutputStream out = openOutputStream(file);
        IOUtils.copy(in, out);
        in.close();
        out.close();
    }


    @After
    public void destroyTempDir() throws IOException
    {
        FileUtils.deleteDirectory(tempDir);
    }


    @Test
    public void testHandle()
    {

        LoadContentListener listener = new LoadContentListener();
        listener.setFileStoreDirectory(tempDir);

        ResultReference<byte[]> ref = new ResultReference<byte[]>();

        listener.handle(new LoadContentEvent(testId), ref);

        byte[] bytes = ref.getResult();
        assertNotNull(bytes);
    }
}

package com.blackbox.presentation.action.persona;

import net.sourceforge.stripes.action.FileBean;
import static org.apache.commons.io.IOUtils.copy;
import org.apache.commons.io.FileUtils;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Ignore;

import java.io.*;
import java.net.URL;

@Ignore
public class PersonaHelperUnitTest {
    
    @Test
    public void testGetCroppedImage() throws Exception {

        DefaultPersonaHelper personaHelper = new DefaultPersonaHelper();

        // hack for creating a temp directory
        File tempFile = File.createTempFile("test", "dir");
        tempFile.delete();
        tempFile.mkdirs();

        try {
            FileBean fb = new FileBean(getTestImage(), "image/png", "daisy.png");
            File image = personaHelper.getCroppedImage(fb, tempFile.getPath(), 300, 12, 300, 261);
            assertNotNull(image);
            assertTrue(image.exists());

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            copy(new BufferedInputStream(new FileInputStream(image)), out);

            byte[] bytes = out.toByteArray();
            assertTrue(bytes.length > 0);
        } finally {
            FileUtils.deleteDirectory(tempFile);
        }
    }


    public File getTestImage() {
        URL resource = getClass().getResource("/daisy.jpg");
        return new File(resource.getPath());
    }
}


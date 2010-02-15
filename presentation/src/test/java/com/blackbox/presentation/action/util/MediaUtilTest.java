package com.blackbox.presentation.action.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import net.sourceforge.stripes.action.FileBean;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
@Ignore
public class MediaUtilTest {

    @Test
    public void testCropImage() throws IOException {
        int imageX=0;
        int imageY=0;
        int imageWidth=50;
        int imageHeight=50;
        URL resource = getClass().getResource("/daisy.jpg");
        File file=new File(resource.getPath());
        FileBean filedata1=new FileBean(file,"image/jpeg","daisy1.jpg");
        FileBean filedata2=new FileBean(file,"image/jpeg","daisy2.jpg");

        File tmpDir = File.createTempFile("test", "dir");
        tmpDir.delete();
        tmpDir.mkdir();
        tmpDir.deleteOnExit();
        
        File croped1=MediaUtil.getCroppedImage(filedata1, tmpDir.getPath(), imageX, imageY, imageWidth, imageHeight);
        File croped2=MediaUtil.getCroppedImage(filedata2, tmpDir.getPath(), imageX, imageY, imageWidth, imageHeight);
        assertNotNull(croped1);
        assertNotNull(croped2);
        assertEquals(croped1.length(),croped2.length());
    }

}

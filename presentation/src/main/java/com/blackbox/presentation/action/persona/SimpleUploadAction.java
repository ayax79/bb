package com.blackbox.presentation.action.persona;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;

/**
 *
 *
 */
public class SimpleUploadAction extends BaseBlackBoxActionBean {

    private static final Logger logger = LoggerFactory.getLogger(SimpleUploadAction.class);

    public FileBean fileData; // must be the same as the fileDataName in the js

    public void setFileData(FileBean fileData) {
        this.fileData = fileData;
    }

    public Resolution test() throws IOException {

        assert getContext().getUser() != null;
        assert fileData != null;
        logger.info("user "+ getContext().getUser());
        logger.info("file received "+fileData.getFileName());

        getContext().getResponse().setHeader("Stripes-Success", "true");
        return new StreamingResolution("text", new StringReader("success"));
    }
}

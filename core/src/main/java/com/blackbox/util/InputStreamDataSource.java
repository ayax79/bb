package com.blackbox.util;

import com.blackbox.media.MediaMetaData;

import javax.activation.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class InputStreamDataSource implements DataSource {

    private String name;
    private String contentType;
    private InputStream inputStream;

    public InputStreamDataSource(String name, String contentType, InputStream inputStream) {
        this.name = name;
        this.contentType = contentType;
        this.inputStream = inputStream;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return inputStream;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public String getName() {
        return name;
    }

    public static InputStreamDataSource createInputStreamDataSource(MediaMetaData md, InputStream in) {
        return new InputStreamDataSource(md.getFileName(), md.getMimeType(), in);
    }
}

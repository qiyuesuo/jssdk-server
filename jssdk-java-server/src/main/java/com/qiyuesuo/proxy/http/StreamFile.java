package com.qiyuesuo.proxy.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamFile implements FileItem {

    private String fileName;
    private InputStream stream;
    private String mimeType;
    private int printCount;

    private static final String DEFAULT_FILE_NAME = "streamFile";

    public StreamFile(String fileName, InputStream stream, String mimeType) {
        this.fileName = fileName;
        this.stream = stream;
        this.mimeType = mimeType;
    }

    public StreamFile(String fileName, InputStream stream) {
        this.fileName = fileName;
        this.stream = stream;
        this.mimeType = MIME_TYPE_DEFAULT;
    }

    public StreamFile(InputStream stream) {
        this.fileName = DEFAULT_FILE_NAME;
        this.stream = stream;
        this.mimeType = MIME_TYPE_DEFAULT;
    }

    public InputStream getStream() {
        return stream;
    }

    @Override
    public boolean isValid() {
        return this.stream != null && this.fileName != null;
    }

    @Override
    public String getFileName() {
        return this.fileName;
    }

    @Override
    public String getMimeType() {
        if (this.mimeType == null) {
            return MIME_TYPE_DEFAULT;
        } else {
            return this.mimeType;
        }
    }

    @Override
    public long getFileLength() {
        return 0L;
    }

    @Override
    public void write(OutputStream output) throws IOException {
        try {
            byte[] buffer = new byte[READ_BUFFER_SIZE];
            int n = 0;
            while (-1 != (n = stream.read(buffer))) {
                output.write(buffer, 0, n);
            }
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    public int getPrintCount() {
        return printCount;
    }

    public void setPrintCount(int printCount) {
        this.printCount = printCount;
    }

}

package com.qiyuesuo.proxy.http;

import java.io.IOException;
import java.io.OutputStream;

public interface FileItem {

	/** 默认媒体类型 **/
	public static final String MIME_TYPE_DEFAULT = "application/octet-stream";
	/** 默认流式读取缓冲区大小 **/
	public static final int READ_BUFFER_SIZE = 1024 * 4;

	public boolean isValid();

	public String getFileName();

	public String getMimeType();

	public long getFileLength();

	public void write(OutputStream output) throws IOException;

}

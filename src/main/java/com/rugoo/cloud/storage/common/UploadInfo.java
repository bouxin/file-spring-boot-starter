package com.rugoo.cloud.storage.common;

import java.io.File;

/**
 * Description
 *
 * @author bouxinLou
 * @date 2021-01-02
 */
public class UploadInfo<T> {
    private T contents;

    private long contentLength;

    private String fileExtension;

    public T getContents() {
        return contents;
    }

    public UploadInfo<T> setContents(T contents) {
        this.contents = contents;

        if (contents instanceof byte[]) {
            setContentLength(((byte[]) contents).length);
        } else if (contents instanceof File) {
            setContentLength(((File) contents).length());
        }

        return this;
    }

    public long getContentLength() {
        return contentLength;
    }

    public UploadInfo<T> setContentLength(long contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public UploadInfo<T> setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
        return this;
    }
}

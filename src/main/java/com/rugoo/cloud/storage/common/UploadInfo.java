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

    private long contentsLength;

    private String fileExtension;

    public T getContents() {
        return contents;
    }

    public UploadInfo<T> setContents(T contents) {
        this.contents = contents;

        if (contents instanceof byte[]) {
            setContentsLength(((byte[]) contents).length);
        } else if (contents instanceof File) {
            setContentsLength(((File) contents).length());
        }

        return this;
    }

    public long getContentsLength() {
        return contentsLength;
    }

    private UploadInfo<T> setContentsLength(long contentsLength) {
        this.contentsLength = contentsLength;
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

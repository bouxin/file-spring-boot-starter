package com.xxx.cloud.storage.common;

import com.xxx.cloud.storage.Transferable;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Example MyunFile for business services
 *
 * @author boxin
 * @date 2020-12-29
 * @see com.xxx.cloud.storage.common
 */
public class CloudFile implements Transferable, Serializable {

    @SuppressWarnings("unset")
    private long fileId;

    /**
     * 业务唯一ID
     */
    private String objectId;

    /**
     * 文件访问路径
     */
    private String accessUrl;

    /**
     * 文件大小，单位byte
     */
    private long fsize;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * Last文件名, for changing filename, record last filename
     */
    private String prevfilename;

    /**
     * 业务、文件分类等基础目录路径
     */
    private String parentpath;

    /**
     * 子存储路径
     */
    private String storepath;

    /**
     * 文件扩展名
     */
    private String extension;

    /**
     * 云存储源
     */
    private String srccloud;

    /**
     * 上传时间
     */
    private LocalDateTime uploadtime;

    private CloudFile() { }

    public static CloudFile createInstance() {
        return new CloudFile();
    }

    public long getFileId() {
        return fileId;
    }

    public CloudFile setFileId(long fileId) {
        this.fileId = fileId;
        return this;
    }

    public String getObjectId() {
        return objectId;
    }

    public CloudFile setObjectId(String objectId) {
        this.objectId = objectId;
        return this;
    }

    public String getAccessUrl() {
        return accessUrl;
    }

    public CloudFile setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
        return this;
    }

    public long getFsize() {
        return fsize;
    }

    public CloudFile setFsize(long fsize) {
        this.fsize = fsize;
        return this;
    }

    public String getFilename() {
        return filename;
    }

    public CloudFile setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public String getPrevfilename() {
        return prevfilename;
    }

    public CloudFile setPrevfilename(String prevfilename) {
        this.prevfilename = prevfilename;
        return this;
    }

    public String getParentpath() {
        return parentpath;
    }

    public CloudFile setParentpath(String parentpath) {
        this.parentpath = parentpath;
        return this;
    }

    public String getStorepath() {
        return storepath;
    }

    public CloudFile setStorepath(String storepath) {
        this.storepath = storepath;
        return this;
    }

    public String getExtension() {
        return extension;
    }

    public CloudFile setExtension(String extension) {
        this.extension = extension;
        return this;
    }

    public String getSrccloud() {
        return srccloud;
    }

    public CloudFile setSrccloud(String srccloud) {
        this.srccloud = srccloud;
        return this;
    }

    public LocalDateTime getUploadtime() {
        return uploadtime;
    }

    public CloudFile setUploadtime(LocalDateTime uploadtime) {
        this.uploadtime = uploadtime;
        return this;
    }
}

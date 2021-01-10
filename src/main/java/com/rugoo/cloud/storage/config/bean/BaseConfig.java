package com.rugoo.cloud.storage.config.bean;


/**
 * Description
 *
 * @author bouxinLou
 * @date 2021-01-10
 */
public class BaseConfig {
    private String endpoint;

    private String domain;

    private String indexKey;

    private String accessKey;

    private String secretKey;

    private String bucket;

    private String storepath;

    public String getEndpoint() {
        return endpoint;
    }

    public BaseConfig setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public String getDomain() {
        return domain;
    }

    public BaseConfig setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public String getIndexKey() {
        return indexKey;
    }

    public BaseConfig setIndexKey(String indexKey) {
        this.indexKey = indexKey;
        return this;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public BaseConfig setAccessKey(String accessKey) {
        this.accessKey = accessKey;
        return this;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public BaseConfig setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public String getBucket() {
        return bucket;
    }

    public BaseConfig setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getStorepath() {
        return storepath;
    }

    public BaseConfig setStorepath(String storepath) {
        if (storepath.startsWith("/")) {
            this.storepath = storepath.substring(1);
        } else {
            this.storepath = storepath;
        }
        return this;
    }
}

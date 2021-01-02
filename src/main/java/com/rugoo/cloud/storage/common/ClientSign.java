package com.rugoo.cloud.storage.common;

/**
 * Description
 *
 * @author bouxinLou
 * @date 2021-01-02
 */
public class ClientSign {
    private String endpoint;

    private String acl;

    private String token;

    private String contentType;

    private String policy;

    private String accessKey;

    private String signature;

    public String getEndpoint() {
        return endpoint;
    }

    public ClientSign setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public String getAcl() {
        return acl;
    }

    public ClientSign setAcl(String acl) {
        this.acl = acl;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public ClientSign setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public String getPolicy() {
        return policy;
    }

    public String getToken() {
        return token;
    }

    public ClientSign setToken(String token) {
        this.token = token;
        return this;
    }

    public ClientSign setPolicy(String policy) {
        this.policy = policy;
        return this;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public ClientSign setAccessKey(String accessKey) {
        this.accessKey = accessKey;
        return this;
    }

    public String getSignature() {
        return signature;
    }

    public ClientSign setSignature(String signature) {
        this.signature = signature;
        return this;
    }
}

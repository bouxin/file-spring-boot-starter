package com.rugoo.cloud.storage.config.bean;

/**
 * Description
 *
 * @author bouxinLou
 * @date 2021-01-10
 */
public class TencentCosConfig extends BaseConfig {
    private String region;

    public String getRegion() {
        return region;
    }

    public TencentCosConfig setRegion(String region) {
        this.region = region;
        return this;
    }
}

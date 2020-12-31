package com.xxx.cloud.storage.config;

import com.xxx.cloud.storage.exception.CloudStorageConfigurationException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import com.xxx.cloud.storage.enums.CloudType;

/**
 * Description
 *
 * @author boxin
 * @date 2020-12-29
 * @see CloudStorageProperties
 */
@Component
@ConfigurationProperties(prefix = "com.xxx.cloud.storage")
public class CloudStorageProperties {

    private boolean disable = false;

    private CloudType preferCloudType;

    private long maxFileSize;

    private HuaweiConfig huaweiConfig;

    private AliyunConfig aliyunConfig;

    private TencentConfig tencentConfig;

    public boolean isDisable() {
        return disable;
    }

    @ConditionalOnProperty(
            prefix = "com.xxx.cloud.storage",
            name = {"disable"},
            matchIfMissing = true
    )
    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public CloudType getPreferCloudType() {
        return preferCloudType;
    }

    public CloudStorageProperties setPreferCloudType(CloudType preferCloudType) {
        if (preferCloudType == null) {
            throw new CloudStorageConfigurationException("Choose any cloud platform!");
        }
        this.preferCloudType = preferCloudType;
        return this;
    }

    public long getMaxFileSize() {
        return maxFileSize;
    }

    public CloudStorageProperties setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
        return this;
    }

    public HuaweiConfig getHuaweiConfig() {
        return huaweiConfig;
    }

    public CloudStorageProperties setHuaweiConfig(HuaweiConfig huaweiConfig) {
        this.huaweiConfig = huaweiConfig;
        return this;
    }

    public AliyunConfig getAliyunConfig() {
        return aliyunConfig;
    }

    public CloudStorageProperties setAliyunConfig(AliyunConfig aliyunConfig) {
        this.aliyunConfig = aliyunConfig;
        return this;
    }

    public TencentConfig getTencentConfig() {
        return tencentConfig;
    }

    public CloudStorageProperties setTencentConfig(TencentConfig tencentConfig) {
        this.tencentConfig = tencentConfig;
        return this;
    }

    public static class HuaweiConfig extends Config {}

    public static class AliyunConfig extends Config  {}

    public static class TencentConfig extends Config {
        private String region;

        public String getRegion() {
            return region;
        }

        public TencentConfig setRegion(String region) {
            this.region = region;
            return this;
        }
    }

    public static class Config {
        private String endpoint;

        private String indexKey;

        private String accessKey;

        private String secretKey;

        private String bucket;

        private String parentpath;

        private String storepath;

        public String getEndpoint() {
            return endpoint;
        }

        public Config setEndpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public String getIndexKey() {
            return indexKey;
        }

        public Config setIndexKey(String indexKey) {
            this.indexKey = indexKey;
            return this;
        }

        public String getAccessKey() {
            return accessKey;
        }

        public Config setAccessKey(String accessKey) {
            this.accessKey = accessKey;
            return this;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public Config setSecretKey(String secretKey) {
            this.secretKey = secretKey;
            return this;
        }

        public String getBucket() {
            return bucket;
        }

        public Config setBucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public String getParentpath() {
            return parentpath;
        }

        public Config setParentpath(String parentpath) {
            this.parentpath = parentpath;
            return this;
        }

        public String getStorepath() {
            return storepath;
        }

        public Config setStorepath(String storepath) {
            this.storepath = storepath;
            return this;
        }
    }
}

package com.rugoo.cloud.storage.config;

import com.rugoo.cloud.storage.config.bean.AliyunOssConfig;
import com.rugoo.cloud.storage.config.bean.HuaweiObsConfig;
import com.rugoo.cloud.storage.config.bean.TencentCosConfig;
import com.rugoo.cloud.storage.exception.CloudStorageConfigurationException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import com.rugoo.cloud.storage.enums.CloudType;
import org.springframework.util.unit.DataSize;

/**
 * Description
 *
 * @author boxin
 * @date 2020-12-29
 * @see CloudStorageProperties
 */
@Component
@ConfigurationProperties(prefix = "rugoo.cloud.storage")
public class CloudStorageProperties {

    private boolean disable = false;

    private CloudType preferCloudType;

    private DataSize maxFileSize = DataSize.ofMegabytes(100L);

    private HuaweiObsConfig huaweiObs;

    private AliyunOssConfig aliyunOss;

    private TencentCosConfig tencentCos;

    public boolean isDisable() {
        return disable;
    }

    @ConditionalOnProperty(
            prefix = "rugoo.cloud.storage",
            name = {"disable"},
            matchIfMissing = true
    )
    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public CloudType getPreferCloudType() {
        return preferCloudType;
    }

    @ConditionalOnProperty(
            prefix = "rugoo.cloud.storage",
            name = {"preferCloudType"},
            matchIfMissing = true
    )
    public CloudStorageProperties setPreferCloudType(CloudType preferCloudType) {
        if (preferCloudType == null) {
            throw new CloudStorageConfigurationException("Choose any cloud platform!");
        }
        this.preferCloudType = preferCloudType;
        return this;
    }

    public DataSize getMaxFileSize() {
        return maxFileSize;
    }

    public CloudStorageProperties setMaxFileSize(DataSize maxFileSize) {
        this.maxFileSize = maxFileSize;
        return this;
    }

    public HuaweiObsConfig getHuaweiObs() {
        return huaweiObs;
    }

    public CloudStorageProperties setHuaweiObs(HuaweiObsConfig huaweiObs) {
        this.huaweiObs = huaweiObs;
        return this;
    }

    public AliyunOssConfig getAliyunOss() {
        return aliyunOss;
    }

    public CloudStorageProperties setAliyunOss(AliyunOssConfig aliyunOss) {
        this.aliyunOss = aliyunOss;
        return this;
    }

    public TencentCosConfig getTencentCos() {
        return tencentCos;
    }

    public CloudStorageProperties setTencentCos(TencentCosConfig tencentCos) {
        this.tencentCos = tencentCos;
        return this;
    }
}

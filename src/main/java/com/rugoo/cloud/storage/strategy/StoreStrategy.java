package com.rugoo.cloud.storage.strategy;

import com.rugoo.cloud.storage.CloudStorable;
import com.rugoo.cloud.storage.config.CloudStorageProperties;
import com.rugoo.cloud.storage.exception.CloudStorageConfigurationException;
import com.rugoo.cloud.storage.strategy.groups.AliyunOssCloud;
import com.rugoo.cloud.storage.strategy.groups.HuaweiObsCloud;
import com.rugoo.cloud.storage.strategy.groups.TencentOssCloud;

/**
 * Description
 *
 * @author boxin
 * @date 2020-12-29
 * @see com.rugoo.cloud.storage.strategy
 */
public class StoreStrategy {

    public static CloudStorable create(CloudStorageProperties properties) {
        if (properties.isDisable()) {
            throw new CloudStorageConfigurationException("Cloud storage was disabled");
        }

        switch (properties.getPreferCloudType()) {
            case HUAWEI:
                return new HuaweiObsCloud(properties.getHuaweiConfig());
            case ALIYUN:
                return new AliyunOssCloud(properties.getAliyunConfig());
            case TENCENT:
                return new TencentOssCloud(properties.getTencentConfig());
            case DEFAULT:
            default:
                throw new CloudStorageConfigurationException("Unknown Cloud platform, add any cloudary store service!");
        }
    }
}

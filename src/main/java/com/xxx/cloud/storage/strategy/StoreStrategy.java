package com.xxx.cloud.storage.strategy;

import com.xxx.cloud.storage.CloudStorable;
import com.xxx.cloud.storage.config.MyunCloudStorageProperties;
import com.xxx.cloud.storage.exception.CloudStorageConfigurationException;
import com.xxx.cloud.storage.strategy.groups.AliyunOssCloud;
import com.xxx.cloud.storage.strategy.groups.HuaweiObsCloud;
import com.xxx.cloud.storage.strategy.groups.TencentOssCloud;

/**
 * Description
 *
 * @author boxin
 * @date 2020-12-29
 * @see com.xxx.cloud.storage.strategy
 */
public class StoreStrategy {

    public static CloudStorable create(MyunCloudStorageProperties properties) {
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

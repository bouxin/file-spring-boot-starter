package com.rugoo.cloud.storage.strategy;

import com.rugoo.cloud.storage.CloudStorageStrategy;
import com.rugoo.cloud.storage.config.CloudStorageProperties;
import com.rugoo.cloud.storage.enums.CloudType;
import com.rugoo.cloud.storage.exception.CloudStorageConfigurationException;
import com.rugoo.cloud.storage.strategy.impl.AliyunOSS;
import com.rugoo.cloud.storage.strategy.impl.HuaweiOBS;
import com.rugoo.cloud.storage.strategy.impl.TencentOSS;

/**
 * Description
 *
 * @author boxin
 * @date 2020-12-29
 * @see com.rugoo.cloud.storage.strategy
 */
public class StorageFactory {

    public static CloudStorageStrategy create(CloudType type) {
        switch (type) {
            case HUAWEI:
                return new HuaweiOBS();
            case ALIYUN:
                return new AliyunOSS();
            case TENCENT:
                return new TencentOSS();
            case DEFAULT:
            default:
                throw new CloudStorageConfigurationException("Unknown Cloud platform, add any cloudary store service!");
        }
    }
}

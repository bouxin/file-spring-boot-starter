package com.rugoo.cloud.storage.strategy;

import com.rugoo.cloud.storage.CloudStorageStrategy;
import com.rugoo.cloud.storage.enums.CloudType;
import com.rugoo.cloud.storage.exception.CloudStorageConfigurationException;
import com.rugoo.cloud.storage.strategy.impl.AliyunOssStorageImpl;
import com.rugoo.cloud.storage.strategy.impl.HuaweiObsStorageImpl;
import com.rugoo.cloud.storage.strategy.impl.TencentCosStorageImpl;

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
                return new HuaweiObsStorageImpl();
            case ALIYUN:
                return new AliyunOssStorageImpl();
            case TENCENT:
                return new TencentCosStorageImpl();
            case DEFAULT:
            default:
                throw new CloudStorageConfigurationException("Unknown Cloud platform, add any cloudary store service!");
        }
    }
}

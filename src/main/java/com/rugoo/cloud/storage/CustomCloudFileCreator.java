package com.rugoo.cloud.storage;

import com.rugoo.cloud.storage.common.CloudFile;
import com.rugoo.cloud.storage.config.CloudStorageProperties;
import com.rugoo.cloud.storage.enums.CloudType;
import com.rugoo.cloud.storage.util.StringUtil;

import java.time.LocalDateTime;

/**
 * Description
 *
 * @author bouxinLou
 * @date 2021-01-02
 */
public interface CustomCloudFileCreator {
    default CloudFile createCloudFile(CloudStorageProperties.Config config,
                                      String fileKey,
                                      String filename,
                                      long contentLen,
                                      CloudType cloudType) {
        return CloudFile.createInstance()
                .setAccessUrl(StringUtil.concat(config.getDomain(), "/", fileKey))
                .setObjectId(fileKey)
                .setStorepath(config.getStorepath())
                .setUploadtime(LocalDateTime.now())
                .setCloudType(cloudType.name())
                .setFsize(contentLen)
                .setPrevfilename(filename)
                .setFilename(filename);
    }
}

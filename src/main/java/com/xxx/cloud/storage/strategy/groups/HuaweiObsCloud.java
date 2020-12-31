package com.xxx.cloud.storage.strategy.groups;

import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.PutObjectResult;
import com.xxx.cloud.storage.CloudStorable;
import com.xxx.cloud.storage.exception.CloudStorageConfigurationException;
import com.xxx.cloud.storage.exception.CloudStorageException;
import org.slf4j.LoggerFactory;
import com.xxx.cloud.storage.anno.ActiveCloud;
import com.xxx.cloud.storage.common.CloudFile;
import com.xxx.cloud.storage.config.CloudStorageProperties;
import com.xxx.cloud.storage.enums.CloudType;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

/**
 * Description
 *
 * @author boxin
 * @date 2020-12-29
 * @see com.xxx.cloud.storage.strategy.groups
 */
@ActiveCloud(type = CloudType.HUAWEI)
public class HuaweiObsCloud implements CloudStorable {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(HuaweiObsCloud.class.getSimpleName());

    private CloudStorageProperties.HuaweiConfig self;

    private HuaweiObsCloud() {}

    public HuaweiObsCloud(CloudStorageProperties.HuaweiConfig props) {
        if (props == null) {
            throw new CloudStorageConfigurationException("HuaweiOBS config unset");
        }
        this.self = props;
    }

    @Override
    public CloudFile store(byte[] fileContents) {
        String filename = UUID.randomUUID().toString();
        String fileKey = String.format(self.getParentpath(), self.getStorepath(), filename);

        CloudFile cloudFile = CloudFile.createInstance();
        ObsClient obs = this.obsClient();

        try {
            PutObjectResult result = obs.putObject(self.getBucket(), fileKey, new ByteArrayInputStream(fileContents));
            cloudFile.setAccessUrl(result.getObjectUrl())
                    .setObjectId(result.getObjectKey())
                    .setStorepath(self.getStorepath())
                    .setParentpath(self.getParentpath())
                    .setUploadtime(LocalDateTime.now())
                    .setSrccloud(CloudType.HUAWEI.name().toLowerCase(Locale.ROOT))
                    .setFsize(fileContents.length)
                    .setPrevfilename(filename)
                    .setFilename(filename);
        } catch (ObsException obsEx) {
            // 防止污染存储源
            obs.deleteObject(self.getBucket(), fileKey);
            throw new CloudStorageException("Upload failed with unexpected exception", obsEx);
        } finally {
            try {
                obs.close();
            } catch (IOException ignored) {
            }
        }

        return cloudFile;
    }

    @Override
    public CloudFile store(File file) {
        String filename = UUID.randomUUID().toString();
        String fileKey = String.format(self.getParentpath(), self.getStorepath(), filename);

        CloudFile cloudFile = CloudFile.createInstance();
        ObsClient obs = this.obsClient();

        try {
            PutObjectResult result = obs.putObject(self.getBucket(), fileKey, file);
            cloudFile.setAccessUrl(result.getObjectUrl())
                    .setObjectId(result.getObjectKey())
                    .setStorepath(self.getStorepath())
                    .setParentpath(self.getParentpath())
                    .setUploadtime(LocalDateTime.now())
                    .setSrccloud(CloudType.HUAWEI.name().toLowerCase(Locale.ROOT))
                    .setFsize(file.length())
                    .setPrevfilename(filename)
                    .setFilename(filename);
        } catch (ObsException obsEx) {
            // 防止污染存储源
            obs.deleteObject(self.getBucket(), fileKey);
            throw new CloudStorageException("Upload failed with unexpected exception", obsEx);
        } finally {
            try {
                obs.close();
            } catch (IOException ignored) {
            }
        }

        return cloudFile;
    }

    @Override
    public InputStream getFileContents(String objectId) {
        return obsClient().getObject(self.getBucket(), objectId).getObjectContent();
    }

    @Override
    public boolean delete(String objectId) {
        try {
            return obsClient().deleteObject(self.getBucket(), objectId).isDeleteMarker();
        } catch (ObsException e) {
            if (log.isDebugEnabled()) {
                log.error("Failed delete HuaweiObs file {}::{}", self.getBucket(), objectId);
            } else {
                log.info("Failed delete HuaweiObs file {}::{}", self.getBucket(), objectId);
            }
            return false;
        }
    }

    private ObsClient obsClient() {
        return new ObsClient(self.getAccessKey(), self.getSecretKey(), self.getEndpoint());
    }

}

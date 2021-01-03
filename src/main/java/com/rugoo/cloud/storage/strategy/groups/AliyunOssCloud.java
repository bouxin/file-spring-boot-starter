package com.rugoo.cloud.storage.strategy.groups;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.rugoo.cloud.storage.CloudStorable;
import com.rugoo.cloud.storage.CustomCloudFileCreator;
import com.rugoo.cloud.storage.common.ClientSign;
import com.rugoo.cloud.storage.common.UploadInfo;
import com.rugoo.cloud.storage.exception.CloudStorageConfigurationException;
import com.rugoo.cloud.storage.exception.CloudStorageException;
import com.rugoo.cloud.storage.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rugoo.cloud.storage.anno.MarkAsCloudStorage;
import com.rugoo.cloud.storage.common.CloudFile;
import com.rugoo.cloud.storage.config.CloudStorageProperties;
import com.rugoo.cloud.storage.enums.CloudType;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

/**
 * Description
 *
 * @author boxin
 * @date 2020-12-29
 * @see com.rugoo.cloud.storage.strategy.groups
 */
@MarkAsCloudStorage(type = CloudType.ALIYUN)
public class AliyunOssCloud implements CloudStorable, CustomCloudFileCreator {

    private static final Logger log = LoggerFactory.getLogger(AliyunOssCloud.class);

    private CloudStorageProperties.AliyunConfig self;

    private AliyunOssCloud() {
    }

    public AliyunOssCloud(CloudStorageProperties.AliyunConfig props) {
        if (props == null) {
            throw new CloudStorageConfigurationException("AliyunOSS config unset");
        }
        this.self = props;
    }

    @Override
    public ClientSign createPostSignature() {
        return null;
    }

    @Override
    public <T> CloudFile store(UploadInfo<T> uploadInfo) {
        String filename = UUID.randomUUID().toString();
        String fileKey = StringUtil.concat(self.getStorepath(), "/", filename, ".", uploadInfo.getFileExtension());

        OSS ossClient = getOssClient();

        try {
            if (uploadInfo.getContents() instanceof byte[]) {
                ossClient.putObject(self.getBucket(), fileKey, new ByteArrayInputStream((byte[]) uploadInfo.getContents()));
            } else if (uploadInfo.getContents() instanceof File) {
                ossClient.putObject(self.getBucket(), fileKey, (File) uploadInfo.getContents());
            }
        } catch (OSSException | ClientException e) {
            ossClient.deleteObject(self.getBucket(), fileKey);
            throw new CloudStorageException("Upload failed with unexpected exception", e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        return createCloudFile(self, fileKey, filename, uploadInfo, CloudType.ALIYUN);
    }

    @Override
    public InputStream getFileContents(String objectId) {
        return getOssClient().getObject(self.getBucket(), objectId).getObjectContent();
    }

    @Override
    public boolean delete(String objectKey) {
        try {
            getOssClient().deleteObject(self.getBucket(), objectKey);
            return true;
        } catch (OSSException | ClientException e) {
            if (log.isDebugEnabled()) {
                log.error("{}", e.toString());
            } else {
                log.info("Delete aliyun oss file failed! {}::{}", self.getBucket(), objectKey);
            }
            return false;
        }
    }

    private OSS getOssClient() {
        return new OSSClientBuilder().build(self.getEndpoint(), self.getAccessKey(), self.getSecretKey());
    }
}

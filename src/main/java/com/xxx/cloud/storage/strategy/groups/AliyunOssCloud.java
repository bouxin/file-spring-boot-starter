package com.xxx.cloud.storage.strategy.groups;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.xxx.cloud.storage.CloudStorable;
import com.xxx.cloud.storage.exception.CloudStorageConfigurationException;
import com.xxx.cloud.storage.exception.CloudStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.xxx.cloud.storage.anno.ActiveCloud;
import com.xxx.cloud.storage.common.CloudFile;
import com.xxx.cloud.storage.config.MyunCloudStorageProperties;
import com.xxx.cloud.storage.enums.CloudType;

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
 * @see com.xxx.cloud.storage.strategy.groups
 */
@ActiveCloud(type = CloudType.ALIYUN)
public class AliyunOssCloud implements CloudStorable {

    private static final Logger log = LoggerFactory.getLogger(AliyunOssCloud.class);

    private MyunCloudStorageProperties.AliyunConfig self;

    private AliyunOssCloud() {
    }

    public AliyunOssCloud(MyunCloudStorageProperties.AliyunConfig props) {
        if (props == null) {
            throw new CloudStorageConfigurationException("AliyunOSS config unset");
        }
        this.self = props;
    }

    @Override
    public CloudFile store(byte[] fileContents) {
        CloudFile cloudFile = CloudFile.createInstance();

        String filename = UUID.randomUUID().toString();
        String fileKey = String.format(self.getParentpath(), self.getStorepath(), filename);

        OSS ossClient = getOssClient();

        try {
            ossClient.putObject(self.getBucket(), fileKey, new ByteArrayInputStream(fileContents));
            cloudFile.setAccessUrl(String.format(self.getEndpoint(), fileKey))
                    .setObjectId(fileKey)
                    .setStorepath(self.getStorepath())
                    .setParentpath(self.getParentpath())
                    .setUploadtime(LocalDateTime.now())
                    .setSrccloud(CloudType.ALIYUN.name().toLowerCase(Locale.ROOT))
                    .setFsize(fileContents.length)
                    .setPrevfilename(filename)
                    .setFilename(filename);
        } catch (OSSException | ClientException e) {
            ossClient.deleteObject(self.getBucket(), fileKey);
            throw new CloudStorageException("Upload failed with unexpected exception", e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        return cloudFile;
    }

    @Override
    public CloudFile store(File file) {
        CloudFile cloudFile = CloudFile.createInstance();

        String filename = UUID.randomUUID().toString();
        String fileKey = String.format(self.getParentpath(), self.getStorepath(), filename);

        OSS ossClient = getOssClient();

        try {
            ossClient.putObject(self.getBucket(), fileKey, file);
            cloudFile.setAccessUrl(String.format(self.getEndpoint(), fileKey))
                    .setObjectId(fileKey)
                    .setStorepath(self.getStorepath())
                    .setParentpath(self.getParentpath())
                    .setUploadtime(LocalDateTime.now())
                    .setSrccloud(CloudType.ALIYUN.name().toLowerCase(Locale.ROOT))
                    .setFsize(file.length())
                    .setPrevfilename(filename)
                    .setFilename(filename);
        } catch (OSSException | ClientException e) {
            ossClient.deleteObject(self.getBucket(), fileKey);
            throw new CloudStorageException("Upload failed with unexpected exception", e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        return cloudFile;
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

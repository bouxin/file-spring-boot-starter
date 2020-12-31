package com.xxx.cloud.storage.strategy.groups;

import com.obs.services.exception.ObsException;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import com.xxx.cloud.storage.CloudStorable;
import com.xxx.cloud.storage.exception.CloudStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.xxx.cloud.storage.anno.ActiveCloud;
import com.xxx.cloud.storage.common.CloudFile;
import com.xxx.cloud.storage.config.MyunCloudStorageProperties;
import com.xxx.cloud.storage.enums.CloudType;
import com.xxx.cloud.storage.exception.CloudStorageConfigurationException;

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
@ActiveCloud(type = CloudType.TENCENT)
public class TencentOssCloud implements CloudStorable {

    private static final Logger log = LoggerFactory.getLogger(TencentOssCloud.class);

    private MyunCloudStorageProperties.TencentConfig self;

    private TencentOssCloud() {
    }

    public TencentOssCloud(MyunCloudStorageProperties.TencentConfig props) {
        if (props == null) {
            throw new CloudStorageConfigurationException("TencentOSS config unset");
        }
        this.self = props;
    }

    @Override
    public CloudFile store(byte[] fileContents) {
        String filename = UUID.randomUUID().toString();
        String fileKey = String.format(self.getParentpath(), self.getStorepath(), filename);

        CloudFile cloudFile = CloudFile.createInstance();
        COSClient cosClient = this.cosClient();

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(self.getBucket(), fileKey, new ByteArrayInputStream(fileContents), null);
            cosClient().putObject(putObjectRequest);
            cloudFile.setAccessUrl(String.format(self.getEndpoint(), fileKey))
                    .setObjectId(fileKey)
                    .setStorepath(self.getStorepath())
                    .setParentpath(self.getParentpath())
                    .setUploadtime(LocalDateTime.now())
                    .setSrccloud(CloudType.TENCENT.name().toLowerCase(Locale.ROOT))
                    .setFsize(fileContents.length)
                    .setPrevfilename(filename)
                    .setFilename(filename);
        } catch (ObsException obsEx) {
            // 防止污染存储源
            cosClient.deleteObject(self.getBucket(), fileKey);
            throw new CloudStorageException("Upload failed with unexpected exception", obsEx);
        } finally {
            cosClient.shutdown();
        }

        return cloudFile;
    }

    @Override
    public CloudFile store(File file) {
        String filename = UUID.randomUUID().toString();
        String fileKey = String.format(self.getParentpath(), self.getStorepath(), filename);

        CloudFile cloudFile = CloudFile.createInstance();
        COSClient cosClient = this.cosClient();

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(self.getBucket(), fileKey, file);
            cosClient().putObject(putObjectRequest);
            cloudFile.setAccessUrl(String.format(self.getEndpoint(), fileKey))
                    .setObjectId(fileKey)
                    .setStorepath(self.getStorepath())
                    .setParentpath(self.getParentpath())
                    .setUploadtime(LocalDateTime.now())
                    .setSrccloud(CloudType.TENCENT.name().toLowerCase(Locale.ROOT))
                    .setFsize(file.length())
                    .setPrevfilename(filename)
                    .setFilename(filename);
        } catch (ObsException obsEx) {
            // 防止污染存储源
            cosClient.deleteObject(self.getBucket(), fileKey);
            throw new CloudStorageException("Upload failed with unexpected exception", obsEx);
        } finally {
            cosClient.shutdown();
        }

        return cloudFile;
    }

    @Override
    public InputStream getFileContents(String objectId) {
        return (InputStream) cosClient().getObject(self.getBucket(), objectId).getObjectContent();
    }

    @Override
    public boolean delete(String objectKey) {
        try {
            cosClient().deleteObject(self.getBucket(), objectKey);
            return true;
        } catch (CosClientException e) {
            if (log.isDebugEnabled()) {
                e.printStackTrace();
                log.error("Failed to delete file {}::{}", self.getBucket(), objectKey);
            } else {
                log.info("Failed to delete file {}::{}", self.getBucket(), objectKey);
            }
            return false;
        }
    }

    private COSClient cosClient() {
        COSCredentials cred = new BasicCOSCredentials(self.getAccessKey(), self.getSecretKey());
        Region region = new Region(self.getRegion());
        ClientConfig clientConfig = new ClientConfig(region);
        return new COSClient(cred, clientConfig);
    }
}

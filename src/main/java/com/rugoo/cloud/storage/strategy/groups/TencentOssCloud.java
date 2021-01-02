package com.rugoo.cloud.storage.strategy.groups;

import com.obs.services.exception.ObsException;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import com.rugoo.cloud.storage.CloudStorable;
import com.rugoo.cloud.storage.CustomCloudFileCreator;
import com.rugoo.cloud.storage.common.ClientSign;
import com.rugoo.cloud.storage.common.UploadInfo;
import com.rugoo.cloud.storage.exception.CloudStorageException;
import com.rugoo.cloud.storage.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rugoo.cloud.storage.anno.MarkAsCloudStorage;
import com.rugoo.cloud.storage.common.CloudFile;
import com.rugoo.cloud.storage.config.CloudStorageProperties;
import com.rugoo.cloud.storage.enums.CloudType;
import com.rugoo.cloud.storage.exception.CloudStorageConfigurationException;

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
@MarkAsCloudStorage(type = CloudType.TENCENT)
public class TencentOssCloud implements CloudStorable, CustomCloudFileCreator {

    private static final Logger log = LoggerFactory.getLogger(TencentOssCloud.class);

    private CloudStorageProperties.TencentConfig self;

    private TencentOssCloud() {
    }

    public TencentOssCloud(CloudStorageProperties.TencentConfig props) {
        if (props == null) {
            throw new CloudStorageConfigurationException("TencentOSS config unset");
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

        COSClient cosClient = cosClient();

        try {
            if (uploadInfo.getContents() instanceof byte[]) {
                cosClient().putObject(self.getBucket(), fileKey, new ByteArrayInputStream((byte[])uploadInfo.getContents()), null);
            } else if (uploadInfo.getContents() instanceof File) {
                cosClient.putObject(self.getBucket(), fileKey, (File) uploadInfo.getContents());
            }
        } catch (ObsException obsEx) {
            // 防止污染存储源
            cosClient.deleteObject(self.getBucket(), fileKey);
            throw new CloudStorageException("Upload failed with unexpected exception", obsEx);
        } finally {
            cosClient.shutdown();
        }

        return createCloudFile(self, fileKey, filename, uploadInfo.getContentsLength(), CloudType.TENCENT);
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

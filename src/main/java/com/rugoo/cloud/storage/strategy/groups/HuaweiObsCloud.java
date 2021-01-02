package com.rugoo.cloud.storage.strategy.groups;

import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.ObjectMetadata;
import com.obs.services.model.PostSignatureRequest;
import com.obs.services.model.PostSignatureResponse;
import com.obs.services.model.PutObjectResult;
import com.rugoo.cloud.storage.CloudStorable;
import com.rugoo.cloud.storage.CustomCloudFileCreator;
import com.rugoo.cloud.storage.common.ClientSign;
import com.rugoo.cloud.storage.common.UploadInfo;
import com.rugoo.cloud.storage.exception.CloudStorageConfigurationException;
import com.rugoo.cloud.storage.exception.CloudStorageException;
import com.rugoo.cloud.storage.util.StringUtil;
import org.slf4j.LoggerFactory;
import com.rugoo.cloud.storage.anno.MarkAsCloudStorage;
import com.rugoo.cloud.storage.common.CloudFile;
import com.rugoo.cloud.storage.config.CloudStorageProperties;
import com.rugoo.cloud.storage.enums.CloudType;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Description
 *
 * @author boxin
 * @date 2020-12-29
 * @see com.rugoo.cloud.storage.strategy.groups
 */
@MarkAsCloudStorage(type = CloudType.HUAWEI)
public class HuaweiObsCloud implements CloudStorable, CustomCloudFileCreator {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(HuaweiObsCloud.class);

    private CloudStorageProperties.HuaweiConfig self;

    private HuaweiObsCloud() {}

    public HuaweiObsCloud(CloudStorageProperties.HuaweiConfig props) {
        if (props == null) {
            throw new CloudStorageConfigurationException("HuaweiOBS config unset");
        }
        this.self = props;
    }

    @Override
    public ClientSign createPostSignature() {
        ClientSign signature = new ClientSign();

        PostSignatureRequest request = new PostSignatureRequest();
        // 设置表单参数
        Map<String, Object> formParams = new HashMap<String, Object>();
        // 设置对象访问权限为公共读
        formParams.put("x-obs-acl", "public-read");
        // 设置对象MIME类型
        formParams.put("content-type", "text/plain");

        request.setFormParams(formParams);
        // 设置表单上传请求有效期，单位：秒
        request.setExpires(3600);
        PostSignatureResponse response = obsClient().createPostSignature(request);

        signature.setAccessKey(self.getAccessKey())
                .setContentType("text/plain")
                .setAcl("public-read")
                .setEndpoint(StringUtil.concat("http://", self.getBucket(), self.getEndpoint(), "/"))
                .setPolicy(response.getPolicy())
                .setToken(response.getToken())
                .setSignature(response.getSignature());

        return signature;
    }

    @Override
    public <T> CloudFile store(UploadInfo<T> uploadInfo) {
        String filename = UUID.randomUUID().toString();
        String fileKey = StringUtil.concat( self.getStorepath(), "/", filename, ".", uploadInfo.getFileExtension());

        ObsClient obs = this.obsClient();

        try {
            if (uploadInfo.getContents() instanceof byte[]) {
                obs.putObject(self.getBucket(), fileKey, new ByteArrayInputStream((byte[]) uploadInfo.getContents()));
            } else if (uploadInfo.getContents() instanceof File) {
                obs.putObject(self.getBucket(), fileKey, (File) uploadInfo.getContents());
            }
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

        return createCloudFile(self, fileKey, filename, uploadInfo.getContentsLength(), CloudType.HUAWEI);
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

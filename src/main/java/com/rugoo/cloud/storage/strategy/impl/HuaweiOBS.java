package com.rugoo.cloud.storage.strategy.impl;

import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.PostSignatureRequest;
import com.obs.services.model.PostSignatureResponse;
import com.rugoo.cloud.storage.CloudStorageStrategy;
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

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Description
 *
 * @author boxin
 * @date 2020-12-29
 * @see com.rugoo.cloud.storage.strategy.impl
 */
@MarkAsCloudStorage(type = CloudType.HUAWEI)
public class HuaweiOBS implements CloudStorageStrategy, CustomCloudFileCreator {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(HuaweiOBS.class);

    @Resource
    private CloudStorageProperties properties;

    private CloudStorageProperties.HuaweiConfig config;

    public HuaweiOBS() {
        if (properties.getHuaweiConfig() == null) {
            throw new CloudStorageConfigurationException("HuaweiOBS config unset");
        }
        this.config = properties.getHuaweiConfig();
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

        signature.setAccessKey(config.getAccessKey())
                .setContentType("text/plain")
                .setAcl("public-read")
                .setEndpoint(StringUtil.concat("http://", config.getBucket(), config.getEndpoint(), "/"))
                .setPolicy(response.getPolicy())
                .setToken(response.getToken())
                .setSignature(response.getSignature());

        return signature;
    }

    @Override
    public <T> CloudFile store(UploadInfo<T> uploadInfo) {
        String filename = UUID.randomUUID().toString();
        String fileKey = StringUtil.concat( config.getStorepath(), "/", filename, ".", uploadInfo.getFileExtension());

        ObsClient obs = this.obsClient();

        try {
            if (uploadInfo.getContents() instanceof byte[]) {
                obs.putObject(config.getBucket(), fileKey, new ByteArrayInputStream((byte[]) uploadInfo.getContents()));
            } else if (uploadInfo.getContents() instanceof File) {
                obs.putObject(config.getBucket(), fileKey, (File) uploadInfo.getContents());
            }
        } catch (ObsException obsEx) {
            // 防止污染存储源
            obs.deleteObject(config.getBucket(), fileKey);
            throw new CloudStorageException("Upload failed with unexpected exception", obsEx);
        } finally {
            try {
                obs.close();
            } catch (IOException ignored) {
            }
        }

        return createCloudFile(config, fileKey, filename, uploadInfo, CloudType.HUAWEI);
    }

    @Override
    public InputStream getFileContents(String objectId) {
        return obsClient().getObject(config.getBucket(), objectId).getObjectContent();
    }

    @Override
    public boolean delete(String objectId) {
        try (ObsClient obs = obsClient()){
            obs.deleteObject(config.getBucket(), objectId);
            return true;
        } catch (ObsException | IOException e) {
            if (log.isDebugEnabled()) {
                log.error("Failed delete HuaweiObs file {}::{}", config.getBucket(), objectId);
            } else {
                log.info("Failed delete HuaweiObs file {}::{}", config.getBucket(), objectId);
            }
            return false;
        }
    }

    private ObsClient obsClient() {
        return new ObsClient(config.getAccessKey(), config.getSecretKey(), config.getEndpoint());
    }

}

package com.rugoo.cloud.storage;

import com.rugoo.cloud.storage.common.ClientSign;
import com.rugoo.cloud.storage.common.CloudFile;
import com.rugoo.cloud.storage.common.UploadInfo;

import java.io.InputStream;

/**
 * 标记存储能力
 *
 * @author boxin
 * @date 2020-12-29
 * @see CloudStorageStrategy
 */
public interface CloudStorageStrategy {
    /**
     * 客户端上传密令
     *
     * @return 密令
     */
    ClientSign createPostSignature();

    /**
     * 存储
     *
     * @param uploadInfo
     * @return
     */
    <T> CloudFile store(UploadInfo<T> uploadInfo);

    /**
     * 读取文件流
     *
     * @param objectId
     * @return
     */
    InputStream getFileContents(String objectId);

    /**
     * 删除操作
     *
     * @param objectId
     * @return
     */
    boolean delete(String objectId);
}

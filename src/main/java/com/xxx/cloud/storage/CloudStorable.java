package com.xxx.cloud.storage;

import com.xxx.cloud.storage.common.CloudFile;

import java.io.File;
import java.io.InputStream;

/**
 * 标记存储能力
 *
 * @author boxin
 * @date 2020-12-29
 * @see CloudStorable
 */
public interface CloudStorable {

    /**
     * 云存储文件后返回访问路径
     *
     * @param fileContents byte array
     * @return {@link CloudFile}
     */
    CloudFile store(byte[] fileContents);

    /**
     * 存储
     *
     * @param file
     * @return
     */
    CloudFile store(File file);

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

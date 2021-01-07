package com.rugoo.cloud.storage;

import com.rugoo.cloud.storage.common.CloudFile;
import com.rugoo.cloud.storage.common.UploadInfo;
import com.rugoo.cloud.storage.exception.CloudStorageInitializingException;


/**
 * Description
 *
 * @author boxin
 * @date 2020-12-29
 * @see IFileService
 */
public interface IFileService {

    default <T> CloudFile upload(UploadInfo<T> uploadInfo) {
        throw new CloudStorageInitializingException("This method did not initialized");
    }

    default byte[] getContents(final String objectId) {
        throw new CloudStorageInitializingException("This method did not initialized");
    }

    default boolean delete(final String objectId) {
        throw new CloudStorageInitializingException("This method did not initialized");
    }
}

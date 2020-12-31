package com.xxx.cloud.storage;

import com.xxx.cloud.storage.common.CloudFile;
import com.xxx.cloud.storage.exception.CloudStorageInitializingException;

import java.io.File;

/**
 * Description
 *
 * @author boxin
 * @date 2020-12-29
 * @see IFileService
 */
public interface IFileService {

    default CloudFile upload(final byte[] contents) {
        throw new CloudStorageInitializingException("This method did not initialized");
    }

    default CloudFile upload(final File file) {
        throw new CloudStorageInitializingException("This method did not initialized");
    }

    default byte[] getContents(final String objectId) {
        throw new CloudStorageInitializingException("This method did not initialized");
    }

    default boolean delete(final String objectId) {
        throw new CloudStorageInitializingException("This method did not initialized");
    }
}

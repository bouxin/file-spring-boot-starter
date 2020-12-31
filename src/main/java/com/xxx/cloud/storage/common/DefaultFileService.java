package com.xxx.cloud.storage.common;

import com.xxx.cloud.storage.IFileService;
import com.xxx.cloud.storage.config.MyunCloudStorageProperties;
import com.xxx.cloud.storage.exception.CloudStorageException;
import com.xxx.cloud.storage.exception.CloudStorageIOException;
import com.xxx.cloud.storage.strategy.StoreStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Description
 *
 * @author boxin
 * @date 2020-12-30
 * @see com.xxx.cloud.storage.common
 */
public class DefaultFileService implements IFileService {

    @Autowired
    private MyunCloudStorageProperties properties;

    @Override
    public CloudFile upload(final byte[] contents) {
        check(contents);
        return StoreStrategy.create(properties).store(contents);
    }

    @Override
    public CloudFile upload(final File file) {
        check(file);
        return StoreStrategy.create(properties).store(file);
    }

    @Override
    public byte[] getContents(final String objectId) {
        check(objectId);
        InputStream fileStream = StoreStrategy.create(properties).getFileContents(objectId);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024]; int len;

        try {
            while ((len = fileStream.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            throw new CloudStorageIOException(e);
        } finally {
            try {
                fileStream.close();
                bos.close();
            } catch (IOException ignored) {
            }
        }

        return bos.toByteArray();
    }

    @Override
    public boolean delete(String objectId) {
        check(objectId);
        return StoreStrategy.create(properties).delete(objectId);
    }

    private void check(byte[] contents) {
        if (contents == null || contents.length < 1) {
            throw new CloudStorageException("Upload request without content!");
        }

        if (properties.getMaxFileSize() != 0 && contents.length > properties.getMaxFileSize()) {
            throw new CloudStorageException("Max file contents limited!");
        }
    }

    private void check(File file) {
        if (file == null || !file.exists()) {
            throw new CloudStorageException("Unknown file passed!");
        }
    }

    private void check(String objectId) {
        if (objectId == null || objectId.trim().length() < 1) {
            throw new CloudStorageException("Invalid objectId");
        }
    }
}

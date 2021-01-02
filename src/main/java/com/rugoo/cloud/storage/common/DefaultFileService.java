package com.rugoo.cloud.storage.common;

import com.rugoo.cloud.storage.IFileService;
import com.rugoo.cloud.storage.config.CloudStorageProperties;
import com.rugoo.cloud.storage.exception.CloudStorageException;
import com.rugoo.cloud.storage.exception.CloudStorageIOException;
import com.rugoo.cloud.storage.strategy.StoreStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Description
 *
 * @author boxin
 * @date 2020-12-30
 * @see com.rugoo.cloud.storage.common
 */
public class DefaultFileService implements IFileService {

    @Autowired
    private CloudStorageProperties properties;

    @Override
    public <T> CloudFile upload(UploadInfo<T> uploadInfo) {
        Assert.state(uploadInfo != null && uploadInfo.getContents() != null, "Upload request without content!");

        if (uploadInfo.getContents() instanceof byte[]) {
            Assert.state(((Byte[]) uploadInfo.getContents()).length < properties.getMaxFileSize().toBytes(), "Max file contents limited!");
        } else if (uploadInfo.getContents() instanceof File) {
            Assert.state(((File) uploadInfo.getContents()).exists(), "Uploading file not exists!");
            Assert.state(((File) uploadInfo.getContents()).length() < properties.getMaxFileSize().toBytes(), "Max file contents limited!");
        }

        return StoreStrategy.create(properties).store(uploadInfo);
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

    private void check(final String objectId) {
        Assert.state(objectId != null && objectId.trim().length() > 0, "Invalid objectId");
    }
}

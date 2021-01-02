package com.rugoo.cloud.storage.strategy.groups;

import com.rugoo.cloud.storage.CloudStorable;
import com.rugoo.cloud.storage.CustomCloudFileCreator;
import com.rugoo.cloud.storage.common.ClientSign;
import com.rugoo.cloud.storage.common.UploadInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rugoo.cloud.storage.anno.MarkAsCloudStorage;
import com.rugoo.cloud.storage.common.CloudFile;

import java.io.File;
import java.io.InputStream;

/**
 * Description
 *
 * @author boxin
 * @date 2020-12-31
 * @see com.rugoo.cloud.storage.strategy.groups
 */
@MarkAsCloudStorage
public class DefaultCloud implements CloudStorable, CustomCloudFileCreator {

    private static final Logger log = LoggerFactory.getLogger(DefaultCloud.class);

    @Override
    public ClientSign createPostSignature() {
        return null;
    }

    @Override
    public <T> CloudFile store(UploadInfo<T> uploadInfo) {
        return null;
    }

    @Override
    public InputStream getFileContents(String objectId) {
        return null;
    }

    @Override
    public boolean delete(String objectId) {
        return false;
    }
}

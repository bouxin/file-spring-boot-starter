package com.xxx.cloud.storage.strategy.groups;

import com.xxx.cloud.storage.CloudStorable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.xxx.cloud.storage.anno.ActiveCloud;
import com.xxx.cloud.storage.common.CloudFile;

import java.io.File;
import java.io.InputStream;

/**
 * Description
 *
 * @author boxin
 * @date 2020-12-31
 * @see com.xxx.cloud.storage.strategy.groups
 */
@ActiveCloud
public class DefaultCloud implements CloudStorable {

    private static final Logger log = LoggerFactory.getLogger(DefaultCloud.class);

    @Override
    public CloudFile store(byte[] fileContents) {
        return null;
    }

    @Override
    public CloudFile store(File file) {
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

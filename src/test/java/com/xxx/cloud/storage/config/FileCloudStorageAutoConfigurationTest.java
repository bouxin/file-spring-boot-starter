package com.xxx.cloud.storage.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;


/**
 * Description
 *
 * @author boxin
 * @date 2020-12-29
 * @see com.xxx.cloud.storage.config
 */
public class FileCloudStorageAutoConfigurationTest {

    @Test
    public void test_readYamlConfiguration_success() {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("application.yml"));

        Assertions.assertNotNull(yaml.getObject());
        Assertions.assertTrue(yaml.getObject().size() > 0);
    }
}
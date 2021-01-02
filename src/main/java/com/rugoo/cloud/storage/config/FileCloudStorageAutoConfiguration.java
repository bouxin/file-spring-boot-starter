package com.rugoo.cloud.storage.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.rugoo.cloud.storage.common.DefaultFileService;

/**
 * Description
 *
 * @author boxin
 * @date 2020-12-29
 * @see com.rugoo.cloud.storage.config
 */
@Configuration
@EnableConfigurationProperties(CloudStorageProperties.class)
public class FileCloudStorageAutoConfiguration {

    public final CloudStorageProperties properties;

    public FileCloudStorageAutoConfiguration(CloudStorageProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultFileService defaultFileService() {
        return new DefaultFileService();
    }

    @Bean
    @ConditionalOnMissingBean
    public CloudStorageProperties properties() {
        return this.properties;
    }

}

package com.xxx.cloud.storage.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.xxx.cloud.storage.common.DefaultFileService;

/**
 * Description
 *
 * @author boxin
 * @date 2020-12-29
 * @see com.xxx.cloud.storage.config
 */
@Configuration
@EnableConfigurationProperties(MyunCloudStorageProperties.class)
public class FileCloudStorageAutoConfiguration {

    public final MyunCloudStorageProperties properties;

    public FileCloudStorageAutoConfiguration(MyunCloudStorageProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultFileService defaultFileService() {
        return new DefaultFileService();
    }

    @Bean
    @ConditionalOnMissingBean
    public MyunCloudStorageProperties properties() {
        return this.properties;
    }

}

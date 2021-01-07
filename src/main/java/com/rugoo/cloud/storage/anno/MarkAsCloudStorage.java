package com.rugoo.cloud.storage.anno;

import com.rugoo.cloud.storage.enums.CloudType;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description
 *
 * @author boxin
 * @date 2020-12-30
 * @see com.rugoo.cloud.storage.anno
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface MarkAsCloudStorage {
    String value() default "";

    CloudType type() default CloudType.DEFAULT;
}

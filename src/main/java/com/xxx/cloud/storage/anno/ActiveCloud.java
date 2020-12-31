package com.xxx.cloud.storage.anno;

import com.xxx.cloud.storage.enums.CloudType;

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
 * @see com.xxx.cloud.storage.anno
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ActiveCloud {
    String value() default "";

    CloudType type() default CloudType.DEFAULT;
}

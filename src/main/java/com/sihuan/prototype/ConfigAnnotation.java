package com.sihuan.prototype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConfigAnnotation {

    //配置的ID
    String value();

    enum Format {XML, JSON, YML, PROPERTIES}

    //配置文件的类型
    Format format() default Format.PROPERTIES;
}

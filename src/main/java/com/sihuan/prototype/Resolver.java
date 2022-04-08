package com.sihuan.prototype;

import java.util.Map;

/**
 * 解析配置文件.
 * 处于demo目的，这里只提供将其转化为Map的接口，用于支持K-V配置的解析，例如java Properties文件解析.
 */
public interface Resolver {
    Map<String, String> resolve(String config);
}

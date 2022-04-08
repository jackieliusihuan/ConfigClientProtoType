package com.sihuan.prototype;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import static com.sihuan.prototype.ConfigAnnotation.Format;

public class ResolverFactory {

    private static final Map<Format, Resolver> resolvers = new ConcurrentHashMap<>();

    public static Resolver getResolver(Format format) {
        switch (format) {
            case XML:
                //TBD
                break;
            case JSON:
                //TBD
                break;
            case YML:
                //TBD
                break;
            case PROPERTIES:
                resolvers.putIfAbsent(Format.PROPERTIES, new PropertiesResolver());
                return resolvers.get(Format.PROPERTIES);
        }
        throw new IllegalStateException("不支持这种格式: " + format);
    }

    public static class PropertiesResolver implements Resolver {
        @Override
        public Map<String, String> resolve(String config) {

            final Properties properties = new Properties();
            try {
                properties.load(new ByteArrayInputStream(config.getBytes(StandardCharsets.UTF_8)));
            } catch (IOException e) {
                throw new IllegalStateException("不能解析Config");
            }
            return (Map) properties;
        }
    }
}

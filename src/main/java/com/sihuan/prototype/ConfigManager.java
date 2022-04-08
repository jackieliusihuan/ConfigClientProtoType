package com.sihuan.prototype;

import com.sihuan.prototype.ConfigAnnotation.Format;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * delegate ConfigClient 来更新内存中的配置
 * 当配置发生改变时通知对应的实例
 */
@Component
public class ConfigManager {

    private final ConfigClient configClient;

    private final Map<String, String> cache = new ConcurrentHashMap<>();

    private final Map<String, Map.Entry<Object, Format>> observers = new ConcurrentHashMap<>();

    public ConfigManager(ConfigClient configClient) {
        this.configClient = configClient;

        final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            try {
                cache.forEach(
                        (configId, oldConfig) -> {
                            final String newConfig = configClient.getConfig(configId);
                            if (!oldConfig.equals(newConfig)) {
                                cache.put(configId, newConfig);
                                notifyObserver(configId, newConfig);
                            }
                        });
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public String getConfig(final String configId) {
        if (cache.containsKey(configId)) {
            return cache.get(configId);
        } else {
            final String config = configClient.getConfig(configId);
            cache.put(configId, config);
            return config;
        }
    }

    public void register(final String configId, final Object bean, final Format format) {
        observers.put(configId, new AbstractMap.SimpleEntry<Object, Format>(bean, format));
    }

    private void notifyObserver(String configId, String config) {
        if (observers.containsKey(configId)) {
            Object bean = observers.get(configId).getKey();
            Format format = observers.get(configId).getValue();
            final Map<String, String> resolvedProperties = ResolverFactory.getResolver(format).resolve(config);
            final Class<?> cls = bean.getClass();
            final Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                final String fieldName = field.getName();
                final String fieldValue = resolvedProperties.get(fieldName);
                if (fieldValue != null) {
                    try {
                        field.set(bean, fieldValue);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                field.setAccessible(false);
            }
        }
    }
}

package com.sihuan.prototype;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;

@Component
public class ConfigAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor {

    private final ConfigManager configManager;

    public ConfigAnnotationBeanPostProcessor(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {

        final Class<?> cls = bean.getClass();

        if (cls.isAnnotationPresent(ConfigAnnotation.class)) {
            final ConfigAnnotation configAnnotation = cls.getAnnotation(ConfigAnnotation.class);

            final String config = configManager.getConfig(configAnnotation.value());
            final Map<String, String> resolvedProperties = ResolverFactory.getResolver(configAnnotation.format())
                    .resolve(config);

            //简便起见，不考虑父类的属性
            final Field[] fields = cls.getDeclaredFields();
            final MutablePropertyValues mutablePropertyValues = new MutablePropertyValues(pvs);
            for (Field field : fields) {
                final String fieldName = field.getName();
                final String fieldValue = resolvedProperties.get(fieldName);
                if (fieldValue != null) {
                    mutablePropertyValues.add(fieldName, fieldValue);
                }
            }
            return mutablePropertyValues;
        }

        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        final Class<?> cls = bean.getClass();
        if (cls.isAnnotationPresent(ConfigAnnotation.class)) {
            final ConfigAnnotation configAnnotation = cls.getAnnotation(ConfigAnnotation.class);
            configManager.register(configAnnotation.value(), bean, configAnnotation.format());
        }
        return bean;
    }
}

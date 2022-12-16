package com.snippet.spring.util;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

@Slf4j
public class PropertyUtils {

    private static Properties properties;

    static {
        String propertyName = "common.properties";
        ClassPathResource resource = new ClassPathResource(propertyName);
        try {
            properties = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            log.error("Read properties [{}] failed.", propertyName);
        }
    }

    public static String getString(@NonNull String key) {
        return properties.getProperty(key, "");
    }

    public static Boolean getBoolean(@NonNull String key) {
        return Boolean.parseBoolean(properties.getProperty(key, "false"));
    }

    public static Integer getInteger(@NonNull String key) {
        return Integer.parseInt(properties.getProperty(key, "0"));
    }
}

package com.assessment.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {
    private static final String CONFIG_FILE = "config.properties";
    private static final Properties PROPERTIES = loadProperties(CONFIG_FILE);

    private ConfigReader() {
    }

    public static String get(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }

        String propertyValue = PROPERTIES.getProperty(key);
        if (propertyValue == null || propertyValue.isBlank()) {
            throw new IllegalArgumentException("Missing required configuration value: " + key);
        }
        return propertyValue;
    }

    public static String getOptional(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null) {
            return systemValue;
        }
        return PROPERTIES.getProperty(key, "");
    }

    public static long getLong(String key) {
        return Long.parseLong(get(key));
    }

    private static Properties loadProperties(String fileName) {
        Properties properties = new Properties();
        try (InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IllegalStateException("Unable to find " + fileName + " on the classpath.");
            }
            properties.load(inputStream);
            return properties;
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load " + fileName, exception);
        }
    }
}

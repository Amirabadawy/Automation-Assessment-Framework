package com.assessment.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class TestDataReader {
    private static final String TEST_DATA_FILE = "test-data/test-data.properties";
    private static final Properties TEST_DATA = loadProperties();

    private TestDataReader() {
    }

    public static String get(String key) {
        String value = TEST_DATA.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing required test data value: " + key);
        }
        return value;
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = TestDataReader.class.getClassLoader().getResourceAsStream(TEST_DATA_FILE)) {
            if (inputStream == null) {
                throw new IllegalStateException("Unable to find " + TEST_DATA_FILE + " on the classpath.");
            }
            properties.load(inputStream);
            return properties;
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load " + TEST_DATA_FILE, exception);
        }
    }
}

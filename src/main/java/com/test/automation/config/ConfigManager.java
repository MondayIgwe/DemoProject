package com.test.automation.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Manages configuration properties for the test framework.
 * Loads properties from a configuration file and provides methods to access them.
 */
public class ConfigManager {
    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    private static ConfigManager instance;
    private final Properties properties;
    
    private ConfigManager() {
        properties = new Properties();
        try {
            // Load default properties first
            loadProperties("src/test/resources/config.properties");
            
            // Check if environment-specific properties file exists and load it
            String env = System.getProperty("env", "qa");
            String envPropertiesPath = "src/test/resources/" + env + ".properties";
            loadProperties(envPropertiesPath);
            
            logger.info("Configuration loaded successfully");
        } catch (IOException e) {
            logger.error("Failed to load configuration properties", e);
        }
    }
    
    /**
     * Loads properties from the specified file path
     */
    private void loadProperties(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            properties.load(fis);
            logger.info("Loaded properties from: {}", filePath);
        } catch (IOException e) {
            logger.warn("Could not load properties file: {}", filePath);
            throw e;
        }
    }
    
    /**
     * Returns the singleton instance of ConfigManager
     */
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }
    
    /**
     * Gets a property value as String
     */
    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Property not found: {}", key);
        }
        return value;
    }
    
    /**
     * Gets a property value with a default fallback
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Gets a property value as boolean
     */
    public boolean getBooleanProperty(String key) {
        String value = getProperty(key);
        return Boolean.parseBoolean(value);
    }
    
    /**
     * Gets a property value as integer
     */
    public int getIntProperty(String key) {
        String value = getProperty(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.error("Failed to parse integer property: {}", key, e);
            return 0;
        }
    }
}
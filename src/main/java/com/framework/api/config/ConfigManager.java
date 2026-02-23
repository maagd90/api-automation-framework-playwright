package com.framework.api.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton configuration manager that loads properties from
 * {@code src/test/resources/config/config.properties}.
 *
 * <p>System properties always take precedence over file-based properties,
 * enabling environment-specific overrides at runtime (e.g., on CI).
 */
public final class ConfigManager {

    private static final Logger LOG = LogManager.getLogger(ConfigManager.class);
    private static final String CONFIG_FILE = "config/config.properties";

    private static ConfigManager instance;
    private final Properties properties = new Properties();

    private ConfigManager() {
        loadProperties();
    }

    /**
     * Returns the singleton {@code ConfigManager} instance, creating it on the
     * first call.
     *
     * @return the singleton {@code ConfigManager}
     */
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // Property accessors
    // -------------------------------------------------------------------------

    /**
     * Returns the value for the given key, giving priority to system properties.
     *
     * @param key property key
     * @return property value, or {@code null} if not found
     */
    public String getProperty(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null) {
            return systemValue;
        }
        return properties.getProperty(key);
    }

    /**
     * Returns the value for the given key, or {@code defaultValue} when not found.
     *
     * @param key          property key
     * @param defaultValue fallback value
     * @return property value
     */
    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return (value != null) ? value : defaultValue;
    }

    /** Convenience: returns the configured base URL for the API under test. */
    public String getBaseUrl() {
        return getProperty("api.base.url");
    }

    /** Convenience: returns the configured request timeout in milliseconds. */
    public int getTimeoutMs() {
        return Integer.parseInt(getProperty("api.timeout.ms", "30000"));
    }

    // -------------------------------------------------------------------------
    // Internal helpers
    // -------------------------------------------------------------------------

    private void loadProperties() {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (stream == null) {
                LOG.warn("Config file not found on classpath: {}", CONFIG_FILE);
                return;
            }
            properties.load(stream);
            LOG.info("Configuration loaded from {}", CONFIG_FILE);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file: " + CONFIG_FILE, e);
        }
    }
}

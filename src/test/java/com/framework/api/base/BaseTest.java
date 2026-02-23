package com.framework.api.base;

import com.framework.api.client.APIClient;
import com.framework.api.client.APIClientFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for all API test classes.
 *
 * <p>Manages the lifecycle of {@link APIClientFactory} and exposes a shared
 * {@link APIClient} instance via the protected {@code apiClient} field.
 * Subclasses can override {@link #extraHeaders()} to inject suite-level headers
 * such as authentication tokens.
 */
public abstract class BaseTest {

    protected final Logger log = LogManager.getLogger(getClass());

    private APIClientFactory clientFactory;
    protected APIClient apiClient;

    @BeforeClass(alwaysRun = true)
    public void setUpSuite() {
        log.info("Setting up test suite: {}", getClass().getSimpleName());
        clientFactory = new APIClientFactory();
        apiClient = clientFactory.createClient(extraHeaders());
    }

    @AfterClass(alwaysRun = true)
    public void tearDownSuite() {
        log.info("Tearing down test suite: {}", getClass().getSimpleName());
        if (clientFactory != null) {
            clientFactory.dispose();
        }
    }

    /**
     * Override in subclasses to supply suite-level HTTP headers that will be
     * included on every request (e.g., {@code Authorization: Bearer <token>}).
     *
     * @return map of header name → value (empty by default)
     */
    protected Map<String, String> extraHeaders() {
        return new HashMap<>();
    }
}

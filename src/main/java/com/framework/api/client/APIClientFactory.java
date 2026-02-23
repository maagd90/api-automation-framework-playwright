package com.framework.api.client;

import com.microsoft.playwright.APIRequest.NewContextOptions;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import com.framework.api.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory that creates and disposes Playwright's {@link APIRequestContext}.
 *
 * <p>Tests obtain an {@link APIClient} through this factory:
 * <pre>
 *   APIClientFactory factory = new APIClientFactory();
 *   APIClient client = factory.createClient();
 *   // ... run tests ...
 *   factory.dispose();
 * </pre>
 */
public class APIClientFactory {

    private static final Logger LOG = LogManager.getLogger(APIClientFactory.class);

    private Playwright playwright;
    private APIRequestContext requestContext;

    /**
     * Creates an {@link APIClient} backed by a fresh Playwright request context.
     * The context is configured with the base URL and default headers from
     * {@code config.properties}.
     *
     * @return a ready-to-use {@link APIClient}
     */
    public APIClient createClient() {
        return createClient(new HashMap<>());
    }

    /**
     * Creates an {@link APIClient} with additional default headers merged on top of
     * the framework defaults (e.g., an Authorization token for authenticated suites).
     *
     * @param extraHeaders headers to add to every request in this context
     * @return a ready-to-use {@link APIClient}
     */
    public APIClient createClient(Map<String, String> extraHeaders) {
        ConfigManager config = ConfigManager.getInstance();
        String baseUrl = config.getBaseUrl();
        int timeoutMs = config.getTimeoutMs();

        LOG.info("Initializing Playwright APIRequestContext for base URL: {}", baseUrl);

        playwright = Playwright.create();

        Map<String, String> defaultHeaders = new HashMap<>();
        defaultHeaders.put("Content-Type", "application/json");
        defaultHeaders.put("Accept", "application/json");
        defaultHeaders.putAll(extraHeaders);

        requestContext = playwright.request().newContext(
                new NewContextOptions()
                        .setBaseURL(baseUrl)
                        .setExtraHTTPHeaders(defaultHeaders)
                        .setTimeout(timeoutMs)
        );

        return new APIClient(requestContext);
    }

    /**
     * Disposes the underlying Playwright resources.
     * Must be called after all tests in a suite have completed.
     */
    public void dispose() {
        if (requestContext != null) {
            requestContext.dispose();
            LOG.info("APIRequestContext disposed");
        }
        if (playwright != null) {
            playwright.close();
            LOG.info("Playwright instance closed");
        }
    }
}

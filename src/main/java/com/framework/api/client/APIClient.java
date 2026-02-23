package com.framework.api.client;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import com.framework.api.config.ConfigManager;
import com.framework.api.models.APIRequest;
import com.framework.api.utils.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Core API client that wraps Playwright's {@link APIRequestContext} to provide
 * a clean, reusable interface for all HTTP methods used in API automation.
 *
 * <p>Each method logs the outgoing request and incoming response so that
 * failures are easy to diagnose from the test report.
 */
public class APIClient {

    private static final Logger LOG = LogManager.getLogger(APIClient.class);

    private final APIRequestContext requestContext;
    private final String baseUrl;

    public APIClient(APIRequestContext requestContext) {
        this.requestContext = requestContext;
        this.baseUrl = ConfigManager.getInstance().getBaseUrl();
    }

    // -------------------------------------------------------------------------
    // HTTP methods
    // -------------------------------------------------------------------------

    /**
     * Sends an HTTP GET request.
     *
     * @param request the request descriptor
     * @return wrapped API response
     */
    public com.framework.api.models.APIResponse get(APIRequest request) {
        String url = buildUrl(request);
        LOG.info("GET  --> {}", url);

        RequestOptions options = buildOptions(request);
        APIResponse response = requestContext.get(url, options);

        return wrap(response);
    }

    /**
     * Sends an HTTP POST request.
     *
     * @param request the request descriptor (body is serialized to JSON)
     * @return wrapped API response
     */
    public com.framework.api.models.APIResponse post(APIRequest request) {
        String url = buildUrl(request);
        LOG.info("POST --> {}", url);
        logBody(request.getBody());

        RequestOptions options = buildOptions(request);
        if (request.getBody() != null) {
            options = options.setData(JsonUtils.toJson(request.getBody()));
        }
        APIResponse response = requestContext.post(url, options);

        return wrap(response);
    }

    /**
     * Sends an HTTP PUT request.
     *
     * @param request the request descriptor (body is serialized to JSON)
     * @return wrapped API response
     */
    public com.framework.api.models.APIResponse put(APIRequest request) {
        String url = buildUrl(request);
        LOG.info("PUT  --> {}", url);
        logBody(request.getBody());

        RequestOptions options = buildOptions(request);
        if (request.getBody() != null) {
            options = options.setData(JsonUtils.toJson(request.getBody()));
        }
        APIResponse response = requestContext.put(url, options);

        return wrap(response);
    }

    /**
     * Sends an HTTP PATCH request.
     *
     * @param request the request descriptor (body is serialized to JSON)
     * @return wrapped API response
     */
    public com.framework.api.models.APIResponse patch(APIRequest request) {
        String url = buildUrl(request);
        LOG.info("PATCH --> {}", url);
        logBody(request.getBody());

        RequestOptions options = buildOptions(request);
        if (request.getBody() != null) {
            options = options.setData(JsonUtils.toJson(request.getBody()));
        }
        APIResponse response = requestContext.patch(url, options);

        return wrap(response);
    }

    /**
     * Sends an HTTP DELETE request.
     *
     * @param request the request descriptor
     * @return wrapped API response
     */
    public com.framework.api.models.APIResponse delete(APIRequest request) {
        String url = buildUrl(request);
        LOG.info("DELETE --> {}", url);

        RequestOptions options = buildOptions(request);
        APIResponse response = requestContext.delete(url, options);

        return wrap(response);
    }

    // -------------------------------------------------------------------------
    // Internal helpers
    // -------------------------------------------------------------------------

    private String buildUrl(APIRequest request) {
        String endpoint = request.getEndpoint();
        if (endpoint.startsWith("http://") || endpoint.startsWith("https://")) {
            return endpoint;
        }
        return baseUrl + endpoint;
    }

    private RequestOptions buildOptions(APIRequest request) {
        RequestOptions options = RequestOptions.create();

        // Start with the default Content-Type, then apply request-level headers
        // (request-level headers take precedence and may override the default)
        Map<String, String> headers = request.getHeaders();
        Map<String, String> mergedHeaders = new LinkedHashMap<>();
        mergedHeaders.put("Content-Type", "application/json");
        if (headers != null) {
            mergedHeaders.putAll(headers);
        }
        for (Map.Entry<String, String> entry : mergedHeaders.entrySet()) {
            options = options.setHeader(entry.getKey(), entry.getValue());
        }

        // Apply query parameters
        Map<String, String> queryParams = request.getQueryParams();
        if (queryParams != null && !queryParams.isEmpty()) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                options = options.setQueryParam(entry.getKey(), entry.getValue());
            }
        }

        return options;
    }

    private com.framework.api.models.APIResponse wrap(APIResponse playwrightResponse) {
        int statusCode = playwrightResponse.status();
        String statusText = playwrightResponse.statusText();
        String body = playwrightResponse.text();
        Map<String, String> headers = playwrightResponse.headers();

        LOG.info("<-- {} {}", statusCode, statusText);
        LOG.debug("Response body: {}", JsonUtils.toPrettyJson(body));

        return new com.framework.api.models.APIResponse(statusCode, statusText, body, headers);
    }

    private void logBody(Object body) {
        if (body != null) {
            LOG.debug("Request body: {}", JsonUtils.toPrettyJson(body));
        }
    }
}

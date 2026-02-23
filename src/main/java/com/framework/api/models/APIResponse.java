package com.framework.api.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.framework.api.utils.JsonUtils;

/**
 * Wraps the HTTP response returned by Playwright's {@code APIResponse},
 * providing convenience methods for status code checking and body parsing.
 */
public class APIResponse {

    private final int statusCode;
    private final String statusText;
    private final String body;
    private final java.util.Map<String, String> headers;

    /**
     * Creates an {@code APIResponse} with the given HTTP response details.
     *
     * @param statusCode the HTTP status code (e.g., 200, 201, 404)
     * @param statusText the HTTP status text (e.g., "OK", "Created")
     * @param body       the raw response body as a string
     * @param headers    the response headers map
     */
    public APIResponse(int statusCode, String statusText, String body, java.util.Map<String, String> headers) {
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.body = body;
        this.headers = headers;
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    /**
     * Returns the HTTP status code of the response (e.g., 200, 404).
     *
     * @return HTTP status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Returns the HTTP status text of the response (e.g., "OK", "Not Found").
     *
     * @return HTTP status text
     */
    public String getStatusText() {
        return statusText;
    }

    /**
     * Returns the raw response body as a string.
     *
     * @return response body string
     */
    public String getBody() {
        return body;
    }

    /**
     * Returns the HTTP response headers as a name-to-value map.
     *
     * @return response headers map
     */
    public java.util.Map<String, String> getHeaders() {
        return headers;
    }

    // -------------------------------------------------------------------------
    // Convenience helpers
    // -------------------------------------------------------------------------

    /** Returns {@code true} when the HTTP status code is in the 2xx range. */
    public boolean isSuccessful() {
        return statusCode >= 200 && statusCode < 300;
    }

    /**
     * Deserializes the response body JSON into the given type.
     *
     * @param <T>   target type
     * @param clazz target class
     * @return deserialized object
     */
    public <T> T as(Class<T> clazz) {
        return JsonUtils.fromJson(body, clazz);
    }

    /**
     * Returns the response body as a Jackson {@link JsonNode} for dynamic
     * field extraction without a dedicated POJO.
     */
    public JsonNode asJsonNode() {
        return JsonUtils.toJsonNode(body);
    }

    @Override
    public String toString() {
        return "APIResponse{statusCode=" + statusCode
                + ", statusText='" + statusText + '\''
                + ", body='" + body + '\'' + '}';
    }
}

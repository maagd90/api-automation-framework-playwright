package com.framework.api.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an outgoing HTTP API request.
 * Use {@link APIRequest.Builder} to construct instances fluently.
 */
public class APIRequest {

    private final String endpoint;
    private final Map<String, String> headers;
    private final Map<String, String> queryParams;
    private final Object body;

    /**
     * Creates an {@code APIRequest} from the given builder.
     *
     * @param builder the builder containing the request configuration
     */
    private APIRequest(Builder builder) {
        this.endpoint = builder.endpoint;
        this.headers = builder.headers;
        this.queryParams = builder.queryParams;
        this.body = builder.body;
    }

    /**
     * Returns the target endpoint path or URL (e.g., {@code /posts/1}).
     *
     * @return endpoint string
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * Returns the map of HTTP headers to include in the request.
     *
     * @return headers map, never {@code null}
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Returns the map of query parameters to append to the URL.
     *
     * @return query parameters map, never {@code null}
     */
    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    /**
     * Returns the request body object that will be serialized to JSON,
     * or {@code null} if no body is set.
     *
     * @return request body, or {@code null}
     */
    public Object getBody() {
        return body;
    }

    // -------------------------------------------------------------------------
    // Fluent builder
    // -------------------------------------------------------------------------

    /**
     * Creates a new {@link Builder} for the given endpoint.
     *
     * @param endpoint the target endpoint path or absolute URL
     * @return a new {@link Builder} instance
     */
    public static Builder builder(String endpoint) {
        return new Builder(endpoint);
    }

    /**
     * Fluent builder for constructing {@link APIRequest} instances.
     */
    public static final class Builder {

        private final String endpoint;
        private final Map<String, String> headers = new HashMap<>();
        private final Map<String, String> queryParams = new HashMap<>();
        private Object body;

        /**
         * Creates a builder for the given endpoint.
         *
         * @param endpoint the target endpoint path or absolute URL; must not be blank
         * @throws IllegalArgumentException if the endpoint is null or blank
         */
        private Builder(String endpoint) {
            if (endpoint == null || endpoint.isBlank()) {
                throw new IllegalArgumentException("Endpoint must not be null or blank");
            }
            this.endpoint = endpoint;
        }

        /**
         * Adds a single HTTP request header.
         *
         * @param name  header name
         * @param value header value
         * @return this builder
         */
        public Builder header(String name, String value) {
            this.headers.put(name, value);
            return this;
        }

        /**
         * Adds multiple HTTP request headers from the given map.
         *
         * @param headers map of header name to value
         * @return this builder
         */
        public Builder headers(Map<String, String> headers) {
            this.headers.putAll(headers);
            return this;
        }

        /**
         * Adds a single query parameter to the request URL.
         *
         * @param name  parameter name
         * @param value parameter value
         * @return this builder
         */
        public Builder queryParam(String name, String value) {
            this.queryParams.put(name, value);
            return this;
        }

        /**
         * Adds multiple query parameters to the request URL.
         *
         * @param params map of parameter name to value
         * @return this builder
         */
        public Builder queryParams(Map<String, String> params) {
            this.queryParams.putAll(params);
            return this;
        }

        /**
         * Sets the request body. The body will be serialized to JSON before
         * being sent.
         *
         * @param body the body object to serialize; {@code null} means no body
         * @return this builder
         */
        public Builder body(Object body) {
            this.body = body;
            return this;
        }

        /**
         * Builds and returns the {@link APIRequest}.
         *
         * @return a new immutable {@link APIRequest}
         */
        public APIRequest build() {
            return new APIRequest(this);
        }
    }
}

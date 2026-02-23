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

    private APIRequest(Builder builder) {
        this.endpoint = builder.endpoint;
        this.headers = builder.headers;
        this.queryParams = builder.queryParams;
        this.body = builder.body;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public Object getBody() {
        return body;
    }

    // -------------------------------------------------------------------------
    // Fluent builder
    // -------------------------------------------------------------------------

    public static Builder builder(String endpoint) {
        return new Builder(endpoint);
    }

    public static final class Builder {

        private final String endpoint;
        private final Map<String, String> headers = new HashMap<>();
        private final Map<String, String> queryParams = new HashMap<>();
        private Object body;

        private Builder(String endpoint) {
            if (endpoint == null || endpoint.isBlank()) {
                throw new IllegalArgumentException("Endpoint must not be null or blank");
            }
            this.endpoint = endpoint;
        }

        public Builder header(String name, String value) {
            this.headers.put(name, value);
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers.putAll(headers);
            return this;
        }

        public Builder queryParam(String name, String value) {
            this.queryParams.put(name, value);
            return this;
        }

        public Builder queryParams(Map<String, String> params) {
            this.queryParams.putAll(params);
            return this;
        }

        public Builder body(Object body) {
            this.body = body;
            return this;
        }

        public APIRequest build() {
            return new APIRequest(this);
        }
    }
}

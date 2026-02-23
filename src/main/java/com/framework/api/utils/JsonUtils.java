package com.framework.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Singleton utility class for JSON serialization and deserialization
 * using the Jackson library.
 */
public final class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    private JsonUtils() {
        // Utility class — no instantiation
    }

    /**
     * Serializes any Java object to its JSON string representation.
     *
     * @param object the object to serialize
     * @return JSON string
     * @throws RuntimeException if serialization fails
     */
    public static String toJson(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }

    /**
     * Deserializes a JSON string into the given target type.
     *
     * @param <T>     target type
     * @param json    JSON string to parse
     * @param clazz   target class
     * @return deserialized object
     * @throws RuntimeException if deserialization fails
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize JSON to " + clazz.getSimpleName(), e);
        }
    }

    /**
     * Parses a JSON string into a Jackson {@link JsonNode} for dynamic field access.
     *
     * @param json JSON string
     * @return {@link JsonNode} tree
     * @throws RuntimeException if parsing fails
     */
    public static JsonNode toJsonNode(String json) {
        try {
            return MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse JSON into JsonNode", e);
        }
    }

    /**
     * Returns a pretty-printed JSON string for the given object.
     * Useful for logging request / response bodies.
     *
     * @param object the object to format
     * @return indented JSON string
     */
    public static String toPrettyJson(Object object) {
        try {
            if (object instanceof String str) {
                // Re-parse and pretty-print an already-serialized JSON string
                JsonNode node = MAPPER.readTree(str);
                return MAPPER.writeValueAsString(node);
            }
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return String.valueOf(object);
        }
    }
}

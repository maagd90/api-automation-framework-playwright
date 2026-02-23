package com.framework.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Post resource from the JSONPlaceholder REST API
 * (https://jsonplaceholder.typicode.com/posts).
 *
 * <p>Used as both request body and response payload in the example test suite.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Post {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("userId")
    private Integer userId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("body")
    private String body;

    public Post() {
        // Required by Jackson
    }

    public Post(Integer userId, String title, String body) {
        this.userId = userId;
        this.title = title;
        this.body = body;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Post{id=" + id + ", userId=" + userId
                + ", title='" + title + '\'' + ", body='" + body + '\'' + '}';
    }
}

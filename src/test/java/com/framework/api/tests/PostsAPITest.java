package com.framework.api.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.framework.api.base.BaseTest;
import com.framework.api.models.APIRequest;
import com.framework.api.models.APIResponse;
import com.framework.api.models.Post;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * End-to-end API tests for the /posts resource on JSONPlaceholder
 * (https://jsonplaceholder.typicode.com), demonstrating GET, POST, PUT,
 * PATCH, and DELETE operations.
 *
 * <p>JSONPlaceholder is a free, publicly available REST API that returns
 * predictable fake data — ideal for framework validation without requiring
 * credentials or a dedicated test environment.
 */
@Epic("API Automation Framework")
@Feature("Posts Resource")
public class PostsAPITest extends BaseTest {

    private static final String POSTS_ENDPOINT = "/posts";
    private static final int EXISTING_POST_ID = 1;

    // -------------------------------------------------------------------------
    // GET — retrieve resources
    // -------------------------------------------------------------------------

    @Test(description = "GET /posts — should return a list of posts")
    @Story("Retrieve all posts")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that fetching all posts returns HTTP 200 and a non-empty JSON array.")
    public void getAllPosts_shouldReturnNonEmptyList() {
        APIRequest request = APIRequest.builder(POSTS_ENDPOINT).build();

        APIResponse response = apiClient.get(request);

        assertThat(response.getStatusCode())
                .as("HTTP status for GET /posts")
                .isEqualTo(200);

        JsonNode body = response.asJsonNode();
        assertThat(body.isArray()).as("Response should be a JSON array").isTrue();
        assertThat(body.size()).as("Post list should not be empty").isGreaterThan(0);

        log.info("GET /posts returned {} posts", body.size());
    }

    @Test(description = "GET /posts/{id} — should return a single post by ID")
    @Story("Retrieve post by ID")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that fetching a post by its ID returns HTTP 200 with the correct ID in the payload.")
    public void getPostById_shouldReturnCorrectPost() {
        String endpoint = POSTS_ENDPOINT + "/" + EXISTING_POST_ID;
        APIRequest request = APIRequest.builder(endpoint).build();

        APIResponse response = apiClient.get(request);
        Post post = response.as(Post.class);

        assertThat(response.getStatusCode())
                .as("HTTP status for GET /posts/1")
                .isEqualTo(200);
        assertThat(post.getId())
                .as("Returned post ID should match the requested ID")
                .isEqualTo(EXISTING_POST_ID);
        assertThat(post.getTitle())
                .as("Post title should not be blank")
                .isNotBlank();

        log.info("Fetched post: {}", post);
    }

    @Test(description = "GET /posts?userId=1 — should filter posts by userId query param")
    @Story("Filter posts by userId")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that applying a userId query parameter returns only posts belonging to that user.")
    public void getPostsByUserId_shouldReturnFilteredList() {
        int targetUserId = 1;
        APIRequest request = APIRequest.builder(POSTS_ENDPOINT)
                .queryParam("userId", String.valueOf(targetUserId))
                .build();

        APIResponse response = apiClient.get(request);

        assertThat(response.getStatusCode())
                .as("HTTP status for GET /posts?userId=1")
                .isEqualTo(200);

        JsonNode posts = response.asJsonNode();
        assertThat(posts.isArray()).isTrue();
        posts.forEach(post ->
                assertThat(post.get("userId").asInt())
                        .as("Each post userId should equal the filter value")
                        .isEqualTo(targetUserId));

        log.info("GET /posts?userId={} returned {} posts", targetUserId, posts.size());
    }

    // -------------------------------------------------------------------------
    // POST — create a resource
    // -------------------------------------------------------------------------

    @Test(description = "POST /posts — should create a new post and return 201")
    @Story("Create a new post")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that creating a new post returns HTTP 201 and echoes the submitted fields back.")
    public void createPost_shouldReturn201WithCreatedResource() {
        Post newPost = new Post(1, "Framework Validation Post", "This post was created by the API automation framework.");
        APIRequest request = APIRequest.builder(POSTS_ENDPOINT)
                .body(newPost)
                .build();

        APIResponse response = apiClient.post(request);
        Post created = response.as(Post.class);

        assertThat(response.getStatusCode())
                .as("HTTP status for POST /posts")
                .isEqualTo(201);
        assertThat(created.getTitle())
                .as("Created post title should match the request")
                .isEqualTo(newPost.getTitle());
        assertThat(created.getBody())
                .as("Created post body should match the request")
                .isEqualTo(newPost.getBody());
        assertThat(created.getId())
                .as("Server should assign a new ID to the created post")
                .isNotNull();

        log.info("Created post with server-assigned ID: {}", created.getId());
    }

    // -------------------------------------------------------------------------
    // PUT — full replacement of a resource
    // -------------------------------------------------------------------------

    @Test(description = "PUT /posts/{id} — should fully replace a post and return 200")
    @Story("Replace a post")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that a PUT replaces all fields of an existing post and returns HTTP 200.")
    public void updatePost_PUT_shouldReturn200WithUpdatedResource() {
        Post updatedPost = new Post(1, "Updated Title via PUT", "Updated body content via PUT request.");
        updatedPost.setId(EXISTING_POST_ID);

        APIRequest request = APIRequest.builder(POSTS_ENDPOINT + "/" + EXISTING_POST_ID)
                .body(updatedPost)
                .build();

        APIResponse response = apiClient.put(request);
        Post result = response.as(Post.class);

        assertThat(response.getStatusCode())
                .as("HTTP status for PUT /posts/1")
                .isEqualTo(200);
        assertThat(result.getTitle())
                .as("Title should be replaced by the PUT request value")
                .isEqualTo(updatedPost.getTitle());
        assertThat(result.getBody())
                .as("Body should be replaced by the PUT request value")
                .isEqualTo(updatedPost.getBody());

        log.info("PUT /posts/{} succeeded: {}", EXISTING_POST_ID, result);
    }

    // -------------------------------------------------------------------------
    // PATCH — partial update of a resource
    // -------------------------------------------------------------------------

    @Test(description = "PATCH /posts/{id} — should partially update a post and return 200")
    @Story("Partially update a post")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that a PATCH updates only the supplied fields and returns HTTP 200.")
    public void updatePost_PATCH_shouldReturn200WithPatchedField() {
        // Only patching the title — body and userId remain unchanged on the server
        Map<String, String> partialUpdate = new HashMap<>();
        partialUpdate.put("title", "Patched Title via PATCH");

        APIRequest request = APIRequest.builder(POSTS_ENDPOINT + "/" + EXISTING_POST_ID)
                .body(partialUpdate)
                .build();

        APIResponse response = apiClient.patch(request);
        JsonNode result = response.asJsonNode();

        assertThat(response.getStatusCode())
                .as("HTTP status for PATCH /posts/1")
                .isEqualTo(200);
        assertThat(result.get("title").asText())
                .as("Patched title should reflect the submitted value")
                .isEqualTo("Patched Title via PATCH");

        log.info("PATCH /posts/{} — updated title: {}", EXISTING_POST_ID, result.get("title").asText());
    }

    // -------------------------------------------------------------------------
    // DELETE — remove a resource
    // -------------------------------------------------------------------------

    @Test(description = "DELETE /posts/{id} — should delete a post and return 200")
    @Story("Delete a post")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that deleting an existing post returns HTTP 200 and an empty JSON object.")
    public void deletePost_shouldReturn200() {
        APIRequest request = APIRequest.builder(POSTS_ENDPOINT + "/" + EXISTING_POST_ID).build();

        APIResponse response = apiClient.delete(request);

        assertThat(response.getStatusCode())
                .as("HTTP status for DELETE /posts/1")
                .isEqualTo(200);

        // JSONPlaceholder returns "{}" for a successful delete
        JsonNode body = response.asJsonNode();
        assertThat(body.isObject()).as("Delete response should be an empty JSON object").isTrue();
        assertThat(body.isEmpty()).as("Delete response object should be empty").isTrue();

        log.info("DELETE /posts/{} succeeded", EXISTING_POST_ID);
    }
}

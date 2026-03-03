# API Automation Framework — Playwright + Java

An enterprise-grade REST API automation framework built on **Playwright for Java**, **TestNG**, and **Allure**. The framework is generic and ready to integrate with any HTTP-based API.

---

## Objectives

This project aims to provide a robust, scalable, and maintainable framework for API automation testing with the following key objectives:

- **Simplify API Testing** — Provide a fluent, developer-friendly API for writing REST API tests without boilerplate
- **Enterprise-Grade Quality** — Leverage industry-standard tools (Playwright, TestNG, Allure) for reliable, professional-grade test automation
- **Framework Reusability** — Build a generic, configuration-driven framework that can be easily adapted to any HTTP-based API
- **Comprehensive Reporting** — Integrate Allure reporting to generate detailed, visual test execution reports
- **Best Practices** — Implement SOLID principles, clean code patterns, and testing best practices
- **Easy Integration** — Provide clear documentation and examples to accelerate adoption and integration into CI/CD pipelines

---

## Technology Stack

| Layer | Library / Tool |
|---|---|
| HTTP client | [Playwright for Java](https://playwright.dev/java/) `APIRequestContext` |
| Test runner | [TestNG](https://testng.org/) |
| Assertions | [AssertJ](https://assertj.github.io/doc/) |
| JSON handling | [Jackson Databind](https://github.com/FasterXML/jackson) |
| Logging | [Log4j 2](https://logging.apache.org/log4j/2.x/) |
| Reporting | [Allure](https://allurereport.org/) |
| Build | [Apache Maven](https://maven.apache.org/) |

---

## Project Structure

```
api-automation-framework-playwright/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/framework/api/
    │   │   ├── client/
    │   │   │   ├── APIClient.java          # Core HTTP client (GET/POST/PUT/PATCH/DELETE)
    │   │   │   └── APIClientFactory.java   # Creates & disposes Playwright request context
    │   │   ├── config/
    │   │   │   └── ConfigManager.java      # Singleton config loader (properties + sys props)
    │   │   ├── models/
    │   │   │   ├── APIRequest.java         # Fluent request builder (endpoint, headers, body)
    │   │   │   └── APIResponse.java        # Response wrapper with typed deserialization
    │   │   └── utils/
    │   │       └── JsonUtils.java          # Jackson helpers (serialize / deserialize / pretty-print)
    │   └── resources/
    │       └── log4j2.xml                  # Console + file logging configuration
    └── test/
        ├── java/com/framework/api/
        │   ├── base/
        │   │   └── BaseTest.java           # TestNG @BeforeClass / @AfterClass lifecycle
        │   ├── models/
        │   │   └── Post.java               # POJO for JSONPlaceholder /posts resource
        │   └── tests/
        │       └── PostsAPITest.java        # Example tests: GET, POST, PUT, PATCH, DELETE
        └── resources/
            ├── config/
            │   └── config.properties       # Base URL, timeout, and other settings
            └── testng.xml                  # TestNG suite definition
```

---

## Prerequisites

- **Java 17+**
- **Apache Maven 3.8+**

---

## Quick Start

### 1. Clone the repository

```bash
git clone https://github.com/maagd90/api-automation-framework-playwright.git
cd api-automation-framework-playwright
```

### 2. Configure the target API

Edit `src/test/resources/config/config.properties`:

```properties
api.base.url=https://jsonplaceholder.typicode.com
api.timeout.ms=30000
```

You can also pass values at runtime without modifying the file:

```bash
mvn test -Dapi.base.url=https://your-api.example.com
```

### 3. Run all tests

```bash
mvn test
```

### 4. Generate an Allure report

```bash
mvn allure:report
# Open target/site/allure-maven-plugin/index.html in a browser
```

---

## Writing Tests

### Minimal test class

```java
public class OrdersAPITest extends BaseTest {

    @Test
    public void getOrder_shouldReturn200() {
        APIRequest request = APIRequest.builder("/orders/42").build();
        APIResponse response = apiClient.get(request);

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.asJsonNode().get("id").asInt()).isEqualTo(42);
    }
}
```

### Adding suite-level authentication

Override `extraHeaders()` in your test class to inject an `Authorization` header for every request:

```java
public class AuthenticatedTest extends BaseTest {

    @Override
    protected Map<String, String> extraHeaders() {
        return Map.of("Authorization", "Bearer " + System.getProperty("api.token"));
    }
}
```

### All supported HTTP methods

```java
// GET
APIResponse getResp   = apiClient.get(APIRequest.builder("/posts/1").build());

// POST
APIResponse postResp  = apiClient.post(APIRequest.builder("/posts").body(newPost).build());

// PUT
APIResponse putResp   = apiClient.put(APIRequest.builder("/posts/1").body(updatedPost).build());

// PATCH
APIResponse patchResp = apiClient.patch(APIRequest.builder("/posts/1").body(partial).build());

// DELETE
APIResponse delResp   = apiClient.delete(APIRequest.builder("/posts/1").build());
```

### Request customization

```java
APIRequest request = APIRequest.builder("/search")
    .header("X-Correlation-Id", UUID.randomUUID().toString())
    .queryParam("status", "active")
    .queryParam("page", "2")
    .body(searchCriteria)
    .build();
```

### Deserializing responses

```java
// Into a typed POJO
Post post = response.as(Post.class);

// Into a Jackson JsonNode for dynamic field access
JsonNode body = response.asJsonNode();
int id = body.get("id").asInt();
```

---

## Configuration Reference

| Property | Default | Description |
|---|---|---|
| `api.base.url` | — | Base URL prepended to all relative endpoints |
| `api.timeout.ms` | `30000` | Request timeout in milliseconds |

All properties can be overridden by JVM system properties (`-D<key>=<value>`).

---

## Logs

Test logs are written to:
- **Console** — all `DEBUG` and above for `com.framework.api`
- **File** — `target/logs/api-tests.log`
# Copilot Instructions

## Repository Overview

This is an **API automation testing framework** built with [Playwright for Java](https://playwright.dev/java/). It provides a structured approach to writing, organizing, and running automated API tests using Playwright's Java `APIRequestContext`.

- **Language**: Java 17+
- **Build tool**: Apache Maven
- **Test runner**: TestNG
- **HTTP client**: Playwright for Java (`APIRequestContext`)
- **Assertions**: AssertJ
- **Reporting**: Allure

## Project Setup

```bash
# Install dependencies and compile
mvn compile

# Run all tests
mvn test

# Run a specific test class
mvn test -Dtest=PostsAPITest

# Generate an Allure report
mvn allure:report
# Open target/site/allure-maven-plugin/index.html in a browser
```

## Project Layout

```
.github/
  copilot-instructions.md   # This file
pom.xml                     # Maven project descriptor
src/
  main/
    java/com/framework/api/
      client/
        APIClient.java          # Core HTTP client (GET/POST/PUT/PATCH/DELETE)
        APIClientFactory.java   # Creates & disposes Playwright request context
      config/
        ConfigManager.java      # Singleton config loader (properties + sys props)
      models/
        APIRequest.java         # Fluent request builder (endpoint, headers, body)
        APIResponse.java        # Response wrapper with typed deserialization
      utils/
        JsonUtils.java          # Jackson helpers (serialize / deserialize / pretty-print)
    resources/
      log4j2.xml              # Console + file logging configuration
  test/
    java/com/framework/api/
      base/
        BaseTest.java           # TestNG @BeforeClass / @AfterClass lifecycle
      models/
        Post.java               # POJO for JSONPlaceholder /posts resource
      tests/
        PostsAPITest.java       # Example tests: GET, POST, PUT, PATCH, DELETE
    resources/
      config/
        config.properties       # Base URL, timeout, and other settings
      testng.xml                # TestNG suite definition
```

## Key Conventions

- **Test files** live under `src/test/java/` and extend `BaseTest`.
- **BaseTest** manages `APIClientFactory` lifecycle via TestNG `@BeforeClass` / `@AfterClass`.
- **Base URL** is configured in `src/test/resources/config/config.properties`; override at runtime with `-Dapi.base.url=`.
- **Environment / system properties** (e.g., `api.base.url`, `api.timeout.ms`) are loaded by `ConfigManager`. Never commit secrets.
- Use `apiClient.get()`, `apiClient.post()`, `apiClient.put()`, `apiClient.patch()`, `apiClient.delete()` for HTTP calls.
- Build requests with `APIRequest.builder("/endpoint").body(obj).build()`.
- Assert responses with AssertJ: `assertThat(response.getStatusCode()).isEqualTo(200)`.

## Running Type Checks and Builds

```bash
# Compile only (type check equivalent)
mvn compile -q

# Full build with tests
mvn verify
```

## Continuous Integration

CI is run via GitHub Actions. Check `.github/workflows/` for workflow definitions. Tests are typically triggered on push and pull request events. Always ensure `mvn compile` succeeds before running tests in CI.

## Tips for the Coding Agent

- Always check `pom.xml` for the correct dependency versions before adding new ones.
- Prefer creating new test helpers in `src/main/java/com/framework/api/` and new test classes extending `BaseTest`.
- Keep test data as POJOs in `src/test/java/com/framework/api/models/`.
- Trust these instructions and only search the codebase if information here appears incomplete or incorrect.

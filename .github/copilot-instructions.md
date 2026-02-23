# Copilot Instructions

## Repository Overview

This is an **API automation testing framework** built with [Playwright](https://playwright.dev/). It provides a structured approach to writing, organizing, and running automated API tests using Playwright's API testing capabilities.

- **Language**: TypeScript / JavaScript (Node.js)
- **Framework**: Playwright (API testing via `@playwright/test`)
- **Purpose**: Automated API testing — sending HTTP requests and asserting responses

## Project Setup

```bash
# Install dependencies (always run before building or testing)
npm install

# Run all tests
npx playwright test

# Run a specific test file
npx playwright test <path/to/test.spec.ts>

# Run tests with a specific tag
npx playwright test --grep @<tag>

# Show the Playwright test report
npx playwright show-report
```

## Project Layout

```
.github/
  copilot-instructions.md   # This file
src/
  tests/                    # Test spec files (*.spec.ts)
  helpers/                  # Reusable helper functions and utilities
  fixtures/                 # Shared test fixtures and setup
  data/                     # Test data (JSON, CSV, etc.)
playwright.config.ts        # Playwright configuration (baseURL, timeouts, reporters)
package.json                # Node.js project manifest and scripts
tsconfig.json               # TypeScript compiler configuration
```

> Note: The project layout above reflects the intended structure. If the structure differs in practice, explore the actual file tree.

## Key Conventions

- **Test files** use the `.spec.ts` extension and live under `src/tests/`.
- **Fixtures** extend Playwright's `test` object to provide reusable API client context.
- **Base URL** is configured in `playwright.config.ts` under `use.baseURL`; tests use relative paths.
- **Environment variables** (e.g., `BASE_URL`, `API_KEY`) are loaded via a `.env` file. Never commit secrets.
- Use `request.get()`, `request.post()`, `request.put()`, `request.delete()` for HTTP calls.
- Assert responses with `expect(response).toBeOK()` and `expect(response.status()).toBe(<code>)`.

## Running Linting and Type Checks

```bash
# Lint (if ESLint is configured)
npm run lint

# Type check
npx tsc --noEmit
```

## Continuous Integration

CI is run via GitHub Actions. Check `.github/workflows/` for workflow definitions. Tests are typically triggered on push and pull request events. Always ensure `npm install` and `npx playwright install` have been run before executing tests in CI.

## Tips for the Coding Agent

- Always run `npm install` before running tests or builds.
- Run `npx playwright install` to ensure browser binaries are available if needed.
- Prefer creating new test helpers in `src/helpers/` and new fixtures in `src/fixtures/`.
- Keep test data in `src/data/` rather than hardcoding values in test files.
- Trust these instructions and only search the codebase if information here appears incomplete or incorrect.

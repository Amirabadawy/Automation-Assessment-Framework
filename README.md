# Automation Assessment

Java Maven automation project covering API and GUI assessment scenarios.

The API suite validates selected FakeRESTApi bookstore endpoints using REST Assured and TestNG. The GUI suite validates selected Internet Herokuapp scenarios using Selenium WebDriver, TestNG, and Page Object Model classes.

## Stack

- Java 17
- Maven
- REST Assured
- Selenium WebDriver
- TestNG
- Allure Report
- Extent Reports
- Page Object Model
- Service Object Model
- Parallel execution through TestNG

## Project Structure

```text
src/main/java/com/assessment/api      REST Assured client, models, and API service objects
src/main/java/com/assessment/core     Shared WebDriver infrastructure and base page
src/main/java/com/assessment/pages    GUI page objects
src/main/java/com/assessment/reporting TestNG reporting listener
src/main/java/com/assessment/utils    Config and test data readers
src/test/java/com/assessment/tests    TestNG API and GUI test classes
src/test/resources                     Config and externalized test data
testng.xml                             TestNG suite
```

## Covered Scenarios

API:

- GET `/api/v1/Books`: validates the books collection and checks a configured existing book.
- POST `/api/v1/Books`: validates the API response echoes the submitted book payload.
- GET `/api/v1/Books/{id}`: validates `404 Not Found` for an unknown book id.

FakeRESTApi simulates write operations. The create test validates the returned response payload, but it does not expect the created book to be permanently stored.

GUI:

- File Upload: uploads a small image and verifies the success message and uploaded filename.
- Dynamic Loading: waits for a rendered element and verifies `Hello World!`.

## Run Tests

Install Java 17 and Maven, then run:

```bash
mvn clean test
```

Run only API tests:

```bash
mvn clean test -Dgroups=api
```

Run only GUI tests in headless mode:

```bash
mvn clean test -Dgroups=gui -Dheadless=true
```

Run tests and generate both Extent and Allure HTML reports:

```bash
mvn clean verify
```

Override the API base URL:

```bash
mvn clean test -DapiBaseUrl=https://fakerestapi.azurewebsites.net
```

Run with a different GUI browser:

```bash
mvn clean test -Dgroups=gui -Dbrowser=firefox
```

Supported local browser values are `chrome`, `firefox`, and `edge`.

## Reports

Every test run writes report output under `target`.

- Extent HTML report: `target/extent-reports/extent-report.html`
- Allure raw results: `target/allure-results`
- Allure HTML report after `mvn verify` or `mvn allure:report`: `target/site/allure-maven-plugin/index.html`

Generate the Allure HTML report from existing results:

```bash
mvn allure:report
```

## CI/CD

GitHub Actions workflow is configured in `.github/workflows/ci.yml`.

The workflow runs on:

- Push to `main`
- Pull request to `main`
- Manual trigger from the GitHub Actions tab

The pipeline uses Java 17 and runs:

```bash
mvn clean verify -Dheadless=true
```

After each run, GitHub Actions uploads these artifacts:

- Surefire reports
- Extent report
- Allure HTML report
- Allure raw results

## Configuration

Default values are in `src/test/resources/config.properties`.

Reusable test data is in `src/test/resources/test-data/test-data.properties`.

Any config property can be overridden from the command line with `-Dkey=value`, for example:

```bash
mvn clean test -DapiBaseUrl=https://fakerestapi.azurewebsites.net
```

## Git

Generated files such as `target/`, `.allure/`, and IDE metadata are ignored. Commit source, configuration, test data, `pom.xml`, `testng.xml`, and this README.

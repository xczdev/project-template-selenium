# Selenium Test Automation — Bot Style Tests

A template for building end-to-end web automation tests, created via vibe coding.

## Tools / Dependencies

| Tool | Purpose |
|---|---|
| Selenium WebDriver | Browser automation |
| TestNG | Test framework and assertions (`org.testng.Assert`) |
| WebDriverManager | Automatic browser driver management |
| Log4j | Logging |
| Allure TestNG | HTML test reports |
| Jackson | JSON processing |
| Commons IO | File utilities |

## Design Philosophy: Bot Style Tests

Based on the **Bot Style** approach by Simon Stewart (creator of Selenium WebDriver).

**Tests read like a script of human actions** — every line is one action or one assertion, nothing more. No helper methods, no business logic, no multi-step flows hidden behind a single call.

```java
@Test
public void searchForLemonde() {
    log("Assert search input is visible");
    assertVisible(SearchPage.SEARCH_INPUT);
    log("Type 'lemonde' and submit");
    typeAndSubmit(SearchPage.SEARCH_INPUT, "lemonde");
    log("Click first result");
    click(SearchPage.FIRST_RESULT);
    log("Assert URL contains lemonde.fr");
    assertUrlContains(TestConstants.LEMONDE_URL);
}
```

Each line answers: *what does the user do next?* or *what should be true now?*
Each `log()` call creates a labeled step in the Allure report.

---

## Architecture

```
src/test/java/com/automation/
├── pages/
│   └── SearchPage.java        # Locators only
├── tests/
│   ├── Test1.java
│   └── Test2.java
└── utils/
    ├── BaseTest.java          # Actions, assertions, log, lifecycle
    ├── DriverFactory.java     # WebDriver initialization and management
    ├── ScreenshotUtil.java    # Screenshot capture utility
    └── TestConstants.java     # URLs, timeouts, credentials
```

> `BasePage` has been merged into `BaseTest`, which lives in `utils/`. Page classes contain locators only.  
> Everything lives under `src/test/java` — this is a test-only project with no production code.

---

## Key Components

### `BaseTest` (in `utils/`) — the bot's vocabulary

`BaseTest` is the single place that knows how to interact with the browser. It holds all **actions**, **assertions**, the **`log`** method, and the WebDriver lifecycle.

**`log(message)`** — prints to the console and adds a step to the Allure report:

```java
protected void log(String message) {
    logger.info(message);
    Allure.step(message);
}
```

**Lifecycle hooks** — override in each test class for per-class setup and teardown:

```java
protected void setup() {}    // called after the driver is ready and the page is loaded
protected void teardown() {} // called before the driver closes
```

**Actions**

| Method | Description |
|---|---|
| `click(locator)` | Wait for element to be clickable, then click |
| `type(locator, text)` | Clear field and type text |
| `typeAndSubmit(locator, text)` | Type text and press Enter |
| `selectByText(locator, text)` | Select dropdown option by visible text |
| `selectByValue(locator, value)` | Select dropdown option by value |
| `getText(locator)` | Return visible text of element |
| `getCurrentUrl()` | Return current page URL |

**Assertions**

| Method | Description |
|---|---|
| `assertVisible(locator)` | Fails if element is not visible |
| `assertNotVisible(locator)` | Fails if element is visible |
| `assertIsSelected(locator)` | Fails if checkbox/radio is not selected |
| `assertIsNotSelected(locator)` | Fails if checkbox/radio is selected |
| `assertIsEnabled(locator)` | Fails if element is disabled |
| `assertIsDisabled(locator)` | Fails if element is enabled |
| `assertTextEquals(locator, text)` | Fails if element text does not exactly match |
| `assertTextContains(locator, text)` | Fails if element text does not contain the value |
| `assertUrlContains(text)` | Fails if the current URL does not contain the value |

---

### Page Objects — locators only

Each page class holds only `public static final By` locator constants — no methods, no logic.

```java
public class SearchPage {
    public static final By SEARCH_INPUT = By.name("q");
    public static final By FIRST_RESULT = By.cssSelector("[data-testid='result-title-a']");
}
```

---

### Tests — one line, one action or one assertion

```java
@Test
@Description("Search 'lemonde' and navigate to lemonde.fr")
public void searchForLemonde() {
    log("Assert search input is visible");
    assertVisible(SearchPage.SEARCH_INPUT);
    log("Type 'lemonde' and submit");
    typeAndSubmit(SearchPage.SEARCH_INPUT, "lemonde");
    log("Assert first result is visible");
    assertVisible(SearchPage.FIRST_RESULT);
    log("Click first result");
    click(SearchPage.FIRST_RESULT);
    log("Assert URL contains lemonde.fr");
    assertUrlContains(TestConstants.LEMONDE_URL);
}
```

No helper methods. No intermediate variables. No multi-step wrappers. The test **is** the script.

---

## Getting Started

**Prerequisites:** Java 11+, Maven 3.6+

**Configuration:** Edit `utils/TestConstants.java` to set `BASE_URL`, timeouts, and test data.

---

## Running Tests

```bash
# All tests
mvn clean test

# Single class
mvn clean test -Dtest=Test1

# Single method
mvn clean test -Dtest=Test1#searchForLemonde

# TestNG suite
mvn clean test -Dsuite=smoke

# Run tests + open Allure report
mvn clean test && mvn allure:serve

# Generate Allure report from existing results and open it (no test run)
mvn allure:report && mvn allure:serve
```

> `allure:serve` generates the report and opens it at `http://localhost:<port>`. Press Ctrl+C to stop.
> `allure:report && allure:serve` regenerates the static report then immediately serves it — useful to view results without re-running tests.

---

## Test Suites

Test suites use TestNG XML files in `src/test/resources/`.

**Create a suite** — add an XML file, e.g. `smoke.xml`:

```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Smoke Suite">
    <test name="Smoke Tests">
        <classes>
            <class name="com.automation.tests.Test1"/>
        </classes>
    </test>
</suite>
```

**Run it:**

```bash
mvn clean test -Dsuite=smoke
```

No POM changes required — just drop the XML file and reference it by name.

**Group tests** with `@Test(groups = {"smoke"})` and filter in the XML or directly from the command line:

```bash
mvn clean test -Dgroups=smoke
```

---

## Adding a New Test

**Step 1** — Create a page object with locators only:

```java
public class LoginPage {
    public static final By USERNAME = By.id("username");
    public static final By PASSWORD = By.id("password");
    public static final By SUBMIT   = By.id("submit");
}
```

**Step 2** — Create a test class extending `BaseTest`:

```java
public class LoginTest extends BaseTest {

    @Override
    protected void setup() {
        // per-class setup, e.g. navigate to login page
    }

    @Test
    @Description("Valid login navigates to dashboard")
    public void loginWithValidCredentials() {
        log("Assert username field is visible");
        assertVisible(LoginPage.USERNAME);
        log("Type username");
        type(LoginPage.USERNAME, TestConstants.USERNAME);
        log("Type password");
        type(LoginPage.PASSWORD, TestConstants.PASSWORD);
        log("Click submit");
        click(LoginPage.SUBMIT);
        log("Assert dashboard URL");
        assertUrlContains("/dashboard");
    }
}
```

---

## References

- [Selenium Documentation](https://www.selenium.dev/documentation/)
- [TestNG Documentation](https://testng.org/doc/)
- [Allure Framework](https://allurereport.org/docs/)
- [WebDriverManager](https://github.com/bonigarcia/webdrivermanager)

---

**Template Version**: 1.0.0 | **Last Updated**: 2026
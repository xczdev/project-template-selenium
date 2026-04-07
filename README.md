# Selenium Test Automation — Bot Style Tests

A test automation framework built with **Selenium WebDriver**, **TestNG**, and **Maven**, designed around the **Bot Style Tests** pattern.

## Design Philosophy: Bot Style Tests

This project follows the **Bot Style** approach to test automation, as described by Simon Stewart (creator of Selenium WebDriver).

The core idea: **tests read like a script of human actions**, where every line is either one action or one assertion — nothing more. There are no helper methods wrapping other methods, no page-level business logic, no multi-step flows hidden behind a single call.

```java
@Test
public void searchForLemonde() {
    assertVisible(SearchPage.SEARCH_INPUT);
    typeAndSubmit(SearchPage.SEARCH_INPUT, "lemonde");
    assertVisible(SearchPage.FIRST_RESULT);
    click(SearchPage.FIRST_RESULT);
    assertUrlContains(TestConstants.LEMONDE_URL);
}
```

Each line answers: *"What does the user do next?"* or *"What should be true now?"*

---

## Architecture

```
src/
└── test/
    └── java/
        └── com/automation/
            ├── pages/
            │   ├── BasePage.java          # All actions and assertions
            │   └── SearchPage.java        # Locators only
            ├── tests/
            │   ├── BaseTest.java          # Setup, teardown, delegate methods
            │   ├── Test1.java             # DuckDuckGo search → lemonde.fr
            │   └── Test2.java             # DuckDuckGo search → lefigaro.fr
            └── utils/
                ├── DriverFactory.java     # WebDriver initialization and management
                ├── ScreenshotUtil.java    # Screenshot capture utility
                └── TestConstants.java     # URLs, timeouts, credentials
```

> Everything lives under `src/test/java` — this is a test-only project with no production code.

---

## Key Components

### `BasePage` — the bot's action and assertion vocabulary

`BasePage` is the single place that knows how to interact with the browser. It holds all reusable **actions** and **assertions**, backed by explicit waits.

**Actions**

| Method | Description |
|---|---|
| `click(locator)` | Wait for element to be clickable, then click |
| `type(locator, text)` | Clear field and type text |
| `typeAndSubmit(locator, text)` | Type text and press Enter |
| `selectByText(locator, text)` | Select dropdown option by visible text |
| `selectByValue(locator, value)` | Select dropdown option by value |
| `getText(locator)` | Return visible text of element |
| `getAttribute(locator, attr)` | Return attribute value of element |
| `getCurrentUrl()` | Return current page URL |

**Assertions** (all use `org.testng.Assert` internally)

| Method | Description |
|---|---|
| `assertVisible(locator)` | Fails if element is not visible |
| `assertNotVisible(locator)` | Fails if element is visible |
| `assertIsSelected(locator)` | Fails if checkbox/radio is not selected |
| `assertIsNotSelected(locator)` | Fails if checkbox/radio is selected |
| `assertIsEnabled(locator)` | Fails if element is disabled |
| `assertIsDisabled(locator)` | Fails if element is enabled |
| `assertTextEquals(locator, text)` | Fails if element text does not exactly match |
| `assertTextContains(locator, text)` | Fails if element text does not contain value |
| `assertUrlContains(text)` | Fails if current URL does not contain value |

---

### `SearchPage` (and all other page objects) — locators only

Page objects do **not** contain methods or logic. They are simple locator registries.

```java
public class SearchPage {
    public static final By SEARCH_INPUT = By.name("q");
    public static final By FIRST_RESULT  = By.cssSelector("[data-testid='result-title-a']");
}
```

To add a new page, create a class with `public static final By` fields — nothing else.

---

### `BaseTest` — setup, teardown, and delegate methods

`BaseTest` handles the WebDriver lifecycle and exposes all `BasePage` methods as direct calls (no `page.` prefix needed in tests).

```java
// BaseTest delegates — so tests call this directly:
assertVisible(locator);        // → page.assertVisible(locator)
click(locator);                // → page.click(locator)
typeAndSubmit(locator, text);  // → page.typeAndSubmit(locator, text)
```

---

### Tests — one line, one action or assertion

```java
@Test
public void searchForLemonde() {
    assertVisible(SearchPage.SEARCH_INPUT);          // assertion
    typeAndSubmit(SearchPage.SEARCH_INPUT, "lemonde"); // action
    assertVisible(SearchPage.FIRST_RESULT);           // assertion
    click(SearchPage.FIRST_RESULT);                   // action
    assertUrlContains(TestConstants.LEMONDE_URL);     // assertion
}
```

No helper methods. No intermediate variables. No multi-step wrappers. The test **is** the script.

## Dependencies

- **Selenium WebDriver** — browser automation
- **TestNG** — test framework and assertions (`org.testng.Assert`)
- **WebDriverManager** — automatic browser driver management
- **Log4j** — logging
- **Allure TestNG** — HTML test reports
- **Jackson** — JSON processing
- **Commons IO** — file utilities

---

## Getting Started

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher

### Configuration

Edit `src/test/java/com/automation/utils/TestConstants.java` to set:
- `BASE_URL` — application entry point
- `IMPLICIT_WAIT` / `EXPLICIT_WAIT` / `PAGE_LOAD_TIMEOUT` — timeouts in seconds
- Test credentials and expected URLs

### Running Tests

**Run all tests (default — no suite)**
```bash
mvn clean test
```

**Run a specific test class**
```bash
mvn clean test -Dtest=Test1
```

**Run a specific test method**
```bash
mvn clean test -Dtest=Test1#myMethodName
```

**Run a test suite**
```bash
mvn clean test -Dsuite=testSuite1
```
> This auto-activates the `suite` Maven profile and runs `src/test/resources/testSuite1.xml`.
> To run a different suite, just change the name — no POM changes needed:
> ```bash
> mvn clean test -Dsuite=testSuite2
> mvn clean test -Dsuite=smoke
> ```

**Run tests + open Allure report in browser automatically**
```bash
mvn clean test && mvn surefire-report:report -DskipTests allure:serve
```

> `allure:serve` generates the report and opens it at `http://localhost:<port>` automatically.
> Press **Ctrl+C** in the terminal to stop the report server when done.

---

## Test Suites

Test suites are managed via TestNG XML files located in `src/test/resources/`.

### How it works

| Command | Behaviour |
|---|---|
| `mvn test` | Runs all tests matching `**/*Test.java` or `**/Test*.java` |
| `mvn test -Dtest=Test1` | Runs only the `Test1` class |
| `mvn test -Dsuite=testSuite1` | Runs the XML suite `src/test/resources/testSuite1.xml` |

When `-Dsuite` is provided, the `suite` Maven profile activates automatically and hands full control to TestNG via the XML file. The `<includes>` pattern is ignored in that mode.

### Creating a new suite

1. Create a new XML file in `src/test/resources/`, e.g. `smoke.xml`:

```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Smoke Suite" verbose="1">
    <test name="Smoke Tests">
        <classes>
            <class name="com.automation.tests.Test1"/>
        </classes>
    </test>
</suite>
```

2. Run it:

```bash
mvn clean test -Dsuite=smoke
```

No POM changes required. Just drop the XML file and reference it by name.

### Grouping tests with `@Test(groups=...)`

You can filter tests within a suite using TestNG groups:

```java
@Test(groups = {"smoke", "regression"})
public void Test1() { ... }

@Test(groups = {"regression"})
public void Test2() { ... }
```

Then target a group in your XML:

```xml
<suite name="Smoke Suite">
    <test name="Smoke Tests">
        <groups>
            <run>
                <include name="smoke"/>
            </run>
        </groups>
        <packages>
            <package name="com.automation.tests"/>
        </packages>
    </test>
</suite>
```

Or directly from the command line (no XML needed):

```bash
mvn clean test -Dgroups=smoke
```

## Adding New Tests

### Step 1: Create a new Page Object

```java
package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductPage extends BasePage {
    
    private static final By PRODUCT_TITLE = By.className("inventory_item_name");
    private static final By ADD_TO_CART = By.className("btn_primary");
    
    public ProductPage(WebDriver driver) {
        super(driver);
    }
    
    @Override
    public void verifyPageLoad() {
        waitForElementVisible(PRODUCT_TITLE);
    }
    
    public void addProductToCart() {
        click(ADD_TO_CART);
    }
}
```

### Step 2: Create Test Class

```java
package com.automation.tests;

import com.automation.pages.ProductPage;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ProductPageTest extends BaseTest {
    
    @Test
    public void testAddProductToCart() {
        // SETUP
        ProductPage productPage = new ProductPage(driver);
        productPage.verifyPageLoad();
        
        // RUN
        productPage.addProductToCart();
        
        // VERIFY
        assertTrue("Product should be added to cart", true);
        
        // TEARDOWN: Automatic
    }
}
```

## Best Practices

1. **Page Objects**: One class per page/component
2. **Locators**: Define as private static final constants
3. **Methods**: Create business-logic methods in page objects (e.g., `login()`, not just `clickLoginButton()`)
4. **Waits**: Always use explicit waits for elements (avoid Thread.sleep)
5. **Assertions**: Use TestNG assertions (`org.testng.Assert`) for verification
6. **Logging**: Add meaningful log statements for debugging
7. **Test Data**: Store in `TestConstants.java` for easy maintenance
8. **Driver Management**: Always initialize in setUp() and close in tearDown()

## Browser Configuration

Edit `DriverFactory.java` to customize browser options:

```java
// Enable headless mode
options.addArguments("--headless");

// Disable notifications
options.addArguments("--disable-notifications");

// Set window size
options.addArguments("--window-size=1920,1080");
```

## Troubleshooting

| Issue | Solution |
|-------|----------|
| WebDriver not found | WebDriverManager should auto-download. Check internet connection |
| Timeouts occurring | Increase `EXPLICIT_WAIT` in TestConstants |
| Tests failing randomly | Use explicit waits instead of Thread.sleep |
| Port already in use | Change browser/driver port in DriverFactory options |

## Future Enhancements

- Add Parallel Test Execution
- Create TestNG XML Suite Configuration (`testng.xml`)
- Add Retry Mechanism for Flaky Tests
- Implement Database Integration
- Add API Testing Capabilities

## References

- [Selenium Documentation](https://www.selenium.dev/documentation/)
- [TestNG Documentation](https://testng.org/doc/)
- [Allure Framework](https://allurereport.org/docs/)
- [Maven Documentation](https://maven.apache.org/guides/)
- [WebDriverManager](https://github.com/bonigarcia/webdrivermanager)

---

**Template Version**: 1.0.0  
**Last Updated**: 2026
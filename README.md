# Selenium Test Automation Template

A professional test automation framework built with Selenium WebDriver, TestNG, and Maven, following best practices including the Page Object Model (POM) pattern.

## Project Structure

```
src/
├── main/
│   └── java/
│       └── com/automation/
│           ├── pages/
│           │   ├── BasePage.java          # Abstract base class for all page objects
│           │   └── SearchPage.java        # Page object for DuckDuckGo search
│           └── utils/
│               ├── DriverFactory.java     # WebDriver initialization and management
│               ├── ScreenshotUtil.java    # Screenshot capture utility
│               └── TestConstants.java     # Test configuration and constants
├── test/
│   └── java/
│       └── com/automation/
│           └── tests/
│               ├── BaseTest.java          # Abstract base class with setup/teardown
│               └── Test1.java             # Example test: DuckDuckGo search → lemonde.fr
└── pom.xml                                # Maven project configuration
```

## Key Components

### 1. **DriverFactory** (`utils/DriverFactory.java`)
- Manages WebDriver lifecycle (initialization, close, reset)
- Supports Chrome and Firefox browsers
- Uses WebDriverManager for automatic driver management
- Singleton pattern for driver instance

### 2. **BasePage** (`pages/BasePage.java`)
- Abstract base class for all page objects
- Provides common methods for element interaction:
  - `click()`, `type()`, `getText()`
  - Explicit waits: `waitForElementVisible()`, `waitForElementClickable()`
  - Dropdown handling: `selectFromDropdown()`
- Implements Page Object Model pattern
- Each page object must implement `verifyPageLoad()` method

### 3. **BaseTest** (`tests/BaseTest.java`)
- Abstract base class for all test classes
- **SETUP** (`@BeforeMethod`): Initializes WebDriver, sets timeouts, navigates to base URL
- **TEARDOWN** (`@AfterMethod`): Closes WebDriver and cleans up resources
- Provides `log()` helper that writes to Log4j and adds an Allure step simultaneously

### 4. **ScreenshotUtil** (`utils/ScreenshotUtil.java`)
- Utility class for capturing browser screenshots
- Saves screenshots to a configurable output directory

### 5. **Test Structure** (3-Part Pattern)
Each test follows the pattern: Setup → Run → Teardown

```java
@Test
public void testExampleScenario() {
    // SETUP: Prepare test data and page objects
    SearchPage searchPage = new SearchPage(driver);
    searchPage.verifyPageLoad();
    
    // RUN: Execute test actions
    searchPage.searchFor("example query");
    
    // VERIFY: Assert expected results
    assertTrue(driver.getCurrentUrl().contains("example"),
               "Expected URL to contain 'example'");
    
    // TEARDOWN: Automatically handled by BaseTest.tearDown() method
}
```

## Dependencies

- **Selenium WebDriver**: Web automation library
- **TestNG**: Testing framework
- **WebDriverManager**: Automatic WebDriver management
- **Log4j**: Logging framework
- **Jackson**: JSON processing
- **Allure TestNG**: Test reporting (generates HTML reports with steps and history)
- **Commons IO**: File I/O utilities (used by ScreenshotUtil)

## Getting Started

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher

### Installation

1. **Clone or navigate to the project directory**
   ```bash
   cd /path/to/tnra-project
   ```

2. **Update TestConstants if needed**
   Edit `src/main/java/com/automation/utils/TestConstants.java` to configure:
   - `BASE_URL`: Application URL
   - `IMPLICIT_WAIT`: Default wait time
   - `EXPLICIT_WAIT`: WebDriver wait timeout
   - Test credentials and data

3. **Build the project**
   ```bash
   mvn clean install
   ```

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
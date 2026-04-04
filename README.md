# Selenium Test Automation Template

A professional test automation framework built with Selenium WebDriver, JUnit, and Maven, following best practices including the Page Object Model (POM) pattern.

## Project Structure

```
src/
├── main/
│   └── java/
│       └── com/automation/
│           ├── pages/
│           │   ├── BasePage.java          # Abstract base class for all page objects
│           │   ├── LoginPage.java         # Example page object for login functionality
│           │   └── HomePage.java          # Example page object for home page
│           └── utils/
│               ├── DriverFactory.java     # WebDriver initialization and management
│               └── TestConstants.java     # Test configuration and constants
├── test/
│   └── java/
│       └── com/automation/
│           └── tests/
│               ├── BaseTest.java          # Abstract base class with setup/teardown
│               └── LoginTest.java         # Example test cases
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
- **SETUP** (`@Before`): Initializes WebDriver, sets timeouts, navigates to base URL
- **TEARDOWN** (`@After`): Closes WebDriver and cleans up resources
- Provides common test utilities

### 4. **Test Structure** (3-Part Pattern)
Each test follows the pattern: Setup → Run → Teardown

```java
@Test
public void testExampleScenario() {
    // SETUP: Prepare test data and page objects
    LoginPage loginPage = new LoginPage(driver);
    loginPage.verifyPageLoad();
    
    // RUN: Execute test actions
    loginPage.login("username", "password");
    
    // VERIFY: Assert expected results
    assertTrue("Expected condition", actualCondition);
    
    // TEARDOWN: Automatically handled by BaseTest.tearDown() method
}
```

## Dependencies

- **Selenium WebDriver**: Web automation library
- **JUnit**: Unit testing framework
- **WebDriverManager**: Automatic WebDriver management
- **Log4j**: Logging framework
- **Jackson**: JSON processing

## Getting Started

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher

### Installation

1. **Clone or navigate to the project directory**
   ```bash
   cd /path/to/tnra-selenium
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

**Run all tests**
```bash
mvn clean test
```

**Run specific test class**
```bash
mvn clean test -Dtest=Test1
```

**Run specific test method**
```bash
mvn clean test -Dtest=Test1#testGoogleSearchLemonde
```

**Run tests + open Allure report in browser automatically**
```bash
mvn clean test && mvn surefire-report:report -DskipTests allure:serve
```

> `allure:serve` generates the report and opens it at `http://localhost:<port>` automatically.
> Press **Ctrl+C** in the terminal to stop the report server when done.

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
import org.junit.Test;
import static org.junit.Assert.*;

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
5. **Assertions**: Use JUnit assertions for verification
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
- Implement Screenshot Capture on Failure
- Add Allure Reporting
- Create TestNG XML Configuration
- Add Retry Mechanism for Flaky Tests
- Implement Database Integration
- Add API Testing Capabilities

## References

- [Selenium Documentation](https://www.selenium.dev/documentation/)
- [JUnit 4 Documentation](https://junit.org/junit4/)
- [Maven Documentation](https://maven.apache.org/guides/)
- [WebDriverManager](https://github.com/bonigarcia/webdrivermanager)

---

**Template Version**: 1.0.0  
**Last Updated**: 2026
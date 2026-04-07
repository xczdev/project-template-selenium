package com.automation.tests;

import com.automation.pages.BasePage;
import com.automation.utils.DriverFactory;
import com.automation.utils.TestConstants;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * BaseTest - Abstract base class for all test classes
 */
public abstract class BaseTest {
    protected WebDriver driver;
    protected BasePage page;
    private final Logger logger = LogManager.getLogger(getClass());

    /**
     * Logs a message to the console AND adds it as a step in the Allure report.
     */
    protected void log(String message) {
        logger.info(message);
        Allure.step(message);
    }

    @BeforeMethod
    public final void beforeEach() {
        driver = DriverFactory.initializeDriver(TestConstants.DEFAULT_BROWSER);
        driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(TestConstants.IMPLICIT_WAIT));
        driver.manage().timeouts().pageLoadTimeout(java.time.Duration.ofSeconds(TestConstants.PAGE_LOAD_TIMEOUT));
        page = new BasePage(driver);
        driver.navigate().to(TestConstants.BASE_URL);
        setup();
    }

    @AfterMethod
    public final void afterEach() {
        teardown();
        if (driver != null) {
            DriverFactory.closeDriver();
        }
    }

    /**
     * Override in a test class to run custom logic after the base beforeEach (driver ready, page loaded).
     */
    protected void setup() {}

    /**
     * Override in a test class to run custom logic before the base afterEach (driver still open).
     */
    protected void teardown() {}

    // --- Action delegates (avoids writing page. in every test) -----------

    protected void click(By locator)                        { page.click(locator); }
    protected void type(By locator, String text)            { page.type(locator, text); }
    protected void typeAndSubmit(By locator, String text)   { page.typeAndSubmit(locator, text); }
    protected void selectByText(By locator, String text)    { page.selectByText(locator, text); }
    protected void selectByValue(By locator, String value)  { page.selectByValue(locator, value); }
    protected String getText(By locator)                    { return page.getText(locator); }
    protected String getCurrentUrl()                        { return page.getCurrentUrl(); }

    // --- Assertion delegates ---------------------------------------------

    protected void assertVisible(By locator)                { page.assertVisible(locator); }
    protected void assertNotVisible(By locator)             { page.assertNotVisible(locator); }
    protected void assertIsSelected(By locator)             { page.assertIsSelected(locator); }
    protected void assertIsNotSelected(By locator)          { page.assertIsNotSelected(locator); }
    protected void assertIsEnabled(By locator)              { page.assertIsEnabled(locator); }
    protected void assertIsDisabled(By locator)             { page.assertIsDisabled(locator); }
    protected void assertTextEquals(By locator, String expected)   { page.assertTextEquals(locator, expected); }
    protected void assertTextContains(By locator, String expected) { page.assertTextContains(locator, expected); }
    protected void assertUrlContains(String expected)              { page.assertUrlContains(expected); }
}

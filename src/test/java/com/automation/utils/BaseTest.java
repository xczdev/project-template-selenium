package com.automation.utils;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public abstract class BaseTest {

    protected WebDriver driver;
    private WebDriverWait wait;
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
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TestConstants.IMPLICIT_WAIT));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(TestConstants.PAGE_LOAD_TIMEOUT));
        wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.EXPLICIT_WAIT));
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

    // ─── Actions ────────────────────────────────────────────────────────────

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void type(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    protected void typeAndSubmit(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
        element.sendKeys(Keys.ENTER);
    }

    protected void selectByText(By locator, String visibleText) {
        new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(locator)))
                .selectByVisibleText(visibleText);
    }

    protected void selectByValue(By locator, String value) {
        new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(locator)))
                .selectByValue(value);
    }

    protected String getText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    // ─── Assertions ─────────────────────────────────────────────────────────

    protected void assertVisible(By locator) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            Assert.assertTrue(driver.findElement(locator).isDisplayed(),
                    "Element should be visible: " + locator);
        } catch (AssertionError e) {
            throw e;
        } catch (Exception e) {
            Assert.fail("Element is not visible: " + locator + " — " + e.getMessage());
        }
    }

    protected void assertNotVisible(By locator) {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (Exception e) {
            Assert.fail("Element should not be visible: " + locator);
        }
    }

    protected void assertIsSelected(By locator) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        Assert.assertTrue(el.isSelected(), "Element should be selected: " + locator);
    }

    protected void assertIsNotSelected(By locator) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        Assert.assertFalse(el.isSelected(), "Element should not be selected: " + locator);
    }

    protected void assertIsEnabled(By locator) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        Assert.assertTrue(el.isEnabled(), "Element should be enabled: " + locator);
    }

    protected void assertIsDisabled(By locator) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        Assert.assertFalse(el.isEnabled(), "Element should be disabled: " + locator);
    }

    protected void assertTextEquals(By locator, String expected) {
        String actual = wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
        Assert.assertEquals(actual, expected, "Text mismatch for: " + locator);
    }

    protected void assertTextContains(By locator, String expected) {
        String actual = wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
        Assert.assertTrue(actual.contains(expected),
                "Text '" + actual + "' does not contain '" + expected + "' for: " + locator);
    }

    protected void assertUrlContains(String expected) {
        String url = driver.getCurrentUrl();
        Assert.assertTrue(url.contains(expected),
                "URL '" + url + "' does not contain '" + expected + "'");
    }
}

package com.automation.pages;

import com.automation.utils.TestConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.time.Duration;

public class BasePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.EXPLICIT_WAIT));
    }

    // ─── Actions ────────────────────────────────────────────────────────────

    public void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    public void type(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    public void typeAndSubmit(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
        element.sendKeys(Keys.ENTER);
    }

    public void selectByText(By locator, String visibleText) {
        new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(locator)))
                .selectByVisibleText(visibleText);
    }

    public void selectByValue(By locator, String value) {
        new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(locator)))
                .selectByValue(value);
    }

    public String getText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

    public String getAttribute(By locator, String attribute) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator)).getAttribute(attribute);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    // ─── Assertions ─────────────────────────────────────────────────────────

    public void assertVisible(By locator) {
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

    public void assertNotVisible(By locator) {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (Exception e) {
            Assert.fail("Element should not be visible: " + locator);
        }
    }

    public void assertIsSelected(By locator) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        Assert.assertTrue(el.isSelected(), "Element should be selected: " + locator);
    }

    public void assertIsNotSelected(By locator) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        Assert.assertFalse(el.isSelected(), "Element should not be selected: " + locator);
    }

    public void assertIsEnabled(By locator) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        Assert.assertTrue(el.isEnabled(), "Element should be enabled: " + locator);
    }

    public void assertIsDisabled(By locator) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        Assert.assertFalse(el.isEnabled(), "Element should be disabled: " + locator);
    }

    public void assertTextEquals(By locator, String expected) {
        String actual = wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
        Assert.assertEquals(actual, expected, "Text mismatch for: " + locator);
    }

    public void assertTextContains(By locator, String expected) {
        String actual = wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
        Assert.assertTrue(actual.contains(expected),
                "Text '" + actual + "' does not contain '" + expected + "' for: " + locator);
    }

    public void assertUrlContains(String expected) {
        String url = driver.getCurrentUrl();
        Assert.assertTrue(url.contains(expected),
                "URL '" + url + "' does not contain '" + expected + "'");
    }
}

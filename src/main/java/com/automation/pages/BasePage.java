package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.automation.utils.TestConstants;
import java.time.Duration;

/**
 * BasePage - Abstract base class for all page objects
 * Provides common methods for element interaction and waits
 */
public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    /**
     * Constructor - Initialize driver and wait
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.EXPLICIT_WAIT));
    }

    /**
     * Wait for element to be visible
     */
    protected WebElement waitForElementVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait for element to be clickable
     */
    protected WebElement waitForElementClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Wait for element to be present
     */
    protected WebElement waitForElementPresent(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Click on element with wait
     */
    protected void click(By locator) {
        waitForElementClickable(locator).click();
    }

    /**
     * Type text into element
     */
    protected void type(By locator, String text) {
        WebElement element = waitForElementVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Get text from element
     */
    protected String getText(By locator) {
        return waitForElementVisible(locator).getText();
    }

    /**
     * Get attribute value from element
     */
    protected String getAttribute(By locator, String attributeName) {
        return waitForElementVisible(locator).getAttribute(attributeName);
    }

    /**
     * Check if element is displayed
     */
    protected boolean isElementDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Select dropdown option by visible text
     */
    protected void selectFromDropdown(By locator, String visibleText) {
        WebElement dropdown = waitForElementVisible(locator);
        Select select = new Select(dropdown);
        select.selectByVisibleText(visibleText);
    }

    /**
     * Select dropdown option by value
     */
    protected void selectFromDropdownByValue(By locator, String value) {
        WebElement dropdown = waitForElementVisible(locator);
        Select select = new Select(dropdown);
        select.selectByValue(value);
    }

    /**
     * Navigate to URL
     */
    protected void navigateTo(String url) {
        driver.navigate().to(url);
    }

    /**
     * Get current page title
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Get current page URL
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Abstract method - Verify page is loaded
     * Must be implemented by subclasses
     */
    public abstract void verifyPageLoad();
}

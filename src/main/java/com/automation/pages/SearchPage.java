package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * SearchPage - Page object for DuckDuckGo search functionality
 */
public class SearchPage extends BasePage {

    private static final By SEARCH_INPUT = By.name("q");
    // DuckDuckGo result links
    private static final By SEARCH_RESULTS = By.cssSelector("[data-testid='result-title-a']");

    public SearchPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void verifyPageLoad() {
        waitForElementVisible(SEARCH_INPUT);
    }

    /**
     * Type a search query and press Enter
     */
    public void searchFor(String query) {
        WebElement input = waitForElementVisible(SEARCH_INPUT);
        input.clear();
        input.sendKeys(query);
        input.sendKeys(Keys.ENTER);
    }

    /**
     * Click the first organic search result link
     */
    public void clickFirstResult() {
        List<WebElement> results = wait.until(
            org.openqa.selenium.support.ui.ExpectedConditions
                .visibilityOfAllElementsLocatedBy(SEARCH_RESULTS)
        );
        results.get(0).click();
    }

    /**
     * Return the current page URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}

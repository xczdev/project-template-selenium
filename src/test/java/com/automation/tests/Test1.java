package com.automation.tests;

import com.automation.pages.SearchPage;
import com.automation.utils.TestConstants;
import io.qameta.allure.Step;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 * GoogleSearchTest - Test Google search and navigate to first result
 */
public class Test1 extends BaseTest {

    @Test
    public void Test1() {
        log("=== Test: Test1 ===");

        SearchPage searchPage = openDuckDuckGoSearchPage();
        searchFor(searchPage, "lemonde");
        clickFirstResult(searchPage);
        verifyLemondePageLoaded(searchPage);
    }

    @Step("Open Google and verify search page is loaded")
    private SearchPage openDuckDuckGoSearchPage() {
        log("[SETUP] Initialize DuckDuckGo search page");
        SearchPage searchPage = new SearchPage(driver);
        searchPage.verifyPageLoad();
        log("DuckDuckGo search page loaded successfully");
        return searchPage;
    }

    @Step("Type 'lemonde' in search box and press Enter")
    private void searchFor(SearchPage searchPage, String query) {
        log("[RUN] Typing search query: " + query);
        searchPage.searchFor(query);
        log("Search submitted");
    }

    @Step("Click the first result link")
    private void clickFirstResult(SearchPage searchPage) {
        log("[RUN] Clicking first search result");
        searchPage.clickFirstResult();
        log("First result clicked");
    }

    @Step("Verify current URL belongs to www.lemonde.fr")
    private void verifyLemondePageLoaded(SearchPage searchPage) {
        String currentUrl = searchPage.getCurrentUrl();
        log("[VERIFY] Current URL: " + currentUrl);
        assertTrue(currentUrl.contains(TestConstants.LEMONDE_URL),
                   "Expected to land on lemonde.fr but got: " + currentUrl);
    }
}


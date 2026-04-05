package com.automation.tests;

import com.automation.pages.SearchPage;
import com.automation.utils.TestConstants;
import io.qameta.allure.Step;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 * Test2 - Search DuckDuckGo and navigate to lefigaro.fr
 */
public class Test2 extends BaseTest {

    @Test
    public void Test2() {
        log("=== Test: Test2 ===");

        SearchPage searchPage = openDuckDuckGoSearchPage();
        searchFor(searchPage, "lefigaro");
        clickFirstResult(searchPage);
        verifyLefigaroPageLoaded(searchPage);
    }

    @Step("Open DuckDuckGo and verify search page is loaded")
    private SearchPage openDuckDuckGoSearchPage() {
        log("[SETUP] Initialize DuckDuckGo search page");
        SearchPage searchPage = new SearchPage(driver);
        searchPage.verifyPageLoad();
        log("DuckDuckGo search page loaded successfully");
        return searchPage;
    }

    @Step("Type 'lefigaro' in search box and press Enter")
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

    @Step("Verify current URL belongs to www.lefigaro.fr")
    private void verifyLefigaroPageLoaded(SearchPage searchPage) {
        String currentUrl = searchPage.getCurrentUrl();
        log("[VERIFY] Current URL: " + currentUrl);
        assertTrue(currentUrl.contains(TestConstants.LEFIGARO_URL),
                   "Expected to land on lefigaro.fr but got: " + currentUrl);
    }
}

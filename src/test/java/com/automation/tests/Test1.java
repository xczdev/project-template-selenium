package com.automation.tests;

import com.automation.pages.SearchPage;
import com.automation.utils.TestConstants;
import io.qameta.allure.Description;
import org.testng.annotations.Test;

public class Test1 extends BaseTest {

    @Test
    @Description("Search 'lemonde' on DuckDuckGo and verify navigation to lemonde.fr")
    public void searchForLemonde() {
        log("Assert search input is visible");
        assertVisible(SearchPage.SEARCH_INPUT);
        log("Type 'lemonde' and submit search");
        typeAndSubmit(SearchPage.SEARCH_INPUT, "lemonde");
        log("Assert first result is visible");
        assertVisible(SearchPage.FIRST_RESULT);
        log("Click first result");
        click(SearchPage.FIRST_RESULT);
        log("Assert URL contains: " + TestConstants.LEMONDE_URL);
        assertUrlContains(TestConstants.LEMONDE_URL);
    }
}


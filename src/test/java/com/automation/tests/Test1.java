package com.automation.tests;

import com.automation.pages.SearchPage;
import com.automation.utils.BaseTest;
import com.automation.utils.TestConstants;
import io.qameta.allure.Description;
import org.testng.annotations.Test;

public class Test1 extends BaseTest {

    @Override
    protected void setup() {
        // Custom setup before each test in this class
        // e.g. log("Test1: setup - navigating to specific section");
        log("Test1: setup actions");

    }

    @Override
    protected void teardown() {
        // Custom teardown after each test in this class
        // e.g. log("Test1: teardown - resetting state");
        log("Test1: teardown actions");
    }

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


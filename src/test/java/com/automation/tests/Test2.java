package com.automation.tests;

import com.automation.pages.SearchPage;
import com.automation.utils.BaseTest;
import com.automation.utils.TestConstants;
import io.qameta.allure.Description;
import org.testng.annotations.Test;

public class Test2 extends BaseTest {

    @Override
    protected void setup() {
        // Custom setup before each test in this class
        // e.g. log("Test2: setup - navigating to specific section");
        log("Test2: setup actions");
    }

    @Override
    protected void teardown() {
        // Custom teardown after each test in this class
        // e.g. log("Test2: teardown - resetting state");
        log("Test2: teardown actions");
    }

    @Test
    @Description("Search 'lefigaro' on DuckDuckGo and verify navigation to lefigaro.fr")
    public void searchForLefigaro() {
        log("Assert search input is visible");
        assertVisible(SearchPage.SEARCH_INPUT);
        log("Type 'lefigaro' and submit search");
        typeAndSubmit(SearchPage.SEARCH_INPUT, "lefigaro");
        log("Assert first result is visible");
        assertVisible(SearchPage.FIRST_RESULT);
        log("Click first result");
        click(SearchPage.FIRST_RESULT);
        log("Assert URL contains: " + TestConstants.LEFIGARO_URL);
        assertUrlContains(TestConstants.LEFIGARO_URL);
    }
}

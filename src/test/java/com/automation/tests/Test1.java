package com.automation.tests;

import com.automation.pages.SearchPage;
import com.automation.utils.TestConstants;
import io.qameta.allure.Description;
import org.testng.annotations.Test;

public class Test1 extends BaseTest {

    @Test
    @Description("Search 'lemonde' on DuckDuckGo and verify navigation to lemonde.fr")
    public void searchForLemonde() {
        assertVisible(SearchPage.SEARCH_INPUT);
        typeAndSubmit(SearchPage.SEARCH_INPUT, "lemonde");
        assertVisible(SearchPage.FIRST_RESULT);
        click(SearchPage.FIRST_RESULT);
        assertUrlContains(TestConstants.LEMONDE_URL);
    }
}


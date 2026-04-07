package com.automation.tests;

import com.automation.pages.SearchPage;
import com.automation.utils.TestConstants;
import io.qameta.allure.Description;
import org.testng.annotations.Test;

public class Test2 extends BaseTest {

    @Test
    @Description("Search 'lefigaro' on DuckDuckGo and verify navigation to lefigaro.fr")
    public void searchForLefigaro() {
        assertVisible(SearchPage.SEARCH_INPUT);
        typeAndSubmit(SearchPage.SEARCH_INPUT, "lefigaro");
        assertVisible(SearchPage.FIRST_RESULT);
        click(SearchPage.FIRST_RESULT);
        assertUrlContains(TestConstants.LEFIGARO_URL);
    }
}

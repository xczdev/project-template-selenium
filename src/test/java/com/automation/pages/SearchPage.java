package com.automation.pages;

import org.openqa.selenium.By;

public class SearchPage {

    public static final By SEARCH_INPUT = By.name("q");
    public static final By FIRST_RESULT  = By.cssSelector("[data-testid='result-title-a']");
}

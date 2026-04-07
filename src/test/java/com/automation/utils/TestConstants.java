package com.automation.utils;

/**
 * TestConstants - Contains all test configuration and constant values
 */
public class TestConstants {
    // Browser configuration
    public static final String DEFAULT_BROWSER = "CHROME";
    
    // Application URLs
    public static final String BASE_URL = "https://www.duckduckgo.com";
    public static final String LEMONDE_URL = "https://www.lemonde.fr";
    public static final String LEFIGARO_URL = "https://www.lefigaro.fr";
    
    // Timeouts (in seconds)
    public static final int IMPLICIT_WAIT = 10;
    public static final int EXPLICIT_WAIT = 15;
    public static final int PAGE_LOAD_TIMEOUT = 20;
    
    // Test Data
    public static final String VALID_USERNAME = "standard_user";
    public static final String VALID_PASSWORD = "secret_sauce";
    public static final String INVALID_USERNAME = "invalid_user";
    public static final String INVALID_PASSWORD = "wrong_password";
    public static final String LOCKED_OUT_USER = "locked_out_user";
}

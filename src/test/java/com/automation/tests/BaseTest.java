package com.automation.tests;

import com.automation.utils.DriverFactory;
import com.automation.utils.TestConstants;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * BaseTest - Abstract base class for all test classes
 * Handles setup, teardown, and common test operations
 */
public abstract class BaseTest {
    protected WebDriver driver;
    private final Logger logger = LogManager.getLogger(getClass());

    /**
     * Logs a message to the console AND adds it as a step in the Allure report.
     */
    protected void log(String message) {
        logger.info(message);
        Allure.step(message);
    }

    /**
     * Setup - Runs before each test
     * Initializes WebDriver and navigates to base URL
     */
    @BeforeMethod
    public void setUp() {
        System.out.println("========== TEST SETUP ==========");
        
        // Initialize driver
        driver = DriverFactory.initializeDriver(TestConstants.DEFAULT_BROWSER);
        System.out.println("WebDriver initialized with: " + TestConstants.DEFAULT_BROWSER);
        
        // Set implicit wait
        driver.manage().timeouts()
                .implicitlyWait(java.time.Duration.ofSeconds(TestConstants.IMPLICIT_WAIT));
        
        // Set page load timeout
        driver.manage().timeouts()
                .pageLoadTimeout(java.time.Duration.ofSeconds(TestConstants.PAGE_LOAD_TIMEOUT));
        
        // Navigate to base URL
        driver.navigate().to(TestConstants.BASE_URL);
        System.out.println("Navigated to: " + TestConstants.BASE_URL);
        
        System.out.println("========== TEST SETUP COMPLETED ==========\n");
    }

    /**
     * Teardown - Runs after each test
     * Closes WebDriver and cleans up resources
     */
    @AfterMethod
    public void tearDown() {
        System.out.println("\n========== TEST TEARDOWN ==========");
        
        if (driver != null) {
            // Optional: Take screenshot on failure (implement as needed)
            // String testName = getCurrentTestName();
            // takeScreenshot(testName);
            
            // Close driver
            DriverFactory.closeDriver();
            System.out.println("WebDriver closed successfully");
        }
        
        System.out.println("========== TEST TEARDOWN COMPLETED ==========");
    }

    /**
     * Asserts that the element identified by the given locator is visible on the page.
     * Waits up to IMPLICIT_WAIT seconds for the element to become visible.
     */
    protected void assertVisible(By locator) {
        try {
            new WebDriverWait(driver, java.time.Duration.ofSeconds(TestConstants.IMPLICIT_WAIT))
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
            Assert.assertTrue(driver.findElement(locator).isDisplayed(),
                    "Element should be visible: " + locator);
        } catch (Exception e) {
            Assert.fail("Element is not visible: " + locator + " — " + e.getMessage());
        }
    }

    /**
     * Get current test name (useful for logging and screenshots)
     */
    protected String getCurrentTestName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }
}

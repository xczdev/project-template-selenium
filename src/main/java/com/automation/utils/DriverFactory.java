package com.automation.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * DriverFactory - Manages WebDriver instance creation and lifecycle
 */
public class DriverFactory {
    private static WebDriver driver;

    /**
     * Initialize WebDriver based on browser type
     *
     * @param browserType Browser type (CHROME, FIREFOX)
     * @return WebDriver instance
     */
    public static WebDriver initializeDriver(String browserType) {
        if (driver == null) {
            switch (browserType.toUpperCase()) {
                case "CHROME":
                    driver = initializeChromeDriver();
                    break;
                case "FIREFOX":
                    driver = initializeFirefoxDriver();
                    break;
                default:
                    driver = initializeChromeDriver();
            }
        }
        return driver;
    }

    /**
     * Initialize Chrome WebDriver
     */
    private static WebDriver initializeChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        // Force X11 mode via XWayland — required on Fedora Wayland for window visibility
        options.addArguments("--ozone-platform=x11");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-dev-shm-usage");
        // Uncomment for headless mode
        // options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        return driver;
    }

    /**
     * Initialize Firefox WebDriver
     */
    private static WebDriver initializeFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        // Uncomment for headless mode
        // options.addArguments("--headless");
        WebDriver driver = new FirefoxDriver(options);
        driver.manage().window().maximize();
        return driver;
    }

    /**
     * Get current WebDriver instance
     */
    public static WebDriver getDriver() {
        return driver;
    }

    /**
     * Close WebDriver and clean up resources
     */
    public static void closeDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    /**
     * Reset driver instance (closes and sets to null)
     */
    public static void resetDriver() {
        closeDriver();
    }
}

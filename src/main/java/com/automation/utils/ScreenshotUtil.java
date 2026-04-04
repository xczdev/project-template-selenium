package com.automation.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ScreenshotUtil - Utility class for taking and saving screenshots
 */
public class ScreenshotUtil {
    private static final String SCREENSHOT_DIR = "target/screenshots/";

    /**
     * Take screenshot and save to file
     *
     * @param driver WebDriver instance
     * @param testName Name of the test
     * @return true if screenshot was taken successfully
     */
    public static boolean takeScreenshot(WebDriver driver, String testName) {
        try {
            // Create screenshots directory if it doesn't exist
            File screenshotDir = new File(SCREENSHOT_DIR);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            // Take screenshot
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File srcFile = screenshot.getScreenshotAs(OutputType.FILE);

            // Generate filename with timestamp
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String filename = testName + "_" + timestamp + ".png";
            String filePath = SCREENSHOT_DIR + filename;

            // Save screenshot
            File destFile = new File(filePath);
            FileUtils.copyFile(srcFile, destFile);

            System.out.println("✓ Screenshot saved: " + filePath);
            return true;

        } catch (IOException e) {
            System.err.println("✗ Failed to take screenshot: " + e.getMessage());
            return false;
        }
    }

    /**
     * Take screenshot with default naming
     */
    public static boolean takeScreenshot(WebDriver driver) {
        String testName = Thread.currentThread().getStackTrace()[2].getMethodName();
        return takeScreenshot(driver, testName);
    }
}

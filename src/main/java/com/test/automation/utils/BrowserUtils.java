package com.test.automation.utils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Utility class for browser-related operations.
 */
public class BrowserUtils {
    private static final Logger logger = LogManager.getLogger(BrowserUtils.class);
    
    /**
     * Private constructor to prevent instantiation
     */
    private BrowserUtils() {
    }
    
    /**
     * Takes a screenshot and returns the file path
     */
    public static String takeScreenshot(WebDriver driver, String name) {
        logger.info("Taking screenshot: {}", name);
        
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = name + "_" + timestamp + ".png";
            String filePath = "test-output/screenshots/" + fileName;
            
            // Create directory if it doesn't exist
            File directory = new File("test-output/screenshots");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            // Take screenshot
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File(filePath));
            
            logger.info("Screenshot saved to: " + filePath);
            return filePath;
        } catch (IOException e) {
            logger.error("Failed to take screenshot", e);
            return null;
        }
    }
    
    /**
     * Switches to a window by title
     */
    public static void switchToWindowByTitle(WebDriver driver, String title) {
        logger.info("Switching to window with title: " + title);
        
        String currentWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();
        
        for (String windowHandle : allWindows) {
            driver.switchTo().window(windowHandle);
            if (driver.getTitle().contains(title)) {
                logger.info("Switched to window: " + title);
                return;
            }
        }
        
        // If no window with the title is found, switch back to original window
        logger.warn("Window with title '" + title + "' not found. Switching back to original window.");
        driver.switchTo().window(currentWindow);
    }
    
    /**
     * Switches to a window by URL
     */
    public static void switchToWindowByUrl(WebDriver driver, String url) {
        logger.info("Switching to window with URL containing: " + url);
        
        String currentWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();
        
        for (String windowHandle : allWindows) {
            driver.switchTo().window(windowHandle);
            if (driver.getCurrentUrl().contains(url)) {
                logger.info("Switched to window with URL containing: " + url);
                return;
            }
        }
        
        // If no window with the URL is found, switch back to original window
        logger.warn("Window with URL containing '" + url + "' not found. Switching back to original window.");
        driver.switchTo().window(currentWindow);
    }
    
    /**
     * Gets all browser cookies
     */
    public static List<Cookie> getAllCookies(WebDriver driver) {
        logger.debug("Getting all cookies");
        return new ArrayList<>(driver.manage().getCookies());
    }
    
    /**
     * Deletes all browser cookies
     */
    public static void deleteAllCookies(WebDriver driver) {
        logger.info("Deleting all cookies");
        driver.manage().deleteAllCookies();
    }
    
    /**
     * Executes JavaScript
     */
    public static Object executeJavaScript(WebDriver driver, String script, Object... args) {
        logger.debug("Executing JavaScript: " + script);
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }
    
    /**
     * Scrolls to an element using JavaScript
     */
    public static void scrollToElement(WebDriver driver, WebElement element) {
        logger.debug("Scrolling to element");
        executeJavaScript(driver, "arguments[0].scrollIntoView(true);", element);
    }
    
    /**
     * Scrolls to the bottom of the page
     */
    public static void scrollToBottom(WebDriver driver) {
        logger.debug("Scrolling to bottom of page");
        executeJavaScript(driver, "window.scrollTo(0, document.body.scrollHeight)");
    }
    
    /**
     * Scrolls to the top of the page
     */
    public static void scrollToTop(WebDriver driver) {
        logger.debug("Scrolling to top of page");
        executeJavaScript(driver, "window.scrollTo(0, 0)");
    }
    
    /**
     * Gets the browser name
     */
    public static String getBrowserName(WebDriver driver) {
        logger.debug("Getting browser name");
        String userAgent = (String) executeJavaScript(driver, "return navigator.userAgent;");
        
        if (userAgent.contains("Chrome")) {
            return "Chrome";
        } else if (userAgent.contains("Firefox")) {
            return "Firefox";
        } else if (userAgent.contains("Safari") && !userAgent.contains("Chrome")) {
            return "Safari";
        } else if (userAgent.contains("Edge")) {
            return "Edge";
        } else {
            return "Unknown";
        }
    }
    
    /**
     * Opens a new tab and switches to it
     */
    public static void openNewTab(WebDriver driver) {
        logger.info("Opening new tab");
        executeJavaScript(driver, "window.open()");
        
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabs.size() - 1));
    }
    
    /**
     * Highlights an element on the page
     */
    public static void highlightElement(WebDriver driver, WebElement element) {
        logger.debug("Highlighting element");
        String originalStyle = element.getAttribute("style");
        executeJavaScript(driver, "arguments[0].setAttribute('style', arguments[1])", 
                element, "border: 2px solid red; background-color: yellow;");
        
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        executeJavaScript(driver, "arguments[0].setAttribute('style', arguments[1])", 
                element, originalStyle);
    }

    /**
     * Upload File
     */

    public static void fileUpload(WebDriver driver, WebElement element, String filePath) {
        logger.info("Uploading file: {}", filePath);
        element.sendKeys(filePath);
    }
}
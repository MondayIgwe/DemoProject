package com.test.automation.utils;

import java.time.Duration;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Utility class for handling various waits in Selenium.
 */
public class WaitUtils {
    private static final Logger logger = LogManager.getLogger(WaitUtils.class);
    
    /**
     * Private constructor to prevent instantiation
     */
    private WaitUtils() {
    }
    
    /**
     * Waits for page to fully load
     */
    public static void waitForPageLoad(WebDriver driver, int timeoutInSeconds) {
        logger.info("Waiting for page to load completely");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        
        try {
            wait.until((ExpectedCondition<Boolean>) wd ->
            {
                assert wd != null;
                return ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete");
            });
            logger.debug("Page loaded successfully");
        } catch (Exception e) {
            logger.warn("Page load wait timed out after " + timeoutInSeconds + " seconds", e);
        }
    }
    
    /**
     * Waits for an element to be visible
     */
    public static WebElement waitForElementVisible(WebDriver driver, By locator, int timeoutInSeconds) {
        logger.debug("Waiting for element to be visible: " + locator);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    /**
     * Waits for an element to be clickable
     */
    public static WebElement waitForElementClickable(WebDriver driver, By locator, int timeoutInSeconds) {
        logger.debug("Waiting for element to be clickable: " + locator);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    /**
     * Waits for an element to be invisible
     */
    public static boolean waitForElementInvisible(WebDriver driver, By locator, int timeoutInSeconds) {
        logger.debug("Waiting for element to be invisible: " + locator);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    
    /**
     * Waits for text to be present in element
     */
    public static boolean waitForTextPresent(WebDriver driver, By locator, String text, int timeoutInSeconds) {
        logger.debug("Waiting for text to be present in element: " + locator + ", Text: " + text);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }
    
    /**
     * Creates a fluent wait with custom settings
     */
    public static <T> Wait<T> createFluentWait(T input, int timeoutInSeconds, int pollingIntervalMillis) {
        return new FluentWait<>(input)
                .withTimeout(Duration.ofSeconds(timeoutInSeconds))
                .pollingEvery(Duration.ofMillis(pollingIntervalMillis))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }
    
    /**
     * Waits for a custom condition using fluent wait
     */
    public static <T, R> R waitForCondition(T input, Function<T, R> condition, int timeoutInSeconds, int pollingIntervalMillis) {
        logger.debug("Waiting for custom condition");
        Wait<T> wait = createFluentWait(input, timeoutInSeconds, pollingIntervalMillis);
        return wait.until(condition);
    }
    
    /**
     * Waits for AJAX calls to complete
     */
    public static void waitForAjax(WebDriver driver, int timeoutInSeconds) {
        logger.debug("Waiting for AJAX calls to complete");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        
        try {
            wait.until((ExpectedCondition<Boolean>) wd -> 
                (Boolean) ((JavascriptExecutor) wd).executeScript("return jQuery.active == 0"));
            logger.debug("AJAX calls completed");
        } catch (Exception e) {
            logger.warn("AJAX wait timed out or jQuery not available", e);
        }
    }
    
    /**
     * Waits for angular requests to complete
     */
    public static void waitForAngular(WebDriver driver, int timeoutInSeconds) {
        logger.debug("Waiting for Angular requests to complete");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        
        try {
            wait.until((ExpectedCondition<Boolean>) wd -> {
                JavascriptExecutor jsExecutor = (JavascriptExecutor) wd;
                String angularReadyScript = "return angular.element(document).injector().get('$http').pendingRequests.length === 0";
                return (Boolean) jsExecutor.executeScript(angularReadyScript);
            });
            logger.debug("Angular requests completed");
        } catch (Exception e) {
            logger.warn("Angular wait timed out or Angular not available", e);
        }
    }
}
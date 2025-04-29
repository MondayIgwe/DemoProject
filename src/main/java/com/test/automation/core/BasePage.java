package com.test.automation.core;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Base class for all Page Objects.
 * Provides common functionality for page interactions.
 */
public abstract class BasePage {
    protected static final Logger logger = LogManager.getLogger(BasePage.class);
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;

    /**
     * Constructor for BasePage
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
        logger.debug("Initialized {}",
                this.getClass().getSimpleName());
    }

    /**
     * Waits for an element to be visible and returns it
     */
    protected WebElement waitForElementVisible(By locator) {
        try {
            logger.debug("Waiting for element to be visible: {}",
                    locator);
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            logger.error("Element not visible after timeout: {}", locator, e);
            throw e;
        }
    }

    /**
     * Waits for an element to be clickable and returns it
     */
    protected WebElement waitForElementClickable(By locator) {
        try {
            logger.debug("Waiting for element to be clickable: {}", locator);
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            logger.error("Element not clickable after timeout: {}", locator, e);
            throw e;
        }
    }

    /**
     * Checks if an element exists
     */
    public boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Safely clicks on an element with proper waits
     */
    protected void click(By locator) {
        logger.debug("Clicking on element: {}", locator);
        waitForElementClickable(locator).click();
    }

    /**
     * Safely enters text in an element with proper waits
     */
    protected void sendKeys(By locator, String text) {
        logger.debug("Entering text in element: {}, Text: {}", locator, text);
        WebElement element = waitForElementVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Gets text from an element
     */
    protected String getText(By locator) {
        logger.debug("Getting text from element: " + locator);
        return waitForElementVisible(locator).getText();
    }

    /**
     * Scrolls to an element
     */
    protected void scrollToElement(WebElement element) {
        logger.debug("Scrolling to element");
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Scrolls to an element using locator
     */
    protected void scrollToElement(By locator) {
        WebElement element = driver.findElement(locator);
        scrollToElement(element);
    }
}
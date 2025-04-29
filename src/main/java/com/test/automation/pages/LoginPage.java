package com.test.automation.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.test.automation.core.BasePage;

/**
 * Page Object for the Login page.
 * Contains all elements and actions related to the login functionality.
 */
public class LoginPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(LoginPage.class);
    
    // Page elements using By locators
    private final By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.className("error-message");
    private final By rememberMeCheckbox = By.id("rememberMe");
    private final By forgotPasswordLink = By.linkText("Forgot Password?");
    
    /**
     * Constructor for LoginPage
     */
    public LoginPage(WebDriver driver) {
        super(driver);
        logger.info("LoginPage initialized");
    }
    
    /**
     * Navigate to the login page
     */
    public LoginPage navigateTo(String url) {
        logger.info("Navigating to login page: {}", url);
        driver.get(url);
        return this;
    }
    
    /**
     * Enter username in the username field
     */
    public LoginPage enterUsername(String username) {
        logger.info("Entering username: {}", username);
        sendKeys(usernameField, username);
        return this;
    }
    
    /**
     * Enter password in the password field
     */
    public LoginPage enterPassword(String password) {
        logger.info("Entering password: [MASKED]");
        sendKeys(passwordField, password);
        return this;
    }
    
    /**
     * Click the login button
     */
    public void clickLogin() {
        logger.info("Clicking login button");
        click(loginButton);
    }
    
    /**
     * Login with the specified credentials
     */
    public void login(String username, String password) {
        logger.info("Performing login with username: {}", username);
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }
    
    /**
     * Check if the remember me checkbox is selected
     */
    public boolean isRememberMeSelected() {
        logger.debug("Checking if remember me is selected");
        return driver.findElement(rememberMeCheckbox).isSelected();
    }
    
    /**
     * Set the remember me checkbox
     */
    public LoginPage setRememberMe(boolean select) {
        logger.info("Setting remember me checkbox to: " + select);
        
        boolean isSelected = isRememberMeSelected();
        if (select != isSelected) {
            click(rememberMeCheckbox);
        }
        
        return this;
    }
    
    /**
     * Click on forgot password link
     */
    public void clickForgotPassword() {
        logger.info("Clicking forgot password link");
        click(forgotPasswordLink);
    }
    
    /**
     * Get the error message text
     */
    public String getErrorMessage() {
        logger.debug("Getting error message");
        if (isElementPresent(errorMessage)) {
            return getText(errorMessage);
        }
        return "";
    }
    
    /**
     * Check if login was successful by verifying URL or element presence
     */
    public boolean isLoginSuccessful() {
        logger.debug("Checking if login was successful");
        // This is a simple implementation. In a real project, you might want to check
        // for specific elements on the dashboard or a URL change.
        return !isElementPresent(errorMessage) && !driver.getCurrentUrl().contains("login");
    }
}
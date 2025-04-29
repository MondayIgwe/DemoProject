package com.test.automation.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.test.automation.config.ConfigManager;
import com.test.automation.core.BaseTest;
import com.test.automation.pages.HomePage;
import com.test.automation.pages.LoginPage;

/**
 * Test class for login functionality.
 */
public class LoginTest extends BaseTest {
    private static final Logger logger = LogManager.getLogger(LoginTest.class);

    @AfterMethod
    public void clearProperties() {
        System.clearProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY);
        System.clearProperty(ChromeDriverService.CHROME_DRIVER_LOG_LEVEL_PROPERTY);
    }
    /**
     * Test for successful login
     */
    @Test(description = "Verify successful login with valid credentials")
    public void testSuccessfulLogin() {
        logger.info("Starting test: testSuccessfulLogin");
        
        // Get credentials from config
        String username = ConfigManager.getInstance().getProperty("test.username");
        String password = ConfigManager.getInstance().getProperty("test.password");
        String baseUrl = ConfigManager.getInstance().getProperty("base.url");
        
        // Initialize pages
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);
        
        // Navigate to login page
        loginPage.navigateTo(baseUrl + "/login");
        
        // Perform login
        loginPage.login(username, password);
        
        // Verify login was successful
        Assert.assertTrue(homePage.isAt(), "User is not on the home page after login");
        
        // Verify welcome message contains username
        String welcomeMessage = homePage.getWelcomeMessage();
        Assert.assertTrue(welcomeMessage.contains(username), 
                "Welcome message does not contain username");
        
        logger.info("Test passed: testSuccessfulLogin");
    }
    
    /**
     * Test for unsuccessful login with invalid credentials
     */
    @Test(description = "Verify error message for login with invalid credentials")
    public void testInvalidLogin() {
        logger.info("Starting test: testInvalidLogin");
        
        String baseUrl = ConfigManager.getInstance().getProperty("base.url");
        
        // Initialize pages
        LoginPage loginPage = new LoginPage(driver);
        
        // Navigate to login page
        loginPage.navigateTo(baseUrl + "/login");
        
        // Perform login with invalid credentials
        loginPage.login("invaliduser", "invalidpassword");
        
        // Verify error message
        String errorMessage = loginPage.getErrorMessage();
        Assert.assertFalse(errorMessage.isEmpty(), "Error message is not displayed");
        Assert.assertTrue(errorMessage.contains("Invalid"), 
                "Error message does not contain expected text");
        
        logger.info("Test passed: testInvalidLogin");
    }
    
    /**
     * Data provider for login with different credentials
     */
    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return new Object[][] {
            {"validuser", "validpassword", true},
            {"invaliduser", "invalidpassword", false},
            {"validuser", "invalidpassword", false},
            {"invaliduser", "validpassword", false},
        };
    }
    
    /**
     * Test login with different credentials using data provider
     */
    @Test(dataProvider = "loginData", description = "Verify login with different credentials")
    public void testLoginWithDifferentCredentials(String username, String password, boolean expectSuccess) {
        logger.info("Starting test: testLoginWithDifferentCredentials with username: {}", username);
        
        String baseUrl = ConfigManager.getInstance().getProperty("base.url");
        
        // Initialize pages
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);
        
        // Navigate to login page
        loginPage.navigateTo(baseUrl + "/login");
        
        // Perform login
        loginPage.login(username, password);
        
        // Verify login result
        if (expectSuccess) {
            Assert.assertTrue(homePage.isAt(), "User is not on the home page after login");
        } else {
            Assert.assertTrue(!loginPage.getErrorMessage().isEmpty(), 
                    "Error message is not displayed for invalid login");
        }
        
        logger.info("Test passed: testLoginWithDifferentCredentials with username: " + username);
    }
    
    /**
     * Test remember me functionality
     */
    @Test(description = "Verify remember me functionality")
    public void testRememberMe() {
        logger.info("Starting test: testRememberMe");
        
        String baseUrl = ConfigManager.getInstance().getProperty("base.url");
        String username = ConfigManager.getInstance().getProperty("test.username");
        String password = ConfigManager.getInstance().getProperty("test.password");
        
        // Initialize pages
        LoginPage loginPage = new LoginPage(driver);
        
        // Navigate to login page
        loginPage.navigateTo(baseUrl + "/login");
        
        // Check remember me and login
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.setRememberMe(true);
        loginPage.clickLogin();
        
        // Verify login was successful
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isAt(), "User is not on the home page after login");
        
        // Logout
        homePage.logout();
        
        // Verify username is remembered
        loginPage = new LoginPage(driver);
        String rememberedUsername = driver.findElement(By.id("username")).getAttribute("value");
        Assert.assertEquals(rememberedUsername, username, "Username is not remembered");
        
        logger.info("Test passed: testRememberMe");
    }
}
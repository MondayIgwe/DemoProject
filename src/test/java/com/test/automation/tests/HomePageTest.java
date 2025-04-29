package com.test.automation.tests;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.test.automation.config.ConfigManager;
import com.test.automation.core.BaseTest;
import com.test.automation.pages.HomePage;
import com.test.automation.pages.LoginPage;

/**
 * Test class for home page functionality.
 */
public class HomePageTest extends BaseTest {
    private static final Logger logger = LogManager.getLogger(HomePageTest.class);
    private HomePage homePage;
    
    /**
     * Setup method to login before each test
     */
    @BeforeMethod
    public void setupTest() {
        logger.info("Setting up test - logging in");
        
        String baseUrl = ConfigManager.getInstance().getProperty("base.url");
        String username = ConfigManager.getInstance().getProperty("test.username");
        String password = ConfigManager.getInstance().getProperty("test.password");
        
        // Login before testing home page
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo(baseUrl + "/login");
        loginPage.login(username, password);
        
        // Initialize home page
        homePage = new HomePage(driver);
        homePage.waitForPageToLoad();
    }
    
    /**
     * Test navigation functionality
     */
    @Test(description = "Verify navigation sections on home page")
    public void testNavigationSections() {
        logger.info("Starting test: testNavigationSections");
        
        // Get all navigation sections
        List<String> sections = homePage.getNavigationSections();
        
        // Verify expected sections are present
        Assert.assertTrue(sections.contains("Dashboard"), "Dashboard section not found");
        Assert.assertTrue(sections.contains("Profile"), "Profile section not found");
        Assert.assertTrue(sections.contains("Settings"), "Settings section not found");
        
        logger.info("Test passed: testNavigationSections");
    }
    
    /**
     * Test section navigation
     */
    @Test(description = "Verify navigation to a specific section")
    public void testSectionNavigation() {
        logger.info("Starting test: testSectionNavigation");
        
        // Navigate to a specific section
        String sectionName = "Profile";
        homePage.navigateToSection(sectionName);
        
        // Verify navigation was successful
        // In a real implementation, you would verify URL or specific elements
        Assert.assertTrue(driver.getCurrentUrl().contains("profile"), 
                "URL does not indicate navigation to profile section");
        
        logger.info("Test passed: testSectionNavigation");
    }
    
    /**
     * Test search functionality
     */
    @Test(description = "Verify search functionality")
    public void testSearch() {
        logger.info("Starting test: testSearch");
        
        // Perform search
        String searchQuery = "test query";
        homePage.search(searchQuery);
        
        // Verify search results
        // In a real implementation, you would verify search results elements
        Assert.assertTrue(driver.getCurrentUrl().contains("search"), 
                "URL does not indicate search was performed");
        Assert.assertTrue(driver.getCurrentUrl().contains(searchQuery), 
                "Search query is not reflected in URL");
        
        logger.info("Test passed: testSearch");
    }
    
    /**
     * Test logout functionality
     */
    @Test(description = "Verify logout functionality")
    public void testLogout() {
        logger.info("Starting test: testLogout");
        
        // Perform logout
        homePage.logout();
        
        // Verify logout was successful
        Assert.assertTrue(driver.getCurrentUrl().contains("login"), 
                "URL does not indicate user was logged out");
        
        // Verify login page is displayed
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isElementPresent(By.id("username")),
                "Login page username field is not displayed after logout");
        
        logger.info("Test passed: testLogout");
    }
    
    /**
     * Test user menu functionality
     */
    @Test(description = "Verify user menu functionality")
    public void testUserMenu() {
        logger.info("Starting test: testUserMenu");
        
        // Open user menu
        homePage.openUserMenu();
        
        // Verify user menu items
        Assert.assertTrue(homePage.isElementPresent(By.id("logout")), 
                "Logout option not found in user menu");
        Assert.assertTrue(homePage.isElementPresent(By.id("profile")), 
                "Profile option not found in user menu");
        
        logger.info("Test passed: testUserMenu");
    }
}
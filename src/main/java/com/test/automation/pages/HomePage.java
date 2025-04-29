package com.test.automation.pages;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.test.automation.core.BasePage;

/**
 * Page Object for the Home page.
 * Contains all elements and actions related to the home page functionality.
 */
public class HomePage extends BasePage {
    private static final Logger logger = LogManager.getLogger(HomePage.class);
    
    // Page elements using By locators
    private final By welcomeMessage = By.id("welcome-message");
    private final By userMenu = By.id("user-menu");
    private final By logoutLink = By.id("logout");
    private final By navigationLinks = By.cssSelector("nav a");
    private final By searchBox = By.id("search-input");
    private final By searchButton = By.id("search-button");
    
    /**
     * Constructor for HomePage
     */
    public HomePage(WebDriver driver) {
        super(driver);
        logger.info("HomePage initialized");
    }
    
    /**
     * Check if the user is on the home page
     */
    public boolean isAt() {
        logger.debug("Checking if user is on home page");
        return isElementPresent(welcomeMessage);
    }
    
    /**
     * Get the welcome message text
     */
    public String getWelcomeMessage() {
        logger.debug("Getting welcome message");
        return getText(welcomeMessage);
    }
    
    /**
     * Open the user menu
     */
    public HomePage openUserMenu() {
        logger.info("Opening user menu");
        click(userMenu);
        return this;
    }
    
    /**
     * Click on logout
     */
    public void logout() {
        logger.info("Logging out");
        openUserMenu();
        click(logoutLink);
    }
    
    /**
     * Navigate to a section by clicking on a navigation link
     */
    public void navigateToSection(String sectionName) {
        logger.info("Navigating to section: " + sectionName);
        By sectionLink = By.linkText(sectionName);
        click(sectionLink);
    }
    
    /**
     * Get all available navigation sections
     */
    public List<String> getNavigationSections() {
        logger.debug("Getting all navigation sections");
        List<WebElement> links = driver.findElements(navigationLinks);
        return links.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }
    
    /**
     * Perform a search
     */
    public void search(String query) {
        logger.info("Performing search with query: " + query);
        sendKeys(searchBox, query);
        click(searchButton);
    }
    
    /**
     * Check if a specific section is available in the navigation
     */
    public boolean isSectionAvailable(String sectionName) {
        logger.debug("Checking if section is available: " + sectionName);
        return getNavigationSections().contains(sectionName);
    }
    
    /**
     * Wait for home page to load completely
     */
    public HomePage waitForPageToLoad() {
        logger.info("Waiting for home page to load");
        waitForElementVisible(welcomeMessage);
        return this;
    }
}
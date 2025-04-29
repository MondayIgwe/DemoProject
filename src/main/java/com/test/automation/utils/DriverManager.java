package com.test.automation.utils;

import java.io.File;
import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumNetworkConditions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;

import com.test.automation.config.ConfigManager;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Manages WebDriver instances for different browsers.
 */
public class DriverManager {
    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    /**
     * Private constructor to prevent instantiation
     */
    private DriverManager() {
    }

    /**
     * Gets a WebDriver instance for the specified browser
     */
    public static WebDriver getDriver(String browser) {
        WebDriver driver = null;

        try {
            switch (browser.toLowerCase()) {
                case "chrome":
                    driver = setupChromeDriver();
                    break;
                case "firefox":
                    driver = setupFirefoxDriver();
                    break;
                case "edge":
                    driver = setupEdgeDriver();
                    break;
                case "safari":
                    driver = setupSafariDriver();
                    break;
                default:
                    logger.warn("Unsupported browser: {}. Using Chrome as default.", browser);
                    driver = setupChromeDriver();
            }

            // Configure WebDriver
            configureDriver(driver);

            // Store driver in ThreadLocal
            driverThreadLocal.set(driver);
            logger.info("{} WebDriver initialized successfully", browser);

        } catch (Exception e) {
            logger.error("Failed to initialize WebDriver for {}", browser, e);
            throw e;
        }

        return driver;
    }

    /**
     * Sets up Chrome WebDriver
     */


    private void simulateNetworkConditions() {
        ChromiumNetworkConditions networkConditions = new ChromiumNetworkConditions();
        networkConditions.setLatency(Duration.ofMillis(20));
        networkConditions.setOffline(false);
        networkConditions.setDownloadThroughput(200 * 1024 / 8);
        networkConditions.setUploadThroughput(2000 * 1024 / 8);
        ((ChromeDriver) driverThreadLocal.get()).setNetworkConditions(networkConditions);
    }

    private static ChromeDriverService getDriverService() {
        ChromeDriverService serviceObj = new ChromeDriverService.Builder().usingAnyFreePort()
                .withLogFile(new File("")).build();
        System.out.printf("%sDriver name: ", serviceObj.getDriverName());
        System.out.printf("%s Driver property", serviceObj.getDriverProperty());
        return serviceObj;
    }

    private static WebDriver setupChromeDriver() {
        logger.info("Setting up Chrome WebDriver");
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        // Add headless mode if configured
        if (ConfigManager.getInstance().getBooleanProperty("headless")) {
            options.addArguments("--headless=new");
            logger.info("Running Chrome in headless mode");
        }

        // Add other Chrome options
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");

        return new ChromeDriver(options);
    }

    /**
     * Sets up Firefox WebDriver
     */
    private static WebDriver setupFirefoxDriver() {
        logger.info("Setting up Firefox WebDriver");
        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions options = new FirefoxOptions();

        // Add headless mode if configured
        if (ConfigManager.getInstance().getBooleanProperty("headless")) {
            options.addArguments("--headless");
            logger.info("Running Firefox in headless mode");
        }

        return new FirefoxDriver(options);
    }

    /**
     * Sets up Edge WebDriver
     */
    private static WebDriver setupEdgeDriver() {
        logger.info("Setting up Edge WebDriver");
        WebDriverManager.edgedriver().setup();

        EdgeOptions options = new EdgeOptions();

        // Add headless mode if configured
        if (ConfigManager.getInstance().getBooleanProperty("headless")) {
            options.addArguments("--headless");
            logger.info("Running Edge in headless mode");
        }

        return new EdgeDriver(options);
    }

    /**
     * Sets up Safari WebDriver
     */
    private static WebDriver setupSafariDriver() {
        logger.info("Setting up Safari WebDriver");
        return new SafariDriver();
    }

    /**
     * Configures WebDriver with common settings
     */
    private static void configureDriver(WebDriver driver) {
        ConfigManager config = ConfigManager.getInstance();

        // Set implicit wait
        int implicitWait = config.getIntProperty("implicit.wait");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

        // Set page load timeout
        int pageLoadTimeout = config.getIntProperty("page.load.timeout");
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));

        // Maximize window
        if (config.getBooleanProperty("maximize.window")) {
            driver.manage().window().maximize();
        }

        // Navigate to base URL if specified
        String baseUrl = config.getProperty("base.url");
        if (baseUrl != null && !baseUrl.isEmpty()) {
            driver.get(baseUrl);
            logger.info("Navigated to base URL: " + baseUrl);
        }
    }

    /**
     * Gets the current WebDriver instance from ThreadLocal
     */
    public static WebDriver getCurrentDriver() {
        return driverThreadLocal.get();
    }

    /**
     * Quits the WebDriver and removes it from ThreadLocal
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
            logger.info("WebDriver closed and removed from ThreadLocal");
        }
    }
}
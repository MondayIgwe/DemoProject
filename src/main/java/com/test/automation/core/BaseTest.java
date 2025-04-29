package com.test.automation.core;

import java.io.File;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.test.automation.config.ConfigManager;
import com.test.automation.utils.DriverManager;

/**
 * Base class for all test classes.
 * Handles test setup, teardown, and reporting.
 */
public abstract class BaseTest {
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected WebDriver driver;
    protected ExtentTest test;
    
    private static ExtentReports extent;
    private static final String REPORT_PATH = "test-output/ExtentReport.html";
    private static final String SCREENSHOT_PATH = "test-output/screenshots/";
    
    /**
     * Setup before the entire test suite
     */
    @BeforeSuite
    public void setupSuite() {
        logger.info("Setting up test suite");
        
        // Initialize ExtentReports
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter(REPORT_PATH);
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        
        // Add system info
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        
        // Create screenshot directory
        File screenshotDir = new File(SCREENSHOT_PATH);
        if (!screenshotDir.exists()) {
            screenshotDir.mkdirs();
        }
    }
    
    /**
     * Setup before each test method
     */
    @BeforeMethod
    @Parameters({"browser"})
    public void setup(Method method, @Optional String browser) {
        // If browser parameter is not provided via TestNG, get it from config
        if (browser == null || browser.isEmpty()) {
            browser = ConfigManager.getInstance().getProperty("browser", "chrome");
        }

        logger.info("Starting test: {} using {}", method.getName(), browser);
        
        // Initialize the WebDriver
        driver = DriverManager.getDriver(browser);
        
        // Create test for ExtentReports
        test = extent.createTest(method.getName());
        test.info("Starting test with browser: " + browser);
    }
    
    /**
     * Cleanup after each test method
     */
    @AfterMethod
    public void tearDown(ITestResult result) {
        // Log test result
        if (result.getStatus() == ITestResult.FAILURE) {
            logger.error("Test failed: {}", result.getName(), result.getThrowable());
            test.log(Status.FAIL, "Test Failed: " + result.getThrowable().getMessage());
            
            // Capture screenshot on failure
            captureScreenshot(result.getName());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            logger.info("Test passed: {}", result.getName());
            test.log(Status.PASS, "Test Passed");
        } else {
            logger.info("Test skipped: {}", result.getName());
            test.log(Status.SKIP, "Test Skipped");
        }
        
        // Quit the WebDriver
        if (driver != null) {
            driver.close();
            driver.quit();
            logger.info("WebDriver closed");
        }
    }
    
    /**
     * Cleanup after the entire test suite
     */
    @AfterSuite
    public void tearDownSuite() {
        logger.info("Tearing down test suite");
        
        // Flush ExtentReports
        if (extent != null) {
            extent.flush();
            logger.info("ExtentReports finalized at: " + REPORT_PATH);
        }
    }
    
    /**
     * Captures a screenshot and adds it to the report
     */
    private void captureScreenshot(String testName) {
        if (driver == null) {
            logger.warn("WebDriver is null, cannot capture screenshot");
            return;
        }
        
        try {
            // Generate screenshot name with timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String screenshotName = testName + "_" + timestamp + ".png";
            String screenshotPath = SCREENSHOT_PATH + screenshotName;
            
            // Take screenshot
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File(screenshotPath));
            
            // Add screenshot to report
            test.addScreenCaptureFromPath(screenshotPath);
            logger.info("Screenshot captured: {}", screenshotPath);
        } catch (Exception e) {
            logger.error("Failed to capture screenshot", e);
        }
    }
}
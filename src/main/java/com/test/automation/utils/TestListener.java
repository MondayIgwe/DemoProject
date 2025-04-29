package com.test.automation.utils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

/**
 * TestNG listener for custom test execution reporting.
 */
public class TestListener implements ITestListener {
    private static final Logger logger = LogManager.getLogger(TestListener.class);
    private static final String SCREENSHOT_PATH = "test-output/screenshots/";
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    
    /**
     * Initialize ExtentReports instance once per test run
     */
    private static synchronized ExtentReports getExtentReports() {
        if (extent == null) {
            ExtentSparkReporter htmlReporter = new ExtentSparkReporter("test-output/ExtentReport.html");
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
        return extent;
    }
    
    @Override
    public void onStart(ITestContext context) {
        logger.info("Starting test suite: " + context.getName());
    }
    
    @Override
    public void onFinish(ITestContext context) {
        logger.info("Finishing test suite: " + context.getName());
        getExtentReports().flush();
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Starting test: " + result.getName());
        ExtentTest test = getExtentReports().createTest(result.getName());
        extentTest.set(test);
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test passed: " + result.getName());
        extentTest.get().log(Status.PASS, "Test passed");
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: " + result.getName(), result.getThrowable());
        
        // Log failure details
        extentTest.get().log(Status.FAIL, "Test failed: " + result.getThrowable().getMessage());
        
        // Get driver from test class
        Object testClass = result.getInstance();
        WebDriver driver = null;
        
        try {
            driver = (WebDriver) testClass.getClass().getDeclaredField("driver").get(testClass);
        } catch (Exception e) {
            logger.error("Could not get WebDriver instance from test class", e);
        }
        
        // Take screenshot if driver is available
        if (driver != null) {
            String screenshotPath = captureScreenshot(driver, result.getName());
            if (screenshotPath != null) {
                extentTest.get().fail("Screenshot",
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            }
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        logger.info("Test skipped: " + result.getName());
        extentTest.get().log(Status.SKIP, "Test skipped");
    }
    
    /**
     * Captures screenshot and returns the file path
     */
    private String captureScreenshot(WebDriver driver, String testName) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String screenshotName = testName + "_" + timestamp + ".png";
            String screenshotPath = SCREENSHOT_PATH + screenshotName;
            
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File(screenshotPath));
            
            logger.info("Screenshot captured: " + screenshotPath);
            return screenshotPath;
        } catch (Exception e) {
            logger.error("Failed to capture screenshot", e);
            return null;
        }
    }
}
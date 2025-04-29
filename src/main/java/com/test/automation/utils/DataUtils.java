package com.test.automation.utils;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.github.javafaker.Faker;

/**
 * Utility class for test data handling.
 */
public class DataUtils {
    private static final Logger logger = LogManager.getLogger(DataUtils.class);
    private static final Faker faker = new Faker();

    /**
     * Private constructor to prevent instantiation
     */
    private DataUtils() {
    }

    /**
     * Reads test data from an Excel file
     * @param filePath Path to the Excel file
     * @param sheetName Name of the sheet to read
     * @return Object[][] containing the test data
     */
    public static Object[][] getExcelData(String filePath, String sheetName) {
        logger.info("Reading test data from Excel: " + filePath + ", Sheet: " + sheetName);
        Object[][] data = null;

        try (Workbook workbook = WorkbookFactory.create(new FileInputStream(filePath))) {
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getLastRowNum();
            int colCount = sheet.getRow(0).getLastCellNum();

            data = new Object[rowCount][colCount];

            for (int i = 1; i <= rowCount; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0; j < colCount; j++) {
                    Cell cell = row.getCell(j);
                    if (cell != null) {
                        if (cell.getCellType() == CellType.STRING) {
                            data[i - 1][j] = cell.getStringCellValue();
                        } else if (cell.getCellType() == CellType.NUMERIC) {
                            data[i - 1][j] = cell.getNumericCellValue();
                        } else if (cell.getCellType() == CellType.BOOLEAN) {
                            data[i - 1][j] = cell.getBooleanCellValue();
                        } else {
                            data[i - 1][j] = "";
                        }
                    } else {
                        data[i - 1][j] = "";
                    }
                }
            }

            logger.info("Successfully read " + rowCount + " rows of test data");

        } catch (IOException e) {
            logger.error("Failed to read Excel data", e);
        }

        return data;
    }

    /**
     * Reads test data from a CSV file
     * @param filePath Path to the CSV file
     * @return List of Maps containing the test data
     */
    public static List<Map<String, String>> getCsvData(String filePath) {
        logger.info("Reading test data from CSV: " + filePath);
        List<Map<String, String>> data = new ArrayList<>();

        // Note: In a real implementation, you would use a CSV parsing library
        // This is a placeholder for demonstration purposes

        logger.info("Successfully read CSV data");
        return data;
    }

    /**
     * Reads properties from a file
     * @param filePath Path to the properties file
     * @return Properties object
     */
    public static Properties getProperties(String filePath) {
        logger.info("Reading properties from: " + filePath);
        Properties properties = new Properties();

        try (FileReader reader = new FileReader(filePath)) {
            properties.load(reader);
            logger.info("Successfully loaded properties");
        } catch (IOException e) {
            logger.error("Failed to read properties file", e);
        }

        return properties;
    }

    /**
     * Generates random test data using JavaFaker
     * @return Map containing random data
     */
    public static Map<String, String> generateRandomTestData() {
        logger.info("Generating random test data");
        Map<String, String> testData = new HashMap<>();

        testData.put("firstName", faker.name().firstName());
        testData.put("lastName", faker.name().lastName());
        testData.put("email", faker.internet().emailAddress());
        testData.put("address", faker.address().streetAddress());
        testData.put("city", faker.address().city());
        testData.put("state", faker.address().state());
        testData.put("zipCode", faker.address().zipCode());
        testData.put("phoneNumber", faker.phoneNumber().phoneNumber());

        return testData;
    }

    /**
     * Generates a random username
     * @return Random username
     */
    public static String generateRandomUsername() {
        return faker.name().username();
    }

    /**
     * Generates a random email address
     * @return Random email address
     */
    public static String generateRandomEmail() {
        return faker.internet().emailAddress();
    }

    /**
     * Generates a random password
     * @param minLength Minimum length of the password
     * @param maxLength Maximum length of the password
     * @return Random password
     */
    public static String generateRandomPassword(int minLength, int maxLength) {
        return faker.internet().password(minLength, maxLength, true, true, true);
    }
}
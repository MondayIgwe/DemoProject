# Selenium TestNG Test Automation Framework

A comprehensive test automation framework built with Selenium WebDriver, TestNG, and Maven.

## Features

- Page Object Model (POM) design pattern
- WebDriver management with cross-browser support
- Configuration management for different environments
- Logging with Log4J2
- Reporting with ExtentReports
- Screenshot capture on test failures
- Data-driven testing capabilities
- Parallel test execution

## Project Structure

```
selenium-testng-framework/
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── test/
│   │               └── automation/
│   │                   ├── config/         # Configuration management
│   │                   ├── core/           # Base classes
│   │                   ├── pages/          # Page objects
│   │                   └── utils/          # Utility classes
│   │
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── test/
│       │           └── automation/
│       │               └── tests/         # Test classes
│       │
│       └── resources/
│           ├── config.properties         # Configuration properties
│           ├── log4j2.xml                # Logging configuration
│           └── testng.xml                # TestNG suite configuration
│
└── pom.xml                               # Maven configuration
```

## Getting Started

### Prerequisites

- Java JDK 11 or higher
- Maven 3.6 or higher
- Chrome, Firefox, or Edge browser

### Installation

1. Clone this repository
2. Navigate to the project directory
3. Run `mvn clean install`

### Running Tests

To run all tests:

```
mvn test
```

To run a specific test class:

```
mvn test -Dtest=LoginTest
```

To run tests with a specific browser:

```
mvn test -Dbrowser=firefox
```

## Configuration

All configuration settings are in `src/test/resources/config.properties`.

Key properties:

- `base.url`: Base URL of the application under test
- `browser`: Default browser (chrome, firefox, edge, safari)
- `headless`: Run in headless mode (true/false)
- `implicit.wait`: Implicit wait timeout in seconds
- `page.load.timeout`: Page load timeout in seconds

## Reporting

After test execution, reports can be found in:

- ExtentReports: `test-output/ExtentReport.html`
- TestNG reports: `target/surefire-reports`
- Screenshots: `test-output/screenshots`
- Logs: `test-output/logs/test.log`

## License

This project is licensed under the MIT License.
package com.test.automation.tests.playwright;

import com.microsoft.playwright.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.nio.file.Path;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class Base {

    Page page = null;
    Browser browser;
    Playwright playwright;

    @BeforeMethod
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().
                launch(new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setDownloadsPath(Path.of("src/test/resources/downloads")));
        page = browser.newPage();
        page.navigate("https://ecommerce-playground.lambdatest.io/");
    }

    @Test
    public void test() {
        Locator account = page.locator("//a[contains(.,' My account')][@role='button']");
        account.hover(new Locator.HoverOptions().setForce(true).setTrial(true));

        String login = "//a[contains(.,'Login')]";
        if (page.locator(login).isVisible()) {
            System.out.println("Login link is visible");
            page.locator(login).click();
            page.getByPlaceholder("E-Mail Address", new Page.GetByPlaceholderOptions()
                    .setExact(true)).fill("sam@gmail.com");
            assertThat(page).not().hasTitle("Google");
            String email = page.locator("label[for='input-email']").textContent();
            assertThat(page.locator("label[for='input-email']")).hasText("E-Mail Address");
            System.out.format("this is the text: %s ", email);

            //System.out.format("is checked%b: ", page.getByLabel("Password").isVisible());
        } else {
            System.out.println("Login link is not visible");
        }
    }

    @AfterMethod
    public void tearDown() {
        if (page != null) {
            page.close();
            browser.close();
            playwright.close();
        }
    }
    /**
     * Goals:
     * Locator strategies - placeholders
     * Assertions
     * textcontent
     */
}

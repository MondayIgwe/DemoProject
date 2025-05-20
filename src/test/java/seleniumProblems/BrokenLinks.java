package seleniumProblems;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BrokenLinks {
    /* Understand the problem domain context to get the concept
     * Characters of broken links
     * link is url
     * anchor tags: a and href attribute with value url
     * Status code of 200
     * error of 404 or 500
     * Web server response when a user clicks the link
     */

    // Step - 1: Identify the broken links

    // Step - 2: Check the status code of the link
    // Step - 3: Check the response of the web server

    static final String herokuapp = "https://the-internet.herokuapp.com/";
    static final String amazon = "https://amazon.com/";
    static final String demoqa = "https://demoqa.com/links";
    static WebDriver driver = new ChromeDriver();

    public static void main(String[] args) {
        // Step - 1: Identify the broken links
        driver.get(herokuapp);
        driver.manage().window().maximize();
        List<WebElement> elementList = driver.findElements(By.tagName("a"));

        Set<String> setLinks = new HashSet<>();
        for (WebElement element : elementList) {
            String url = element.getDomAttribute("href");
            setLinks.add(url); // avoid duplicate links
        }
        System.out.println(setLinks);


        // Step - 2: Check the status code of the link using java API
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        for (String uri : setLinks) {
            executorService.submit(() -> {
                try {
                    java.net.HttpURLConnection connection = (java.net.HttpURLConnection) new java.net.URL(uri).openConnection();
                    connection.setRequestMethod("HEAD");
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        System.out.println(uri + " is a valid link");
                    } else {
                        System.out.println(uri + " is a broken link with response code: " + responseCode);
                    }
                } catch (Exception e) {
                    System.out.println(uri + " is a broken link with exception: " + e.getMessage());
                }
            });
        }
        driver.close();
        driver.quit();
    }

    /**
     * Ques.: How can we find broken links in Selenium?
     * Me: We will extract all anchor tag elements, store in a list, iterate the list, get the URL using "href" attribute, open the HTTP connection, get the response, if response over 400, the link is broken.
     * Then interviewer updated the question: What if we have more than 2000 links in web page, how can you reduce time?
     * Me: I pondered for a min, and said we can use HashSet and add all the unique URLs into it. By doing so, there will be less links to connect and time would be reduced.
     * Interviewer thanked to my response and moved to next question.
     * Then after the interview, when I had lot of time to think and read. Another thought came to my mind, that is what if I can use multi-threading using ExecutorService. Using it, I can distribute multiple links to a pool of threads,
     * which will be verifed concurrently and lesser time would be used.
     */

}

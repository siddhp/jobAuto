package suite;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;

public class AmazonHiringJob {

    
//
//    public static void main(String[] args) {
//        WebDriver driver = null;
//
//        try {
//            WebDriverManager.chromedriver().setup();
//
//            if (!isChromeDebugPortActive()) {
//                System.err.println("❌ Chrome debugging is NOT running on port 9222.");
//                System.err.println("👉 Please start Chrome with your .bat file first.");
//                return;
//            }
//
//            ChromeOptions options = new ChromeOptions();
//            options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
//            driver = new ChromeDriver(options);
//            System.out.println("✅ Attached to existing Chrome session successfully!");
//
//            driver.switchTo().newWindow(WindowType.TAB);
//            driver.get("https://hiring.amazon.ca/job-opportunities/warehouse-jobs");
//
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
//
//            try {
//                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'I consent')]"))).click();
//            } catch (Exception e) {
//                System.out.println("No consent popup found or already accepted.");
//            }
//
//            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(),'My Account')]"))).click();
//            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(),'Sign In')]"))).click();
//
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[contains(@id,'login')]")))
//                    .sendKeys("Nidhipatel153@outlook.com");
//
//            driver.findElement(By.xpath("//button[@data-test-id='button-continue']")).click();
//
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='pin']")))
//                    .sendKeys("102010");
//
//            driver.findElement(By.xpath("//button[@data-test-id='button-continue']")).click();
//
//            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(),'Send verification code')]"))).click();
//
//            Thread.sleep(5000);
//            System.out.println("✅ Clicked on verification code successfully!");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (driver != null) {
//                // driver.quit(); // Optional
//            }
//        }
//    }
}

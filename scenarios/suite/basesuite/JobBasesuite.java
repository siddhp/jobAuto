package suite.basesuite;

import java.net.HttpURLConnection;
import java.net.URL;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import io.github.bonigarcia.wdm.WebDriverManager;
import utils.BaseSuite;
import utils.Result;

public class JobBasesuite extends BaseSuite {

	private static final String DEBUG_ADDRESS = "127.0.0.1:9222";

	// Check whether Chrome's debugging port is reachable
	private boolean isChromeDebugPortActive() {
		try {
			URL url = new URL("http://" + DEBUG_ADDRESS + "/json");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(3000);
			conn.connect();
			int responseCode = conn.getResponseCode();
			System.out.println("🔍 Debug Port Check: HTTP " + responseCode);
			return responseCode == 200;
		} catch (Exception e) {
			System.err.println("⚠️ Debug Port Connection Failed: " + e.getMessage());
			return false;
		}
	}

	@BeforeSuite
	public void setUp() {
		Result.initExtentReport("Job_Automation");
	}

	@BeforeClass
	public void setUpDriver() {
		attachToChrome();
	}

	// Create or reattach to ChromeDriver
	private void attachToChrome() {
		try {
			WebDriverManager.chromedriver().setup();

			if (!isChromeDebugPortActive()) {
				System.err.println("❌ Chrome debugging is NOT running on port 9222.");
				System.err.println("👉 Please start Chrome with your .bat file first.");
				return;
			}

			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("debuggerAddress", DEBUG_ADDRESS);
			driver = new ChromeDriver(options);
			System.out.println("✅ Attached to existing Chrome session successfully!");
			driver.manage().window().maximize();
		} catch (Exception e) {
			System.err.println("⚠️ Could not attach to Chrome session: " + e.getMessage());
		}
	}

	// Simple health-check used by other classes
	public void ensureDriverActive() {
		try {
			if (driver == null) {
				System.out.println("🔁 Driver was null, re-attaching...");
				attachToChrome();
				return;
			}
			driver.getTitle(); // lightweight health check
		} catch (org.openqa.selenium.NoSuchSessionException e) {
			System.err.println("💀 ChromeDriver session lost. Re-attaching...");
			attachToChrome();
		} catch (Exception e) {
			System.err.println("⚠️ Driver check error: " + e.getMessage());
		}
	}

	@BeforeMethod
	public void before(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		Result.startTest(testName);
	}

	@AfterSuite
	public void tearDown() {
		try {
			// Don't quit Chrome — it’s manually opened
			if (driver != null) {
				driver.close(); // just close WebDriver connection
			}
		} catch (Exception e) {
			System.err.println("⚠️ TearDown issue: " + e.getMessage());
		} finally {
			Result.flushReport();
		}
	}
}

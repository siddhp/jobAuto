package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pages.constants.WaitType;
import suite.basesuite.JobBasesuite;

public class Job extends CommonFunctions implements Job_OR {

	private final JobBasesuite base = new JobBasesuite();

	// refreshUntilCondition: safe long-running loop
	public boolean refreshUntilCondition(By stopLocator, int intervalSeconds) {
		long intervalMillis = intervalSeconds * 1000L;
		long start = System.currentTimeMillis();
		long maxRunMillis = 6L * 60 * 60 * 1000; // run up to 6 hours

		Result.logInfo("Starting refresh loop — every " + intervalSeconds + " s", true);

		while (System.currentTimeMillis() - start < maxRunMillis) {
			try {
				base.ensureDriverActive(); // 🩺 check & re-attach if needed

				if (isElementVisible(stopLocator, 3)) {
					Result.logPass("Stop condition found! Exiting loop.", true);
					return true;
				}

				driver.navigate().refresh();
				Result.logInfo("Page refreshed", false);

				if (isElementVisible(all, 40)) {
					click(all);
				}

				Thread.sleep(intervalMillis);
			} catch (org.openqa.selenium.NoSuchSessionException e) {
				Result.logFail("Lost driver session, reconnecting...", true);
				base.ensureDriverActive();
			} catch (Exception e) {
				Result.logFail("Loop error: " + e.getMessage(), true);
			}
		}

		Result.logInfo("Max loop time reached, exiting.", true);
		return false;
	}

	private boolean isElementVisible(By locator, int timeoutSeconds) {
		try {
			WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
			shortWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			return true;
		} catch (TimeoutException e) {
			return false;
		}
	}

	public void test() {
		if (refreshUntilCondition(jobLocator, 30)) {
			scrollToElement(jobLocator);
			Result.logInfo("Found job: ", true);

			click(jobLocator);
			if (waitForElement(selectSchedule, 60, WaitType.VISIBLE))
				click(selectSchedule);
			String globalHandle = getCurrentWindowHandle();

			if (waitForElement(applyButton, 60, WaitType.VISIBLE))
				clickAndSwitchToNewTab(applyButton, 20);
			if (waitForElement(nextButton, 60, WaitType.VISIBLE)) {
				pause(6);
				click(nextButton);
			}
			if (waitForElement(createApplication, 60, WaitType.CLICKABLE)) {
				pause(6);
				click(createApplication);
			}
			if (waitForElement(noButton, 60, WaitType.VISIBLE))
				click(noButton);
			if (waitForElement(continueBtn, 60, WaitType.VISIBLE))
				click(continueBtn);
			pause(30);
			Result.logInfo("Job Applied.", true);
			closeCurrentWindowAndSwitchTo(globalHandle);
		}
	}
}

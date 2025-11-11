package pages;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import suite.basesuite.BaseSuite;

public class Result extends BaseSuite {
	private static ExtentReports extent;
	private static ExtentTest test;
	private static String reportFolder;
	private static String screenshotFolder;
	private static String reportPath;

	// 🔹 Initialize Extent Report with folder structure
	public static void initExtentReport(String suiteName) {
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		reportFolder = "reports/" + suiteName + "_" + timestamp; // Folder per execution
		screenshotFolder = reportFolder + "/screenshots"; // Screenshots inside execution folder
		reportPath = reportFolder + "/ExtentReport.html"; // HTML report path

		// 🔹 Create folders
		new File(screenshotFolder).mkdirs();
		System.out.println("Report folder created: " + reportFolder);

		// 🔹 Setup Extent Report
		ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);
		System.out.println("Extent Report initialized at: " + reportPath);
	}

// Start a new test in Extent Report
	public static void startTest(String testName) {
		test = extent.createTest(testName);
		System.out.println("Started Extent Report Test: " + testName);
	}

	// ✅ Log a PASS message with an optional screenshot
	public static void logPass(String message, boolean takeScreenshot) {
		System.out.println("PASS: " + message);
		if (takeScreenshot) {
			test.log(Status.PASS, message, attachScreenshot()); // ✅ Attach Screenshot in Log
		} else {
			test.log(Status.PASS, message);
		}
		extent.flush();
	}

	// ✅ Log a FAIL message with an optional screenshot
	public static void logFail(String message, boolean takeScreenshot) {
		System.out.println("FAIL: " + message);
		if (takeScreenshot) {
			test.log(Status.FAIL, message, attachScreenshot());
		} else {
			test.log(Status.FAIL, message);
		}
		extent.flush();
	}

	// ✅ Log an INFO message with an optional screenshot
	public static void logInfo(String message, boolean takeScreenshot) {
		System.out.println("INFO: " + message);
		if (takeScreenshot) {
			test.log(Status.INFO, message, attachScreenshot());
		} else {
			test.log(Status.INFO, message);
		}
		extent.flush();
	}

	private static Media attachScreenshot() {
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date()); // Milliseconds
		String screenshotName = "Screenshot_" + timestamp + ".png";
		String screenshotPath = screenshotFolder + "/" + screenshotName; // Full path
		String relativePath = "screenshots/" + screenshotName; // Relative path for Extent Report

		try {
			File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(srcFile, new File(screenshotPath));

			System.out.println("Screenshot saved: " + screenshotPath);
			return MediaEntityBuilder.createScreenCaptureFromPath(relativePath).build(); // ✅ Return Screenshot Entity
		} catch (IOException e) {
			System.out.println("Failed to capture screenshot: " + e.getMessage());
			return null;
		}
	}

// Capture screenshot on test failure
	public static void captureFailureScreenshot(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE) {
			System.out.println("Test Failed: " + result.getName());
			logFail("Test Failed: " + result.getThrowable(), true);
		}
	}

// Flush and generate the report
	public static void flushReport() {
		if (extent != null) {
			extent.flush();
			System.out.println("Extent Report generated: " + reportPath);
		}
	}
}

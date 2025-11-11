package pages;

import java.time.Duration;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pages.constants.WaitType;
import suite.basesuite.BaseSuite;

public class CommonFunctions extends BaseSuite {

	// Get WebElement with logging
	public WebElement getWebElement(By locator) {
		try {
			WebElement element = driver.findElement(locator);
			Result.logInfo("Found element: " + locator, false);
			return element;
		} catch (NoSuchElementException e) {
			Result.logFail("Element not found: " + locator, true);
			return null;
		}
	}

	// ✅ Click method with logging & screenshot on failure
	public void click(By elementToClick) {
		try {
			WebElement element = getWebElement(elementToClick);
			if (element != null) {
				element.click();
				Result.logPass("Clicked on element: " + elementToClick, false);
			} else {
				Result.logFail("Click failed: Element is null - " + elementToClick, true);
			}
		} catch (Exception e) {
			Result.logFail("Exception in click(): " + e.getMessage(), true);
		}
	}

	// ✅ Set value in field with logging
	public void setValue(By locator, String value) {
		try {
			WebElement element = getWebElement(locator);
			if (element != null) {
				element.clear();
				element.sendKeys(value);
				Result.logPass("Entered value: '" + value + "' in element: " + locator, false);
			} else {
				Result.logFail("SetValue failed: Element is null - " + locator, true);
			}
		} catch (Exception e) {
			Result.logFail("Exception in setValue(): " + e.getMessage(), true);
		}
	}

	// ✅ Pause execution with logging
	public void pause(int timeInSeconds) {
		try {
			Thread.sleep(timeInSeconds * 1000);
			Result.logInfo("Paused for " + timeInSeconds + " seconds", false);
		} catch (InterruptedException e) {
			Result.logFail("Pause interrupted: " + e.getMessage(), false);
			Thread.currentThread().interrupt();
		}
	}

	// ✅ Scroll to element with logging
	public void scrollToElement(By locator) {
		try {
			WebElement element = driver.findElement(locator);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
			Result.logInfo("Scrolled to element: " + locator, false);
		} catch (Exception e) {
			Result.logFail("Error while scrolling: " + e.getMessage(), true);
		}
	}

	// ✅ JavaScript Click with logging
	public void jsClick(By locator) {
		try {
			WebElement element = driver.findElement(locator);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", element);
			Result.logPass("JS Click performed on element: " + locator, false);
		} catch (Exception e) {
			Result.logFail("Error while performing JS Click: " + e.getMessage(), true);
		}
	}

	// ✅ Check if element is displayed with logging
	public boolean isElementDisplayed(By locator, boolean... forFail) {
		try {
			boolean isDisplayed = driver.findElement(locator).isDisplayed();
			Result.logInfo("Element displayed: " + locator + " - " + isDisplayed, false);
			return isDisplayed;
		} catch (NoSuchElementException e) {
			if (forFail.length > 0 && forFail[0]) {
				Result.logFail("Element not found: " + locator, true);
			}
			return false;
		}
	}

	public boolean clickAndVerify(By clickLocator, By verifyLocator) {
		try {
			// Click on the first element using existing click() method
			click(clickLocator);
			Result.logInfo("Clicked on element: " + clickLocator, false);

			// Verify if the second element is displayed using isElementDisplayed() method
			boolean isDisplayed = waitForElement(verifyLocator, 20, WaitType.PRESENT);
			if (isDisplayed) {
				Result.logPass("Verified element is displayed: " + verifyLocator, false);
			} else {
				Result.logFail("Element is NOT displayed: " + verifyLocator, true);
			}
			return isDisplayed;
		} catch (Exception e) {
			Result.logFail("Exception in clickAndVerifyElement(): " + e.getMessage(), true);
			return false;
		}
	}

	public boolean waitForElement(By locator, int timeInSeconds, WaitType waitType) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeInSeconds));

			switch (waitType) {
			case CLICKABLE:
				wait.until(ExpectedConditions.elementToBeClickable(locator));
				Result.logPass("Element is clickable: " + locator, false);
				break;

			case VISIBLE:
				wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
				Result.logPass("Element is visible: " + locator, false);
				break;
			case INVISIBLE:
				wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
				Result.logPass("Element is invisible: " + locator, false);
				break;
			case PRESENT:
				wait.until(ExpectedConditions.presenceOfElementLocated(locator));
				Result.logPass("Element is present in DOM: " + locator, false);
				break;

			default:
				Result.logFail("Invalid WaitType specified!", true);
				return false;
			}
			return true; // Condition met successfully
		} catch (Exception e) {
			Result.logFail("Timeout waiting for element: " + locator + " - Error: " + e.getMessage(), true);
			return false; // Condition failed
		}
	}

	public void setValueUsingJS(By locator, String value) {
		try {
			// Find the element
			WebElement element = driver.findElement(locator);

			// Execute JavaScript to set the value
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].value = '" + value + "'", element);

			// Log PASS if the value is set successfully
			Result.logPass("Successfully set value to the element with locator: " + locator, false);
		} catch (NoSuchElementException e) {
			// Log FAIL if the element is not found
			Result.logFail("Element not found for locator: " + locator, false);
		} catch (Exception e) {
			// Log any other failure
			Result.logFail("Error occurred while setting value using JavaScript: " + e.getMessage(), true);
		}
	}

	public String getValueUsingJS(By locator) {
		try {
			// Find the element
			WebElement element = driver.findElement(locator);

			// Execute JavaScript to get the value of the element
			JavascriptExecutor js = (JavascriptExecutor) driver;
			String value = (String) js.executeScript("return arguments[0].value;", element);

			// Log success with the retrieved value
			Result.logPass("Successfully retrieved value: " + value + " from element with locator: " + locator, false);

			// Return the retrieved value
			return value;
		} catch (Exception e) {
			// Log failure if any exception occurs
			Result.logFail("Error occurred while retrieving value using JavaScript for locator: " + locator
					+ ". Error: " + e.getMessage(), true);
			return null;
		}
	}

	public String getTextWebElement(By locator) {
		try {
			// Find the element
			WebElement element = driver.findElement(locator);

			// Retrieve the text content of the element
			String textContent = element.getText();

			// Log success with the retrieved text
			Result.logPass("Successfully retrieved text: '" + textContent + "' from element with locator: " + locator,
					false);

			// Return the text content
			return textContent;
		} catch (Exception e) {
			// Log failure if any exception occurs
			Result.logFail("Error occurred while retrieving text using getTextWebElement for locator: " + locator
					+ ". Error: " + e.getMessage(), true);
			return null;
		}
	}

	public String getElementAttribute(By locator, String attributeName) {
		try {
			// Find the element using the locator
			WebElement element = driver.findElement(locator);

			// Retrieve the value of the specified attribute
			String attributeValue = element.getDomAttribute(attributeName);

			// Log success with the retrieved attribute value
			Result.logPass("Successfully retrieved attribute '" + attributeName + "' with value: '" + attributeValue
					+ "' from element with locator: " + locator, false);

			// Return the attribute value
			return attributeValue;
		} catch (Exception e) {
			// Log failure if any exception occurs
			Result.logFail("Error occurred while retrieving attribute '" + attributeName
					+ "' for element with locator: " + locator + ". Error: " + e.getMessage(), true);
			return null;
		}
	}

	public void keyPress(By locator, Keys keyAction) {
		try {
			// Find the element using the locator
			WebElement element = driver.findElement(locator);

			// Create an Actions object
			Actions actions = new Actions(driver);

			// Perform the key action on the element
			actions.sendKeys(element, keyAction).perform();

			// Log the success of the action
			Result.logPass(
					"Successfully performed keyboard action: '" + keyAction + "' on element with locator: " + locator,
					false);
		} catch (Exception e) {
			// Log failure if any exception occurs
			Result.logFail("Error occurred while performing keyboard action: '" + keyAction
					+ "' on element with locator: " + locator + ". Error: " + e.getMessage(), true);
		}
	}

	/**
	 * Clicks the given locator and switches to the newly opened tab/window. Returns
	 * the new window handle ID.
	 */
	public String clickAndSwitchToNewTab(By locator, int timeoutSeconds) {
		String newHandle = null;
		try {
			Set<String> oldWindows = driver.getWindowHandles();
			click(locator);
			Result.logInfo("Clicked element: " + locator, true);

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
			wait.until(driver -> driver.getWindowHandles().size() > oldWindows.size());

			Set<String> newWindows = driver.getWindowHandles();
			newWindows.removeAll(oldWindows);
			newHandle = newWindows.iterator().next();

			driver.switchTo().window(newHandle);
			Result.logPass("Switched to new window: " + newHandle, true);

		} catch (Exception e) {
			Result.logFail("Failed to switch to new tab: " + e.getMessage(), true);
		}
		return newHandle;
	}

	/**
	 * Closes the tab/window with the given handle (window ID).
	 */
	public void closeWindowById(String windowHandle) {
		try {
			String currentHandle = driver.getWindowHandle();
			driver.switchTo().window(windowHandle);
			driver.close();
			Result.logInfo("Closed window with handle: " + windowHandle, true);

			// Switch back to a valid remaining window
			Set<String> remainingWindows = driver.getWindowHandles();
			if (remainingWindows.contains(currentHandle)) {
				driver.switchTo().window(currentHandle);
			} else if (!remainingWindows.isEmpty()) {
				driver.switchTo().window(remainingWindows.iterator().next());
			}
		} catch (Exception e) {
			Result.logFail("Failed to close window by handle: " + e.getMessage(), true);
		}
	}

	/**
	 * Returns the current active window handle.
	 */
	public String getCurrentWindowHandle() {
		try {
			String handle = driver.getWindowHandle();
			Result.logInfo("Current window handle: " + handle, true);
			return handle;
		} catch (Exception e) {
			Result.logFail("Could not retrieve window handle: " + e.getMessage(), true);
			return null;
		}
	}

	/**
	 * Closes the current active window/tab and switches to the given window handle.
	 *
	 * @param targetWindowHandle
	 *            The handle (ID) of the window to switch to after
	 *            closing the current one.
	 */
	public void closeCurrentWindowAndSwitchTo(String targetWindowHandle) {
		try {
			// Get the current active window ID
			String currentHandle = driver.getWindowHandle();

			// Close the current window
			driver.close();
			Result.logInfo("Closed current window: " + currentHandle, true);

			// Switch to the target window if it still exists
			Set<String> remainingWindows = driver.getWindowHandles();
			if (remainingWindows.contains(targetWindowHandle)) {
				driver.switchTo().window(targetWindowHandle);
				Result.logPass("Switched to target window: " + targetWindowHandle, true);
			} else {
				// If the target window no longer exists, switch to any remaining one
				if (!remainingWindows.isEmpty()) {
					String fallbackHandle = remainingWindows.iterator().next();
					driver.switchTo().window(fallbackHandle);
					Result.logInfo("Target window not found. Switched to remaining window: " + fallbackHandle, true);
				} else {
					Result.logFail("No remaining windows to switch to after closing current one!", true);
				}
			}

		} catch (Exception e) {
			Result.logFail("Failed to close current window or switch: " + e.getMessage(), true);
		}
	}

}

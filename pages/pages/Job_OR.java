package pages;

import org.openqa.selenium.By;

public interface Job_OR {

	By jobLocator = By.xpath("//div[@data-test-id='JobCard']");
	By selectSchedule = By.xpath("//button[@data-test-id='jobDetailSelectScheduleButton']");
	By applyButton = By.xpath("//button[@data-test-id='ScheduleCardSelectScheduleLink']");
	By nextButton = By.xpath("//button/div[text()='Next']");
	By createApplication = By.xpath("//button/div[text()='Create Application']");
	By noButton = By.xpath("//label[@data-test-id='No']");
	By continueBtn = By.xpath("//button/div[text()='Continue']");
	By all = By.xpath("//div[text()='All']");
}

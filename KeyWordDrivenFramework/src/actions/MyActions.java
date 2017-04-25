package actions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MyActions {
	WebDriver driver;

	public MyActions(WebDriver driver) {
		this.driver = driver;
	}

	public void doActions(String action, WebElement element, String testData) {
		switch (action) {
		case "OpenURL":
			driver.get(testData);
			break;
		case "Enter":
			element.sendKeys(testData);
			break;
		case "Click":
			element.click();
			break;
		case "Select":
			break;
		default:
			break;
		}
	}

}
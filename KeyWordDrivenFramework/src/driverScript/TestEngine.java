package driverScript;

import java.io.IOException;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import actions.MyActions;
import dataEngine.ExcelUtility;
import dataEngine.ReadProperties;

public class TestEngine {
	static String filePath;
	static String filename;
	static String sheetName;
	static String priorityProperty;
	static String allowedPriority;
	static WebDriver driver;

	public static void main(String[] args) throws IOException {
		driver = new FirefoxDriver();
		driver.manage().window().maximize();
		priorityProperty = ReadProperties.getNameValue("PriorityToTest");
		findPriority();

		filePath = ReadProperties.getNameValue("DataEngineFilePath");
		filename = ReadProperties.getNameValue("DataEngineFileName");
		sheetName = ReadProperties.getNameValue("DataEngineSheetName");
		// priorityProperty = ReadProperties.getNameValue("PriorityToTest");

		for (int row = 1; row < 6; row++) {
			String testPriority = ExcelUtility.readCell(filePath, filename, sheetName, row, 6);
			if (allowedPriority.contains(testPriority)) {
				String action = ExcelUtility.readCell(filePath, filename, sheetName, row, 1);
				String attribute = ExcelUtility.readCell(filePath, filename, sheetName, row, 3);
				String attributeValue = ExcelUtility.readCell(filePath, filename, sheetName, row, 4);
				String testData = ExcelUtility.readCell(filePath, filename, sheetName, row, 5);

				WebElement webElement = getWebElement(attribute, attributeValue, driver);
				MyActions objActions = new MyActions(driver);
				objActions.doActions(action, webElement, testData);
			}
		}
	}

	public static void findPriority() {
		priorityProperty = priorityProperty.toUpperCase();
		switch (priorityProperty) {
		case "P1":
			allowedPriority = "P1";
			break;
		case "P2":
			allowedPriority = "P1P2";
			break;
		case "P3":
			allowedPriority = "P1P2P3";
			break;
		case "P4":
			allowedPriority = "P1P2P3P4";
			break;
		default:
			System.out.println("Wrong priority configured for execution");
			break;
		}
	}

	public static WebElement getWebElement(String locatorType, String locatorValue, WebDriver driver) {
		List<WebElement> elements = null;
		locatorType = locatorType.toUpperCase();
		int timeOutInSeconds = Integer.parseInt(ReadProperties.getNameValue("ControlLoadTime"));
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);

		if (locatorType.length() > 0) {
			switch (locatorType) {
			case "ID":
				elements = driver.findElements(By.id(locatorValue));
				break;
			case "NAME":
				elements = driver.findElements(By.name(locatorValue));
				break;
			case "TAGNAME":
				elements = driver.findElements(By.tagName(locatorValue));
				break;
			case "LINK":
				elements = driver.findElements(By.linkText(locatorValue));
				break;
			case "PARTIALLINKTEXT":
				elements = driver.findElements(By.partialLinkText(locatorValue));
				break;
			case "CLASS":
				elements = driver.findElements(By.className(locatorValue));
				break;
			case "CSSSELECTOR":
				elements = driver.findElements(By.cssSelector(locatorValue));
				break;
			case "XPATH":
				elements = driver.findElements(By.xpath(locatorValue));
				break;
			default:
				break;
			}

			if (elements.size() > 0) {
				wait.until(ExpectedConditions.visibilityOfAllElements(elements));
				return elements.get(0);
			}
		}
		return null;
	}
}
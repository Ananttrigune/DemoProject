import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import utl.MaintainLog;

public class BrokenLinks {
	public static int invalidLink;
	String currentLink;
	String temp;
	static WebDriver driver;

	static List<WebElement> allURLs = new ArrayList<>();
	static List<WebElement> allJavaScripts = new ArrayList<>();
	static List<String> linkAlreadyVisited = new ArrayList<String>();

	public static void main(String[] args) throws IOException {
		MaintainLog.logInfo("***Service Started***");
		launchBrowser();
		recursiveMode();
		MaintainLog.logInfo("***Service Completed***");
	}

	public static void recursiveMode() {
		getAllURLs();
		validateURLs();
		for (WebElement link : allURLs) {
			if (link.isDisplayed() && !linkAlreadyVisited.contains(link.getText())) {
				// Add link to list of links already visited
				linkAlreadyVisited.add(link.getText());
				System.out.println(link.getText());
				// Click on the link. This opens a new page
				link.click();
				// Call recursiveLinkTest on the new page
				recursiveMode();
			}
		}
	}

	public static void launchBrowser() {
		// Launch The Chrome Browser
		// System.setProperty("webdriver.chrome.driver", "C:/QA
		// Setup/chromedriver_win32/chromedriver.exe");
		// WebDriver driver = new ChromeDriver();
		MaintainLog.logInfo("Browser Opening");
		System.setProperty("webdriver.ie.driver", "C:/QA Setup/IEDriverServer_x32_3.3/IEDriverServer.exe");
		driver = new InternetExplorerDriver();

		MaintainLog.logInfo("Maximising browser");
		driver.manage().window().maximize();
		MaintainLog.logInfo("Setting page and control load timeouts");
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

		// Enter Url
		String TestURL = "https://myservices.relianceada.com/launchAMSS.do";
		MaintainLog.logInfo("Opening URL: " + TestURL);
		driver.get(TestURL);
	}

	public static void getAllURLs() {
		// Get all the URLs
		MaintainLog.logInfo("Opened page title: " + driver.getTitle());
		allURLs = driver.findElements(By.tagName("a"));
		MaintainLog.logInfo("All tags on a page: " + allURLs.size());
		MaintainLog.logInfo("Excluding Java Scripts");
		// for (int i = 0; i < allURLs.size(); i++) {
		// excludeJavaScripts(allURLs.get(i), i);
		// }
		excludeJavaScripts();
		MaintainLog.logInfo("All URLs on a page: " + allURLs.size());
	}

	public static void validateURLs() {
		boolean isValid = false;
		for (int i = 0; i < allURLs.size(); i++) {
			String myURL = allURLs.get(i).getAttribute("href");
			// MaintainLog.logInfo("Checking for URL: " + myURL);
			isValid = isValidURL(myURL);
			if (isValid == true) {
				MaintainLog.logInfo("Valid Link:   " + myURL);
			} else {
				MaintainLog.logInfo("Invalid Link: " + myURL + "\t\tResponse is: " + getResponseCode(myURL));
			}
		}
	}

	public static boolean isValidURL(String urlString) {
		int responseCode;
		boolean isValid = false;
		try {
			responseCode = getResponseCode(urlString);
			if (responseCode == 200) {
				isValid = true;
			}
		} catch (Exception e) {
			MaintainLog.logError("Exception in isValidURL for URL String-" + urlString + "-Exception Message: " + e);
		}
		return isValid;
	}

	public static int getResponseCode(String urlString) {
		try {
			URL u = new URL(urlString);
			if (urlString.toUpperCase().contains("HTTPS")) {
				HttpsURLConnection h = (HttpsURLConnection) u.openConnection();
				h.setRequestMethod("GET");
				h.connect();
				return h.getResponseCode();
			} else {
				HttpURLConnection h = (HttpURLConnection) u.openConnection();
				h.setRequestMethod("GET");
				h.connect();
				return h.getResponseCode();
			}
		} catch (Exception e) {
			MaintainLog.logError("Exception in getResponseCode for URL String-" + urlString + "-Exception Message: " + e);
			return -1;
		}
	}

	public static void excludeJavaScripts(WebElement URL, int index) {
		try {
			if (URL.getAttribute("href").contains("javascript:")) {
				allJavaScripts.add(URL);
				allURLs.remove(URL);
			}
		} catch (Exception e) {
			MaintainLog.logError("Exception in excludeJavaScripts for Element at index-" + index + " with URL-" + URL + "-Exception Message: " + e);
		}
	}

	public static void excludeJavaScripts() {
		allJavaScripts = driver.findElements(By.xpath("//a[contains(@href,'javascript')]"));
		try {
			for (WebElement webEleJavaScript : allJavaScripts) {
				allURLs.remove(webEleJavaScript);
			}
		} catch (Exception e) {
			MaintainLog.logError("Exception in excludeJavaScripts Exception Message: " + e);
		}
	}

}
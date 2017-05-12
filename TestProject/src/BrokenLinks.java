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
	static String pageTitle;

	static List<WebElement> allURLs = new ArrayList<>();
	static List<WebElement> allJavaScripts = new ArrayList<>();
	static List<String> linkAlreadyVisited = new ArrayList<String>();
	static List<String> pagesVisited = new ArrayList<String>();

	public static void main(String[] args) throws IOException {
		try {
			MaintainLog.logInfo("***Service Started***");
			String TestURL = "http://myservices.relianceada.com/launchAMSS.do";
			launchBrowser(TestURL);
			recursiveMode();
			MaintainLog.logInfo("Closing Browser");
			driver.close();
			MaintainLog.logInfo("***Service Completed***");
		} catch (Exception e) {
			MaintainLog.logError("Exception in main: " + e);
		}
	}

	public static void recursiveMode() {
		try {
			getAllURLs();
			validateURLs();
			for (WebElement link : allURLs) {
				String LinkText;
				if (link.getText() == null) {
					LinkText = link.getText();
				} else {
					LinkText = link.getAttribute("href");
				}
				if (link.isDisplayed() && !linkAlreadyVisited.contains(LinkText)) {
					// Add link to list of links already visited
					linkAlreadyVisited.add(LinkText);
					System.out.println(LinkText);
					MaintainLog.logInfo("Visiting link: " + LinkText);
					// Click on the link. This opens a new page
					link.click();
					recursiveMode();
				}
			}
		} catch (Exception e) {
			MaintainLog.logError("Exception in recursiveMode: " + e);
		}
	}

	public static void launchBrowser(String TestURL) {
		try {
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

			MaintainLog.logInfo("Opening URL: " + TestURL);
			driver.get(TestURL);
		} catch (Exception e) {
			MaintainLog.logError("Exception in launchBrowser: " + e);
		}
	}

	public static void getAllURLs() {
		try {
			// Get all the URLs
			pageTitle = driver.getTitle();
			if (pagesVisited.contains(pageTitle)) {
				MaintainLog.logInfo("Navigated to already visited page: " + pageTitle);
			} else {
				MaintainLog.logInfo("Opened page title: " + pageTitle);
				// List<WebElement> allURLRemove = new ArrayList<WebElement>(allURLs);
				// allURLs.removeAll(allURLRemove);
				allURLs = driver.findElements(By.tagName("a"));
				MaintainLog.logInfo("All tags on a page: " + allURLs.size());
				MaintainLog.logInfo("Excluding Java Scripts");
				// for (int i = 0; i < allURLs.size(); i++) {
				// excludeJavaScripts(allURLs.get(i), i);
				// }
				excludeJavaScripts();
				MaintainLog.logInfo("All URLs on a page: " + allURLs.size());
			}
		} catch (Exception e) {
			MaintainLog.logError("Exception in getAllURLs: " + e);
		}
	}

	public static void validateURLs() {
		boolean isValid = false;
		try {
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
		} catch (Exception e) {
			MaintainLog.logError("Exception in validateURLs: " + e);
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
		try {
			allJavaScripts = driver.findElements(By.xpath("//a[contains(@href,'javascript')]"));
			for (WebElement webEleJavaScript : allJavaScripts) {
				allURLs.remove(webEleJavaScript);
			}
		} catch (Exception e) {
			MaintainLog.logError("Exception in excludeJavaScripts Exception Message: " + e);
		}
	}

}
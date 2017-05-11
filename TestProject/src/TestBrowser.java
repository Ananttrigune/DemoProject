import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import utl.TestHelper;
import utl.MaintainLog;
import utl.ReadProperties;

public class TestBrowser {

	public static String browser;
	public static WebDriver driver;

	public static void main(String[] args) throws IOException {
		try {
			MaintainLog.logInfo("***Test Starts: Suite Name: " + "S1" + " and Test Case: " + "TC1" + "***");
			SetupBrowser();
			driver.quit();
			MaintainLog.logInfo("***Test Completed: Suite Name: " + "S1" + " and Test Case: " + "TC1" + "***");
		} catch (Exception e) {
			MaintainLog.logInfo("Exception in main: " + e);
		}
	}

	public static void SetupBrowser() throws IOException {
		try {
			FirefoxProfile myFirefoxProfile = null;
			browser = ReadProperties.getValueName("Browser").toLowerCase();
			switch (browser) {
			case "firefox": {
				MaintainLog.logInfo("Starting browser Firefox");
				myFirefoxProfile = new FirefoxProfile();
				break;
			}
			case "firefox_incognito": {
				myFirefoxProfile = new FirefoxProfile();
				myFirefoxProfile.setPreference("browser.privatebrowsing.autostart", true);
				MaintainLog.logInfo("Starting browser Firefox");
				break;
			}
			// Check if parameter passed as 'chrome'
			case "chrome": {
				System.setProperty("webdriver.chrome.driver", ReadProperties.getValueName("ChromeDriver"));
				// create chrome instance
				driver = new ChromeDriver();
				String BroswerAuthAutoIT = ReadProperties.getValueName("Chrome_BroswerAuthAutoITFile");
				Runtime.getRuntime().exec(BroswerAuthAutoIT);
				MaintainLog.logInfo("Starting browser Chrome");
				break;
			}
			case "chrome_incognito": {
				System.setProperty("webdriver.chrome.driver", ReadProperties.getValueName("ChromeDriver"));
				// create chrome incognito mode
				DesiredCapabilities capabilities = DesiredCapabilities.chrome();
				ChromeOptions options = new ChromeOptions();
				options.addArguments("incognito");
				capabilities.setCapability(ChromeOptions.CAPABILITY, options);
				driver = new ChromeDriver();
				String BroswerAuthAutoIT = ReadProperties.getValueName("Chrome_BrowserAuthAutoITFile");
				Runtime.getRuntime().exec(BroswerAuthAutoIT);
				MaintainLog.logInfo("Starting browser Chrome");
				break;
			}
			case "ie": {
				System.setProperty("webdriver.ie.driver", ReadProperties.getValueName("IEDriver"));
				// create ie instance
				driver = new InternetExplorerDriver();
				String BroswerAuthAutoIT = ReadProperties.getValueName("Internet_BroswerAuthAutoITFile");
				Runtime.getRuntime().exec(BroswerAuthAutoIT);
				MaintainLog.logInfo("Starting browser Internet Explorer");
				break;
			}
			case "ie_incognito": {
				System.setProperty("webdriver.ie.driver", ReadProperties.getValueName("IEDriver"));
				// create ie incognito instance
				DesiredCapabilities cap = DesiredCapabilities.internetExplorer();
				cap.setCapability(InternetExplorerDriver.FORCE_CREATE_PROCESS, true);
				cap.setCapability(InternetExplorerDriver.IE_SWITCHES, "-private");
				driver = new InternetExplorerDriver();
				String BroswerAuthAutoIT = ReadProperties.getValueName("Internet_BroswerAuthAutoITFile");
				Runtime.getRuntime().exec(BroswerAuthAutoIT);
				MaintainLog.logInfo("Starting browser Internet Explorer");
				break;
			}
			default: {
				// If no browser passed throw exception
				System.out.println("Browser is not correct");
				MaintainLog.logFatal("No Browser Found so running with Headless browser");
				driver = new HtmlUnitDriver(true);
				String BroswerAuthAutoIT = ReadProperties.getValueName("BroswerAuthAutoITFile");
				Runtime.getRuntime().exec(BroswerAuthAutoIT);
				java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
			}
			}
			MaintainLog.logInfo("Setting up the time for loading");

			// Authentication not working - Firefox so Set Preference and proxy
			// for Firefox Browser*/
			if (browser.equals("firefox") || browser.equals("firefox_incognito")) {
				DesiredCapabilities cap = TestHelper.setProxy(myFirefoxProfile);
				System.setProperty("webdriver.gecko.driver", ReadProperties.getValueName("GeckoDriver"));
				driver = new FirefoxDriver(cap);
				String BroswerAuthAutoIT = ReadProperties.getValueName("Firefox_BroswerAuthAutoITFile");
				Runtime.getRuntime().exec(BroswerAuthAutoIT);
			}
			/* Set proxy for Headless Browser */
			if (!browser.equals("firefox") && (!browser.equals("chrome")) && (!browser.equals("ie"))) {

			}
			driver.manage().timeouts().implicitlyWait(Integer.parseInt(ReadProperties.getValueName("ControlLoadTime")),
					TimeUnit.SECONDS);
			driver.manage().timeouts().pageLoadTimeout(Integer.parseInt(ReadProperties.getValueName("PageLoadTime")),
					TimeUnit.SECONDS);
			driver.manage().window().maximize();
			driver.get(ReadProperties.getValueName("URL"));
			MaintainLog.logInfo("Opening the URL for application: " + ReadProperties.getValueName("URL"));
			MaintainLog.logInfo("Web page opened: " + driver.getTitle());
			Thread.sleep(2000);
			driver.close();
			MaintainLog.logInfo("Closed the browser");
		} catch (Exception e) {
			MaintainLog.logFatal("Exception in setting up browser: " + e);
		}
	}

}
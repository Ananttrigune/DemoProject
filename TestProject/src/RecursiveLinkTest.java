import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class RecursiveLinkTest {

	static List<String> linkAlreadyVisited = new ArrayList<String>(); // list to save visited links
	WebDriver driver;

	public RecursiveLinkTest(WebDriver driver) {
		this.driver = driver;
	}

	public void linkTest() {
		// Loop over all the a elements in the page
		for (WebElement link : driver.findElements(By.tagName("a"))) {
			// Check if link is displayed and not previously visited
			if (link.isDisplayed() && !linkAlreadyVisited.contains(link.getText())) {
				// Add link to list of links already visited
				linkAlreadyVisited.add(link.getText());
				System.out.println(link.getText());
				// Click on the link. This opens a new page
				link.click();
				// Call recursiveLinkTest on the new page
				new RecursiveLinkTest(driver).linkTest();
			}
		}
		driver.navigate().back();
	}

	public static void main(String[] args) throws InterruptedException {
		System.setProperty("webdriver.gecko.driver", "C:/QA Setup/geckodriver-v0.16.1-win64/geckodriver.exe");
		WebDriver driver = new FirefoxDriver();
		driver.get("http://newtours.demoaut.com/");
		// Start recursive linkText
		new RecursiveLinkTest(driver).linkTest();
	}
}
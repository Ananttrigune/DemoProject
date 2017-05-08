package monitor;

import java.util.Properties;
import java.util.concurrent.TimeUnit;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import utilities.MaintainLog;
import utilities.ManageProperties;

public class MonitorService {

	public static void main(String[] args) {
		String emailSubject;
		try {
			MaintainLog.logInfo("***Service Started***");
			boolean monitorJenkinsCICDResult = monitorJenkinsCICD();
			MaintainLog.logInfo("Result of Monitoring service: " + monitorJenkinsCICDResult);
			// If Service Failed
			if (!monitorJenkinsCICDResult) {
				emailSubject = ManageProperties.getValueName("Subject") + "-Failed";
				MaintainLog.logError("Service Failed so triggering email with subject: " + emailSubject);
				triggerEmail(emailSubject);
				ManageProperties.setValueName("LastServiceStatus", "Failed");
			}
			// If Service is running
			else {
				if (ManageProperties.getValueLastStatus("LastServiceStatus").equals("Failed")) {
					emailSubject = ManageProperties.getValueName("Subject") + "-Back to Normal";
					MaintainLog.logInfo("Service is back to Normal so triggering mail with subject: " + emailSubject);
					triggerEmail(emailSubject);
					ManageProperties.setValueName("LastServiceStatus", "Success");
				}
			}
			MaintainLog.logInfo("***Service Completed***");
		} catch (Exception e) {
			MaintainLog.logError("Exception in MonitorService.main: " + e);
		}
	}

	public static void triggerEmail(String emailSubject) {
		MaintainLog.logInfo("Starting email with subject: " + emailSubject);

		String emailFrom = ManageProperties.getValueName("From");
		String emailTo = ManageProperties.getValueName("To");
		String emailCc = ManageProperties.getValueName("Cc");
		String emailBcc = ManageProperties.getValueName("Bcc");
		String emailBody = ManageProperties.getValueName("URL") + " : " + ManageProperties.getValueName("Body");

		String host = "cias-server-smtp-uk";
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
		Session session = Session.getDefaultInstance(properties);
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(emailFrom));
			message.addRecipients(Message.RecipientType.TO, emailTo);
			if (!(emailCc == null)) {
				if (!emailCc.isEmpty()) {
					message.addRecipients(Message.RecipientType.CC, emailCc);
				} else {
					emailCc = "Not specified";
				}
			} else {
				emailCc = "";
			}
			if (!(emailBcc == null)) {
				if (!emailBcc.isEmpty()) {
					message.addRecipients(Message.RecipientType.BCC, emailBcc);
				} else {
					emailBcc = "Not Specified";
				}
			} else {
				emailBcc = "";
			}
			message.setSubject(emailSubject);
			message.setText(emailSubject + " : " + emailBody);
			Transport.send(message);
			MaintainLog.logInfo("Email sent successfully with Subject: " + emailSubject + " To: " + emailTo + " Cc: " + emailCc + " Bcc: " + emailBcc);
		} catch (Exception e) {
			MaintainLog.logError("Exception in triggerEmail: " + e);
		}

	}

	public static boolean monitorJenkinsCICD() {
		WebDriver driver = null;
		boolean monitorResult = false;
		try {
			MaintainLog.logInfo("Opening browser Chrome");
			System.setProperty("webdriver.chrome.driver", ManageProperties.getValueName("ChromeDriver"));
			driver = new ChromeDriver();
			MaintainLog.logInfo("Setting Page Load time: " + ManageProperties.getValueName("PageLoadTime") + " Control Load time: "
					+ ManageProperties.getValueName("ControlLoadTime"));
			driver.manage().timeouts().implicitlyWait(Integer.parseInt(ManageProperties.getValueName("ControlLoadTime")), TimeUnit.SECONDS);
			driver.manage().timeouts().pageLoadTimeout(Integer.parseInt(ManageProperties.getValueName("PageLoadTime")), TimeUnit.SECONDS);
			driver.get(ManageProperties.getValueName("URL"));
			MaintainLog.logInfo("Opened URL: " + ManageProperties.getValueName("URL"));

			/*
			 * MaintainLog.logInfo("Logging in with input Credential"); driver.findElement(By.xpath("//input[@id='j_username']")).sendKeys("Releaser");
			 * driver.findElement(By.xpath("//input[@name='j_password']")).sendKeys("Releaser1"); driver.findElement(By.xpath("//button")).click();
			 */
			/*
			 * MaintainLog.logInfo("Logging out and closing browser"); driver.findElement(By.xpath("//a[@href='/logout']")).click();
			 */

			MaintainLog.logInfo("Reading Page Title");
			String pageTitle = driver.getTitle();
			if (pageTitle.equalsIgnoreCase(ManageProperties.getValueName("ExpectedResult"))) {
				MaintainLog.logInfo("Page title is correct as expected: " + pageTitle);
				monitorResult = true;
				System.out.println("Service Ok");
			} else {
				MaintainLog.logError("Page title is incorrect. Actual :" + pageTitle + " Expected: " + ManageProperties.getValueName("ExpectedResult"));
			}
			MaintainLog.logInfo("Closing browser");
			driver.quit();
		} catch (Exception e) {
			MaintainLog.logError("Exception in montiorJenkinsCICD: " + e.getMessage());
			if (e.getMessage().contains("timeout")) {
				driver.quit();
			}
		}
		return monitorResult;
	}

}
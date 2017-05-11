package utl;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
/*import org.testng.ITestContext;
import org.testng.ITestResult;*/
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/*import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.CapabilityType;*/

public class TestHelper {

	public static boolean compareAllData(String[] expectedValues, String[] actualValues) {
		boolean mismatchFound = false;
		for (int i = 0; i < actualValues.length; i++) {
			if (expectedValues[i].equals(actualValues[i])) {
				MaintainLog.logInfo("Values matches for: " + expectedValues[i]);
			} else {
				MaintainLog.logError("Expected and Actual values does not match for-Expected value: "
						+ expectedValues[i] + " Actual value: " + actualValues[i]);
				mismatchFound = true;
			}
		}
		if (mismatchFound == false) {
			return true;
		}
		return false;
	}

	public static boolean compareAllDataIgnoringCase(String[] expectedValues, String[] actualValues) {
		boolean mismatchFound = false;
		for (int i = 0; i < actualValues.length; i++) {
			if (expectedValues[i].equalsIgnoreCase(actualValues[i])) {
				MaintainLog.logInfo("Values matches for: " + expectedValues[i]);
			} else {
				MaintainLog.logError("Expected and Actual values does not match for-Expected value: "
						+ expectedValues[i] + " Actual value: " + actualValues[i]);
				mismatchFound = true;
			}
		}
		if (mismatchFound == false) {
			return true;
		}
		return false;
	}

	public static String formSQLquery(String[] Parameters, String inputSqlQuery) {
		String sql = "";
		int i = 1;
		while (inputSqlQuery.contains("Parameter")) {
			String toReplace = "Parameter" + i + "";
			sql = inputSqlQuery.replace(toReplace, Parameters[i - 1]);
			inputSqlQuery = sql;
			i++;
		}
		return sql;
	}

	public static DesiredCapabilities setProxy(FirefoxProfile myFirefoxProfile) {
		/*
		 * Proxy p = new Proxy(); String proxySet1 = "cias-exion-int"; String
		 * proxySet2 = "cias-exion-uat"; p.setHttpProxy(proxySet1);
		 * p.setHttpProxy(proxySet2); DesiredCapabilities cap = new
		 * DesiredCapabilities(); cap.setCapability(CapabilityType.PROXY, p);
		 */

		DesiredCapabilities dc = DesiredCapabilities.firefox();
		// dc.setCapability("firefox_binary","C:/Program Files (x86)/Mozilla
		// Firefox/firefox.exe");
		myFirefoxProfile.setPreference("network.proxy.no_proxies_on",
				"localhost, 127.0.0.1,ci-intranet,cias-isat,cias-bpmi,civpgbasspfnlb,cias-intranet-int");
		// myFirefoxProfile.setPreference("network.proxy.type", "1");
		myFirefoxProfile.setPreference("network.proxy.type", 1);
		dc.setCapability(FirefoxDriver.PROFILE, myFirefoxProfile);
		return dc;
	}

	/*
	 * public static String getTestName(ITestResult testresult) { String
	 * XMLTestName = null; try { XMLTestName =
	 * testresult.getTestContext().getCurrentXmlTest().getName(); } catch
	 * (Exception e) {
	 * MaintainLog.logFatal("Exception in TestHelper.getTestName: " + e); }
	 * return XMLTestName; }
	 * 
	 * public static String getTestMethodName(ITestResult testresult) { String
	 * TestMethodName = testresult.getName(); return TestMethodName; }
	 */

	/**
	 * This method helps to get the current system date and time in given
	 * format. Below formats are supported dd-MM-yy 31-01-12 dd-MM-yyyy
	 * 31-01-2012 MM-dd-yyyy 01-31-2012 yyyy-MM-dd 2012-01-31 yyyy-MM-dd
	 * HH:mm:ss 2012-01-31 23:59:59 yyyy-MM-dd HH:mm:ss.SSS 2012-01-31
	 * 23:59:59.999 yyyy-MM-dd HH:mm:ss.SSSZ 2012-01-31 23:59:59.999+0100 EEEEE
	 * MMMMM yyyy HH:mm:ss.SSSZ Saturday November 2012 10:45:42.720+0100
	 */
	public static String getCurrentDateTime(String MyFormat) {
		DateFormat df = new SimpleDateFormat(MyFormat);
		Date dateobj = new Date();
		return df.format(dateobj);
	}

	public static void openReport() {
		WebDriver driver = new FirefoxDriver();
		driver.manage().window().maximize();
	}

	/*
	 * public static String getXMLFileName(ITestContext ctx) { String
	 * SuiteFileName = null; try { SuiteFileName =
	 * ctx.getCurrentXmlTest().getSuite().getFileName(); } catch (Exception e) {
	 * MaintainLog.logFatal("Exception in TestHelper.getXMLFileName"); } return
	 * SuiteFileName; }
	 * 
	 * public static String getXMLSuiteNameToReport(ITestContext ctx) { String
	 * SuiteName = null; try { SuiteName =
	 * ctx.getCurrentXmlTest().getSuite().getName(); } catch (Exception e) {
	 * MaintainLog.logFatal("Exception in TestHelper.getXMLFileName"); } return
	 * SuiteName; }
	 * 
	 * public static String getXMLTestNameToReport(ITestContext ctx) { String
	 * TestName = null; try { TestName = ctx.getCurrentXmlTest().getName(); }
	 * catch (Exception e) {
	 * MaintainLog.logFatal("Exception in TestHelper.getXMLFileName"); } return
	 * TestName; }
	 */

	public static void updateConfigXML(String PathandFile, String SuiteName) {
		try {
			String filepath = PathandFile;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			// Get the root element
			Node extentreports = doc.getFirstChild();
			Node configuration = doc.getElementsByTagName("configuration").item(0);
			// loop the configuration child node
			NodeList list = configuration.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				// get the element, and update the value
				if ("reportHeadline".equals(node.getNodeName())) {
					node.setTextContent(SuiteName);
					break;
				}
			}
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		}
	}

}
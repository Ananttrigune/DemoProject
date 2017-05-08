package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ManageProperties {

	public static String getValueName(String Value) {
		File file = new File("./MyConfig.properties");

		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Properties prop = new Properties();

		// Load properties file
		try {
			prop.load(fileInput);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop.getProperty(Value);
	}

	public static String getValueLastStatus(String Value) {
		File file = new File("./LastRun.properties");

		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Properties prop = new Properties();

		// Load properties file
		try {
			prop.load(fileInput);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop.getProperty(Value);
	}

	public static void setValueName(String Key, String Value) {
		File file = new File("./LastRun.properties");
		FileOutputStream fileSaved = null;
		try {
			Properties prop = new Properties();
			fileSaved = new FileOutputStream(file);
			prop.setProperty(Key, Value);
			prop.store(fileSaved, null);
			fileSaved.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

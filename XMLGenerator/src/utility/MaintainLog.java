package utility;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/******************************************
 * This maintains Logs for Info, Error and Fatal
 * 
 * @author Ananta
 */
public class MaintainLog {
	private static Logger Log = Logger.getLogger(MaintainLog.class.getName());

	/************
	 * 
	 * @param LoggerInfo
	 */
	public static void logInfo(String LoggerInfo) {
		DOMConfigurator.configure("log4j.xml");
		Log.info(LoggerInfo);
	}

	public static void logFatal(String LoggerFatalError) {
		DOMConfigurator.configure("log4j.xml");
		Log.fatal(LoggerFatalError);
	}

	public static void logError(String LoggerError) {
		DOMConfigurator.configure("log4j.xml");
		Log.error(LoggerError);
	}
}
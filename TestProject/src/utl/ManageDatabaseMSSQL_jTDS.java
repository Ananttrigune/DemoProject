package utl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class ManageDatabaseMSSQL_jTDS {	
	/***
	 * This method returns String array for given RowNo which has all columns as
	 * per RecordSet
	 * 
	 * @author Ananta
	 * @param Query
	 * @param RowNo
	 * @return String[]
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static String[] getRequiredOutput(String Query, int RowNo) throws ClassNotFoundException, SQLException {
		Connection dbConnnection = dbConnect();
		ResultSet dbResultSet = dbRead(dbConnnection, Query);
		
		//Checking for Number of columns in resultset
		ResultSetMetaData dbResultSetMetaData = (ResultSetMetaData) dbResultSet.getMetaData();
		int noOfCols = dbResultSetMetaData.getColumnCount();
		MaintainLog.logInfo("Returning db Result having number of columns: "+noOfCols+" Row No.=" +RowNo);
		
		//Iterated to required Row Number
		String[] Result = new String[noOfCols];
		int myRow = 1;
		while (dbResultSet.next()) {
			if (myRow == RowNo) {
				break;
			}
			myRow++;
		}

		for (int i = 0; i < noOfCols; i++) {
			Result[i] = dbResultSet.getString(i + 1);
			MaintainLog.logInfo("Column No." + (i + 1) + " " + Result[i]);
		}
		dbClose(dbConnnection);
		return Result;
	}

	/***
	 * This method returns a String from given RowNo and ColNo
	 * 
	 * @author Ananta
	 * @param Query
	 * @param RowNo
	 * @param ColNo
	 * @return String from ResultSet
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static String getRequiredOutput(String Query, int RowNo, int ColNo)
			throws ClassNotFoundException, SQLException {
		String toReturn = "";
		Connection dbConnnection = dbConnect();
		ResultSet dbResultSet = dbRead(dbConnnection, Query);
		ResultSetMetaData dbResultSetMetaData = (ResultSetMetaData) dbResultSet.getMetaData();
		int noOfCols = dbResultSetMetaData.getColumnCount();
		if (ColNo > noOfCols) {
			MaintainLog.logError("Invalid Column Number ... returning null");
		} else {
			int myRow = 1;
			while (dbResultSet.next()) {
				if (myRow == RowNo) {
					break;
				}
				myRow++;
			}
			//dbResultSet.absolute(RowNo);
			for (int i = 0; i < noOfCols; i++) {
				if ((i + 1) == ColNo) {
					MaintainLog.logInfo("Returning db Result: Row= " + RowNo + " Column= " + ColNo + "-->"
							+ dbResultSet.getString(i + 1));
					toReturn = dbResultSet.getString(i + 1);
					break;
				}
			}
		}
		dbClose(dbConnnection);
		return toReturn;		
	}

	public static Connection dbConnect() throws ClassNotFoundException, SQLException {
		/* Connection URL */
		String dbURL = ReadProperties.getValueName("DbUrl");
		/* Database User name, Password */		
		String dbUserName = ReadProperties.getValueName("DbUserName");
		dbUserName=EncryptDecrypt.getDecrypted(dbUserName);
		String dbPassword = ReadProperties.getValueName("DbPassword");
		dbPassword=EncryptDecrypt.getDecrypted(dbPassword);
		/*Load JDBC*/
		Class.forName("net.sourceforge.jtds.jdbc.Driver");
		/* Create Connection to DB */
		Connection dbConnnection = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
		return dbConnnection;
	}

	public static ResultSet dbRead(Connection dbConnnection, String dbQuery)
			throws ClassNotFoundException, SQLException {
		/* Create Statement Object */
		Statement dbStatement = dbConnnection.createStatement();

		/* Execute the SQL Query. Store results in ResultSet */
		ResultSet dbResultSet = dbStatement.executeQuery(dbQuery);
		return dbResultSet;
	}

	public static void dbClose(Connection dbConnnection) throws ClassNotFoundException, SQLException {
		/* Closing DB Connection */
		dbConnnection.close();
	}
	
}
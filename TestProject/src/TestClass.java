import java.io.IOException;

import utl.EncryptDecrypt;
import utl.ReadProperties;

public class TestClass {

	public static void main(String[] args) throws IOException {
		String DBURL = ReadProperties.getValueName("DbUrl");
		String DBU = ReadProperties.getValueName("DbUserName");
		String DBP = ReadProperties.getValueName("DbPassword");

		System.out.println("Database URL: " + DBURL);
		System.out.println("Database UserName: " + DBU);
		System.out.println("Database Password: " + DBP);

		if (ReadProperties.getValueName("Encryption_Enabled").equalsIgnoreCase("true")) {
			String DBU_Decrypted = EncryptDecrypt.decrypt(DBU);
			System.out.println("Database UserName Decrypted: " + DBU_Decrypted);
			String DBP_Decrypted = EncryptDecrypt.decrypt(DBP);
			System.out.println("Database Password Decrypted: " + DBP_Decrypted);
			// String DBU_DecryptedLong = EncryptDecrypt.decryptLong(DBU, 3);
			// System.out.println("Database UserName Decrypted Long: " +
			// DBU_DecryptedLong);
		}
	}

}
package utl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class EncryptDecrypt {

	/*public static String encrypt(String s) {
		String encodedBytes = Base64.getEncoder().encodeToString(s.getBytes());
		System.out.println("Encrypted String: " + encodedBytes);
		return encodedBytes;
	}*/

	public static String decrypt(String encrypted) {
		String decryptedPassword;
		byte[] decryptedPasswordBytes = Base64.getDecoder().decode(encrypted);
		decryptedPassword = new String(decryptedPasswordBytes);
		// System.out.println("Decrypted String: "+decryptedPassword);
		return decryptedPassword;
	}
	
	public static String decryptLong(String encrypted,int n) throws IOException {
		String decryptedPassword;
		String encrypted1=encrypted.substring(0, 4*n);
		String encrypted2=encrypted.substring(4*n+1);
		
		byte[] decryptedPasswordBytes1 = Base64.getDecoder().decode(encrypted1);
		byte[] decryptedPasswordBytes2 = Base64.getDecoder().decode(encrypted2);
		ByteArrayOutputStream decryptedPasswordBytes_C = new ByteArrayOutputStream( );
		decryptedPasswordBytes_C.write( decryptedPasswordBytes1 );
		decryptedPasswordBytes_C.write( decryptedPasswordBytes2 );
		byte decryptedPasswordBytes[] = decryptedPasswordBytes_C.toByteArray( );
		
		decryptedPassword = new String(decryptedPasswordBytes);
		// System.out.println("Decrypted String: "+decryptedPassword);
		return decryptedPassword;
	}

	public static String getDecrypted(String encrypted){
		String decrypted=encrypted;
		if(ReadProperties.getValueName("Encryption_Enabled").toLowerCase().equals("true")){
			decrypted=EncryptDecrypt.decrypt(encrypted);
		}
		return decrypted;
	}

}
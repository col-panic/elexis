package at.medevit.elexisHelpers;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.apache.commons.codec.DecoderException;

import ch.rgw.tools.PasswordEncryptionService;

public class PasswordGenerator {
	
	private static PasswordEncryptionService pes = new PasswordEncryptionService();
	
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, DecoderException{
		if(args.length<1) {
			System.out.println("Usage: java -jar PasswordGenerator.jar password");
			return;
		}
		
		String password = args[0];
		String salt = pes.generateSaltAsHexString();
		String hashed_pw = pes.getEncryptedPasswordAsHexString(password, salt);
		System.out.println("Password:\t"+password);
		System.out.println("Hashed PW:\t"+hashed_pw);
		System.out.println("Salt:\t"+salt);
	}
	
}

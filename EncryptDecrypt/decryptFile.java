/*	Author: Gregory Muchka - 10153582
 * 	Date: 	October 6 2017
 *	decryptFile.java - decrypts using SHA-1 for message authentication and AES\CBC\PKCS5Padding 
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class decryptFile {

	public static void main(String[] args) {
		String filename = args[0];
		byte[] seed = args[1].getBytes();
		FileInputStream in_stream = null;
		File data = new File(filename);
		
		try {
			//reading cipher text into bytes
			byte[] cipher_text = new byte[(int)data.length()];
			in_stream = new FileInputStream(data);
			in_stream.read(cipher_text);
			
			//create key from seed
			SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
			rand.setSeed(seed);
			byte key[] = new byte[16];
			rand.nextBytes(key);
			SecretKey sec_key = new SecretKeySpec(key, "AES");
			
			//decrypt cipher text
			Cipher cipher_aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher_aes.init(Cipher.DECRYPT_MODE, sec_key, new IvParameterSpec(new byte[16]));
			byte[] decrypted_bytes = cipher_aes.doFinal(cipher_text);
			
			//creating message digest
			byte[] clear_text_message = new byte[decrypted_bytes.length - 20];
			byte[] clear_text_digest = new byte[20];
			//removing last 20 bytes from array: this is the message digest to be checked
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			System.arraycopy(decrypted_bytes, 0, clear_text_message, 0, decrypted_bytes.length - 20);
			System.arraycopy(decrypted_bytes, decrypted_bytes.length-20, clear_text_digest, 0, 20);
			byte[] digest = sha.digest(clear_text_message);
			
			if(Arrays.equals(digest, clear_text_digest))
				System.out.println("Message was decrypted successfully!");
			else
				System.out.println("Message was not decrypted successfully!! Messaged was altered in transit!");
		}

		catch(FileNotFoundException e) {
			System.out.println("File Not Found " + e);
		}
		catch(IOException ioe){
			System.out.println("Exception while reading file " + ioe);
			ioe.printStackTrace();
		} 
		catch (NoSuchAlgorithmException nsae) {
			System.out.println("Error while getting message digest: " + nsae);
			nsae.printStackTrace();
		} 
		catch (NoSuchPaddingException nspe) {
			System.out.println("Error while creating cipher: " + nspe);
			nspe.printStackTrace();
		} 
		catch (IllegalBlockSizeException ibe) {
			System.out.println("Error with block size: " + ibe);
			ibe.printStackTrace();
		} 
		catch (BadPaddingException bpe) {
			System.out.println("Error: Key may not be the same that was used for encryption!");
			bpe.printStackTrace();
		} catch (InvalidKeyException ike) {
			System.out.println("Error with key: " + ike);
			ike.printStackTrace();
		} catch (InvalidAlgorithmParameterException iape) {
			System.out.println("Error with parameter: " + iape);
			iape.printStackTrace();
		}
		finally {
			try {
				if(in_stream != null)
					in_stream.close();
			}
			catch(IOException ioe) {
				System.out.println("Error while closing Stream: " + ioe);
			}
		}
	}

}

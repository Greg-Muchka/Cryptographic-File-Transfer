/*	Author: Gregory Muchka - 10153582
 * 	Date: 	October 6 2017
 *	secureFile.java - encrypts using SHA-1 for message authentication and AES\CBC\PKCS5Padding 
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class secureFile {

	public static void main(String[] args) {
		String filename = args[0];
		byte[] seed = args[1].getBytes();
		FileInputStream in_stream = null;
		FileOutputStream out_stream = null;
		File data = new File(filename);
		
		try {
			//reading plain text into bytes
			byte[] clear_text = new byte[(int)data.length()];
			in_stream = new FileInputStream(data);
			in_stream.read(clear_text);
			
			//creating message digest
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			byte[] digest = sha.digest(clear_text);
			//append digest to end of plain text
			byte[] clear_text_digest = new byte[clear_text.length + digest.length];
			System.arraycopy(clear_text, 0, clear_text_digest, 0, clear_text.length);
			System.arraycopy(digest, 0, clear_text_digest, clear_text.length, digest.length);
			
			//create key from seed
			SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
			rand.setSeed(seed);
			byte key[] = new byte[16];
			rand.nextBytes(key);
			SecretKey sec_key = new SecretKeySpec(key, "AES");
			
			//encryption - cipher text creation
			Cipher cipher_aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher_aes.init(Cipher.ENCRYPT_MODE, sec_key, new IvParameterSpec(new byte[16]));
			byte[] cipher_text = cipher_aes.doFinal(clear_text_digest); 
			
			//writing cipher text output file
			out_stream = new FileOutputStream("CipherText");
			out_stream.write(cipher_text);
			out_stream.close();
			System.out.println("Encryption Complete, CipherText file created!");
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
			System.out.println("Error with padding: " + bpe);
			bpe.printStackTrace();
		} catch (InvalidKeyException ike) {
			System.out.println("Error with key: " + ike);
			ike.printStackTrace();
		} 
		catch (InvalidAlgorithmParameterException iape) {
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

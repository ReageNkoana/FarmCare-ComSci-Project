//package signature.
package CryptoPackage;
//list of all library imports. 
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class Cryptography 
{
	//defining a function that hashes for the proof of work. 
	public static String proofOfWorkHash()
	{
		return null;
	}
	//defining a function that hashes a given block.
	public static String hashBlock(String passedBlockData)
	{
		//initialising all the block data.
        String hashString = passedBlockData;
        //creating reference to hash object.
        MessageDigest messageDigest;
        
		try 
		{	//creating an instance of the message digest object with algorithm.
			messageDigest = MessageDigest.getInstance("SHA-256");
			//passing the bytes to the message digest and passing charset.
	        messageDigest.update(hashString.getBytes(StandardCharsets.UTF_8));
	        //getting and storing the bytes of digest.
	        byte[] digestObject = messageDigest.digest();
	        //creating new string buffer object.
	        StringBuffer stringBuffer = new StringBuffer();
	        //looping through all the bytes in the array.
	        for (byte currentByte : digestObject)
	        {	//adding the byte representation to the string buffer.
	        	stringBuffer.append(String.format("%02x", currentByte & 0xff));
	        }
	        //getting string from the buffer.
	        hashString = stringBuffer.toString();
		} 
		catch (NoSuchAlgorithmException e) 
		{	//displaying readable error message.
			System.out.println("Could not create specified hash object.");
			//displaying trace.
			e.printStackTrace();
		}
		//returning hash.
		return hashString;
	}
	
	//defining a function that hashes a password
	 public static String hashPassword(String password) {
	        try {
	            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
	            messageDigest.update(password.getBytes(StandardCharsets.UTF_8));
	            byte[] digestObject = messageDigest.digest();
	            StringBuffer stringBuffer = new StringBuffer();
	            for (byte currentByte : digestObject) {
	                stringBuffer.append(String.format("%02x", currentByte & 0xff));
	            }
	            return stringBuffer.toString();
	        } catch (NoSuchAlgorithmException e) {
	            System.out.println("Could not create specified hash object.");
	            e.printStackTrace();
	            return null;
	        }
	    }
}

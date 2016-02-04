package example;

import java.io.FileInputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 *
 * Example of WebHooks
 *
 * This is to demonstrate how to validate the callback notification from Ping++
 *
 */
public class WebHooksVerifyExample {

	// ping++'s public key
	// copy/pasted from ping++ backend
	private static String filePath = "my-server.pub";

	// in real life, we should get the request body from ping++
	// but in this demo, we just load it from a static file
	private static String eventPath = "charge";

	// in real life, we should get the signature from HTTP Header field "x-pingplusplus-signature"
	// but in this demo, we just read it from a static file
	private static String signPath = "sign";

	public static void main(String[] args) throws Exception {

		boolean result = verifyData(getByteFromFile(eventPath, false), getByteFromFile(signPath, true), getPubKey());
		System.out.println("result: "+result);
	}

	/**
	 * read file content into byte array
	 * @param file file name
	 * @param base64
	 * @return
	 * @throws Exception
	 */
	public static byte[] getByteFromFile(String file, boolean base64) throws Exception {
		FileInputStream in = new FileInputStream(file);
		byte[] fileBytes = new byte[in.available()];
		in.read(fileBytes);
		in.close();
		String pubKey = new String(fileBytes, "UTF-8");
		if (base64) {
			fileBytes = Base64.decodeBase64(pubKey);
		}
		return fileBytes;
	}

	/**
	 * get public key from file
	 * @return
	 * @throws Exception
	 */
	public static PublicKey getPubKey() throws Exception {
		// read key bytes
		FileInputStream in = new FileInputStream(filePath);
		byte[] keyBytes = new byte[in.available()];
		in.read(keyBytes);
		in.close();

		String pubKey = new String(keyBytes, "UTF-8");
		pubKey = pubKey.replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)", "");

		keyBytes = Base64.decodeBase64(pubKey);

		// generate public key
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(spec);
		return publicKey;
	}

	/**
	 * validate the signature
	 *
	 * @param data the request body that is needed to be validated
	 * @param sigBytes the signature from HTTP request header
	 * @param publicKey the public key
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 */
	public static boolean verifyData(byte[] data, byte[] sigBytes, PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initVerify(publicKey);
		signature.update(data);
		return signature.verify(sigBytes);
	}

}

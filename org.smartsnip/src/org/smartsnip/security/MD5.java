package org.smartsnip.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Implements the MD5 hash algorithm
 * 
 */
public class MD5 implements IHash {

	static final MessageDigest messageDigest;
	static final MD5 instance = new MD5();

	/** MessageDigest initialisation */
	static {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			System.err.println("MD5 ist not available");
		} finally {
			messageDigest = digest;
		}
	}

	/**
	 * Converts a given input to MD5
	 * 
	 * @param input
	 *            to be MD5-hashed
	 * @return the md5 hash of the input
	 */
	public static String md5(String input) {
		if (input == null) input = "";

		byte[] array = messageDigest.digest(input.getBytes());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; ++i) {
			sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
		}
		return sb.toString();
	}

	@Override
	public String hash(String input) {
		return md5(input);
	}

	public static IHash getInstance() {
		return instance;
	}
}

package org.smartsnip.security;

/**
 * Simply interface for an arbitrary hashing algorithm.
 * 
 */
public interface IHash {
	/**
	 * Do the hash of the interface
	 * 
	 * @param input
	 *            string to be hashed
	 * @return the resulting hash code
	 */
	public String hash(String input);
}

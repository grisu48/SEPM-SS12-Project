/**
 * File: File.java
 * Date: 11.06.2012
 */
package org.smartsnip.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * File persistable to database
 * 
 * @author Gerhard Aigner
 * @author Felix Niederwanger
 * 
 */
public class File {

	/**
	 * Create a file
	 * 
	 * @param content
	 *            the content of the file
	 * @param name
	 *            the filename
	 */
	File(Byte[] content, String name) {
		super();
		this.content = content;
		this.name = name;
	}

	/**
	 * some binary content
	 */
	private Byte[] content;

	/**
	 * the filename
	 */
	private String name;

	/**
	 * @return the content
	 */
	public Byte[] getContent() {
		return this.content;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	void setContent(Byte[] content) {
		this.content = content;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	void setName(String name) {
		this.name = name;
	}

	/**
	 * Reads the contents of a file and returns a byte array, that contains
	 * exactly the file contents
	 * 
	 * @param filename
	 *            Source file name
	 * @return Byte-array of the file contents
	 * @throws IOException
	 *             Thrown if occuring during file reading
	 */
	public static Byte[] getContents(String filename) throws IOException {
		final long maxSize = 1024 * 1024 * 50;// MAX 50 MB

		InputStream input = new FileInputStream(filename);
		try {
			final int chunk = 1024; // read 1kb chunks
			java.io.File file = new java.io.File(filename);
			if (file.length() > maxSize)
				throw new IOException("Maximum file size exceeded");
			int size = (int) file.length(); // maxSize MUST be in integer
											// interval!
			Byte[] result = new Byte[size];
			int i = 0;
			int len;
			byte[] buffer = new byte[chunk];
			while (i < size) {
				len = input.read(buffer);
				if (len <= 0)
					break; // SAFETY

				int x = 0;
				while (--len >= 0) {
					result[i++] = buffer[x++];
				}
				// len = x; // Read x bytes (for debugging)

			}

			return result;
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				// Ignored, but should NOT overwrite an existing IOException
				// Threrefore in another try block
			}
		}
	}
}

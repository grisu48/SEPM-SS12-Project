/**
 * File: File.java
 * Date: 11.06.2012
 */
package org.smartsnip.core;

/**
 * File persistable to database
 * 
 * @author littlelion
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
	 * @param content the content to set
	 */
	void setContent(Byte[] content) {
		this.content = content;
	}

	/**
	 * @param name the name to set
	 */
	void setName(String name) {
		this.name = name;
	}
}

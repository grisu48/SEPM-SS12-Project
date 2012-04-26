package org.smartsnip.core;


/**
 * Concrete code object for java code
 * 
 */
public class CodeJava extends Code {

	public final static String language = "Java";

	/**
	 * Creates a new java code. The given code must not be null or empty.
	 * 
	 * @param code
	 */
	CodeJava(String code, Snippet snippet) {
		super(code, language, snippet);
	}

	@Override
	public String getFormattedHTML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String formatCode(String code) {
		// TODO Auto-generated method stub
		return code;
	}

}

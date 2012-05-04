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
	 *            Java code of the snippet
	 * @param snippet
	 *            Owner snippet
	 * @param ID
	 *            of the code object. If null, it has not been assigned form the
	 *            persistence yet
	 */
	CodeJava(String code, Snippet snippet, Long id) {
		super(code, language, snippet, id);
	}

	@Override
	public String getFormattedHTML() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	protected String formatCode(String code) {
		// TODO Auto-generated method stub
		return code;
	}

}

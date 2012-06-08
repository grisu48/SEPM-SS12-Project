package org.smartsnip.core;

public abstract class HTMLFormatter {

	/** Instance of the Java formatter */
	private static JavaFormatter javaFormatter = null;

	HTMLFormatter() {
	}

	/**
	 * Formats a java code
	 * 
	 * @param java
	 *            java code to be formatted
	 * @return HTML code of the formatted java code
	 */
	public static String formatCodeJava(CodeJava java) {
		if (java == null)
			return null;
		if (javaFormatter == null)
			javaFormatter = new JavaFormatter();

		return javaFormatter.format(java.code);
	}

	/**
	 * Formats a text code
	 * 
	 * @param text
	 *            text code to be formatted
	 * @return HTML code of the formatted text code
	 */
	public static String formatCodeText(CodeText text) {
		if (text == null)
			return null;

		return "<code>" + text.code + "</code>";
	}

	/**
	 * Formats a null code object
	 * 
	 * @param dummy
	 *            Dummy object
	 * @return HTML code of the formatted dummy code object
	 */
	public static String formatCodeNull(CodeNull dummy) {
		if (dummy == null)
			return null;

		return "<code>" + dummy.code + "</code>";
	}

	/**
	 * Formats an arbitrary code object. If the given code object is not (yet)
	 * supported, the raw code is returned
	 * 
	 * @param code
	 *            to be formatted
	 * @return HTML code
	 */
	public static String formatCode(Code code) {
		if (code == null)
			return null;
		if (code instanceof CodeJava) {
			return (formatCodeJava((CodeJava) code));
		} else if (code instanceof CodeText) {
			return (formatCodeText((CodeText) code));
		} else if (code instanceof CodeNull) {
			return (formatCodeNull((CodeNull) code));

		} else
			return code.code; // UNKNOWN
	}

	/**
	 * This method is used for formatting in a concrete language
	 * 
	 * @param code
	 *            to be fromatted
	 * @return the HTML code of the formatted code
	 */
	public abstract String format(String code);
}

package org.smartsnip.core;

import java.util.ArrayList;
import java.util.List;

import org.smartsnip.shared.Pair;

public abstract class HTMLFormatter {

	/** Instance of the Java formatter */
	private static JavaFormatter javaFormatter = null;

	private static final List<Pair<String, String>> htmlSpecialChars = new ArrayList<Pair<String, String>>(
			3);

	static {
		htmlSpecialChars.add(new Pair<String, String>("&", "&amp;"));
		htmlSpecialChars.add(new Pair<String, String>("<", "&lt;"));
		htmlSpecialChars.add(new Pair<String, String>(">", "&gt;"));
	}

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
		StringBuilder builder = new StringBuilder(
				"<pre class='prettyprint linenums:1'><code class='language-java'>");
		builder.append(replaceHtmlSpecialChars(java.code));
		builder.append("</code></pre>");
		return builder.toString();
	}

	/**
	 * Formats a java code
	 * 
	 * @param java
	 *            java code to be formatted
	 * @return HTML code of the formatted java code
	 */
	// public static String formatCodeJava(CodeJava java) {//FIXME
	// if (java == null)
	// return null;
	// if (javaFormatter == null)
	// javaFormatter = new JavaFormatter();
	//
	// return javaFormatter.format(java.code);
	// }

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
	 * Replace all HTML syntax characters with their HTML special chars.
	 * 
	 * @param code
	 *            string of plain source code
	 * @return source code with HTML special characters
	 */
	private static String replaceHtmlSpecialChars(String code) {
		for (Pair<String, String> special : htmlSpecialChars) {
			// remove HTML tag marker
			code = code.replace(special.first, special.second);
		}
		return code;
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

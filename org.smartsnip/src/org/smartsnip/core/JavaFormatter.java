package org.smartsnip.core;

public class JavaFormatter extends HTMLFormatter {

	private String code = "";
	private int currentPos = 0;

	private StringBuffer result = null;

	/** Java keywords */
	private final String[] keywords = new String[] { "abstract", "continue",
			"for", "new", "switch", "assert", "default", "goto", "package",
			"synchronized", "boolean", "do", "if", "private", "this", "break",
			"double", "implements", "protected", "throw", "byte", "else",
			"import", "public", "throws", "case", "enum", "instanceof",
			"return", "transient", "catch", "extends", "int", "short", "try",
			"char", "final", "interface", "static", "void", "class", "finally",
			"long", "volatile", "const", "float", "native", "super" };

	JavaFormatter() {
	}

	@Override
	public synchronized String format(String code) {
		if (code == null || code.isEmpty())
			return "";

		this.code = code;
		currentPos = 0;
		result = new StringBuffer();
		result.append("<code>");

		// remove html tag marker
		code = code.replace("<", "&lt;");
		code = code.replace(">", "&gt;");
		
		// Format keywords
		for (String keyword : keywords) {
			code = code.replace(keyword, "<b>" + keyword + "</b>");
		}

		// TODO Improve formatter!

		result.append(code);
		result.append("</code>");

		return result.toString();
	}
}

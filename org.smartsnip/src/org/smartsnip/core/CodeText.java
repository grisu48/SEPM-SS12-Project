/**
 * 
 */
package org.smartsnip.core;

/**
 * Text-based code, not highlighted, used as failback if the given language is
 * not found
 * 
 */
public class CodeText extends Code {

	/** Language of this code object */
	public final String language;

	/**
	 * Creates a new java code. The given code must not be null or empty.
	 * 
	 * @param code
	 *            Java code of the snippet
	 * @param language
	 *            Language of this code item
	 * @param snippet
	 *            Owner snippet
	 * @param ID
	 *            of the code object. If null, it has not been assigned form the
	 *            persistence yet
	 */
	CodeText(String code, String language, Snippet snippet, Long id, int version) {
		super(code, language, snippet, id, version);

		this.language = language;
	}

	@Override
	public String getFormattedHTML() {
		StringBuffer result = new StringBuffer();
		String[] lines = code.split("\n");

		for (String line : lines) {
			result.append("<p>");
			result.append(line);
			result.append("</p>");
		}
		return result.toString();
	}

	@Override
	protected String formatCode(String code) {
		// This object does nothing with the code
		return code;
	}

}

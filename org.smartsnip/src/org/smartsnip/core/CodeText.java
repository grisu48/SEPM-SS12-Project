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
	 * @param snippetId
	 *            Owner snippet
	 * @param id
	 *            of the code object. If null, it has not been assigned form the
	 *            persistence yet
	 * @param version the version number
	 */
	CodeText(String code, String language, Long snippetId, Long id, int version) {
		super(code, language, snippetId, id, version);

		this.language = language;
	}

	@Override
	public String getFormattedHTML() {
		return HTMLFormatter.formatCode(this);
	}

	@Override
	protected String formatCode(String code) {
		// This object does nothing with the code
		return code;
	}

}

/**
 * File: CodeGeneric.java
 * Date: 15.06.2012
 */
package org.smartsnip.core;

import java.io.IOException;

/**
 * This class represents the concrete code. It is a generic object built of the
 * properties of the database.
 * 
 * @author Gerhard Aigner
 * 
 */
public class CodeGeneric extends Code {

	/**
	 * Language of this code object
	 */
	public final String language;

	/**
	 * the highlighter to use with this code object
	 */
	private String highlighter = null;

	/**
	 * This constant represents the highlighting property if there is no syntax
	 * highlighter available for this code.
	 */
	public static final String HIGHLIGHTER_NONE = "none";

	/**
	 * Creates a new code. The given code must not be null or empty.
	 * 
	 * @param code
	 *            Code of the snippet
	 * @param language
	 *            Language of this code item
	 * @param snippetId
	 *            Owner snippet
	 * @param id
	 *            of the code object. If null, it has not been assigned form the
	 *            persistence yet
	 * @param version
	 *            the version of the code
	 * @param downloadAbleSource
	 *            the filename of the downloadable source or null if not present
	 */
	public CodeGeneric(String code, String language, Long snippetId, Long id,
			int version, String downloadAbleSource) {
		super(code, language, snippetId, id, version, downloadAbleSource);
		this.language = language;
	}

	/**
	 * @see org.smartsnip.core.Code#getFormattedHTML()
	 */
	@Override
	public String getFormattedHTML() {
		return HTMLFormatter.formatCode(this);
	}

	/**
	 * @see org.smartsnip.core.Code#formatCode(java.lang.String)
	 */
	@Override
	protected String formatCode(String code) {
		// do nothing here
		return code;
	}

	/**
	 * @return The highlighter string to passed as argument on the syntax
	 *         highlighting engine. If no syntax highlighting is available for
	 *         this code object this method returns {@link #HIGHLIGHTER_NONE}.
	 */
	public String getHighlighter() {
		if (this.highlighter == null) {
			try {
				this.highlighter = Persistence.instance
						.getLanguageProperties(this.language).first;
			} catch (IOException ignored) {
				this.highlighter = HIGHLIGHTER_NONE;
			}
		}
		if (this.highlighter == null || this.highlighter.trim().isEmpty()) {
			this.highlighter = HIGHLIGHTER_NONE;
		}
		return this.highlighter;
	}

}

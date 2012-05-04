package org.smartsnip.core;

public abstract class Code {
	/** Concrete code */
	public final String code;
	/** Code language */
	public final String language;
	/** Owner snippet of the code object */
	public final Snippet snippet;
	/** Version of this code object, auto incrementing */
	private int version;

	/** Identifier of this code segment */
	private long id;

	/**
	 * Exception that is used to express, that a given coding language is not
	 * supported by the system.
	 * 
	 */
	static class UnsupportedLanguageException extends IllegalArgumentException {

		/**
		 * Generated serialisation ID
		 */
		private static final long serialVersionUID = 3384542306263829999L;

		UnsupportedLanguageException(String message) {
			super(message);
		}

		UnsupportedLanguageException(Throwable cause) {
			super(cause);
		}

		UnsupportedLanguageException(String message, Throwable cause) {
			super(message, cause);
		}

		UnsupportedLanguageException() {
			super();
		}
	}

	Code(String code, String language, Snippet snippet) {
		if (code.length() == 0)
			throw new IllegalArgumentException("Cannot create snippet with no code");
		if (language.length() == 0)
			throw new IllegalArgumentException("No coding language defined");
		if (snippet == null)
			throw new NullPointerException("Cannot create code segment to no snippet");
		this.code = formatCode(code);
		this.language = language;
		this.snippet = snippet;
	}

	public boolean equals(Code code) {
		if (code == null)
			return false;
		return this.code.equals(code.code);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof Code)
			return equals((Code) obj);
		if (obj instanceof String)
			return this.code.equals(obj);
		return false;
	}

	@Override
	public int hashCode() {
		return code.hashCode();
	}

	@Override
	public String toString() {
		return code;
	}

	/**
	 * @return the formatted and highlighted code in HTML
	 */
	public abstract String getFormattedHTML();

	/**
	 * Formats the code. Must be implemented in the corresponding concrete code
	 * class
	 * 
	 * @param code
	 *            Raw
	 * @return the formatted code
	 */
	protected abstract String formatCode(String code);

	/**
	 * Gets the code of the snippet in plaintext.
	 * 
	 * @return the code in plaintext
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the code language of the code
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @return current version of this code object
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Creates a new code object with the given code and the language.
	 * 
	 * The method automatically detects the concrete class by inspecting the
	 * language. If the given language is not supported, a new
	 * {@link UnsupportedLanguageException} is thrown. The inspection is not
	 * case-sensitive.
	 * 
	 * @param code
	 *            concrete code. Must not be null or empty
	 * @param language
	 *            Coding language. It must be supported by the system, otherwise
	 *            a new {@link UnsupportedLanguageException} is thrown.
	 * @param owner
	 *            The owner snippet of the code
	 * @return The newly generated code object
	 * @throws UnsupportedLanguageException
	 *             Thrown if the given language is not supported
	 * @throws NullPointerException
	 *             Thrown if the code or the language or the snippet is null
	 * @throws IllegalArgumentException
	 *             Thrown if the code or if the language is empty
	 */
	static Code createCode(String code, String language, Snippet owner) throws UnsupportedLanguageException {
		if (code == null || language == null)
			throw new NullPointerException();
		if (code.isEmpty())
			throw new IllegalArgumentException("Cannot create code object with no code");
		if (language.isEmpty())
			throw new IllegalArgumentException("Cannot create code object with no language");
		if (owner == null)
			throw new NullPointerException("Cannot create code segment without a snippet");

		language = language.trim().toLowerCase();

		/* Here the language inspection takes place */
		Code result = null;
		if (language.equals("java")) { // Java object
			result = new CodeJava(code, owner);
		}

		if (result == null)
			throw new UnsupportedLanguageException("Language \"" + language + "\" is not supported");
		return result;
	}

	/**
	 * @return the identifier object of this code object
	 */
	public long getID() {
		return id;
	}

	/**
	 * Sets the identifier of this code object
	 * 
	 * @param id
	 *            to be set to
	 */
	void setID(long id) {
		this.id = id;
	}
}

package org.smartsnip.core;

import java.io.IOException;

import org.smartsnip.persistence.IPersistence;

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
	private long id = 0L;

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

	/**
	 * Constructor of a new code object
	 * 
	 * @param code
	 * @param language
	 * @param snippet
	 * @param id
	 *            of the object. If null, the id has not been assigned from the
	 *            persistence yet
	 * @param version
	 */
	Code(String code, String language, Snippet snippet, Long id, int version) {
		if (code.length() == 0)
			throw new IllegalArgumentException("Cannot create snippet with no code");
		if (language.length() == 0)
			throw new IllegalArgumentException("No coding language defined");
		if (snippet == null)
			throw new NullPointerException("Cannot create code segment to no snippet");
		this.code = formatCode(code);
		this.language = language;
		this.snippet = snippet;
		this.version = version;

		// If the id is null, it has not been assigned from the peristence yet
		if (id != null) {
			this.id = id;
		}
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
	 * @throws IOException
	 *             Thrown, if occuring during database access
	 * @throws NullPointerException
	 *             Thrown if the code or the language or the snippet is null
	 * @throws IllegalArgumentException
	 *             Thrown if the code or if the language is empty
	 */
	// TODO add Version
	public static Code createCode(String code, String language, Snippet owner) throws UnsupportedLanguageException,
			IOException {
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
			result = new CodeJava(code, owner, null, 0);
		}

		if (result == null)
			throw new UnsupportedLanguageException("Language \"" + language + "\" is not supported");

		addToDB(result);
		return result;
	}

	/**
	 * Creates a new code object with the given code and the language.
	 * 
	 * This method should only be used by the database to create object in the
	 * application layer!
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
	 * @param id the identifier
	 * @return The newly generated code object
	 * @throws UnsupportedLanguageException
	 *             Thrown if the given language is not supported
	 * @throws IOException
	 *             Thrown, if occuring during database access
	 * @throws NullPointerException
	 *             Thrown if the code or the language or the snippet is null
	 * @throws IllegalArgumentException
	 *             Thrown if the code or if the language is empty
	 */
	public static Code createCodeDB(String code, String language, Snippet owner, long id, int version)
			throws UnsupportedLanguageException {
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
			result = new CodeJava(code, owner, id, version);
		}

		if (result == null)
			throw new UnsupportedLanguageException("Language \"" + language + "\" is not supported");

		/*
		 * THIS METHOD IS CALLED FROM THE DB, DO NOT WRITE INTO THE DB!!
		 */

		return result;
	}

	/**
	 * @return the identifier object of this code object
	 * @deprecated Because of name convention. Use getHashID() instant.
	 */
	@Deprecated
	public long getID() {
		return id;
	}

	/**
	 * @return the identifier object of this code object
	 */
	public long getHashID() {
		return id;
	}

	/**
	 * Adds
	 */
	protected static synchronized void addToDB(Code code) throws IOException {
		if (code == null)
			return;

		Persistence.instance.writeCode(code, IPersistence.DB_NEW_ONLY);
	}
}

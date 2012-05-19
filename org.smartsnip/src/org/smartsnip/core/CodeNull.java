package org.smartsnip.core;

/**
 * Dummy object, if no code object is currently assigned to a snippet.
 *
 */
public class CodeNull extends Code {

	/** Singleton instance */
	public static CodeNull instance = new CodeNull();
	
	private CodeNull() {
		super("", "", null, 0L, 0);
	}

	@Override
	public String getFormattedHTML() {
		return "<p> </p>";
	}

	@Override
	protected String formatCode(String code) {
		return code;
	}

	public static CodeNull getInstance() {
		return instance;
	}

}

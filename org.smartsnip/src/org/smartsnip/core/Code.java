package org.smartsnip.core;

public abstract class Code {
	/** Concrete code */
	public final String code;
	/** Code language */
	public final String language;

	Code(String code, String language) {
		if (code.length() == 0) throw new IllegalArgumentException("Cannot create snippet with no code");
		if (language.length() == 0) throw new IllegalArgumentException("No coding language defined");
		this.code = code;
		this.language = language;
	}

	public boolean equals(Code code) {
		if (code == null) return false;
		return this.code.equals(code.code);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof Code) return equals((Code) obj);
		if (obj instanceof String) return this.code.equals(obj);
		return false;
	}

	@Override
	public String toString() {
		return code;
	}

	/**
	 * @return the formatted and highlighted code in HTML
	 */
	public abstract String getFormattedHTML();

}

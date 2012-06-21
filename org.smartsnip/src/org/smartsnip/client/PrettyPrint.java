package org.smartsnip.client;

import org.smartsnip.shared.XCode;

import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * This panel implements the PrettyPrint highlighter. The result is a panel,
 * that displays a given code with syntax highlighting.
 * 
 * @author Felix Niederwanger
 * 
 */

public class PrettyPrint extends HTMLPanel {

	/**
	 * Initialise the pretty print highlighter with given code in html format
	 * 
	 * @param codeHTML
	 *            codehtml to be interpreted
	 */
	public PrettyPrint(String codeHTML) {
		super(codeHTML);
	}

	/**
	 * Initialise the pretty print highlighter with given {@link XCode} object
	 * 
	 * @param code
	 *            code object to be highlightes
	 */
	public PrettyPrint(XCode code) {
		this(code.codeHTML);
	}

	/**
	 * @see com.google.gwt.user.client.ui.Widget#onLoad()
	 */
	@Override
	protected void onLoad() {
		Control.prettyPrint();
	};
}

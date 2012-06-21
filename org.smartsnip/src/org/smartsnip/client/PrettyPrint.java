package org.smartsnip.client;

import org.smartsnip.shared.XCode;

import com.google.gwt.user.client.ui.HTMLPanel;

public class PrettyPrint extends HTMLPanel {

	public PrettyPrint(String codeHTML) {
		super(codeHTML);
	}

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

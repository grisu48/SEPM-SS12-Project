package org.smartsnip.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A blank page, that may be edited at runtime
 * 
 * @author Felix Niederwanger
 * 
 */
public class BlankPage extends Composite {
	/** The main panel of the blank page */
	private final Panel mainPanel;

	/**
	 * Initialises a new blank page with a given panel. If null, a new
	 * {@link FlowPanel}is created
	 * 
	 * @param panel
	 *            Panel to be applied
	 */
	public BlankPage(Panel panel) {
		if (panel == null)
			panel = new FlowPanel();
		this.mainPanel = panel;

		initWidget(mainPanel);
	}

	/**
	 * Creates a blank page with a new {@link FlowPanel}
	 */
	public BlankPage() {
		this(new FlowPanel());
	}

	/**
	 * Adds a widget to the panel. If null, nothing will be done
	 * 
	 * @param widget
	 *            to be added.
	 */
	public void add(Widget widget) {
		if (widget == null)
			return;
		mainPanel.add(widget);
	}
}

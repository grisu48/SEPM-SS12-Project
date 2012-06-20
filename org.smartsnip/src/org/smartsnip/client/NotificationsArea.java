package org.smartsnip.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The area for managing notifications
 * 
 * @author Felix Niederwanger
 * 
 */
public class NotificationsArea extends Composite {

	/** Root panel for the notifications */
	private final Panel rootPanel = new VerticalPanel();

	/* Components */

	private final Label lblTitle = new Label("Notifications");

	/* End of components */

	/** Initialises a new {@link NotificationsArea} */
	public NotificationsArea() {
		rootPanel.add(lblTitle);

		initWidget(rootPanel);

		applyStyles();
	}

	private void applyStyles() {

	}

	/** Updates the component */
	public void update() {
		// TODO Auto-generated method stub

	}
}

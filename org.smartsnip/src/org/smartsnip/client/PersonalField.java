package org.smartsnip.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * 
 * @author Paul
 * 
 * 
 *         A composed Widget to display the personal data
 * 
 */
public class PersonalField extends Composite {

	/* Components */
	private final VerticalPanel rootPanel;
	private final Label lblTitle;
	private final Label lblName;
	private final TextBox txtName;
	private final Label lblEMail;
	private final TextBox txtEMail;
	private final Label lblPasswordHeader;
	private final Label lpassword1;
	private final PasswordTextBox tbPW1;
	private final Label lpassword2;
	private final PasswordTextBox tbPW2;
	private final Button bChange;

	/* End of Components */

	/**
	 * 
	 * Initalizes the field with data
	 * 
	 */
	public PersonalField() {

		final Control control = Control.getInstance();

		if (control.isLoggedIn()) {

		}

		rootPanel = new VerticalPanel();
		lblTitle = new Label("Personal Information");
		txtName = new TextBox();
		txtName.setText(control.getUsername());
		txtEMail = new TextBox();
		txtEMail.setText(control.getUserMail());
		tbPW1 = new PasswordTextBox();
		tbPW2 = new PasswordTextBox();
		lblName = new Label("Username");
		lblEMail = new Label("Mail adress");
		lblPasswordHeader = new Label("Password");
		lpassword1 = new Label("Change Password");
		lpassword2 = new Label("Confirm Password");
		bChange = new Button("Change Password");
		bChange.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Control control = Control.getInstance();

				// TODO Set password
			}

		});

		rootPanel.add(lblTitle);
		rootPanel.add(lblName);
		rootPanel.add(txtName);
		rootPanel.add(lblEMail);
		rootPanel.add(txtEMail);
		rootPanel.add(lblPasswordHeader);
		rootPanel.add(lpassword1);
		rootPanel.add(tbPW1);
		rootPanel.add(lpassword2);
		rootPanel.add(tbPW2);
		rootPanel.add(bChange);

		initWidget(rootPanel);
		applyStyles();
	}

	/** Applies all the styles */
	private void applyStyles() {

		lblTitle.setStyleName("personalTitle");
		// Give the overall composite a style name.
		setStyleName("personalField");
	}

	/** Updates the component */
	public void update() {
		// TODO Auto-generated method stub

	}

}

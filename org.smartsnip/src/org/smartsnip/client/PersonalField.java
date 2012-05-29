package org.smartsnip.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PersonalField extends Composite {

	private VerticalPanel pnlField;
	private Label lTitle;
	private Label lname;
	private TextBox name;
	private Label lmail;
	private TextBox mail;
	private Label lHeader;
	private Label lpassword1;
	private PasswordTextBox tbPW1;
	private Label lpassword2;
	private PasswordTextBox tbPW2;
	private Button bChange;

	public PersonalField() {
		
		Control control = Control.getInstance();
		
		pnlField = new VerticalPanel();
		lTitle = new Label("Personal Information");
		name = new TextBox();
		name.setText(control.getUsername());
		mail = new TextBox();
		mail.setText(control.getUserMail());
		tbPW1 = new PasswordTextBox();
		tbPW2 = new PasswordTextBox();
		lname = new Label("Username");
		lmail = new Label("Mail adress");
		lHeader = new Label("Password");
		lpassword1 = new Label("Change Password");
		lpassword2 = new Label("Confirm Password");
		bChange = new Button("Change Password");
		bChange.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Control control = Control.getInstance();
				control.setPassword(tbPW1.getText(), tbPW2.getText());
			}

		});
		
		pnlField.add(lTitle);
		pnlField.add(lname);
		pnlField.add(name);
		pnlField.add(lmail);
		pnlField.add(mail);
		pnlField.add(lHeader);
		pnlField.add(lpassword1);
		pnlField.add(tbPW1);
		pnlField.add(lpassword2);
		pnlField.add(tbPW2);
		pnlField.add(bChange);
		


		initWidget(pnlField);
		// Give the overall composite a style name.
		setStyleName("personalField");

	}

	public void update(boolean worked) {
		if (worked)
		bChange.setText("Password changed");
		else
		bChange.setText("Password change failed");	
	}

}

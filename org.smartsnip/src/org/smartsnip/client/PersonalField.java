package org.smartsnip.client;

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
	private Label lpassword1;
	private PasswordTextBox password1;
	private Label lpassword2;
	private PasswordTextBox password2;

	public PersonalField() {
		
		Control control = Control.getInstance();
		
		pnlField = new VerticalPanel();
		lTitle = new Label("Personal Information");
		name = new TextBox();
		name.setText(control.getUsername());
		mail = new TextBox();
		mail.setText(control.getUserMail());
		password1 = new PasswordTextBox();
		password2 = new PasswordTextBox();
		lname = new Label("Username");
		lmail = new Label("Mail adress");
		lpassword1 = new Label("Change Password");
		lpassword2 = new Label("Confirm Password");
		
		pnlField.add(lTitle);
		pnlField.add(lname);
		pnlField.add(name);
		pnlField.add(lmail);
		pnlField.add(mail);
		pnlField.add(lpassword1);
		pnlField.add(password1);
		pnlField.add(lpassword2);
		pnlField.add(password2);
		


		initWidget(pnlField);
		// Give the overall composite a style name.
		setStyleName("personalField");

	}

}

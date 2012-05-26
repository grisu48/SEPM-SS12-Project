package org.smartsnip.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PersonalArea extends Composite {

	private VerticalPanel vertPanel;
	private ResultArea raOwn;
	private ResultArea raFav;
	private HorizontalPanel horPanel;
	private Label lname;
	private TextBox name;
	private Label lmail;
	private TextBox mail;
	private Label lpassword1;
	private PasswordTextBox password1;
	private Label lpassword2;
	private PasswordTextBox password2;

	public PersonalArea() {

		horPanel = new HorizontalPanel();
		raOwn = new ResultArea();
		raOwn.setWidth("400px");
		raFav = new ResultArea();
		raFav.setWidth("400px");
		vertPanel = new VerticalPanel();
		name = new TextBox();
		mail = new TextBox();
		password1 = new PasswordTextBox();
		password2 = new PasswordTextBox();
		lname = new Label("Name");
		lmail = new Label("E-Mail");
		lpassword1 = new Label("Change Password");
		lpassword2 = new Label("Confirm Password");
		
		vertPanel.add(lname);
		vertPanel.add(name);
		vertPanel.add(lmail);
		vertPanel.add(mail);
		vertPanel.add(lpassword1);
		vertPanel.add(password1);
		vertPanel.add(lpassword2);
		vertPanel.add(password2);
		
	
		horPanel.add(vertPanel);
		horPanel.add(raOwn);
		horPanel.add(raFav);

		initWidget(horPanel);
		// Give the overall composite a style name.
		setStyleName("personalArea");

	}

}

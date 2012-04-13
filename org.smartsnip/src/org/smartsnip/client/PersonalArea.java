package org.smartsnip.client;

import org.smartsnip.core.ISnippet;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PersonalArea extends Composite {
	
	private VerticalPanel vertPanel;
	private TextBox name;
	private TextBox mail;
	private PasswordTextBox password1;
	private PasswordTextBox password2;
	

	public PersonalArea() {
		vertPanel = new VerticalPanel();
		name = new TextBox();
		mail = new TextBox();
		password1 = new PasswordTextBox();
		password2 = new PasswordTextBox();
		vertPanel.add(name);
		vertPanel.add(mail);
		vertPanel.add(password1);
		vertPanel.add(password2);
		
		initWidget(vertPanel);
	    // Give the overall composite a style name.
	    setStyleName("personalArea");
		
	}
	
	

}

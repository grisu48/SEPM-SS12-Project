package org.smartsnip.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Register extends Composite {

	private final PopupPanel parent;

	private VerticalPanel vertPanel;
	private HorizontalPanel hortPanel;
	private Label lname;
	private TextBox name;
	private TextBox mail;
	private Label lmail;
	private Label lpw1;
	private PasswordTextBox pw1;
	private Label lpw2;
	private PasswordTextBox pw2;
	private Button register;
	private Button close;

	public Register(final PopupPanel parent) {
		super();
		this.parent = parent;

		vertPanel = new VerticalPanel();
		hortPanel = new HorizontalPanel();
		lname = new Label("Username");
		name = new TextBox();
		name.setText("myusername");
		mail = new TextBox();
		lmail = new Label("E-Mail");
		mail.setText("user@provider.com");
		lpw1 = new Label("Create Password");
		pw1 = new PasswordTextBox();
		lpw2 = new Label("Confirm Password");
		pw2 = new PasswordTextBox();
		register = new Button("Register");
		close = new Button("Close");
		register.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Control control = Control.getInstance();
				control.register(name.getText(), mail.getText(), lpw2.getText());
			}
		});
		close.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Register.this.parent.hide();
			}
		});

		vertPanel.add(lname);
		vertPanel.add(name);
		vertPanel.add(lmail);
		vertPanel.add(mail);
		vertPanel.add(lpw1);
		vertPanel.add(pw1);
		vertPanel.add(lpw2);
		vertPanel.add(pw2);

		hortPanel.add(register);
		hortPanel.add(close);

		vertPanel.add(hortPanel);

		initWidget(vertPanel);
		// Give the overall composite a style name.
		setStyleName("login");
	}
}

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

public class Login extends Composite {

	private final PopupPanel parent;

	private HorizontalPanel buttonPanel = new HorizontalPanel();
	private VerticalPanel vertPanel = new VerticalPanel();
	private Label lname = new Label("Username");
	private TextBox name = new TextBox();
	private Label lpw = new Label("Password");
	private PasswordTextBox pw = new PasswordTextBox();
	private Button login = new Button("Login");
	private Button close = new Button("Cancel");
	private Label lStatus = new Label("");

	public Login(PopupPanel parent) {

		this.parent = parent;

		vertPanel = new VerticalPanel();
		lname = new Label("Username");
		name = new TextBox();
		lpw = new Label("Password");
		pw = new PasswordTextBox();
		login = new Button("Login");
		login.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				login.setEnabled(false);
				Control control = Control.getInstance();
				control.login(name.getText(), pw.getText(), Login.this);
			}

		});
		close.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Login.this.parent.hide();
			}
		});

		vertPanel.add(lname);
		vertPanel.add(name);
		vertPanel.add(lpw);
		vertPanel.add(pw);
		buttonPanel.add(login);
		buttonPanel.add(close);

		vertPanel.add(buttonPanel);
		vertPanel.add(lStatus);
		initWidget(vertPanel);
		// Give the overall composite a style name.
		setStyleName("login");
	}

	/**
	 * Called from the controller, if the login proceudure fails.
	 * 
	 * @param reason
	 *            Message, why the login failed
	 */
	void loginFailure(String reason) {
		lStatus.setText("Login failure: " + reason);
		login.setEnabled(true);
	}

	/**
	 * Called from the Control, if the login procedure succeeds
	 */
	void loginSuccess() {
		parent.hide();
	}
}

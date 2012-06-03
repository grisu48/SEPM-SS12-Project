package org.smartsnip.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
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
	private PasswordTextBox ptbPassword = new PasswordTextBox();
	private Button login;
	private Button close;
	private Label lStatus = new Label("");
	private Label lTitle;

	public Login(PopupPanel parent) {

		this.parent = parent;

		vertPanel = new VerticalPanel();
		lTitle = new Label("Smartsnip Login");
		lTitle.setStyleName("h3");
		lname = new Label("Username");
		name = new TextBox();
		lpw = new Label("Password");
		ptbPassword = new PasswordTextBox();
		ptbPassword.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (KeyCodes.KEY_ENTER == event.getNativeKeyCode()) {
					login();
				}
			}
		});
		
		login = new Button("Login");
		login.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				login();
			}

		});
		close = new Button("Close");
		close.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Login.this.parent.hide();
			}
		});

		vertPanel.add(lTitle);
		vertPanel.add(lname);
		vertPanel.add(name);
		vertPanel.add(lpw);
		vertPanel.add(ptbPassword);
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
	
	void login() {
		login.setEnabled(false);
		Control control = Control.getInstance();
		control.login(name.getText(), ptbPassword.getText(), Login.this);
	}
}

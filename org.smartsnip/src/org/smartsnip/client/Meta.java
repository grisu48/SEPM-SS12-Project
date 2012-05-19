package org.smartsnip.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class Meta extends Composite {

	private HorizontalPanel metaPanel;
	private Label user;
	private Anchor login;
	private Anchor register;
	private Anchor logout;

	public Meta() {

		metaPanel = new HorizontalPanel();
		user = new Label(Control.getInstance().getUsername());
		login = new Anchor("Login");
		login.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Control control = Control.getInstance();
				control.changeSite('l');
			}

		});
		
		register = new Anchor("Register");
		register.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Control control = Control.getInstance();
				control.changeSite('r');
			}

		});
		
		logout = new Anchor("Logout");
		logout.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Control control = Control.getInstance();
				control.logout();
			}

		});
		
		metaPanel.add(user);
		metaPanel.add(login);
		metaPanel.add(register);
		metaPanel.add(logout);
		
		initWidget(metaPanel);
		// Give the overall composite a style name.
		setStyleName("meta");
		refresh();
	}
	
	public void refresh() {
		user.setText(Control.getInstance().getUsername());
// XXX Use session.isloggedin instant of check if username is guest 
		if (user.getText().equalsIgnoreCase("Guest")) {
			login.setVisible(true);
			register.setVisible(true);
			user.setVisible(false);
			logout.setVisible(false);
		}
		else {
			user.setVisible(true);
			login.setVisible(false);
			register.setVisible(false);
			logout.setVisible(true);
		}
		
	}

}

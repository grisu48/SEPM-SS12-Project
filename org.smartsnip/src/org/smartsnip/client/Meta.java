package org.smartsnip.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Meta extends Composite {

	private final VerticalPanel metaVertical;
	private final HorizontalPanel metaPanel;
	private final HorizontalPanel metaControl;
	private final Anchor user;
	private final Anchor login;
	private final Anchor register;
	private final Anchor logout;

	private final Button btCreateSnippet;

	public Meta() {

		metaVertical = new VerticalPanel();

		metaControl = new HorizontalPanel();
		metaPanel = new HorizontalPanel();
		user = new Anchor(Control.getInstance().getUsername());
		user.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Control control = Control.getInstance();
				control.changeSite('p');
			}

		});

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

		btCreateSnippet = new Button("Create snippet");
		btCreateSnippet.setVisible(false);
		btCreateSnippet.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Control.getInstance().changeSite('n');
			}
		});

		metaPanel.add(user);
		metaPanel.add(login);
		metaPanel.add(register);
		metaPanel.add(logout);

		metaControl.add(btCreateSnippet);

		metaVertical.add(metaPanel);
		metaVertical.add(metaControl);

		initWidget(metaVertical);

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
			btCreateSnippet.setVisible(false);
		} else {
			user.setVisible(true);
			login.setVisible(false);
			register.setVisible(false);
			logout.setVisible(true);
			btCreateSnippet.setVisible(true);
		}

	}

}

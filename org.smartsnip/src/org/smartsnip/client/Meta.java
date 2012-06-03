package org.smartsnip.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Meta extends Composite {

	private final VerticalPanel metaVertical;
	private final HorizontalPanel metaPanel;
	private final HorizontalPanel metaControl;
	private final Anchor user;
	private final Anchor login;
	private final Anchor register;
	private final Anchor logout;
	private Image icon;

	

	public Meta() {

		metaVertical = new VerticalPanel();

		metaControl = new HorizontalPanel();
		metaPanel = new HorizontalPanel();
		

		icon = new Image("/images/user1.png");
		icon.setSize("35px", "35px");
		
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

		


		metaPanel.add(icon);
		metaPanel.add(user);
		metaPanel.add(login);
		metaPanel.add(register);
		metaPanel.add(logout);


		metaVertical.add(metaPanel);
		metaVertical.add(metaControl);

		initWidget(metaVertical);

		// Give the overall composite a style name.
		setStyleName("meta");
		update();
	}

	public void update() {
		
		user.setText(Control.getInstance().getUsername());
		Control control = Control.getInstance();
		
		if (control.isLoggedIn()) {
			icon.setVisible(true);
			user.setVisible(true);
			login.setVisible(false);
			register.setVisible(false);
			logout.setVisible(true);
		} else {
			icon.setVisible(false);
			login.setVisible(true);
			register.setVisible(true);
			user.setVisible(false);
			logout.setVisible(false);
		}
		

	}

}

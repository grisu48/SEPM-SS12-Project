package org.smartsnip.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

import com.google.gwt.user.client.ui.VerticalPanel;

public class Meta extends Composite {

	private final VerticalPanel pnlUser;
	private final HorizontalPanel metaPanel;
	private final Anchor user;
	private final Anchor login;
	private final Anchor register;
	private final Anchor logout;
	private Image icon;
	private Control control;
	

	public Meta() {

		control = Control.getInstance();
		pnlUser = new VerticalPanel();
		metaPanel = new HorizontalPanel();

		

		icon = new Image("/images/user1.png");
		icon.setSize("35px", "35px");
		
		user = new Anchor(Control.getInstance().getUsername());
		user.setStyleName("user");
		user.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				control.changeSite('p');
			}

		});

		login = new Anchor(" > Login");
		login.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				control.changeSite('l');
			}

		});

		register = new Anchor(" > Register");
		register.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				control.changeSite('r');
			}

		});

		logout = new Anchor(" > Logout");
		logout.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				control.logout();
			}

		});

		

		metaPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		metaPanel.add(icon);
		metaPanel.add(user);
		metaPanel.add(login);
		metaPanel.add(register);
		metaPanel.add(logout);




		initWidget(metaPanel);

		// Give the overall composite a style name.
		setStyleName("meta");
		update();
	}

	public void update() {
		
		user.setText(control.getUsername() + " | " + control.getUserMail());
		
		if (control.isLoggedIn()) {
			user.setVisible(true);
			icon.setVisible(true);
			login.setVisible(false);
			register.setVisible(false);
			logout.setVisible(true);
		} else {
			user.setVisible(false);
			icon.setVisible(false);
			login.setVisible(true);
			register.setVisible(true);
			pnlUser.setVisible(false);
			logout.setVisible(false);
		}
		

	}

}

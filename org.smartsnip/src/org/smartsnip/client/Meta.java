package org.smartsnip.client;

import org.smartsnip.shared.IModerator;
import org.smartsnip.shared.IUser;
import org.smartsnip.shared.XUser;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * 
 * 
 * @author Paul
 * @author Felix Niederwanger
 * 
 * 
 *         A composed Widget to display the meta navigation menu
 * 
 */
public class Meta extends Composite {

	/* Controls */

	private final VerticalPanel pnlUser;
	private final HorizontalPanel metaPanel;
	private final Anchor user;
	private final Anchor login;
	private final Anchor register;
	private final Anchor logout;
	private final Anchor mod;
	private final Image icon;
	private final Control control;

	private final NotificationIcon notificationIcon;

	/* End of controls */

	/** To prevent too much updates we store the current logged in flag */
	private final boolean loggedInFlag = false;

	/**
	 * Initialises the menu
	 */
	public Meta() {

		control = Control.getInstance();
		pnlUser = new VerticalPanel();
		metaPanel = new HorizontalPanel();
		mod = new Anchor("> Moderator");
		login = new Anchor(" > Login");
		user = new Anchor("Guest");
		register = new Anchor(" > Register");
		logout = new Anchor(" > Logout");

		notificationIcon = new NotificationIcon();

		icon = new Image(Control.baseURL + "/images/guest.png");
		icon.setSize("35px", "35px");

		user.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				control.changeSite('p');
			}

		});

		mod.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				control.changeSite('m');
			}

		});

		login.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				control.changeSite('l');
			}

		});

		register.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				control.changeSite('r');
			}

		});

		logout.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				control.logout();
			}

		});

		// Assume we are not logged in
		metaPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		metaPanel.add(icon);
		metaPanel.add(user);
		metaPanel.add(login);
		metaPanel.add(register);

		// Default visibility
		changeToolbarVisibility(false);

		initWidget(metaPanel);

		applyStyles();

		// Initial update
		update();
	}

	private void applyStyles() {
		mod.setStyleName("mod");
		user.setStyleName("user");

		// Give the overall composite a style name.
		setStyleName("meta");
	}

	/** Update */
	public void update() {
		update(Control.getInstance().isLoggedIn());
	}

	/**
	 * Second step for the update cycle. Called after the callback for isLogged
	 * in has been received
	 * 
	 * @param isLoggedin
	 *            indicating if the current session is logged in or not
	 */
	void update(final boolean isLoggedin) {
		changeToolbarVisibility(isLoggedin);

		if (isLoggedin) {
			IUser.Util.getInstance().getMe(new AsyncCallback<XUser>() {

				@Override
				public void onSuccess(XUser result) {
					if (result == null)
						user.setText("Guest");
					else
						user.setText(result.realname + " | " + result.email);
				}

				@Override
				public void onFailure(Throwable caught) {
					user.setText("<< Error fetching Userdata >>");
				}
			});
			IModerator.Util.getInstance().isModerator(new AsyncCallback<Boolean>() {

				@Override
				public void onSuccess(Boolean result) {
					if (result == null)
						return;
					boolean isModerator = result;
					mod.setVisible(isModerator);

					if (isModerator)
						icon.setUrl(Control.baseURL + "/images/moderator.png");
					else
						icon.setUrl(Control.baseURL + "/images/user.png");
				}

				@Override
				public void onFailure(Throwable caught) {
				}
			});
		} else {
			// If not logged in
			user.setText("Guest");
			icon.setUrl(Control.baseURL + "/images/guest.png");
		}
		notificationIcon.update();
	}

	/**
	 * Changes the visibility state of the toolbar elements based on the login
	 * status
	 * 
	 * @param loggedIn
	 *            login status
	 */
	private void changeToolbarVisibility(final boolean loggedIn) {
		// With the visibility this frak is not working
		// FRAKKING GWT!!!

		// XXX Ugly hack to get rid of the visibility problem
		metaPanel.clear();

		metaPanel.add(icon);
		metaPanel.add(user);

		if (!loggedIn) {
			metaPanel.add(login);
			metaPanel.add(register);
		} else {
			metaPanel.add(mod);
			metaPanel.add(notificationIcon);
			metaPanel.add(logout);
		}

		// login.setVisible(!loggedIn);
		// register.setVisible(!loggedIn);
		// logout.setVisible(loggedIn);
		// notificationIcon.setVisible(loggedIn);
		// if (!loggedIn)
		// mod.setVisible(false);
	}
}

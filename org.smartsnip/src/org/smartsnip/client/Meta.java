package org.smartsnip.client;

import org.smartsnip.shared.IModerator;
import org.smartsnip.shared.ISession;
import org.smartsnip.shared.IUser;
import org.smartsnip.shared.XUser;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
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

	/**
	 * This component is based on a button with text and a image
	 * 
	 * It acts as the button for the user
	 * */
	private class UserIcon extends Image {
		/* Resources */
		private final String GUEST_SRC = Control.baseURL + "/images/guest.png";
		private final String USER_SRC = Control.baseURL + "/images/user.png";
		private final String NOTIF_SRC = Control.baseURL + "/images/user_msg.png";
		/* End of resources */

		/** Notification refresh delay, if in user mode. In milliseconds */
		private static final int NOTIFICATION_REFRESH_DELAY = 5000;

		/** Notification refresh timer */
		private final Timer refresher = new Timer() {

			@Override
			public void run() {
				refresh();
				refresher.schedule(NOTIFICATION_REFRESH_DELAY);
			}
		};

		/** Current image URL */
		private String currentImage = "";

		/** If set as user or not */
		private boolean isUser = false;
		/** Current number of new notifications */
		private int newNotifications = 0;

		private UserIcon() {
			setGuestImage();

			refresher.schedule(NOTIFICATION_REFRESH_DELAY);
			this.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					UserIcon.this.onClick();
				}
			});
		}

		/** Event on click */
		private void onClick() {
			if (isUser)
				Control.myGUI.showNotificationPage();
		}

		/**
		 * Sets the current image to the USER image
		 */
		private void setUserImage() {
			this.setUrl(USER_SRC);
		}

		/**
		 * Sets the current image to the USER with new NOTIFICATION image
		 */
		private void setNewNotificationImage() {
			this.setUrl(NOTIF_SRC);
		}

		/**
		 * Sets the current image to the GUEST image
		 */
		private void setGuestImage() {
			this.setUrl(GUEST_SRC);
		}

		@Override
		public void setUrl(String url) {
			if (url == null)
				return;
			if (currentImage.equals(url))
				return;
			this.currentImage = url;
			super.setUrl(url);
		}

		/** Set guest mode */
		private void setGuest() {
			isUser = false;
			newNotifications = 0;
			setGuestImage();

		}

		/** Set user mode */
		private void setUser() {
			isUser = true;
			setUserImage();
		}

		/**
		 * Does a refresh cycle. This clal should be called from the refresh
		 * timer {@link #refresher}
		 */
		private void refresh() {
			if (!isUser)
				return;

			ISession.Util.getInstance().getNotificationCount(true, new AsyncCallback<Long>() {

				@Override
				public void onSuccess(Long result) {
					if (result == null) {
						onFailure(new IllegalArgumentException("Null returned"));
						return;
					}

					long temp = result;
					newNotifications = (int) temp;
					refreshComponent();
				}

				@Override
				public void onFailure(Throwable caught) {
					// Ignore
					newNotifications = 0;
					refreshComponent();
				}
			});
		}

		/**
		 * Refreshes the component based on it's parameters. Should be called
		 * from {@link #refresh()}
		 */
		private void refreshComponent() {
			if (!isUser)
				return;

			boolean newArrived = (newNotifications > 0);
			if (newArrived) {
				setNewNotificationImage();
				setTitle(newNotifications + " new notifications");
			} else {
				setUser();
				setTitle("");
			}
		}
	}

	/* Controls */

	private final VerticalPanel pnlUser;
	private final HorizontalPanel metaPanel;
	private final Anchor user;
	private final Anchor login;
	private final Anchor register;
	private final Anchor logout;
	private final Anchor mod;
	private final UserIcon userIcon;
	private final Control control;

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

		userIcon = new UserIcon();
		userIcon.setSize("35px", "35px");

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
		metaPanel.add(userIcon);
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
			userIcon.setUser();
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
				}

				@Override
				public void onFailure(Throwable caught) {
				}
			});
		} else {
			// If not logged in
			user.setText("Guest");
			userIcon.setGuest();
		}
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

		metaPanel.add(userIcon);
		metaPanel.add(user);

		if (!loggedIn) {
			metaPanel.add(login);
			metaPanel.add(register);
		} else {
			metaPanel.add(mod);
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

package org.smartsnip.client;

import org.smartsnip.shared.IUser;

import com.google.gwt.user.client.ui.PopupPanel;

/**
 * Static object to handle GUI interactions with the {@link IUser} interface
 * 
 * @author Felix Niederwanger
 * 
 */
public class User {
	/**
	 * No outer instances allowed
	 */
	private User() {

	}

	/**
	 * Shows the login popup This ist just a redirect to
	 * {@link Login#showLoginPopup()}
	 */
	public static void showLoginPopup() {
		Login.showLoginPopup();
	}

	/**
	 * Shows a {@link PopupPanel} to change the password of the currently logged
	 * in user
	 */
	public static void showChangePasswordPopup() {

	}

	/**
	 * Shows a {@link PopupPanel} to change the password of an arbitary user,
	 * identified by its username
	 * 
	 * @param username
	 *            of the user
	 */
	public static void showChangePasswordPopup(String username) {

	}

	/**
	 * Shows a {@link PopupPanel} for changing the user details for the
	 * currently logged in user
	 */
	public static void showChangeUserData() {

	}
}

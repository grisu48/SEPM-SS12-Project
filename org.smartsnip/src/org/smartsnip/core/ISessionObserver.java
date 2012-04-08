package org.smartsnip.core;

/**
 * This class is the interface for the main observable pattern of the session.
 * Each observer must implement this.
 * 
 * This interface is used in the GUI
 * 
 */
public interface ISessionObserver {

	/**
	 * If the session has been logged in with a given username
	 * 
	 * @param username
	 *            of the user that has been logged on
	 */
	public void login(String username);

	/**
	 * If the session has been logged out
	 */
	public void logout();

	/**
	 * The current session received a new notifications. Mostly used for user
	 * notifications
	 * 
	 * @param notification
	 *            that has been received
	 */
	public void notification(Notification notification);
}

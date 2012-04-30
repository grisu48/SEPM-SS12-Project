package org.smartsnip.shared;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * This interface handles the main interactions between the client and the
 * server.
 * 
 * 
 */
@RemoteServiceRelativePath("session")
public interface ISession extends RemoteService, IsSerializable {

	/** This class provides easy access to the proxy object */
	public static class Util {
		private static ISessionAsync instance = null;

		/** Get the proxy object instance */
		public static ISessionAsync getInstance() {
			if (instance == null) {
				instance = GWT.create(ISession.class);
			}
			return instance;
		}
	}

	/** Identifier for the session id cookie used by server and client */
	public static final String cookie_Session_ID = "smartsnip_SID";

	/** Gets the username of the current session, or null if a guest session */
	public String getUsername();

	/**
	 * @return the number of active session
	 */
	public int getActiveSessionCount();

	/**
	 * @return the number of active session
	 */
	public int getGuestSessionCount();

	/**
	 * @return the number of active guest session
	 */
	public int getUserCount();

	/**
	 * @return the number of categories in the system
	 */
	public int getCategoryCount();

	/**
	 * @return the number of snippets in the system
	 */
	public int getSnippetCount();

	/**
	 * Tries to login this session with the given username and password.
	 * 
	 * If the login fails, a new {@link NoAccessException} is thrown.
	 * 
	 * If the session is already logged in, the method returns false, and
	 * nothing else is done
	 * 
	 * @param username
	 *            to be logged in with
	 * @param password
	 *            to be logged in with
	 * @return true if successful. Returns false, if the current session is
	 *         already logged in
	 * @throws NoAccessException
	 *             Thrown if the login process fails
	 */
	public boolean login(String username, String password) throws NoAccessException;

	/**
	 * Logs the session out
	 */
	public void logout();

	/**
	 * True if the current session is logged in, false if a guest session
	 * 
	 * @return
	 */
	public boolean isLoggedIn();
}

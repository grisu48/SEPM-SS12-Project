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
			if (instance == null) instance = GWT.create(ISession.class);
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
	 * Logs the sesion in with the gien username and password. If the given
	 * username and password is null, the method has no effect
	 * 
	 * If the login fails because of an access denial, a new
	 * {@link NoAccessException} is thrown.
	 * 
	 * @param username
	 *            To be logged in
	 * @param password
	 *            To be logged in
	 * @throws NoAccessException
	 *             Thrown if the login process fails
	 * @return true if the login was done successfully, false if the login
	 *         failed because of wrong arguments.
	 * 
	 */
	public boolean login(String username, String password) throws NoAccessException;

	/**
	 * Logs the session out. If the session is not logged in, the method has no
	 * effect
	 */
	public void logout();

	/**
	 * Gets the access interface to a category.
	 * 
	 * If no such category exists, the result of the method is null. If there
	 * was an access denial during the call, a new {@link NoAccessException} is
	 * thrown
	 * 
	 * @param name
	 *            The name of the category to get
	 * @return an interface providing access to the given category, or null, if
	 *         no such category exists.
	 * @throws NoAccessException
	 *             Thrown, if the access to the category was denied
	 */
	public IsSerializable getCategory(String name) throws NoAccessException;

	/**
	 * Gets the access interface to a snippet, with the hash code of the
	 * snippet.
	 * 
	 * If the given hash code is not found, null is returned.
	 * 
	 * @param hash
	 *            Hash code of the snippet
	 * @return an interface providing access to the snippet with the hash code,
	 *         or null, if no such snippet exists
	 * @throws NoAccessException
	 *             Thrown, if the access to the snippet was denied
	 */
	public IsSerializable getSnippet(int hash) throws NoAccessException;

	/**
	 * Gets an access interface to the user module, given with the username.
	 * 
	 * If no such user was found, the result is null. If the access is denied, a
	 * new {@link NoAccessException} is thrown.
	 * 
	 * @param username
	 *            Username of the user to find
	 * @return an interface providing access to the given user, or null if no
	 *         such user exists
	 * @throws NoAccessException
	 *             Thrown if the access to the given user was denied
	 */
	public IsSerializable getUser(String username) throws NoAccessException;

	/**
	 * True if the current session is logged in, false if a guest session
	 * 
	 * @return
	 */
	public boolean isLoggedIn();
}

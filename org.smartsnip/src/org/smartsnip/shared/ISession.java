package org.smartsnip.shared;

import java.util.List;

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
	public boolean login(String username, String password)
			throws NoAccessException;

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

	/**
	 * Gets a subset of the total search results of doing a snippet search on
	 * the server. Maximum search results that are returned are 100.
	 * 
	 * If one of the parameters is null, null is returned
	 * 
	 * @param searchString
	 *            String to be searched for
	 * @param tags
	 *            Search tags
	 * @param category
	 *            Search categories
	 * @param start
	 *            Start index of the subset of the total search set
	 * @param count
	 *            Count of maximal objects that are returned
	 * @param id
	 *            Search id, that is given by the client and returned by the
	 *            server in the XSearch result to identify multiple search
	 *            requests
	 * @return a List with max count snippets that match the search string, or
	 *         null, if something went wrong
	 */
	public XSearch doSearch(String searchString, List<String> tags,
			List<String> categories, XSearch.SearchSorting sorting, int start,
			int count, int id);

	/**
	 * Gets a list of favourites for the current session. If the session is
	 * logged in, the result is the list of the user favourites. If a guest
	 * session it returns a list of the session favourite snippets.
	 * 
	 * This method should be used instant of {@link IUser#getFavorites()}
	 * 
	 * @return List of favourite snippets for the current session or of the
	 *         user, if logged in
	 */
	public List<XSnippet> getFavorites();

	/**
	 * @return Gets the server status object
	 */
	public XServerStatus getServerStatus();

	/**
	 * Registers a new user with the given arguments. If one of the arguments is
	 * null or empty, the method fails. If the given username is already in the
	 * database the methods fails.
	 * 
	 * Returns true if the user has been successfully registered, or false if
	 * the method fails (user already registered or invalid argument(
	 * 
	 * @param username
	 *            Username of the new user. Must not be null or empty
	 * @param password
	 *            Password of the new user. Must not be null or empty
	 * @return true if success, false if something went wrong
	 * @throws NoAccessException
	 *             Thrown if the server denies the access
	 */
	public boolean registerNewUser(String username, String password,
			String email) throws NoAccessException;

	/**
	 * @return Gets the cookie value for this session
	 */
	public String getSessionCookie();

	public XUser getUser(String username);

}

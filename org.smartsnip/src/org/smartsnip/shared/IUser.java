package org.smartsnip.shared;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * This interface handles the interactions of the GUI on a concrete user. It is
 * given by the session to the GUI
 * 
 */
@RemoteServiceRelativePath("user")
public interface IUser extends RemoteService, IsSerializable {

	/** This class provides easy access to the proxy object */
	public static class Util {
		private static IUserAsync instance = null;

		/** Get the proxy object instance */
		public static IUserAsync getInstance() {
			if (instance == null) {
				instance = GWT.create(IUser.class);
			}
			return instance;
		}
	}

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
	 * Sets the email for the current logged in user
	 * 
	 * @param newAddress
	 *            New email address set to
	 * @throws NoAccessException
	 *             Thrown if the access is denied by the server
	 * @throws IllegalArgumentException
	 *             Thrown, if the given email adress is invalid
	 */
	public void setEmail(String newAddress) throws NoAccessException, IllegalArgumentException;

	/**
	 * Sets the new real name of the logged in user
	 * 
	 * @param newName
	 *            The new realname of the user
	 * @throws NoAccessException
	 *             Thrown, if there was an access denial from the server, e.g.
	 *             if there was no currently logged in user
	 */
	public void setRealName(String newName) throws NoAccessException;

	/**
	 * Gets a list of snippets, where the current logged in user is the owner
	 * 
	 * @return List of the own snippets of the user
	 * @throws NoAccessException
	 *             Thrown if the server denied the access
	 */
	public List<XSnippet> getSnippets() throws NoAccessException;

	/**
	 * Gets the favourites of the current user.
	 * 
	 * @deprecated Deprecated because it does not return the favourites for a
	 *             guest session. Use {@link ISession#getFavorites()} instant.
	 * @return A list of the favourites of the user, if successful.
	 * @throws NoAccessException
	 *             Thrown if the server denies the access, i.e. if the current
	 *             session is not logged in
	 */
	@Deprecated
	public List<XSnippet> getFavorites() throws NoAccessException;

	/**
	 * Sets the password for a user
	 * 
	 * @param oldpassword
	 *            old password to verify
	 * @param newpassword
	 *            to be set to
	 * @throws NoAccessException
	 *             Thrown, if the server denies the access
	 */
	public void setPassword(String oldpassword, String newpassword) throws NoAccessException;

	/**
	 * Gets a subset of the list of all users of the system
	 * 
	 * @param start
	 *            Start index of the subset
	 * @param count
	 *            Maximum number of users
	 * @return List of {@link XUser}
	 * @throws NoAccessException
	 *             Thrown if the server denies the access
	 */
	public List<XUser> getUsers(int start, int count) throws NoAccessException;

	/**
	 * @return {@link XUser} object of the user, that is currently logged in or
	 *         null, if a guest session
	 */
	public XUser getMe();
}

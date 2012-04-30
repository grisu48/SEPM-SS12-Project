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

	void setEmail(String newAddress) throws NoAccessException, IllegalArgumentException;

	void setRealName(String newName) throws NoAccessException;

	List<XSnippet> getSnippets() throws NoAccessException;

	List<XSnippet> getFavorites() throws NoAccessException;

}

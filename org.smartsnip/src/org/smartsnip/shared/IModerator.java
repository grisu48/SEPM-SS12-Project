package org.smartsnip.shared;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("moderator")
public interface IModerator extends RemoteService, IsSerializable {

	/** This class provides easy access to the proxy object */
	public static class Util {
		private static IModeratorAsync instance = null;

		/** Get the proxy object instance */
		public static IModeratorAsync getInstance() {
			if (instance == null) {
				instance = GWT.create(IModerator.class);
			}
			return instance;
		}
	}

	/**
	 * Requests on the server, if the logged in user in the current session is a
	 * moderator. If a guest session (not logged in) the result is always false
	 * 
	 * @return true if the current user has moderator status, otherwise false
	 */
	public boolean isModerator();

	/**
	 * @return Gets a list of all sessions
	 * @throws NoAccessException
	 *             Thrown, if the access is denied
	 */
	public List<XSession> getSessions() throws NoAccessException;

	/**
	 * Closes a session identified by a key
	 * 
	 * @param key
	 *            of the session to be closed
	 * @throws NotFoundException
	 *             Thrown if the session key is not found
	 * @throws NoAccessException
	 *             Thrown if the server denies the access
	 */
	public void closeSession(String key) throws NotFoundException,
			NoAccessException;

	/**
	 * Change the state of a user
	 * 
	 * @param username
	 *            Username of the user to be changed
	 * @param state
	 *            new userstate
	 * @throws NotFoundException
	 *             Thrown if the username is not found
	 * @throws NoAccessException
	 *             Thrown if the server denies the access
	 */
	public void setUserState(String username, XUser.UserState state)
			throws NotFoundException, NoAccessException;
}

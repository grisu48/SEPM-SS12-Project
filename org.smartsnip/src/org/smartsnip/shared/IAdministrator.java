package org.smartsnip.shared;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * This interface gives access to administrative services on the server. This
 * service also applys all commands of the moderator interface
 * {@link IModerator}
 * 
 * @author Felix Niederwanger
 */
@RemoteServiceRelativePath("administrator")
public interface IAdministrator extends RemoteService, IsSerializable, IModerator {

	/** This class provides easy access to the proxy object */
	public static class Util {
		private static IAdministratorAsync instance = null;

		/** Get the proxy object instance */
		public static IAdministratorAsync getInstance() {
			if (instance == null) {
				instance = GWT.create(IAdministrator.class);
			}
			return instance;
		}
	}

	/**
	 * Checks if the current user is administrator
	 * 
	 * @return true if an administrator, otherwise false
	 */
	public boolean isAdministrator();

	/**
	 * Sets the password for a user
	 * 
	 * @param username
	 *            The password is set for
	 * @param password
	 *            New password to be set
	 * @throws NotFoundException
	 *             If the user is not found
	 * @throws NoAccessException
	 *             If the access is denied by the server
	 */
	public void setPassword(String username, String password) throws NotFoundException, NoAccessException;

	/**
	 * Delete a user from the system
	 * 
	 * @param username
	 *            Username of the user that should be deleted
	 * @throws NotFoundException
	 *             Thrown if the user is not found
	 * @throws NoAccessException
	 *             Thrown if the server denies the access
	 */
	void deleteUser(String username) throws NotFoundException, NoAccessException;

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
	public void closeSession(String key) throws NotFoundException, NoAccessException;
}

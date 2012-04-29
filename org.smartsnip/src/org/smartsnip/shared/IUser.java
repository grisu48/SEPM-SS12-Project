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
			if (instance == null) instance = GWT.create(IUser.class);
			return instance;
		}
	}

	/**
	 * @return the name of the user
	 */
	public String getName();

	/**
	 * @return the email address
	 * @throws NoAccessException
	 *             Thrown if the call is not permitted
	 */
	public String getEmail() throws NoAccessException;

	/**
	 * Sets the email-address of the user. If the address is null or empty,
	 * nothing is done. If the address does not match the format of an
	 * email-address, a new {@link IllegalArgumentException} is thrown.
	 * 
	 * @param newAddress
	 *            new email-address of the user
	 * @throws NoAccessException
	 *             Thrown if the session cannot change the email-address of the
	 *             user
	 * @throws link
	 *             IllegalArgumentException Thrown if the given email-address is
	 *             invalid
	 */
	public void setEmail(String newAddress) throws NoAccessException, IllegalArgumentException;

	/**
	 * Sets the real name of the user. If the name is null or empty, nothing is
	 * done.
	 * 
	 * @param newName
	 *            New name of the user
	 * @throws NoAccessException
	 *             Thrown if the current session cannot access this property
	 */
	public void setRealName(String newName) throws NoAccessException;

	/**
	 * @return the real name of the user
	 * @throws NoAccessException
	 *             Thrown if the call is not permitted
	 */
	public String getRealName() throws NoAccessException;

	/**
	 * Closes the current session of the user
	 * 
	 * @throws NoAccessException
	 *             Thrown if the call cannot be executed by this session
	 */
	public void logout() throws NoAccessException;

	/**
	 * 
	 * @return a list containing all snippets of the user
	 * @throws NoAccessException
	 *             Thrown if the call cannot be executed by this session
	 * @obsolete A list with all interfaces is a waste of system resources.
	 *           Check this!
	 */
	public List<ISnippet> getSnippets() throws NoAccessException;

	/**
	 * 
	 * @return a list containing all favourite snippets of the user
	 * @throws NoAccessException
	 *             Thrown if the call cannot be executed by this session
	 */
	public List<ISnippet> getFavorites() throws NoAccessException;

	/**
	 * Reports an abusive user.
	 * 
	 * @param reason
	 *            Reason why the user is behaving abusive
	 */
	public void report(String reason);

}

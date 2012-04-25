package org.smartsnip.shared;

import java.util.List;

/**
 * This interface handles the interactions of the GUI on a concrete user. It is
 * given by the session to the GUI
 * 
 */
public interface IUser {
	/**
	 * @return the name of the user
	 */
	public String getName();

	/**
	 * @return the email address
	 * @throws IllegalAccessException
	 *             Thrown if the call is not permitted
	 */
	public String getEmail() throws IllegalAccessException;

	/**
	 * Sets the email-address of the user. If the address is null or empty,
	 * nothing is done. If the address does not match the format of an
	 * email-address, a new {@link IllegalArgumentException} is thrown.
	 * 
	 * @param newAddress
	 *            new email-address of the user
	 * @throws IllegalAccessException
	 *             Thrown if the session cannot change the email-address of the
	 *             user
	 * @throws link
	 *             IllegalArgumentException Thrown if the given email-address is
	 *             invalid
	 */
	public void setEmail(String newAddress) throws IllegalAccessException, IllegalArgumentException;

	/**
	 * Sets the real name of the user. If the name is null or empty, nothing is
	 * done.
	 * 
	 * @param newName
	 *            New name of the user
	 * @throws IllegalAccessException
	 *             Thrown if the current session cannot access this property
	 */
	public void setRealName(String newName) throws IllegalAccessException;

	/**
	 * @return the real name of the user
	 * @throws IllegalAccessException
	 *             Thrown if the call is not permitted
	 */
	public String getRealName() throws IllegalAccessException;

	/**
	 * Closes the current session of the user
	 * 
	 * @throws IllegalAccessException
	 *             Thrown if the call cannot be executed by this session
	 */
	public void logout() throws IllegalAccessException;

	/**
	 * 
	 * @return a list containing all snippets of the user
	 * @throws IllegalAccessException
	 *             Thrown if the call cannot be executed by this session
	 * @obsolete A list with all interfaces is a waste of system resources.
	 *           Check this!
	 */
	public List<ISnippet> getSnippets() throws IllegalAccessException;

	/**
	 * 
	 * @return a list containing all favourite snippets of the user
	 * @throws IllegalAccessException
	 *             Thrown if the call cannot be executed by this session
	 */
	public List<ISnippet> getFavorites() throws IllegalAccessException;

	/**
	 * Reports an abusive user.
	 * 
	 * @param reason
	 *            Reason why the user is behaving abusive
	 */
	public void report(String reason);

}

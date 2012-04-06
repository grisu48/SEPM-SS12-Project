package org.smartsnip.core;

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
}

package org.smartsnip.shared;

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
@RemoteServiceRelativePath("category")
public interface IAdministrator extends RemoteService, IsSerializable,
		IModerator {

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
	public void setPassword(String username, String password)
			throws NotFoundException, NoAccessException;
}

package org.smartsnip.server;

import org.smartsnip.shared.IAdministrator;
import org.smartsnip.shared.NoAccessException;
import org.smartsnip.shared.NotFoundException;
import org.smartsnip.shared.XUser.UserState;

/**
 * Implementation for the administrator user. Some methods have to be
 * overwritten because of the higher access privileges of the administrator
 * 
 * @author Felix Niederwanger
 * 
 */
public class IAdministratorImpl extends IModeratorImpl implements
		IAdministrator {

	/** Serialisation ID */
	private static final long serialVersionUID = 2747963244731693434L;

	@Override
	public void closeSession(String key) throws NotFoundException,
			NoAccessException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUserState(String username, UserState state)
			throws NotFoundException, NoAccessException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isAdministrator() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPassword(String username, String password)
			throws NotFoundException, NoAccessException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteUser(String username) throws NotFoundException,
			NoAccessException {
		// TODO Auto-generated method stub

	}

}

package org.smartsnip.server;

import java.util.ArrayList;
import java.util.List;

import org.smartsnip.core.Logging;
import org.smartsnip.core.User;
import org.smartsnip.shared.IAdministrator;
import org.smartsnip.shared.NoAccessException;
import org.smartsnip.shared.NotFoundException;
import org.smartsnip.shared.XSession;
import org.smartsnip.shared.XUser.UserState;

/**
 * Implementation for the administrator user. Some methods have to be
 * overwritten because of the higher access privileges of the administrator
 * 
 * @author Felix Niederwanger
 * 
 */
public class IAdministratorImpl extends IModeratorImpl implements IAdministrator {

	/** Serialisation ID */
	private static final long serialVersionUID = 2747963244731693434L;

	@Override
	public void setUserState(String username, UserState state) throws NotFoundException, NoAccessException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isAdministrator() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPassword(String username, String password) throws NotFoundException, NoAccessException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteUser(String username) throws NotFoundException, NoAccessException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<XSession> getSessions() throws NoAccessException {
		Session session = getSession();
		User user = session.getUser();
		if (user == null || !session.isLoggedIn())
			throw new NoAccessException();
		if (!user.isAdministrator())
			throw new NoAccessException();

		List<Session> sessions = Session.getSessions();
		List<XSession> result = new ArrayList<XSession>(sessions.size());
		for (Session s : sessions)
			result.add(s.toXSession());

		return result;
	}

	@Override
	public void closeSession(String key) throws NotFoundException, NoAccessException {
		if (key == null)
			return;

		Session session = getSession();
		User user = session.getUser();
		if (user == null || !session.isLoggedIn())
			throw new NoAccessException();
		if (!user.isAdministrator())
			throw new NoAccessException();

		Session targetSession = Session.getObfuscatedSession(key);
		if (targetSession == null)
			throw new NotFoundException();

		User targetSessionUser = targetSession.getUser();
		Logging.printInfo("MODERATOR: Terminating session for: "
				+ (targetSessionUser == null ? "Guest session" : targetSessionUser.getUsername()));
		targetSession.deleteSession();
	}

}

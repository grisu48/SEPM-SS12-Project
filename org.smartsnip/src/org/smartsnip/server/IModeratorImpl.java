package org.smartsnip.server;

import java.util.ArrayList;
import java.util.List;

import org.smartsnip.core.User;
import org.smartsnip.core.User.UserState;
import org.smartsnip.shared.IModerator;
import org.smartsnip.shared.NoAccessException;
import org.smartsnip.shared.NotFoundException;
import org.smartsnip.shared.XSession;

public class IModeratorImpl extends GWTSessionServlet implements IModerator {

	/** Serialisation ID */
	private static final long serialVersionUID = -3154461839994229670L;

	@Override
	public boolean isModerator() {
		Session session = getSession();
		if (!session.isLoggedIn())
			return false;
		User user = session.getUser();
		if (user == null)
			return false;
		return user.canModerate();
	}

	@Override
	public List<XSession> getSessions() throws NoAccessException {
		Session session = getSession();
		User user = session.getUser();
		if (user == null || !session.isLoggedIn())
			throw new NoAccessException();
		if (!user.isModerator())
			throw new NoAccessException();

		List<Session> sessions = Session.getSessions();
		List<XSession> result = new ArrayList<XSession>(sessions.size());
		for (Session s : sessions)
			result.add(s.toXSession());

		return result;
	}

	@Override
	public void closeSession(String key) throws NotFoundException,
			NoAccessException {
		if (key == null)
			return;

		Session session = getSession();
		User user = session.getUser();
		if (user == null || !session.isLoggedIn())
			throw new NoAccessException();
		if (!user.isModerator())
			throw new NoAccessException();

		Session targetSession = Session.getObfuscatedSession(key);
		if (targetSession == null)
			throw new NotFoundException();

		targetSession.deleteSession();
	}

	@Override
	public void setUserState(String username, UserState state)
			throws NotFoundException, NoAccessException {
		if (username == null)
			return;

		Session session = getSession();
		User user = session.getUser();
		if (user == null || !session.isLoggedIn())
			throw new NoAccessException();
		if (!user.isModerator())
			throw new NoAccessException();

		User targetUser = User.getUser(username);
		if (targetUser == null)
			throw new NotFoundException();

		targetUser.setState(state);
	}

}

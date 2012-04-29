package org.smartsnip.core;

import java.util.List;

import org.smartsnip.shared.ICategory;
import org.smartsnip.shared.IComment;
import org.smartsnip.shared.ISession;
import org.smartsnip.shared.ISnippet;
import org.smartsnip.shared.IUser;
import org.smartsnip.shared.NoAccessException;

/**
 * This class acts as the servlet that coordiantes the transactions between the
 * session and the client
 * 
 */
public class ISessionImpl extends SessionServlet implements ISession {

	/** Serialisation ID */
	private static final long serialVersionUID = 51299L;

	/** Gets the username that is currenlt logged in, or null if a guest session */
	@Override
	public String getUsername() {
		Session session = getSession();

		User user = session.getUser();
		if (user == null) return null;
		return user.getUsername();
	}

	@Override
	public int getActiveSessionCount() {
		return Session.activeCount();
	}

	@Override
	public int getGuestSessionCount() {
		return Session.guestSessions();
	}

	@Override
	public int getUserCount() {
		return Session.getUserCount();
	}

	@Override
	public int getCategoryCount() {
		return Session.getCategoryCount();
	}

	@Override
	public int getSnippetCount() {
		return Session.getSnippetCount();
	}

	@Override
	public boolean login(String username, String password) throws NoAccessException {
		Session session = getSession();

		if (session.isLoggedIn()) return false;
		if (username == null || password == null) return false;

		// This method throws a NoAccessException, if the login fails
		session.login(username, password);
		return true;
	}

	@Override
	public void logout() {
		Session session = getSession();

		session.logout();
	}

	@Override
	public ICategory getCategory(String name) throws NoAccessException {
		Session session = getSession();

		return session.getICategory(name);
	}

	@Override
	public ISnippet getSnippet(int hash) throws NoAccessException {
		Session session = getSession();

		return session.getISnippet(hash);
	}

	@Override
	public IUser getUser(String username) throws NoAccessException {
		Session session = getSession();

		return session.getIUser(username);
	}

	@Override
	public boolean isLoggedIn() {
		Session session = getSession();

		return session.isLoggedIn();
	}
}

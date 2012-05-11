package org.smartsnip.server;

import java.util.List;

import org.smartsnip.core.Category;
import org.smartsnip.core.Snippet;
import org.smartsnip.core.User;
import org.smartsnip.shared.ISession;
import org.smartsnip.shared.NoAccessException;
import org.smartsnip.shared.XSnippet;

/**
 * This class acts as the servlet that coordiantes the transactions between the
 * session and the client
 * 
 */
public class ISessionImpl extends SessionServlet implements ISession {

	/** Serialisation ID */
	private static final long serialVersionUID = 51299L;

	@Override
	public String getUsername() {
		Session session = getSession();
		if (!session.isLoggedIn())
			return null;

		return session.getUsername();
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
		return User.totalCount();
	}

	@Override
	public int getCategoryCount() {
		return Category.totalCount();
	}

	@Override
	public int getSnippetCount() {
		return Snippet.totalCount();
	}

	@Override
	public boolean login(String username, String password) throws NoAccessException {

		Session session = getSession();

		if (session.isLoggedIn())
			return false;

		session.login(username, password);
		return true;
	}

	@Override
	public void logout() {
		Session session = getSession();
		session.logout();
	}

	@Override
	public boolean isLoggedIn() {
		Session session = getSession();
		return session.isLoggedIn();
	}

	@Override
	public List<XSnippet> doSearch(String searchString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<XSnippet> doSearch(String searchString, int start, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean registerNewUser(String username, String password, String email) throws NoAccessException {
		if (username == null)
			return false;
		if (password == null || password.isEmpty())
			return false;
		if (email == null || email.isEmpty())
			return false;

		username = User.trimUsername(username);
		if (username.isEmpty())
			return false;

		Session session = getSession();
		if (!session.getPolicy().canRegister(session))
			throw new NoAccessException();

		if (User.exists(username))
			return false;
		try {
			if (User.createNewUser(username, password, email) == null)
				return false;

			// Success
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
}

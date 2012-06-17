package org.smartsnip.server;

import java.util.List;

import org.smartsnip.core.User;
import org.smartsnip.shared.IUser;
import org.smartsnip.shared.NoAccessException;
import org.smartsnip.shared.XSnippet;

/**
 * This is the implementation of the {@link org.smartsnip.shared.IUser}
 * interface that runs on the server
 * 
 */
public class IUserImpl extends GWTSessionServlet implements IUser {

	/** Serialisation ID */
	private static final long serialVersionUID = -6793875859564213616L;

	@Override
	public void setEmail(String newAddress) throws NoAccessException,
			IllegalArgumentException {
		Session session = getSession();
		User user = session.getUser();

		if (user == null || !session.getPolicy().canEditUserData(session, user))
			throw new NoAccessException();
		user.setEmail(newAddress);
	}

	@Override
	public void setRealName(String newName) throws NoAccessException {
		Session session = getSession();
		User user = session.getUser();

		if (user == null || !session.getPolicy().canEditUserData(session, user))
			throw new NoAccessException();

		user.setRealName(newName);
	}

	@Override
	public boolean login(String username, String password)
			throws NoAccessException {
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
	public List<XSnippet> getSnippets() throws NoAccessException {
		Session session = getSession();
		User user = session.getUser();

		if (user == null || !session.isLoggedIn())
			throw new NoAccessException();

		List<XSnippet> result = toXSnippets(user.getMySnippets());
		// XXX Check this one day ...
		for (XSnippet snippet : result)
			snippet.isOwn = true;
		return result;
	}

	@Override
	public List<XSnippet> getFavorites() throws NoAccessException {
		Session session = getSession();
		User user = session.getUser();

		if (user == null || !session.isLoggedIn())
			throw new NoAccessException();
		return toXSnippets(user.getFavoriteSnippets());
	}

	@Override
	public void setPassword(String password) throws NoAccessException {
		if (password == null || password.isEmpty())
			return;

		Session session = getSession();
		User user = session.getUser();

		if (user == null || !session.getPolicy().canEditUserData(session, user))
			throw new NoAccessException();

		user.setPassword(password);
	}

}

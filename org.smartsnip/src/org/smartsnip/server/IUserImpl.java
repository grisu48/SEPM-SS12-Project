package org.smartsnip.server;

import java.util.ArrayList;
import java.util.List;

import org.smartsnip.core.Snippet;
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

		return toXSnippets(user.getMySnippets());
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

	/**
	 * Converts a list of snippet into a list of XSnippet objects
	 * 
	 * If the source list is null, the method returns null
	 * 
	 * @param source
	 *            Source list of snippets.
	 * @return a list containing {@link XSnippet} objects of the corresponding
	 *         source objects
	 */
	private List<XSnippet> toXSnippets(List<Snippet> source) {
		if (source == null)
			return null;
		List<XSnippet> result = new ArrayList<XSnippet>();

		Session session = getSession();
		User user = session.getUser();

		for (Snippet snippet : source) {
			XSnippet xsnippet = snippet.toXSnippet();

			if (user == null)
				xsnippet.isFavorite = session.isFavourite(snippet);
			else
				xsnippet.isFavorite = user.isFavourite(snippet);
			xsnippet.canDelete = session.getPolicy().canDeleteSnippet(session,
					snippet);
			xsnippet.canEdit = session.getPolicy().canEditSnippet(session,
					snippet);

			result.add(xsnippet);
		}

		return result;
	}

}

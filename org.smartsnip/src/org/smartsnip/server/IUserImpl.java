package org.smartsnip.server;

import java.util.ArrayList;
import java.util.List;

import org.smartsnip.core.Snippet;
import org.smartsnip.core.User;
import org.smartsnip.shared.IUser;
import org.smartsnip.shared.NoAccessException;
import org.smartsnip.shared.XSnippet;

public class IUserImpl extends GWTSessionServlet implements IUser {

	/** Serialisation ID */
	private static final long serialVersionUID = 1235424507650742693L;

	/** Associated user object */
	protected final User user;

	public IUserImpl(User user) {
		super();

		if (user == null)
			throw new NullPointerException();
		this.user = user;
	}

	@Override
	public void setEmail(String newAddress) throws NoAccessException,
			IllegalArgumentException {
		Session session = getSession();

		if (!session.getPolicy().canEditUserData(session, user))
			throw new NoAccessException();
		user.setEmail(newAddress);
	}

	@Override
	public void setRealName(String newName) throws NoAccessException {
		Session session = getSession();

		if (!session.getPolicy().canEditUserData(session, user))
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
		// XXX Check security privileges
		return toXSnippets(user.getMySnippets());
	}

	@Override
	public List<XSnippet> getFavorites() throws NoAccessException {
		// XXX Check security privileges
		return toXSnippets(user.getFavoriteSnippets());
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

		for (Snippet snippet : source) {
			result.add(snippet.toXSnippet());
		}

		return result;
	}

	@Override
	public void setPassword(String pw1) throws NoAccessException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Implement me");
	}
}

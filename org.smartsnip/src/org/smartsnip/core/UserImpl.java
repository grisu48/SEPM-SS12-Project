package org.smartsnip.core;

import java.util.ArrayList;
import java.util.List;

import org.smartsnip.shared.ISnippet;
import org.smartsnip.shared.IUser;
import org.smartsnip.shared.NoAccessException;

public class UserImpl extends SessionServlet implements IUser {

	/** Serialisation ID */
	private static final long serialVersionUID = 1235424507650742693L;

	/** Associated user object */
	protected final User user;

	public UserImpl(User user) {
		super();

		if (user == null) throw new NullPointerException();
		this.user = user;
	}

	@Override
	public String getName() {
		return user.getUsername();
	}

	@Override
	public String getEmail() throws NoAccessException {
		return user.getEmail();
	}

	@Override
	public void setEmail(String newAddress) throws NoAccessException, IllegalArgumentException {
		Session session = getSession();

		if (!session.getPolicy().canEditUserData(session, user)) throw new NoAccessException();
		user.setEmail(newAddress);
	}

	@Override
	public void setRealName(String newName) throws NoAccessException {
		Session session = getSession();

		if (!session.getPolicy().canEditUserData(session, user)) throw new NoAccessException();

		user.setRealName(newName);
	}

	@Override
	public String getRealName() throws NoAccessException {
		return user.getRealName();
	}

	@Override
	public void logout() throws NoAccessException {
		Session session = getSession();

		if (!user.equals(session.getUser())) throw new NoAccessException();
	}

	@Override
	public List<ISnippet> getSnippets() throws NoAccessException {
		// XXX Check security privileges
		return toISnippets(user.getMySnippets());
	}

	@Override
	public List<ISnippet> getFavorites() throws NoAccessException {
		// XXX Check security privileges
		return toISnippets(user.getFavoriteSnippets());
	}

	@Override
	public void report(String reason) {
		if (reason == null) {
			reason = "";
		}
		Session.report(user, reason);
	}

	/**
	 * Converts a list of snippets to a list of interfaces to the corresponding
	 * snippets
	 * 
	 * @param snippets
	 * @return
	 */
	private List<ISnippet> toISnippets(List<Snippet> snippets) {
		Session session = getSession();

		if (snippets == null) return null;
		List<ISnippet> result = new ArrayList<ISnippet>();

		for (Snippet snippet : snippets) {
			result.add(session.getISnippet(snippet));
		}

		return result;
	}

}

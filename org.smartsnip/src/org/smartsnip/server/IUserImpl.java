package org.smartsnip.server;

import java.util.ArrayList;
import java.util.List;

import org.smartsnip.core.Logging;
import org.smartsnip.core.User;
import org.smartsnip.security.PrivilegeController;
import org.smartsnip.shared.IUser;
import org.smartsnip.shared.NoAccessException;
import org.smartsnip.shared.XSnippet;
import org.smartsnip.shared.XUser;

/**
 * This is the implementation of the {@link org.smartsnip.shared.IUser}
 * interface that runs on the server
 * 
 * @author Felix Niederwanger
 */
public class IUserImpl extends GWTSessionServlet implements IUser {

	/** Serialisation ID */
	private static final long serialVersionUID = -6793875859564213616L;

	@Override
	public void setEmail(String newAddress) throws NoAccessException, IllegalArgumentException {
		Session session = getSession();
		User user = session.getUser();

		if (user == null || !session.getPolicy().canEditUserData(session, user))
			throw new NoAccessException();

		Logging.printInfo("Sets his new email to " + newAddress);
		user.setEmail(newAddress);
	}

	@Override
	public void setRealName(String newName) throws NoAccessException {
		Session session = getSession();
		User user = session.getUser();

		if (user == null || !session.getPolicy().canEditUserData(session, user))
			throw new NoAccessException();

		Logging.printInfo("Sets his new realname to " + newName);
		user.setRealName(newName);
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

		List<XSnippet> result = toXSnippets(user.getFavoriteSnippets());
		// XXX Check this one day ...
		for (XSnippet snippet : result)
			snippet.isFavorite = true;
		return result;
	}

	@Override
	public void setPassword(String oldpassword, String newpassword) throws NoAccessException, IllegalArgumentException {
		if (newpassword == null || newpassword.isEmpty() || oldpassword == null || oldpassword.isEmpty())
			return;

		Session session = getSession();
		User user = session.getUser();

		if (user == null || !session.getPolicy().canEditUserData(session, user)) {
			Logging.printWarning("Request for setting new password denied, access denied by policy");
			throw new NoAccessException("Access denied");
		}
		if (!user.checkPassword(oldpassword)) {
			Logging.printWarning("Request for setting new password denied, wrong credentials");
			throw new NoAccessException("Wrong password");
		}

		if (!PrivilegeController.acceptPassword(newpassword))
			throw new IllegalArgumentException("Password denied");

		Logging.printInfo("Sets a new password");
		user.setPassword(newpassword);
	}

	@Override
	public List<XUser> getUsers(int start, int count) throws NoAccessException {
		Session session = getSession();
		if (!session.isLoggedIn())
			throw new NoAccessException();

		List<User> users = User.getUsers(start, count);
		if (users == null)
			return null; // Something went wrong ...
		List<XUser> result = new ArrayList<XUser>(users.size());
		for (User user : users)
			result.add(toXUser(user, session));
		return result;
	}

	@Override
	public XUser getMe() {
		User user = getSession().getUser();
		if (user == null)
			return null;
		return toXUser(user, getSession());
	}

	/**
	 * Converts a given user to a {@link XUser} object and adds also
	 * session-specific properties
	 * 
	 * @param user
	 *            to be converted
	 * @param session
	 *            the object belongs to
	 * @return Converted {@link XUser} object or null, if user was null
	 */
	public static XUser toXUser(User user, Session session) {
		if (user == null)
			return null;
		final XUser result = user.toXUser();

		// Currently the parameter session is not used, but still here in
		// respect to further development

		result.isLoggedIn = Session.isLoggedIn(user);

		return result;
	}
}

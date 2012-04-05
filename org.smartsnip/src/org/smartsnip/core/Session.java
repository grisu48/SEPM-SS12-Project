package org.smartsnip.core;

import org.smartsnip.security.IAccessPolicy;
import org.smartsnip.security.PrivilegeController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Session {
	/** Session storage, each session is identified with the cookie string */
	private static HashMap<String, Session> storedSessions = new HashMap<String, Session>();

	/** Cookie with that the session is identified */
	private final String cookie;

	/** State where the current session currently is in */
	private SessionState state = SessionState.active;

	/**
	 * Assigned user to this session or null if currently no user has been
	 * assigned
	 */
	private User user = null;

	/**
	 * General access policy that applies to this session
	 */
	private IAccessPolicy policy = PrivilegeController.getGuestAccessPolicty();

	/**
	 * The state in witch the current session is. An active session becomes
	 * inactive after a given time period, and after becoming inactive it will
	 * be deleted after another defined time period
	 * 
	 */
	public enum SessionState {
		active, inactive, deleted
	}

	/**
	 * Session must be created with the static Session factory method.
	 */
	private Session(String cookie) {
		this.cookie = cookie;
		this.policy = PrivilegeController.getGuestAccessPolicty();
	}

	/**
	 * @return the cookie of the session
	 */
	public String getCookie() {
		return cookie;
	}

	/**
	 * @return the state of the session
	 */
	public SessionState getState() {
		return state;
	}

	/**
	 * @return the logged in user, or null if in guest account
	 */
	User getUser() {
		return user;
	}

	/**
	 * Gets the session that is associated with the given cookie. If the session
	 * exists, the return value is exactly this given session. If there is no
	 * session associated with the cookies, the method will create a new session
	 * and return it.
	 * 
	 * If the cookie is null or empty, null will be returned.
	 * 
	 * @param cookie
	 *            Used for session identification
	 * @return Session identified by the cookie
	 */
	public synchronized static Session getSession(String cookie) {
		if (cookie == null || cookie.length() == 0) return null;

		Session result = storedSessions.get(cookie);
		if (result == null) result = createNewSession(cookie);
		return result;
	}

	/**
	 * FactoryMethod: Creates a new session and stores it into the session map.
	 * Caution: The given session must not exists, otherwise it will be
	 * overwritten. Check this before making this call
	 * 
	 * @param cookie
	 *            new Session's cookie
	 * @return Created session
	 */
	private synchronized static Session createNewSession(String cookie) {
		if (cookie == null || cookie.length() == 0)
			throw new NullPointerException("Cannot create session with null cookie");
		Session newSession = new Session(cookie);
		storedSessions.put(cookie, newSession);
		return newSession;
	}

	public synchronized boolean isLoggedIn() {
		return user != null;
	}

	/**
	 * Tries to do a login procedure. If currently a user is logged in a new
	 * {@link IllegalAccessException} will be thrown. If the login fails also a
	 * new {@link IllegalAccessException} will be thrown with a reason message.
	 * If the username or the password is null the login will fail, resulting in
	 * an {@link IllegalAccessException}.
	 * 
	 * @param username
	 *            Name of the user needed for the login
	 * @param password
	 *            Password of the user
	 * @throws IllegalAccessException
	 *             Thrown as security exception when the login process fails.
	 */
	public synchronized void login(String username, String password) throws IllegalAccessException {
		if (username.length() == 0 || password.length() == 0)
			throw new IllegalAccessException("Login credentials missing");
		if (isLoggedIn()) throw new IllegalAccessException("The session is already logged in");

	}

	/**
	 * The session wants to gain access to a user account. It returns a new
	 * Interface to handle the user actions if found, or null if the user cannot
	 * be found.
	 * 
	 * If the given username is null or empty, null is returned instantaneous.
	 * 
	 * If the session is not allowed to do this call (or on that user) the
	 * method throws a new {@link IllegalAccessException}.
	 * 
	 * @param username
	 *            Name of the user to be searched
	 * @return the found user or null if not found
	 * @throws IllegalAccessException
	 *             Thrown as security exception when the session is not allowed
	 *             to do that
	 */
	public IUser getIUser(String username) throws IllegalAccessException {
		if (username == null || username.length() == 0) return null;

		return getIUser(User.getUser(username));
	}

	/**
	 * Internal call to generate the interface out of an concrete user object.
	 * 
	 * @param user
	 *            concrete entity the interface belongs to. If null, also null
	 *            is the result
	 * @return Interface that handles the inputs of the GUI to the user or null
	 *         if the given user is null.
	 */
	IUser getIUser(final User user) {
		if (user == null) return null;

		final boolean canEdit = policy.canEditUserData(this, user);

		return new IUser() {

			final User owner = user;

			@Override
			public synchronized void logout() throws IllegalAccessException {
				if (Session.this.user != owner) return;
				Session.this.logout();
			}

			@Override
			public synchronized List<ISnippet> getSnippets() throws IllegalAccessException {
				return getUserSnippets(user);
			}

			@Override
			public String getName() {
				return owner.getUsername();
			}
		};
	}

	/**
	 * Logs the session out
	 */
	public void logout() {
		// TODO Auto-generated method stub

	}

	/**
	 * Returns a new interface for the session to manipulate the user data. If
	 * currently no user has been logged in, null is returned
	 * 
	 * @return interface for the sesssion to manipulate the user date of the
	 *         logged in user.
	 */
	public IUser getIUser() {
		if (user == null) return null;
		return new IUser() {

			@Override
			public void logout() throws IllegalAccessException {
				logout();
			}

			@Override
			public List<ISnippet> getSnippets() throws IllegalAccessException {
				return getUserSnippets(user);
			}

			@Override
			public String getName() {
				return user.username;
			}
		};
	}

	/**
	 * Creates a list with interfaces for all snippets of a given user.
	 * 
	 * This call currently is very ineffective and considered therefore obsolete
	 * 
	 * @param user
	 * @return
	 * @obsolete
	 */
	private List<ISnippet> getUserSnippets(User user) {
		if (user == null) return new ArrayList<ISnippet>();
		List<Snippet> snippets = user.getMySnippets();
		List<ISnippet> result = new ArrayList<ISnippet>(snippets.size());

		for (final Snippet snippet : snippets) {
			result.add(new ISnippet() {

				Snippet owner = snippet;

				@Override
				public void removeTag(String tag) throws IllegalAccessException {
					if (tag == null || tag.length() == 0) return;
				}

				@Override
				public void increaseViewCounter() throws IllegalAccessException {
					owner.increaseViewCounter();
				}

				@Override
				public int getViewCount() {
					return owner.getViewcount();
				}

				@Override
				public List<String> getTags() {
					// TODO: Ineffective call
					List<Tag> tags = owner.getTags();
					List<String> result = new ArrayList<String>(tags.size());
					for (Tag tag : tags)
						result.add(tag.name);
					return result;
				}

				@Override
				public IUser getOwner() {
					return getIUser(this.owner.owner); // this.owner.owner -> a
														// bit confusing ...
				}

				@Override
				public String getName() {
					return owner.getName();
				}

				@Override
				public String getLanguage() {
					return owner.getCode().language;
				}

				@Override
				public int getHash() {
					return owner.hash;
				}

				@Override
				public String getDesc() {
					return owner.getDescription();
				}

				@Override
				public List<IComment> getComments() throws IllegalAccessException {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public String getCodeHTML() {
					return owner.getCode().getFormattedHTML();
				}

				@Override
				public String getCategory() {
					return owner.getCategory().getName();
				}

				@Override
				public void addTag(String tag) throws IllegalAccessException {
					if (!policy.canTagSnippet(Session.this, owner)) throw new IllegalAccessException();

					// Security check passed. Add tag
					// TODO: Get TAG entity
					Tag tagEntity = null;
					// TODO: Implement me ....
				}

				@Override
				public IComment addComment(String comment) throws IllegalAccessException {
					// TODO Auto-generated method stub
					return null;
				}
			});
		}

		return result;
	}
}

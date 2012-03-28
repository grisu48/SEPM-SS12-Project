package org.smartsnip.core;

import org.smartsnip.security.IAccessPolicy;
import org.smartsnip.security.PrivilegeController;

import java.util.HashMap;

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
	 * 
	 */
	private IAccessPolicy policy;

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
	public User getUser() {
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
	public static Session getSession(String cookie) {
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
	 */
	public synchronized void login(String username, String password) throws IllegalAccessException {
		if (username.length() == 0 || password.length() == 0)
			throw new IllegalAccessException("Login credentials missing");
		if (isLoggedIn()) throw new IllegalAccessException("The session is already logged in");

		if (!policy.canLogin(this)) throw new IllegalAccessException("Access denied");
		if (User.auth(username, password)) throw new IllegalAccessException("Access denied");

		User user = User.getUser(username);
		if (user == null) throw new IllegalAccessException("Access denied");

		this.user = user;
	}
}

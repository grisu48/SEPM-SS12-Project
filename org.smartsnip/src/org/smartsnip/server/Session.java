package org.smartsnip.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.smartsnip.core.Category;
import org.smartsnip.core.Comment;
import org.smartsnip.core.Persistence;
import org.smartsnip.core.Search;
import org.smartsnip.core.Snippet;
import org.smartsnip.core.User;
import org.smartsnip.security.IAccessPolicy;
import org.smartsnip.security.PrivilegeController;
import org.smartsnip.shared.ISessionObserver;
import org.smartsnip.shared.NoAccessException;

public class Session {
	/** Session storage, each session is identified with the cookie string */
	private static HashMap<String, Session> storedSessions = new HashMap<String, Session>();

	/** Cookie with that the session is identified */
	private final String cookie;

	private static final Session staticGuestSession = new Session("");

	/** State where the current session currently is in */
	private SessionState state = SessionState.active;

	/**
	 * Assigned user to this session or null if currently no user has been
	 * assigned
	 */
	private User user = null;

	/**
	 * Temporary list of last search strings
	 */
	private transient final List<String> lastSearchStrings = new ArrayList<String>();

	/** Maximum number of stored last search strings */
	private transient static int maxStoredSearchStrings = 5;

	/**
	 * General access policy that applies to this session.
	 * 
	 * The policy is refreshed by the method refreshPolicy and should never be
	 * touched outside this method for writing.
	 */
	private IAccessPolicy policy = PrivilegeController.getGuestAccessPolicty();

	/**
	 * The session implements an observable pattern for the GUI.
	 * 
	 * In this list all given observers are stored.
	 */
	private final List<ISessionObserver> observers = new ArrayList<ISessionObserver>();

	/**
	 * Implementation of the observer to serve the observers with the given
	 * methods.
	 * 
	 * Each method of the interface is implemented to redirect the call to the
	 * observers.
	 * */

	private final ISessionObserver observable = new ISessionObserver() {

	};

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
	 * Delay in milliseconds after that a session becomes inactive
	 */
	public final static long inactiveDelay = 5 * 60 * 1000; // After 5 minutes
															// of
	// inactivity the
	// session goes to
	// inactive
	/**
	 * Delay in milliseconds after that a session is deleted
	 */
	public final static long deleteDelay = 60 * 60 * 1000; // After an hour of
	// inactivity the session
	// will be deleted

	/** Timer of the last activity */
	private long lastActivityTime = System.currentTimeMillis();

	/**
	 * Session initialisation
	 */
	static {
		if (!Persistence.isInitialized()) {
			try {

				Persistence.initialize();

			} catch (IllegalAccessException e) {
			}
		}
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
	 * @return if the session is alive
	 */
	boolean isAlive() {
		return getState() == SessionState.active;
	}

	/**
	 * @return if the session is deleted
	 */
	boolean isDead() {
		return getState() == SessionState.deleted;
	}

	/**
	 * @return if the given session is inactive
	 */
	boolean isInactive() {
		return getState() == SessionState.inactive;
	}

	/**
	 * Gets the session that is associated with the given cookie. If the session
	 * exists, the return value is exactly this given session. If there is no
	 * session associated with the cookies, the method will create a new session
	 * and return it.
	 * 
	 * If the cookie is null or empty, the default static guest session for a
	 * cookie-blocking account is activated
	 * 
	 * @param cookie
	 *            Used for session identification
	 * @return Session identified by the cookie
	 */
	public static Session getSession(String cookie) {
		if (cookie == null || cookie.length() == 0) return getStaticGuestSession();

		synchronized (storedSessions) {
			Session result = storedSessions.get(cookie);
			if (result == null) {
				result = createNewSession(cookie);
			} else if (result.isDead()) {
				storedSessions.remove(cookie);
				result = null;
			}
			return result;
		}

	}

	/**
	 * Checks if the given cookie exists. If the cookies is null or empty, false
	 * is returned.
	 * 
	 * This call checks also if a found session is alive. If not it will be
	 * deleted out of the memory
	 * 
	 * @param cookie
	 *            to be checked
	 * @return true if the given cookie exists, false if not existing or the
	 *         given cookie is null or empty
	 */
	public static boolean existsCookie(String cookie) {
		if (cookie == null || cookie.length() == 0) return false;
		synchronized (storedSessions) {
			Session result = storedSessions.get(cookie);
			if (result == null) return false;
			if (result.isDead()) {
				storedSessions.remove(cookie);
				return false;
			}
			return true;
		}
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
	private static Session createNewSession(String cookie) {
		if (cookie == null || cookie.length() == 0)
			throw new NullPointerException("Cannot create session with null cookie");
		Session newSession = new Session(cookie);
		synchronized (storedSessions) {
			storedSessions.put(cookie, newSession);
		}
		return newSession;
	}

	/**
	 * 
	 * @return true if the session is logged in, false if a guest session
	 */
	public synchronized boolean isLoggedIn() {
		return user != null;
	}

	/**
	 * Checks if the given user is the user, that is currently logged in with
	 * the session.
	 * 
	 * If the given user is null, the method checks, if the current session is a
	 * guest session.
	 * 
	 * This method is mostly used in the security layer.
	 * 
	 * @param user
	 *            to be checked. If null it is assumed as guest session
	 * @return true if the user matches the session user
	 */
	public synchronized boolean isLoggedInUser(User user) {
		if (user == null) return this.user == null;
		return user.equals(this.user);
	}

	/**
	 * Checks if the given user is the user, that is currently logged in with
	 * the session.
	 * 
	 * If the given user is null, the method checks, if the current session is a
	 * guest session.
	 * 
	 * This method is mostly used in the security layer.
	 * 
	 * @param user
	 *            to be checked. If null it is assumed as guest session
	 * @return true if the user matches the session user
	 */
	public boolean isLoggedInUser(String owner) {
		if (owner == null) return false;
		User user = User.getUser(owner);
		return isLoggedInUser(user);
	}

	/**
	 * Tries to do a login procedure. If currently a user is logged in a new
	 * {@link NoAccessException} will be thrown. If the login fails also a new
	 * {@link NoAccessException} will be thrown with a reason message. If the
	 * username or the password is null the login will fail, resulting in an
	 * {@link NoAccessException}.
	 * 
	 * In all cases before the login, a logout happens.
	 * 
	 * @param username
	 *            Name of the user needed for the login
	 * @param password
	 *            Password of the user
	 * @throws NoAccessException
	 *             Thrown as security exception when the login process fails.
	 */
	public synchronized void login(String username, String password) throws NoAccessException {
		doActivity();
		logout();

		if (username.length() == 0 || password.length() == 0) throw new NoAccessException("Login credentials missing");
		if (isLoggedIn()) throw new NoAccessException("The session is already logged in");

		if (!User.auth(username, password)) throw new NoAccessException("Invalid username or password");
		User user = User.getUser(username);
		if (user == null) throw new NoAccessException("Invalid username or password");

		this.user = user;
		refreshPolicy();
	}

	/**
	 * Logs the session out. If currently a guest session, nothing is done
	 */
	public synchronized void logout() {
		if (user == null) {
			// Just a safety call
			refreshPolicy();
			return;
		}

		user = null;
		refreshPolicy();
	}

	/**
	 * Adds an observer to the session. The given observer must not be null or
	 * already added, otherwise the method returns without any effect.
	 * 
	 * @param observer
	 *            to be added
	 */
	public void addObserver(ISessionObserver observer) {
		synchronized (observers) {
			if (observer == null) return;
			if (observers.contains(observer)) return;
			observers.add(observer);
		}
	}

	/**
	 * Removes a given observer from the session. The given observer must not be
	 * null and must be an observer, otherwise the method returns without any
	 * effect.
	 * 
	 * @param observer
	 *            to be removed
	 */
	public void removeObserver(ISessionObserver observer) {
		synchronized (observers) {
			if (observer == null) return;
			if (!observers.contains(observer)) return;
			observers.remove(observer);
		}
	}

	/**
	 * Calculates the time the current session was idle in milliseconds.
	 * 
	 * @return the time the session was idle in milliseconds
	 */
	public long getIdleTime() {
		return System.currentTimeMillis() - lastActivityTime;
	}

	/**
	 * Refreshes the access policy
	 */
	private void refreshPolicy() {
		policy = PrivilegeController.getAccessPolicty(this);
	}

	/**
	 * Refreshes the activity counter and revokes the session if inactive.
	 * 
	 * @throws IllegalStateException
	 *             Thrown if the session is deleted
	 */
	public void doActivity() {
		synchronized (state) {
			if (getState() == SessionState.deleted) throw new IllegalStateException();
			this.lastActivityTime = System.currentTimeMillis();
			if (this.state != SessionState.active) {
				refreshSessionState();
			}
		}
	}

	/**
	 * Refreshes the session state.
	 * 
	 * @throws IllegalStateException
	 *             Thrown if the session is deleted
	 */
	private void refreshSessionState() {
		synchronized (state) {
			if (getState() == SessionState.deleted) throw new IllegalStateException();
			long delay = System.currentTimeMillis() - this.lastActivityTime;
			if (delay > this.deleteDelay) {
				deleteSession();
			} else if (delay > this.inactiveDelay) {
				inactivateSession();
			} else if (getState() != SessionState.active) {
				reactivateSession();
			}
		}
	}

	/**
	 * Session is going to be deleted
	 */
	public void deleteSession() {
		synchronized (state) {
			this.state = SessionState.deleted;
		}
		synchronized (storedSessions) {
			storedSessions.remove(this);
		}
	}

	/**
	 * Deletes a session associated with a cookie. If the cookie is empty or
	 * null, nothing is done.
	 * 
	 * If no such session exists, the method returns without effect.
	 * 
	 * In all cases after this call the session associated with the given cookie
	 * does not exists anymore.
	 * 
	 * @param cookie
	 *            of the session to be deleted.
	 */
	public static void deleteSession(String cookie) {
		if (cookie == null || cookie.length() == 0) return;

		Session session = getSession(cookie);
		if (session == null) return;
		session.deleteSession();
	}

	/**
	 * Session is going to be inactive due to a big timeout
	 * 
	 * @throws IllegalStateException
	 *             Thrown if the session is deleted
	 */
	private void inactivateSession() {
		synchronized (state) {
			if (getState() == SessionState.deleted) throw new IllegalStateException();
			this.state = SessionState.inactive;
		}
	}

	/**
	 * A inactive session is going to be re-activated. If the session is deleted
	 * a new {@link IllegalStateException} is thrown. If the session state is
	 * not inactive the method returns without effect.
	 * 
	 * @throws IllegalStateException
	 *             Thrown if the session is deleted
	 */
	private void reactivateSession() {
		synchronized (state) {
			switch (getState()) {
			case deleted:
				throw new IllegalStateException();
			case active:
				return;
			}

			this.state = SessionState.active;
		}
	}

	/**
	 * Creates a new session with a new randomised session ID.
	 * 
	 * @return The new created session
	 */
	public static Session createNewSession() {
		String sid;
		Session session;

		synchronized (storedSessions) {
			int maxTries = storedSessions.size() * 1000;
			while (storedSessions.containsKey((sid = getRandomizedSID())))
				if (maxTries-- <= 0) throw new RuntimeException("Session creation failure");

			session = createNewSession(sid);
			// XXX: Maybe a new created session can have a reduced lifetime ...
			storedSessions.put(sid, session);
		}

		return session;
	}

	/**
	 * Creates a new randomised session ID value without checking, if already
	 * existing
	 * 
	 * @return creates a randomised SID value
	 */
	private static String getRandomizedSID() {
		StringBuffer result = new StringBuffer("SID.");
		int rnd = (int) (Math.random() * Integer.MAX_VALUE);
		result.append(rnd);
		rnd = (int) (Math.random() * 26);
		result.append((char) ('a' + rnd));

		return result.toString();
	}

	/**
	 * Gets the username of the currently logged in user, or "guest" if it is a
	 * guest session
	 * 
	 * @return the username or "guest" if a guest session
	 */
	public String getUsername() {
		if (!isLoggedIn()) return "guest";
		return user.getUsername();
	}

	/**
	 * @return the total number of registered users
	 */
	public static int getUserCount() {
		return User.totalCount();
	}

	/**
	 * @return the total number of snippets in the system
	 */
	public static int getSnippetCount() {
		return Snippet.totalCount();
	}

	/**
	 * @return the total number of categories in the system
	 */
	public static int getCategoryCount() {
		return Category.totalCount();
	}

	/**
	 * @return the number of currently active sessions
	 */
	public static int activeCount() {
		// TODO Implement me
		return storedSessions.size();
	}

	/**
	 * @return the number of currently active guest sessions
	 */
	public static int guestSessions() {
		int count = 0;
		List<Session> sessions = new ArrayList<Session>(storedSessions.values());
		for (Session session : sessions) {
			if (!session.isLoggedIn()) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 
	 * @return gets the session instance for a static guest session with no
	 *         cookie
	 */
	static Session getStaticGuestSession() {
		return staticGuestSession;
	}

	/**
	 * @return the {@link IAccessPolicy} of this session
	 */
	IAccessPolicy getPolicy() {
		return policy;
	}

	/**
	 * Reports a comment to a moderator. If the comment is null, nothing happens
	 * 
	 * @param comment
	 *            to be reported.
	 */
	static void report(Comment comment) {
		if (comment == null) return;
		// TODO Auto-generated method stub

	}

	/**
	 * Adds a favorited snippet to the session. If the snippet is null, nothing
	 * happes
	 * 
	 * If the session is logged in, then the snippet is added to the user's
	 * favorites.
	 * 
	 * If the session is a guest session, currently no action is done
	 * 
	 * @param snippet
	 *            To be dadded
	 */
	public void addFavorite(Snippet snippet) {
		if (snippet == null || !isLoggedIn()) return;
		User owner = getUser();
		if (owner == null) return;

		owner.addFavorite(snippet);

	}

	/**
	 * Removes a favorited snippet from the session. If the snippet is null,
	 * nothing happes
	 * 
	 * If the session is logged in, then the snippet is removed to the user's
	 * favorites.
	 * 
	 * If the session is a guest session, currently no action is done
	 * 
	 * @param snippet
	 *            To be removed
	 */
	public void removeFavorite(Snippet snippet) {
		if (snippet == null || !isLoggedIn()) return;
		User owner = getUser();
		if (owner == null) return;

		owner.removeFavorite(snippet);

	}

	/**
	 * Reposts a user with a given reason. If the reason is null, the method
	 * does nothing. If the given user is null, nothing is done.
	 * 
	 * @param user
	 *            to be reported.
	 * @param reason
	 *            of the report
	 */
	public static void report(User user, String reason) {
		if (user == null || reason == null) return;

		// TODO Implement me
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Session)) return false;
		Session session = (Session) obj;

		return session.cookie.equals(this.cookie);
	}

	@Override
	public int hashCode() {
		return cookie.hashCode();
	}

	public String getRealname() {
		if (!isLoggedIn()) return "realname guest";
		return user.getRealName();
	}

	public String getMail() {
		if (!isLoggedIn()) return "mail guest";
		return user.getEmail();
	}

	/**
	 * Gets a list with search suggestions for this session
	 * 
	 * @return the list of search suggestions
	 */
	public synchronized List<String> getSearchSuggestions(int max) {
		List<String> result = new ArrayList<String>(lastSearchStrings);

		int remaining = max - result.size();
		if (remaining > 0) result.addAll(Search.Stats.getPopularSearchStrings(remaining));
		else
			while (remaining++ < 0)
				result.remove(0);

		return result;
	}

	/**
	 * Adds a search string to the last search strings, used for
	 * {@link Session#getSearchSuggestions()}
	 * 
	 * @param searchString
	 *            to be added. If null or empty it is ignored
	 */
	synchronized void addSearchString(String searchString) {
		if (searchString == null || searchString.isEmpty()) return;

		// Crop list if necessary
		while (lastSearchStrings.size() >= maxStoredSearchStrings)
			lastSearchStrings.remove(0);

		lastSearchStrings.add(searchString);
	}

	/**
	 * Sets the maximum number of last stored search strings
	 * 
	 * @param count
	 *            number to be set. if zero or les it is ignored
	 */
	synchronized void setMaxNumberOfLastSearchStrings(int count) {
		if (count <= 0) return;
		maxStoredSearchStrings = count;
	}
}

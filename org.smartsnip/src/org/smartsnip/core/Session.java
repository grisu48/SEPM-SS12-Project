package org.smartsnip.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.smartsnip.core.Code.UnsupportedLanguageException;
import org.smartsnip.security.IAccessPolicy;
import org.smartsnip.security.PrivilegeController;
import org.smartsnip.shared.ICategory;
import org.smartsnip.shared.IComment;
import org.smartsnip.shared.INotification;
import org.smartsnip.shared.ISessionObserver;
import org.smartsnip.shared.ISnippet;
import org.smartsnip.shared.IUser;

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

		@Override
		public void notification(Session source, Notification notification) {
			if (notification == null)
				return;

			for (ISessionObserver observer : observers) {
				observer.notification(Session.this, notification);
			}
		}

		@Override
		public void logout(Session source) {
			for (ISessionObserver observer : observers) {
				observer.logout(Session.this);
			}
		}

		@Override
		public void login(Session source, String username) {
			for (ISessionObserver observer : observers) {
				observer.login(Session.this, username);
			}
		}
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
		Persistence.initialize();
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
	 * If the cookie is null or empty, null will be returned.
	 * 
	 * @param cookie
	 *            Used for session identification
	 * @return Session identified by the cookie
	 */
	public synchronized static Session getSession(String cookie) {
		if (cookie == null || cookie.length() == 0)
			return null;

		Session result = storedSessions.get(cookie);
		if (result == null) {
			result = createNewSession(cookie);
		} else if (result.isDead()) {
			storedSessions.remove(cookie);
			result = null;
		}

		return result;
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
	public synchronized static boolean existsCookie(String cookie) {
		if (cookie == null || cookie.length() == 0)
			return false;
		Session result = storedSessions.get(cookie);
		if (result == null)
			return false;
		if (result.isDead()) {
			storedSessions.remove(cookie);
			return false;
		}
		return true;
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
		if (user == null)
			return this.user == null;
		return user == this.user;
	}

	/**
	 * Tries to do a login procedure. If currently a user is logged in a new
	 * {@link IllegalAccessException} will be thrown. If the login fails also a
	 * new {@link IllegalAccessException} will be thrown with a reason message.
	 * If the username or the password is null the login will fail, resulting in
	 * an {@link IllegalAccessException}.
	 * 
	 * In all cases before the login, a logout happens.
	 * 
	 * @param username
	 *            Name of the user needed for the login
	 * @param password
	 *            Password of the user
	 * @throws IllegalAccessException
	 *             Thrown as security exception when the login process fails.
	 */
	public synchronized void login(String username, String password) throws IllegalAccessException {
		doActivity();
		logout();

		if (username.length() == 0 || password.length() == 0)
			throw new IllegalAccessException("Login credentials missing");
		if (isLoggedIn())
			throw new IllegalAccessException("The session is already logged in");

		if (!User.auth(username, password))
			throw new IllegalAccessException("Invalid username or password");
		User user = User.getUser(username);
		if (user == null)
			throw new IllegalAccessException("Invalid username or password");

		this.user = user;
		refreshPolicy();
		observable.login(this, user.getUsername());
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
		if (username == null || username.length() == 0)
			return null;

		return createIUser(User.getUser(username));
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
		observable.logout(this);
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
			if (observer == null)
				return;
			if (observers.contains(observer))
				return;
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
			if (observer == null)
				return;
			if (!observers.contains(observer))
				return;
			observers.remove(observer);
		}
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
		if (user == null)
			return new ArrayList<ISnippet>();
		List<Snippet> snippets = user.getMySnippets();
		List<ISnippet> result = new ArrayList<ISnippet>(snippets.size());

		for (final Snippet snippet : snippets) {
			result.add(createISnippet(snippet));
		}

		return result;
	}

	/**
	 * Returns a new interface for the session to manipulate the user data. If
	 * currently no user has been logged in, null is returned
	 * 
	 * @return interface for the sesssion to manipulate the user date of the
	 *         logged in user.
	 */
	private IUser createIUser(final User user) {
		if (user == null)
			return null;

		return new IUser() {

			/** Cached list of favourite snippets */
			final List<ISnippet> emptyList = new ArrayList<ISnippet>();
			List<ISnippet> favourites = emptyList;

			@Override
			public void logout() throws IllegalAccessException {
				if (user != Session.this.user)
					throw new IllegalAccessException();
				logout();
			}

			@Override
			public List<ISnippet> getSnippets() throws IllegalAccessException {
				if (user != Session.this.user)
					throw new IllegalAccessException();
				return getUserSnippets(user);
			}

			@Override
			public String getName() {
				return user.username;
			}

			@Override
			public List<ISnippet> getFavorites() throws IllegalAccessException {
				if (!isLoggedIn()) {
					if (user != null)
						throw new IllegalAccessException("Cannot get favorites from foreign user");

					// TODO Implement guest user session favorites
					return new ArrayList<ISnippet>();
				} else {
					if (!isLoggedInUser(user))
						throw new IllegalAccessException("Cannot get favorites from foreign user");

					synchronized (favourites) {
						if (favourites == emptyList) {
							createFavourites();
						}

						return favourites;
					}
				}
			}

			/**
			 * Creates the favourite list of snippets
			 */
			private void createFavourites() {
				synchronized (favourites) {
					List<Snippet> snippets = user.getFavoriteSnippets();
					favourites = new ArrayList<ISnippet>();

					for (Snippet snippet : snippets) {
						favourites.add(createISnippet(snippet));
					}
				}

			}

			@Override
			public String getEmail() throws IllegalAccessException {
				if (!isLoggedIn())
					throw new IllegalAccessException();

				return user.getEmail();
			}

			@Override
			public String getRealName() throws IllegalAccessException {
				if (!isLoggedIn())
					throw new IllegalAccessException();

				return user.getRealName();
			}

			@Override
			public void setEmail(String newAddress) throws IllegalAccessException, IllegalArgumentException {
				if (!policy.canEditUserData(Session.this, user))
					throw new IllegalAccessException();

				user.setEmail(newAddress);
			}

			@Override
			public void setRealName(String newName) throws IllegalAccessException {
				if (!policy.canEditUserData(Session.this, user))
					throw new IllegalAccessException();

				user.setRealName(newName);
			}

			@Override
			public void report(String reason) {
				// TODO Auto-generated method stub

			}
		};
	}

	/**
	 * Creates a comment interface for the GUI for this session. If the given
	 * comment is null, null is also the result.
	 * 
	 * @param comment
	 *            entity where the interface belongs to
	 * @return interface providing access from the GUI or null if the given
	 *         comment is null
	 */
	private IComment createIComment(final Comment comment) {
		if (comment == null)
			return null;

		return new IComment() {

			// Just for reasons of readability
			private final Session session = Session.this;

			@Override
			public void removeRating() throws IllegalAccessException {
				if (!policy.canRateSnippet(session, comment.snippet))
					throw new IllegalAccessException();

				comment.unvote(getUser());
			}

			@Override
			public void ratePositive() throws IllegalAccessException {
				if (!policy.canRateSnippet(session, comment.snippet))
					throw new IllegalAccessException();

				comment.votePositive(getUser());
			}

			@Override
			public void rateNegative() throws IllegalAccessException {
				if (!policy.canRateSnippet(session, comment.snippet))
					throw new IllegalAccessException();

				comment.voteNegative(getUser());
			}

			@Override
			public IUser getOwner() throws IllegalAccessException {
				return createIUser(comment.owner);
			}

			@Override
			public void delete() throws IllegalAccessException {
				if (!policy.canEditComment(session, comment))
					throw new IllegalAccessException();

				comment.delete();
			}

			@Override
			public void edit(String newComment) throws IllegalAccessException {
				if (newComment == null || newComment.isEmpty())
					return;
				if (!policy.canEditComment(session, comment))
					throw new IllegalAccessException();

				comment.edit(newComment);
			}

			@Override
			public void report() throws IllegalAccessException {
				// TODO Auto-generated method stub

			}

			@Override
			public String getMessage() {
				return comment.getMessage();
			}

			@Override
			public Date getLastModificationTime() {
				return comment.getTime();
			}
		};
	}

	/**
	 * Creates a snippet interface for the GUI for this session. If the given
	 * snippet is null, null is also the result.
	 * 
	 * @param snippet
	 *            entity where the interface belongs to
	 * @return interface providing access from the GUI or null if the given
	 *         snippet is null
	 */
	private ISnippet createISnippet(final Snippet snippet) {
		return new ISnippet() {

			@Override
			public void removeTag(String tag) throws IllegalAccessException {
				if (!policy.canTagSnippet(Session.this, snippet))
					throw new IllegalAccessException();

				Tag objTag = Tag.getTag(tag);
				if (objTag == null)
					return;
				snippet.removeTag(objTag);
			}

			@Override
			public void increaseViewCounter() throws IllegalAccessException {
				snippet.increaseViewCounter();
			}

			@Override
			public int getViewCount() {
				return snippet.getViewcount();
			}

			@Override
			public List<String> getTags() {
				// TODO Auto-generated method stub
				return new ArrayList<String>();
			}

			@Override
			public IUser getOwner() {
				return createIUser(snippet.owner);
			}

			@Override
			public String getName() {
				return snippet.getName();
			}

			@Override
			public String getLanguage() {
				return snippet.getCode().getLanguage();
			}

			@Override
			public int getHash() {
				return snippet.hash;
			}

			@Override
			public String getDesc() {
				return snippet.getDescription();
			}

			@Override
			public List<IComment> getComments() throws IllegalAccessException {
				// TODO Auto-generated method stub
				return new ArrayList<IComment>();
			}

			@Override
			public String getCodeHTML() {
				return snippet.getCode().getFormattedHTML();
			}

			@Override
			public String getCategory() {
				Category category = snippet.getCategory();
				if (category == null)
					return "";
				return snippet.getCategory().getName();
			}

			@Override
			public void addTag(String tag) throws IllegalAccessException {
				snippet.addTag(Tag.createTag(tag));
			}

			@Override
			public IComment addComment(String comment) throws IllegalAccessException {
				if (comment == null || comment.isEmpty())
					return null;
				if (!policy.canComment(Session.this))
					throw new IllegalAccessException();

				Comment objComment = Comment.createComment(user, snippet, comment);
				snippet.addComment(objComment);

				// TODO Auto-generated method stub
				return createIComment(objComment);
			}

			@Override
			public void addFavorite() {
				if (Session.this.user == null)
					// TODO Add support for guest user favorites
					return;

				Session.this.user.addFavorite(snippet);
			}

			@Override
			public void removeFavorite() {
				if (Session.this.user == null)
					// TODO Add support for guest user favorites
					return;

				Session.this.user.removeFavorite(snippet);
			}

			@Override
			public void delete() throws IllegalAccessException {
				if (!policy.canDeleteSnippet(Session.this, snippet))
					throw new IllegalAccessException();

				snippet.delete();
			}
		};
	}

	/**
	 * Creates a notification interface for the GUI for this session. If the
	 * given notification is null, null is also the result.
	 * 
	 * This FactoryMethod is a bit different from the others: The interface does
	 * not include security check mechanisms. If the currenly logged in user is
	 * not the same as the notification's owner, the result will be null.
	 * 
	 * @param notification
	 *            entity where the interface belongs to
	 * @return interface providing access from the GUI or null if the given
	 *         notification is null
	 */
	private INotification createINotification(final Notification notification) {
		if (notification == null)
			return null;
		if (!isLoggedIn())
			return null;
		if (user != notification.getOwner())
			return null;

		return new INotification() {

			@Override
			public void markUnread() {
				notification.markUnread();
			}

			@Override
			public void markRead() {
				notification.markRead();
			}

			@Override
			public boolean isRead() {
				return notification.isRead();
			}

			@Override
			public String getTime() {
				return notification.getTime();
			}

			@Override
			public String getSource() {
				return notification.getSource();
			}

			@Override
			public String getMessage() {
				return notification.getMessage();
			}

			@Override
			public void delete() {
				// Here the check takes place, because this call is criitcal to
				// user data
				if (notification.getOwner() != user)
					return;

				// TODO Implement me
			}
		};
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
			if (getState() == SessionState.deleted)
				throw new IllegalStateException();
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
			if (getState() == SessionState.deleted)
				throw new IllegalStateException();
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
		if (cookie == null || cookie.length() == 0)
			return;

		Session session = getSession(cookie);
		if (session == null)
			return;
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
			if (getState() == SessionState.deleted)
				throw new IllegalStateException();
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
				if (maxTries-- <= 0)
					throw new RuntimeException("Session creation failure");

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
	 * Creates the IUser interface for the currently logged in user, or null, if
	 * it is a guest session
	 * 
	 * @return the IUser interface for the session user
	 */
	public IUser getIUser() {
		if (!isLoggedIn())
			return null;
		try {
			return getIUser(user.getUsername());
		} catch (IllegalAccessException e) {
			// Access denied
			return null;
		}
	}

	/**
	 * Gets the username of the currently logged in user, or "guest" if it is a
	 * guest session
	 * 
	 * @return the username or "guest" if a guest session
	 */
	public String getUsername() {
		if (!isLoggedIn())
			return "guest";
		return user.getUsername();
	}

	/**
	 * Gets a concrete category interface out of a category with a name. Returns
	 * null if no such category exists, or if the input string is null or empty.
	 * 
	 * @param name
	 *            Name of the category to be searched for
	 * @return An interface for the category or null, if no such category has
	 *         been found
	 */
	public ICategory getCategory(String name) {
		if (name == null || name.isEmpty())
			return null;

		Category category = Category.getCategory(name);
		if (category == null)
			return null;

		return createICategory(category);
	}

	/**
	 * Creates an interface for the GUI to handle a category
	 * 
	 * @param category
	 *            the interface should be created for
	 * @return the created interface
	 */
	private ICategory createICategory(final Category category) {
		if (category == null)
			return null;

		return new ICategory() {

			List<ISnippet> snippets = null;

			@Override
			public void setName(String name) throws IllegalAccessException {
				if (name == null || name.isEmpty())
					return;
				if (!policy.canEditCategory(Session.this, category))
					throw new IllegalAccessException();

				category.setName(name);
			}

			@Override
			public void setDescription(String desc) throws IllegalAccessException {
				if (desc == null || desc.isEmpty())
					return;
				if (!policy.canEditCategory(Session.this, category))
					throw new IllegalAccessException();

				// TODO Auto-generated method stub

			}

			@Override
			public List<Pair<Integer, String>> getSnippets() {
				List<Pair<Integer, String>> result = new ArrayList<Pair<Integer, String>>();
				for (Snippet snippet : category.getSnippets()) {
					result.add(new Pair<Integer, String>(snippet.hash, snippet.getName()));
				}
				return result;
			}

			@Override
			public List<ISnippet> getISnippets() {
				if (snippets == null) {
					for (Snippet snippet : category.getSnippets()) {
						snippets.add(createISnippet(snippet));
					}
				}
				return snippets;
			}

			@Override
			public String getName() {
				return category.getName();
			}

			@Override
			public String getDescription() {
				return category.getDescription();
			}

			@Override
			public void addSnippet(String name, String description, String code, String language)
					throws IllegalArgumentException, IllegalAccessException {
				if (policy.canCreateSnippet(Session.this, category))
					throw new IllegalAccessException();
				/* This security call is hard-coded and remains as-it-is! */
				if (user == null)
					throw new IllegalAccessException();

				try {
					// IllegalArgumentExceptions are created in this call
					Snippet snippet = Snippet.createSnippet(user, name, description, code, language);
					category.addSnippet(snippet);
				} catch (UnsupportedLanguageException e) {
					throw new IllegalArgumentException("Language not supported: " + language, e);
				}
			}
		};

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
		return 0;
	}

	/**
	 * @return the number of currently active guest sessions
	 */
	public static int guestSessions() {
		// TODO Implement me
		return 0;
	}

	/**
	 * Creates an interface to a snippet. If the given hash was not found, null
	 * is returned
	 * 
	 * @param hash
	 *            of the snippet
	 * @return the interface to access the snippet, or null, if not such snippet
	 *         exists
	 */
	public synchronized ISnippet getISnippet(int hash) {
		Snippet snippet = Snippet.getSnippet(hash);
		if (snippet == null)
			return null;

		return createISnippet(snippet);
	}
}

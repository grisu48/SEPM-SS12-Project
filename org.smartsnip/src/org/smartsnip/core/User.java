package org.smartsnip.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.smartsnip.persistence.IPersistence;
import org.smartsnip.security.IHash;
import org.smartsnip.security.MD5;

// NOTE: TODO: Implement the notification system

/**
 * This is the user of the system. TODO: Write me
 * 
 * The hash code for each user is given by the hash code of the unique user name
 * 
 */
public class User {

	/** This hash algorithm is used for password hashing */
	private final static IHash hashAlgorithm = MD5.getInstance();

	/** Final identifier of the username */
	public final String username;

	/** Real name of the user */
	private String realName = "";

	/** Encrypted password of the user */
	private String password = "";

	/** Users email address */
	private String email = "";

	/** State of the user */
	private UserState state = UserState.unvalidated;

	public synchronized void setState(UserState state) {
		this.state = state;
	}

	/**
	 * List of favourite snippets of the user
	 */
	private final List<Snippet> favorites;

	/**
	 * Determines the status of the user, currently if the user has been
	 * validated or not
	 * 
	 */
	public enum UserState {
		unvalidated, validated
	}

	/**
	 * Constructor for the DB
	 * 
	 * Generates a new user with the given parameters. All arguments except
	 * realName and favorites must not be null or empty, otherwise a new
	 * {@link IllegalArgumentException} is thrown.
	 * 
	 * The password must alreadby be encrypted. Here the password is stored as
	 * given, in all other cases the password is first encrypted, and then
	 * stored in the system.
	 * 
	 * @param username
	 *            of the new user
	 * @param realName
	 *            the real name of the user. Can be null or empty.
	 * @param email
	 *            of the new user
	 * @param password
	 *            encrypted password for the user.
	 * @param favorites
	 *            Favorited snippets of the user. If null, a new list is created
	 * 
	 * @throws IllegalArgumentException
	 *             Thrown if one of the arguments is null or empty
	 */
	User(String username, String realName, String email, UserState state, List<Snippet> favorites) {
		if (username == null || username.isEmpty())
			throw new IllegalArgumentException("Cannot create user with empty username");
		if (realName == null) {
			realName = "";
		}
		if (email == null || email.isEmpty())
			throw new IllegalArgumentException("Cannot create user with empty email");
		if (favorites == null) {
			favorites = new ArrayList<Snippet>();
		}

		this.username = username.toLowerCase();
		this.realName = realName;
		this.email = email;
		this.state = state;

		this.favorites = favorites;

		/* MUST be the last thing to check */
		// XXX no password check at this place!
//		try {
//			this.password = Persistence.instance.getPassword(this);
//		} catch (IOException e) {
//			this.password = null;
//		}
	}

	/**
	 * Generates a new user with the given parameters. Here no more checks are
	 * done. They are ment to be done in the createNewUser-method The given
	 * password will be encrypted here.
	 * 
	 * @param username
	 *            of the new user
	 * @param email
	 *            of the new user
	 * @param password
	 *            cleartext password for the user. Password will be encrypted
	 * @param realName
	 *            Real name of the user
	 */
	private User(String username, String email, String password, String realName) {
		this.username = username.toLowerCase();
		this.email = email;
		this.password = hashAlgorithm.hash(password);
		this.realName = realName;
		this.favorites = new ArrayList<Snippet>();
	}

	/**
	 * Searches for a user in the system and returns the user if found. If not
	 * user can be found, the result is null. The search for the username is NOT
	 * case sensitive.
	 * 
	 * @param username
	 *            Name of the user that should be searched
	 * @return found user with the given username or null if not found
	 */
	public synchronized static User getUser(String username) {
		if (username.length() == 0)
			return null;
		username = username.toLowerCase();

		try {
			return Persistence.instance.getUser(username);
		} catch (IOException e) {
			System.err.println("IOException during getting User \"" + username + "\":" + e.getMessage());
			e.printStackTrace(System.err);
			return null;
		}
	}

	/**
	 * Checks if the given username exists in the system. If the username is
	 * empty, false will be returned
	 * 
	 * @param username
	 *            to be checked
	 * @return true if existing otherwise false
	 */
	public synchronized static boolean exists(String username) {
		return getUser(username) != null;
	}

	/**
	 * Tries to create a new user in the system with the given username and
	 * email. If the username is already used a new
	 * {@link IllegalArgumentException} will be thrown. If the email-address is
	 * invalid a new {@link IllegalArgumentException} will be thrown
	 * 
	 * The real name of the new user is set to be the password
	 * 
	 * @param username
	 *            for the new user. Will be lower-cased
	 * @param email
	 *            for the user. Must be valid according to the
	 *            isValidEmailAddress-Method
	 * @param realname
	 *            The displayed real name of the user. If null or empty it will
	 *            be replaced with the username
	 * @throws IllegalArgumentException
	 *             Thrown if an Argument is invalid. This is the case if one of
	 *             the strings is empty, the username is already taken or if the
	 *             email-address is invalid
	 */
	public static synchronized User createNewUser(String username, String password, String email)
			throws IllegalArgumentException {
		return createNewUser(username, password, email, username);
	}

	public static synchronized User createNewUser(String username, String password, String email, String realname)
			throws IllegalArgumentException {
		if (username.length() == 0)
			throw new IllegalArgumentException("Username cannot be empty");
		if (password.isEmpty())
			throw new IllegalArgumentException("Password cannot be empty");
		if (email.length() == 0)
			throw new IllegalArgumentException("e-mail address cannot be empty");
		if (!isValidEmailAddress(email))
			throw new IllegalArgumentException("Illegal email address");
		// Check for duplicated user entries
		username = username.toLowerCase();
		if (exists(username))
			throw new IllegalArgumentException("Username already taken");
		if (realname == null || realname.isEmpty()) {
			realname = username;
		}

		// All test passed. Create new user
		User newUser = new User(username, email, password, realname);
		addToDB(newUser);
		return newUser;
	}

	/**
	 * Removes a user from the system. If the given user cannot be found,
	 * nothing is done
	 * 
	 * @param username
	 *            name of the user to be deleted
	 */
	public synchronized static void deleteUser(String username) {
		deleteUser(getUser(username));
	}

	/**
	 * Removes a user from the system. If the given user is null, nothing
	 * happens
	 * 
	 * @param user
	 *            that should be deleted.
	 */
	public synchronized static void deleteUser(User user) {
		if (user == null)
			return;

		try {
			Persistence.instance.removeUser(user, IPersistence.DB_DEFAULT);
		} catch (IOException e) {
			System.err.println("IOException during deleteUser(" + user.username + "): " + e.getMessage());
			e.printStackTrace(System.err);
		}
	}

	/**
	 * Internal call to check if the email address is valid
	 * 
	 * @param email
	 *            to be checked
	 * @return true if valid otherwise false
	 */
	private static boolean isValidEmailAddress(String email) {
		if (email.length() == 0)
			return false;
		int atSign = email.indexOf('@');
		if (atSign < 1)
			return false;
		if (atSign >= email.length())
			return false;
		return true;
	}

	/**
	 * Matches the given password with the user password. Always use this method
	 * Instant of manual equivalence checking, because the password uses a
	 * security hash code.
	 * 
	 * @param password
	 *            to be checked
	 * @return true if the password check is positive, false if a password
	 *         denial happens
	 */
	public boolean checkPassword(String password) {
		password = hashAlgorithm.hash(password);

		if (this.password != null)
			return this.password.equals(password);

		try {
			return Persistence.instance.verifyPassword(this, password);
		} catch (IOException e) {
			System.err.println("IOException during checking password for user \"" + username + "\": " + e.getMessage());
			e.printStackTrace(System.err);
			return false;
		}

	}

	/**
	 * @return the real name of the user
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * @return the email address of the user
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Set a new email-address for the user. The email-address must be valid,
	 * otherwise a new {@link IllegalArgumentException} will be thrown
	 * 
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) throws IllegalArgumentException {
		if (email.length() == 0)
			throw new IllegalArgumentException("Empty email address not allowed");
		if (!isValidEmailAddress(email))
			throw new IllegalArgumentException("Illegal email-address");

		if (this.email.equals(email))
			return;
		this.email = email;
		refreshDB();
	}

	/**
	 * 
	 * Sets the real name of the user. If the new name is null or empty, or the
	 * name does not changes, the method returns without any effect.
	 * 
	 * @param name
	 *            new real name of the user
	 */
	public void setRealName(String name) {
		if (name == null || name.isEmpty())
			return;
		if (this.realName.equalsIgnoreCase(name))
			return;
		this.realName = name;
		refreshDB();
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the total count of registered users in the system
	 */
	public static int totalCount() {
		try {
			return Persistence.instance.getUserCount();
		} catch (IOException e) {
			System.err.println("IOException during getUserCount(): " + e.getMessage());
			e.printStackTrace(System.err);
			return -1;
		}
	}

	/**
	 * Sets the user password. Password must not be empty, or a new
	 * {@link IllegalArgumentException} will be thrown
	 * 
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) throws IllegalArgumentException {
		if (password.length() == 0)
			throw new IllegalArgumentException("Empty password not allowed");
		password = hashAlgorithm.hash(password);
		if (this.password.equals(password))
			return;

		this.password = password;
		refreshDB();
	}

	/**
	 * Tries to authorise a user login. If the login succeeds, true is resulted.
	 * If the username or the password does not exists/match false is returned
	 * without a reason.
	 * 
	 * @param username
	 *            to be user
	 * @param password
	 *            for that user
	 * @return true if the login is successful, otherwise false (without reason
	 *         message)
	 */
	public static boolean auth(String username, String password) {
		User user = getUser(username);
		if (user == null)
			return false;
		return user.checkPassword(password);
	}

	@Override
	public String toString() {
		return username;
	}

	/**
	 * @return a list of the snippets created by the user
	 */
	public List<Snippet> getMySnippets() {
		// TODO: Implement me
		return new ArrayList<Snippet>();
	}

	/**
	 * @return a list of the hash codes of the snippets, created by the user
	 */
	List<Integer> getMySnippetsHash() {
		// TODO: Implement me
		return new ArrayList<Integer>();
	}

	/**
	 * @return a list of the users' favourite snippets
	 */
	public List<Snippet> getFavoriteSnippets() {
		/* Copy list */
		List<Snippet> result = new ArrayList<Snippet>(favorites.size());
		for (Snippet snippet : favorites) {
			result.add(snippet);
		}
		return result;

	}

	/**
	 * @return a list of the hash codes of the users' favourite snippets
	 */
	List<Long> getFavoriteSnippetsHash() {
		List<Long> result = new ArrayList<Long>(favorites.size());
		for (Snippet snippet : favorites) {
			result.add(snippet.id);
		}
		return result;

	}

	/**
	 * Deletes this user from the database
	 */
	public synchronized void delete() {
		deleteUser(this);
	}

	/**
	 * Invokes the refreshing process for the database
	 */
	protected void refreshDB() {
		try {
			Persistence.instance.writeUser(this, IPersistence.DB_DEFAULT);
		} catch (IOException e) {
			System.err.println("IOException writing out user \"" + username + "\": " + e.getMessage());
			e.printStackTrace(System.err);
		}
	}

	/**
	 * Adds a snippet to the user's favourites. If the snippet is null, nothing
	 * happens. If the snippet as already been added, nothing happens.
	 * 
	 * @param snippet
	 *            to be added
	 */
	public void addFavorite(Snippet snippet) {
		if (snippet == null)
			return;

		if (favorites.contains(snippet))
			return;
		favorites.add(snippet);
		refreshDB();
	}

	/**
	 * Removes a snippet from the user's favourites. If the snippet is null,
	 * nothing happens. If the snippet is not in the user's favorite list,
	 * nothing happens
	 * 
	 * @param snippet
	 */
	public void removeFavorite(Snippet snippet) {
		if (snippet == null)
			return;

		if (!favorites.contains(snippet))
			return;
		favorites.remove(snippet);
		refreshDB();
	}

	/**
	 * @return the current user state
	 */
	public UserState getState() {
		return state;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof User))
			return false;

		User user = (User) obj;
		return user.username.equals(this.username);
	}

	@Override
	public int hashCode() {
		return username.hashCode();
	}

	/**
	 * Writes a user out to the database. If the user is null, nothing happens
	 * 
	 * The database is told not to overwrite an existing user.
	 * 
	 * @param user
	 *            to be written out
	 */
	protected static synchronized void addToDB(User user) {
		if (user == null)
			return;

		try {
			Persistence.instance.writeUser(user, IPersistence.DB_NEW_ONLY);
		} catch (IOException e) {
			System.err
					.println("IOException during adding user \"" + user.getUsername() + "\" to db: " + e.getMessage());
			e.printStackTrace(System.err);
		}
	}

	/**
	 * Trims the username and lowercases him
	 * 
	 * @param username
	 *            to be trimmed and lowercased
	 * @return the trimmed and lowercased username
	 */
	public static String trimUsername(String username) {
		if (username == null)
			return null;
		return username.trim().toLowerCase();
	}
}

package org.smartsnip.core;

import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;

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
	/**
	 * Container for all users of the system. The key is the username for all
	 * users, stored in lower case
	 */
	private final static HashMap<String, User> allUsers = new HashMap<String, User>();

	/** This hash algorithm is used for password hashing */
	private final static IHash hashAlgorithm = MD5.getInstance();

	/** Final identifier of the username */
	public final String username;

	/** Encrypted password of the user */
	private String password = "";

	/** Users email address */
	private String email = "";

	/** State of the user */
	private UserState state = UserState.unvalidated;

	/**
	 * List of favourite snippets of the user
	 */
	private List<Snippet> favorites = new ArrayList<Snippet>();

	/**
	 * Determines the status of the user, currently if the user has been
	 * validated or not
	 * 
	 */
	enum UserState {
		unvalidated, validated
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
	 */
	private User(String username, String email, String password) {
		this.username = username.toLowerCase();
		this.email = email;
		this.password = hashAlgorithm.hash(password);
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
	synchronized static User getUser(String username) {
		if (username.length() == 0) return null;
		username = username.toLowerCase();

		return allUsers.get(username);
	}

	/**
	 * Checks if the given username exists in the system. If the username is
	 * empty, false will be returned
	 * 
	 * @param username
	 *            to be checked
	 * @return true if existing otherwise false
	 */
	synchronized static boolean exists(String username) {
		if (username.length() == 0) return false;
		username = username.toLowerCase();
		return allUsers.containsKey(username);
	}

	/**
	 * Tries to create a new user in the system with the given username and
	 * email. If the username is already used a new
	 * {@link IllegalArgumentException} will be thrown. If the email-address is
	 * invalid a new {@link IllegalArgumentException} will be thrown
	 * 
	 * @param username
	 *            for the new user. Will be lower-cased
	 * @param email
	 *            for the user. Must be valid according to the
	 *            isValidEmailAddress-Method
	 * @throws IllegalArgumentException
	 *             Thrown if an Argument is invalid. This is the case if one of
	 *             the strings is empty, the username is already taken or if the
	 *             email-address is invalid
	 */
	synchronized static User createNewUser(String username, String password, String email)
			throws IllegalArgumentException {
		if (username.length() == 0) throw new IllegalArgumentException("Username cannot be empty");
		if (email.length() == 0) throw new IllegalArgumentException("e-mail address cannot be empty");
		if (!isValidEmailAddress(email)) throw new IllegalArgumentException("Illegal email address");
		// Check for duplicated user entries
		username = username.toLowerCase();
		if (exists(username)) throw new IllegalArgumentException("Username already taken");

		// All test passed. Create new user
		User newUser = new User(username, email, password);
		allUsers.put(username, newUser);
		return newUser;
	}

	/**
	 * Internal call to check if the email address is valid
	 * 
	 * @param email
	 *            to be checked
	 * @return true if valid otherwise false
	 */
	private static boolean isValidEmailAddress(String email) {
		if (email.length() == 0) return false;
		int atSign = email.indexOf('@');
		if (atSign < 1) return false;
		if (atSign >= email.length()) return false;
		return true;
	}

	/**
	 * Matches the given password with the user password. Always use this method
	 * Instant of manual equivalence checking, because the password uses a
	 * security hash code.
	 * 
	 * @param password
	 *            to be checked
	 * @return
	 */
	private boolean checkPassword(String password) {
		password = hashAlgorithm.hash(password);
		return this.password.equals(password);
	}

	/**
	 * @return the email address of the user
	 */
	String getEmail() {
		return email;
	}

	/**
	 * Set a new email-address for the user. The email-address must be valid,
	 * otherwise a new {@link IllegalArgumentException} will be thrown
	 * 
	 * @param email
	 *            the email to set
	 */
	void setEmail(String email) throws IllegalArgumentException {
		if (email.length() == 0) throw new IllegalArgumentException("Empty email address not allowed");
		if (!isValidEmailAddress(email)) throw new IllegalArgumentException("Illegal email-address");

		if (this.email.equals(email)) return;
		this.email = email;
		refreshDB();
	}

	/**
	 * @return the username
	 */
	String getUsername() {
		return username;
	}

	/**
	 * Sets the user password. Password must not be empty, or a new
	 * {@link IllegalArgumentException} will be thrown
	 * 
	 * @param password
	 *            the password to set
	 */
	void setPassword(String password) throws IllegalArgumentException {
		if (password.length() == 0) throw new IllegalArgumentException("Empty password not allowed");
		password = hashAlgorithm.hash(password);
		if (this.password.equals(password)) return;

		this.password = password;
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
	 * @return true if the login is successfull, otherwise false (without reason
	 *         message)
	 */
	static boolean auth(String username, String password) {
		User user = getUser(username);
		if (user == null) return false;
		return user.checkPassword(password);
	}

	@Override
	public int hashCode() {
		return username.hashCode();
	}

	@Override
	public String toString() {
		return username;
	}

	/**
	 * Invokes the refreshing process for the database
	 */
	protected void refreshDB() {

	}

	/**
	 * @return a list of the snippets created by the user
	 */
	List<Snippet> getMySnippets() {
		return new ArrayList<Snippet>();
	}
}

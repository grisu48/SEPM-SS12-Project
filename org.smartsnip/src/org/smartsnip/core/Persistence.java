package org.smartsnip.core;

/**
 * This class is used to create some test objects in the application layer to
 * test the implementation of the GUI.
 * 
 * It becomes obsolete, when the persistence layer is ready to use
 * 
 */
public class Persistence {

	/**
	 * Static constructor used to create some hard-coded test objects
	 */
	static {
		User user1 = User.createNewUser("javajoe", "test", "joe@java.com");
		User user2 = User.createNewUser("rubyrupert", "test", "rupert@spam.com");
		User user3 = User.createNewUser("misterX", "test", "misterX@hidemyass.com");

	}

	/**
	 * Initialises the persistence layer
	 */
	public static void initialize() {
		// Empty. All initialisation process is done in the static constructor
	}
}

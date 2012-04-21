package org.smartsnip.core;

/**
 * This class is used to create some test objects in the application layer to
 * test the implementation of the GUI.
 * 
 * It becomes obsolete, when the persistence layer is ready to use
 * 
 */
public class Persistence {

	public final static String testuser1 = "javajoe";
	public final static String testuser2 = "rubyrupert";
	public final static String testuser3 = "misterX";

	public final static String password1 = "test";
	public final static String password2 = "Test";
	public final static String password3 = "TEST";

	public final static String testCategory1 = "Main";
	public final static String testCategory2 = "Network";
	public final static String testCategory3 = "P2P";

	public static Snippet testSnippet = null;
	public static Comment testComment = null;

	private static boolean testEnvironment = false;

	/**
	 * Static constructor used to create some hard-coded test objects
	 */
	static {
		createTestEnvironment();
	}

	/**
	 * Initialises the persistence layer
	 */
	public static void initialize() {
		// Empty. All initialisation process is done in the static constructor
	}

	/**
	 * This method creates the test environment
	 */
	public synchronized static void createTestEnvironment() {
		if (testEnvironment) return;

		User user1 = User.createNewUser(testuser1, password1, "joe@java.com");
		User user2 = User.createNewUser(testuser2, password2, "rupert@spam.com");
		User user3 = User.createNewUser(testuser3, password3, "misterX@hidemyass.com");

		Category mainCategory = Category.createCategory(testCategory1, "Main category", null);
		Category networkCategory = Category.createCategory(testCategory2, "Networking snippets", null);
		Category p2pCategory = Category.createCategory(testCategory3, "Networking snippets", networkCategory);

		final String testCode = "// Not even hello world is found here :-/";
		for (int i = 0; i < 10; i++) {

			Snippet snippet = Snippet.createSnippet(user1, "Testsnippet" + i, "I am the test snippet #" + i,
					new CodeJava(testCode));
			snippet.setCategory(mainCategory);
		}

		Snippet snippet = Snippet.createSnippet(user1, "P2P", "P2P Example", new CodeJava(testCode));
		testSnippet = snippet;
		snippet.setCategory(p2pCategory);

		testComment = Comment.createComment(user2, snippet, "I like your snippet!");
		testEnvironment = true;
	}
}

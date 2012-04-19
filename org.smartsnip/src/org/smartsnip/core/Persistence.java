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

		Category mainCategory = Category.createCategory("Main", "Main category", null);
		Category networkCategory = Category.createCategory("Network", "Networking snippets", null);
		Category p2pCategory = Category.createCategory("P2P", "Networking snippets", networkCategory);

		final String testCode = "// Not even hello world is found here :-/";
		for (int i = 0; i < 10; i++) {

			Snippet snippet = Snippet.createSnippet(user1, "Testsnippet" + i, "I am the test snippet #" + i,
					new CodeJava(testCode));
			snippet.setCategory(mainCategory);
		}

		Snippet snippet = Snippet.createSnippet(user3, "P2P", "P2P Example", new CodeJava(testCode));
		snippet.setCategory(p2pCategory);

		Comment.createComment(user2, snippet, "I like your snippet!");
	}

	/**
	 * Initialises the persistence layer
	 */
	public static void initialize() {
		// Empty. All initialisation process is done in the static constructor
	}
}

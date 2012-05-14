package org.smartsnip.core;

import java.io.IOException;

import org.smartsnip.persistence.IPersistence;
import org.smartsnip.persistence.MemPersistence;
import org.smartsnip.persistence.PersistenceFactory;

/**
 * This class is used to create some test objects in the application layer to
 * test the implementation of the GUI.
 * 
 * It becomes obsolete, when the persistence layer is ready to use
 * 
 */
public class Persistence {

	/** The persistence layer, that is used. If null it is not initialised */
	protected static IPersistence instance = null;

	/**
	 * Initialises the persistence layer with SQL persistence layer
	 * 
	 * @throws IllegalAccessException
	 * @throws IllegalStateException
	 *             Thrown if the persistence layer has already been initialised
	 */
	public static void initialize() throws IllegalAccessException {
		// XXX MEMORY ONLY MODE CURRENTLY ENABLED!!
		initialize(true);
	}

	public synchronized static void initialize(boolean memOnly) throws IllegalAccessException {
		initialize(memOnly, true);
	}

	/**
	 * Initialises the persistence layer. If memOnly is false, the
	 * SqlPersistence is initialised. If the initialisation of the
	 * SQLPersistence fails, the MemPersistence is used instant, and the
	 * resulting {@link IOException} is thrown.
	 * 
	 * So in all cases after this call a Persistence layer is active.
	 * 
	 * @param memOnly
	 *            if true a memory-only persistence layer is used, if false the
	 *            concrete SQL persistence layer is used
	 * @param createTestObjects
	 *            Indicating if some silly test objects are created
	 * @throws IllegalAccessException
	 * @throws IllegalStateException
	 *             Thrown if the persistence layer has already been initialised
	 * @deprecated Becase the silly test objects should be removed
	 */
	@Deprecated
	public synchronized static void initialize(boolean memOnly, boolean createTestObjects)
			throws IllegalAccessException {
		if (instance != null)
			throw new IllegalStateException("Persistence layer already initialised.");

		// TODO
		if (memOnly) {
			System.out.println("WARNING: Persistence is running in memory-only mode!");

			PersistenceFactory.setDefaultType(PersistenceFactory.PERSIST_MEMORY_VOLATILE);
			instance = PersistenceFactory.getInstance();
		} else {
			PersistenceFactory.setDefaultType(PersistenceFactory.PERSIST_SQL_DB);
			try {
				instance = PersistenceFactory.getInstance();
			} catch (IllegalAccessException e) {
				// Fail-safe method. Use memory persistance layer
				PersistenceFactory.setDefaultType(PersistenceFactory.PERSIST_MEMORY_VOLATILE);
				instance = PersistenceFactory.getInstance();
				throw e;
			}

		}

		if (createTestObjects)
			addSomeSillyObjects();
	}

	/**
	 * 
	 * @return true if the persistence layer has been initialised, otherwise
	 *         false
	 */
	public static synchronized boolean isInitialized() {
		return instance != null;
	}

	/**
	 * @return true if the initialised persistence layer is a memory only
	 *         persistence layer
	 */
	public static synchronized boolean isMemoryOnly() {
		// Ex falso quodlibet
		if (instance == null)
			return true;
		return (instance instanceof MemPersistence);
	}

	/**
	 * @return the used instance of the persistence layer or null if no one has
	 *         been initialised
	 */
	public static IPersistence getInstance() {
		return instance;
	}

	/**
	 * Clean shutdown
	 */
	public static void close() {
		// TODO Auto-generated method stub

	}

	/** Adds some silly objects for testing */
	private static void addSomeSillyObjects() {
		final String[] users = new String[] { "javajoe", "rubyrupert" };
		final String[] userPasswords = new String[] { "password1", "password2" };
		final String[] emails = new String[] { "joe@java.com", "rupert123@internet.com" };

		final String[] categories = new String[] { "Java code", "Sorting" };

		final String[] snippets = new String[] { "// No code here", "/* Java comment is here! */" };
		final String[] snippetOwner = new String[] { "// No code here", "/* Java comment is here! */" };
		final String[] snippetDesc = new String[] { "Simple single line comment",
				"Second variant of single line comment" };
		final String[] snippetCategories = new String[] { categories[0], categories[1] };

		try {

			for (int i = 0; i < users.length; i++) {
				User.createNewUser(users[i], userPasswords[i], emails[i]);
			}

			for (String categorie : categories) {
				Category.createCategory(categorie, "Test category", null);
			}

			for (int i = 0; i < snippets.length; i++) {
				User owner = User.getUser(snippetOwner[i]);
				Category category = Category.getCategory(snippetCategories[i]);

				if (owner == null || category == null)
					throw new IOException("Snippet owner or snippet category not found");

				Snippet.createSnippet(owner, "Snippet " + i, snippetDesc[i], category, snippets[i], "Java", "GPLv3",
						null);
			}

		} catch (IOException e) {
			System.err.println("IOException during creation of some silly objects!!");
			throw new RuntimeException("IOException during creation of some silly objects", e);
		}

		// Objects created

	}
}

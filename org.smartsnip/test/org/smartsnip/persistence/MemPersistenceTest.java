package org.smartsnip.persistence;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.smartsnip.core.Category;
import org.smartsnip.core.Comment;
import org.smartsnip.core.Persistence;
import org.smartsnip.core.Snippet;
import org.smartsnip.core.Tag;
import org.smartsnip.core.User;

public class MemPersistenceTest {

	/** Testuser information container */
	private static class TestUser {
		protected TestUser(String name, String email, String password,
				String realname) {
			this.name = name;
			this.email = email;
			this.password = password;
			this.realname = realname;
		}

		protected String name;
		protected String email;
		protected String password;
		protected String realname;

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (obj instanceof TestUser) {
				// TODO Implement me, never used
				return true;
			} else if (obj instanceof User) {
				User user = (User) obj;
				if (!user.getUsername().equalsIgnoreCase(name))
					return false;
				if (!user.getEmail().equalsIgnoreCase(email))
					return false;

				if (!user.getRealName().equalsIgnoreCase(realname))
					return false;

				return true;

			} else
				return false;
		}
	}

	private static class TestSnippet {
		protected TestSnippet(String owner, String name, long id,
				String description, String code, String language,
				String category) {
			this.owner = owner;
			this.name = name;
			this.id = id;
			this.description = description;
			this.code = code;
			this.language = language;
			this.category = category;
		}

		protected String owner;
		protected String name;
		protected long id;
		protected String description;
		protected String code;
		protected String language;
		protected String category;
		protected List<Tag> tags = new ArrayList<Tag>();

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (obj instanceof TestSnippet) {
				TestSnippet snippet = (TestSnippet) obj;
				return snippet.id == this.id;
			} else if (obj instanceof Snippet) {
				Snippet snippet = (Snippet) obj;
				return ((Long) id).equals(snippet.getHashId());
			} else
				return false;
		}
	}

	/** If the memory-only persistence layer has been initialised successfully */
	private static boolean isInitialized = false;

	private static final String[] testCategories = new String[] { "Network",
			"Sorting", "Social" };

	private static final TestUser[] testUsers = new TestUser[] {
			new TestUser("Joe", "joe@mail.com", "pass1", "Java Joe"),
			new TestUser("Rupert", "rupert@yjiik.net", "pass2", "Ruby Rupert") };

	private static final String[] testLanguages = new String[] { "Java",
			"Ruby", "C" };

	private static final TestSnippet[] testSnippets = new TestSnippet[] { new TestSnippet(
			testUsers[0].name, "BubbleSort", 0,
			"Bubble sort is a n² sorting algorithm",
			"// NO CODE CURRENTLY HERE", testLanguages[0], testCategories[1]) };

	private static final String testComment = "Test comment";

	@BeforeClass
	public static void setUp() throws Exception {
		// Do not create test objects
		Persistence.initialize(true, false);

		isInitialized = Persistence.isMemoryOnly();
		if (!isInitialized) {
			System.err.println("Memory only persistence layer not initialized");
		}
	}

	@AfterClass
	public static void tearDown() throws Exception {
		Persistence.close();
	}

	/**
	 * Tests if the persistence layer has been initialised in a memory only
	 * mode.
	 * 
	 * <b>This check must be done in every test</b> - it checks if the active
	 * persistence layer is a memory only. In all other cases all tests will
	 * fail to provide, that the test cases are not running on an active sql
	 * layer!
	 */
	private void checkInitialisation() {
		if (!Persistence.isMemoryOnly()) {
			fail("Memory only persistence layer not initialized");
		}
		isInitialized = true;
	}

	@Test
	public void testCategories() throws IOException {
		checkInitialisation();

		for (String category : testCategories) {
			Category.createCategory(category, "No description", null);
			Category obj = Persistence.getInstance().getCategory(category);
			if (obj == null)
				fail("Failure during getting created category");
			if (!obj.getName().equals(category))
				fail("Name of created category is invalid");
		}
		int delta = Persistence.getInstance().getCategoryCount()
				- testCategories.length;
		if (delta != 0) {
			fail("Category creation failure. Delta = " + delta + " != 0");
		}

	}

	@Test
	public void testUsers() throws IOException {
		checkInitialisation();

		for (TestUser testuser : testUsers) {
			User.createNewUser(testuser.name, testuser.password,
					testuser.email, testuser.realname);
			if (!User.auth(testuser.name, testuser.password)) {
				fail("User login failed for \"" + testuser.name + "\"!");
			}
			if (User.auth(testuser.name, testuser.password + "_FAIL")) {
				fail("User \"" + testuser.name
						+ "\" was able to login with wrong password!");
			}
			if (!testuser.equals(User.getUser(testuser.name)))
				fail("Error getting created user \"" + testuser.name + "\"!");
		}

		int delta = Persistence.getInstance().getUserCount() - testUsers.length;
		if (delta != 0) {
			fail("User creation failure: Delta = " + delta + " != 0");
		}
	}

	@Test
	public void testSnippets() throws IOException {
		checkInitialisation();

		int i = 0;
		for (TestSnippet testSnippet : testSnippets) {
			User owner = Persistence.getInstance().getUser(testSnippet.owner);
			if (owner == null)
				fail("Failure getting owner \"" + testSnippet.owner
						+ "\" for snippet!");
			Category category = Persistence.getInstance().getCategory(
					testSnippet.category);
			if (category == null)
				fail("Failure getting category \"" + testSnippet.category
						+ "\" for snippet!");

			Snippet snippet = Snippet.createSnippet(owner.getUsername(),
					testSnippet.name, testSnippet.description,
					category.getName(), testSnippet.code, testSnippet.language,
					"GPLv3", testSnippet.tags);

			testSnippet.id = snippet.getHashId();

			/* Re-read snippet */
			snippet = Snippet.getSnippet(testSnippet.id);
			if (snippet == null)
				fail("Failure getting created snippet with id = "
						+ testSnippet.id);
			if (!testSnippet.equals(snippet))
				fail("Failure matching created snippet with id = "
						+ testSnippet.id + " against creation template");
		}

		int delta = Persistence.getInstance().getSnippetsCount()
				- testSnippets.length;
		if (delta != 0) {
			fail("Snippet creation failure: Delta = " + delta + " != 0");
		}
	}

	@Test
	public void testCommenting() throws IOException {
		checkInitialisation();

		Snippet snippet = Snippet.getSnippet(testSnippets[0].id);
		User owner = User.getUser(testUsers[0].name);
		if (owner == null)
			fail("Failure getting test user");
		if (snippet == null)
			fail("Failure getting test snippet");

		Comment comment = snippet.addComment(testComment, owner);
		if (comment == null)
			fail("Comment creation failure");

		if (comment.getTotalVotes() != 0)
			fail("Vote count of new comment is not 0");

		comment.votePositive(owner);
		if (comment.getPositiveVotes() != 1)
			fail("Error voting positive (posvotes != 1)");
		if (comment.getNegativeVotes() != 0)
			fail("Error voting positive (negvotes != 0)");
		if (comment.getTotalVotes() != 1)
			fail("Error voting positive (total votes != 1)");
		comment.voteNegative(owner);
		if (comment.getPositiveVotes() != 0)
			fail("Error voting negative (posvotes != 0)");
		if (comment.getNegativeVotes() != 1)
			fail("Error voting negative (negvotes != 1)");
		if (comment.getTotalVotes() != 1)
			fail("Error voting negative (total votes != 1)");
		comment.unvote(owner);
		if (comment.getPositiveVotes() != 0)
			fail("Error unvoting (posvotes != 0)");
		if (comment.getNegativeVotes() != 0)
			fail("Error unvoting (negvotes != 0)");
		if (comment.getTotalVotes() != 0)
			fail("Error unvoting (total votes != 0)");

		int delta = 1 - snippet.getCommentCount();
		if (delta != 0)
			fail("Comment count of test snippet not valid. Delta = " + delta);
	}

	@Test
	public void testSearch() throws IOException {
		checkInitialisation();

		List<Snippet> results = Persistence.getInstance().search(
				testSnippets[0].name, 0, 2);
		if (results.size() <= 0)
			fail("Search for snippet #1 did not return any contents");

		results = Persistence.getInstance().search(
				"THIS_SNIPPET_NAME_MUST_NOT_EXIST", 0, 2);

		if (results.size() > 0)
			fail("Search for snippet that not exists returned contents");
	}
}

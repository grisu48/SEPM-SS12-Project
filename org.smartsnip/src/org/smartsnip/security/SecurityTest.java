package org.smartsnip.security;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smartsnip.core.Persistence;
import org.smartsnip.core.Session;
import org.smartsnip.shared.ISnippet;
import org.smartsnip.shared.IUser;

/**
 * Security policy tester
 * 
 */
public class SecurityTest {
	private static String cookie = "testCookie";
	private static Session session = null;

	@Before
	public void setUp() throws Exception {
		Persistence.createTestEnvironment();

		// TODO: Disable persistence layer!!
		session = Session.createNewSession();
		cookie = session.getCookie();
	}

	@After
	public void tearDown() throws Exception {
		Session.deleteSession(cookie);
	}

	/**
	 * Creates a test session
	 */
	@Test
	public void createGuestSession() {
		if (session.isLoggedIn()) fail("Guest session creation failure");
	}

	/**
	 * Test the access policies of the huest
	 */
	@Test
	public void testGuestPolicies() {
		// TODO Extend with expanding IUser interface

		if (session.getIUser() != null) fail("Guest session returned IUser interface");
		IUser testuser = null;
		try {
			testuser = session.getIUser(Persistence.testuser1);
		} catch (IllegalAccessException e) {
			fail("Testuser not given");
		}
		try {
			testuser.getFavorites();
			fail("Guest user should not get favorites from testuser1");
		} catch (IllegalAccessException e) {
		}
		try {
			testuser.logout();
			fail("Guest user should not be possible to log testuser1 out");
		} catch (IllegalAccessException e) {
		}

		ISnippet snippet = session.getISnippet(Persistence.testSnippet.hash);
		try {
			snippet.addComment("Testcomment");
			fail("Guest is able to post a comment to the test snippet");
		} catch (IllegalAccessException e) {
		}

	}

	@Test
	public void testLogin() {
		// TODO: Remove hardcoded path, if possible
		try {
			session.login(Persistence.testuser1, Persistence.password2);
			fail("Login with wrong password was successfull for testuser1");
		} catch (IllegalAccessException e) {
		}
		try {
			session.login(Persistence.testuser1, Persistence.password3);
			fail("Login with wrong password was successfull for testuser1");
		} catch (IllegalAccessException e) {
		}
		try {
			session.login(Persistence.testuser2, Persistence.password1);
			fail("Login with wrong password was successfull for testuser2");
		} catch (IllegalAccessException e) {
		}
		try {
			session.login(Persistence.testuser2, Persistence.password3);
			fail("Login with wrong password was successfull for testuser2");
		} catch (IllegalAccessException e) {
		}
		try {
			session.login(Persistence.testuser3, Persistence.password1);
			fail("Login with wrong password was successfull for testuser3");
		} catch (IllegalAccessException e) {
		}
		try {
			session.login(Persistence.testuser3, Persistence.password2);
			fail("Login with wrong password was successfull for testuser3");
		} catch (IllegalAccessException e) {
		}

		try {
			session.login(Persistence.testuser1, Persistence.password1);
			session.login(Persistence.testuser2, Persistence.password2);
			session.login(Persistence.testuser3, Persistence.password3);
		} catch (IllegalAccessException e) {
			fail("Login failure");
		}
	}

	@Test
	public void testUserPolicies() {
		IUser user = null;
		try {
			session.login(Persistence.testuser1, Persistence.password1);
			user = session.getIUser(Persistence.testuser1);
		} catch (IllegalAccessException e) {
			fail("Login failure");
		}
		try {
			user.getFavorites();
		} catch (IllegalAccessException e) {
			fail("Getting favorites failed");
		}
		try {
			user.getSnippets();
		} catch (IllegalAccessException e) {
			fail("Getting snippets failed");
		}

		ISnippet snippet = session.getISnippet(Persistence.testSnippet.hash);
		try {
			snippet.addComment("Testcomment");
		} catch (IllegalAccessException e) {
			fail("User cannot comment test snippet");
		}

		try {
			snippet.delete();
		} catch (IllegalAccessException e) {
			fail("User cannot delete own test snippet");
		}

		// TODO: Extend with expanding IUser
	}
}

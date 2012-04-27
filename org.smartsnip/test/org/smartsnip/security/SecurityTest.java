package org.smartsnip.security;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smartsnip.core.Session;

/**
 * Security policy tester
 * 
 */
public class SecurityTest {
	private static String cookie = "testCookie";
	private static Session session = null;

	@Before
	public void setUp() throws Exception {
		// TODO: Create test environment in persistence
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
		if (session.isLoggedIn()) {
			fail("Guest session creation failure");
		}
	}

}

package org.smartsnip.server;

import static org.junit.Assert.fail;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.smartsnip.core.Persistence;

public class SessionTest {

	private static final int testCookieCount = 10;
	private static final String[] testCookies = new String[testCookieCount];

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Set up a memory-only persistence
		Persistence.initialize(true);
		checkInitialisation();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Persistence.close();
	}

	private static void checkInitialisation() {
		if (!Persistence.isInitialized())
			fail("Persistence not initialized");
		if (!Persistence.isMemoryOnly())
			fail("Persistence not in memory-only mode");

	}

	/**
	 * Creates a new session and returns the cookie value of the new session
	 * 
	 * @return new randomised cookie value
	 */
	private static String getNewCookieID() {
		Session session = Session.createNewSession();
		if (session == null)
			throw new RuntimeException("Session creation failure");
		return session.getCookie();
	}

	@Test
	public void testSessionManagement() {
		checkInitialisation();
		int delta;

		for (int i = 0; i < testCookieCount; i++) {
			testCookies[i] = getNewCookieID();
			if (!Session.existsCookie(testCookies[i]))
				fail("Randomised test session creation failed");
		}

		delta = testCookieCount - Session.activeCount();
		if (delta != 0)
			fail("Session count after session creation not valid; Delta = " + delta);

		for (int i = 0; i < testCookieCount; i++) {
			Session.deleteSession(testCookies[i]);
			if (Session.existsCookie(testCookies[i]))
				fail("Session exists after delete it");
		}

		delta = Session.activeCount();
		if (delta != 0)
			fail("Session count after session deletion not valid; Delta = " + delta);

	}

}

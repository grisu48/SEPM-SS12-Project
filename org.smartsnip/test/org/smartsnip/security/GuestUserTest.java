package org.smartsnip.security;

import static org.junit.Assert.fail;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.smartsnip.core.Persistence;
import org.smartsnip.server.Session;

public class GuestUserTest {

	private static Session guestSession = null;

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

	@Test
	public void createGuestSession() {
		checkInitialisation();

		guestSession = Session.createNewSession();
		if (guestSession.isLoggedIn())
			fail("New created session is logged in");

		Session nullCookieSession = Session.getSession(null);
		if (nullCookieSession == null)
			fail("Session for cookie denied browser is null");
		if (nullCookieSession.isLoggedIn())
			fail("No-Cookie session is logged in");
	}

}

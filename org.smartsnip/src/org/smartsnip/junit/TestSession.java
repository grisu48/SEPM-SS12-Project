package org.smartsnip.junit;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicBoolean;

import org.smartsnip.core.*;
import org.smartsnip.core.Session.SessionState;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestSession {

	/** Testing session */
	private Session testSession = null;

	/** the test cookie String. We all love the Cookie Monster ;-) */
	private String testCookie = "testCookieForTheCookieMonster";

	@Before
	public void setUp() throws Exception {
		testSession = null;
		// TODO Disable persistence layer for all testing methods!!
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * This test performs actions on the cookie management system.
	 * 
	 * It creates 1000 different cookies and checks
	 */
	@Test
	public void testCookieManagnement() {
		final int cookieCount = 1000;
		System.out.println("Creating " + cookieCount + " sessions ... ");
		String sids[] = new String[cookieCount];
		for (int i = 0; i < cookieCount; i++) {
			sids[i] = testSession.getNewSessionCookie();
		}
		for (int i = 0; i < cookieCount; i++) {
			Session sessionObject = Session.getSession(sids[i]);
			if (sessionObject == null)
				fail("Getting of session " + i + " failed. Cookie not found in session management");
			sessionObject.deleteSession();
			if (Session.existsCookie(sids[i]))
				fail("Delete session " + i + " failed. Session exists after deleteing it!");
		}
	}

	@Test
	public void testUserManagement() {
		// TODO
	}

	/*
	 * NOTE: This test takes LOTS of time. Always perfom this test at the end of
	 * other tests!
	 */
	/**
	 * This test performs actions on the session lifecycle.
	 * 
	 * If gets a session with the test cookie and lets the session become
	 * inactive and then deleted.
	 * 
	 * CAUTION: Depending on the delays of the lifecycle this test can wait for
	 * a LONG time (hours and hours and hours and ...)
	 */
	@Test
	public void testGetState() {
		final int threadcount = 10;

		/*
		 * XXX: This test case has currently serveral problems with the
		 * IllegalStateException that normally should NOT be thrown!!!
		 * 
		 * I have to work on this
		 */

		System.out.print("Testing session state with " + threadcount + " threads ... ");
		Thread[] threads = new Thread[threadcount];
		final AtomicBoolean success = new AtomicBoolean(true);

		for (int i = 0; i < threadcount; i++) {
			final int index = i;

			threads[i] = new Thread(new Runnable() {

				@Override
				public void run() {
					testSession = Session.getSession(testCookie + index);
					if (testSession == null) {
						fail("Test session creation failure");
						return;
					}

					testSession.doActivity();
					if (testSession.getState() != SessionState.active) {
						fail("Test session's state is not active");
						return;
					}

					try {
						long delay;
						delay = Session.inactiveDelay + 5000;
						Thread.sleep(delay);
						if (testSession.getState() != SessionState.inactive) {
							fail("Test session not becomming inactive");
							return;
						}

						delay = Session.deleteDelay + 10000 - delay;
						Thread.sleep(delay);
						if (testSession.getState() != SessionState.deleted) {
							fail("Test session not deleted");
							return;
						}

					} catch (InterruptedException e) {
						fail("Interrupted");
					}
				}

				private void fail(String message) {
					success.set(false);
					System.err.println("Thread " + index + " failed with message: " + message);
				}
			});
			threads[i].start();
		}

		// Wait for threads
		try {
			for (int i = 0; i < threadcount; i++) {
				threads[i].join();
			}
		} catch (InterruptedException e) {
			System.err.println("testGetState() interrupted");
			for (int i = 0; i < threadcount; i++)
				threads[i].interrupt();
		}
		if (!success.get()) fail("testGetState() failure flag set");

	}

}

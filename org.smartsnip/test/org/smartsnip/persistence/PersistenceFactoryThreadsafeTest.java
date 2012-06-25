/**
 * File: PersistenceFactoryThreadsafeTest.java
 * Date: 29.04.2012
 */
package org.smartsnip.persistence;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Gerhard Aigner
 * 
 */
public class PersistenceFactoryThreadsafeTest {

	/**
	 * Test method for thread safety for
	 * {@link org.smartsnip.persistence.PersistenceFactory#getInstance()}.
	 * <p>
	 * Multiple runs are necessary to find possible problems.
	 * @throws Throwable
	 */
	@Test
	public void testThreadSafety() throws Throwable {
		IPersistence instance1 = null;
		IPersistence prevInstance2 = null;
		IPersistence instance2 = null;

		class Tester implements Runnable {
			private IPersistence instance = null;
			private IPersistence prevInstance = null;
			private boolean duplicate = false;
			private Throwable exn = null;

			@Override
			public void run() {
				try {
					for (int i = 0; i < 100; ++i) {
						instance = PersistenceFactory.getInstance();
						if (prevInstance != null && prevInstance != instance) {
							this.duplicate = true;
						}
						prevInstance = instance;
					}
				} catch (IllegalAccessException e) {
					/*
					 * JUnit gets no exception from an other thread, so it is
					 * passed by a getter.
					 */
					this.exn = e;
				}
			}

			/**
			 * @return the instance
			 */
			public IPersistence getInstance() {
				return this.instance;
			}

			/**
			 * @return the Throwable
			 */
			public Throwable getThrowable() {
				return this.exn;
			}

			/**
			 * @return the duplicate
			 */
			public boolean isDuplicate() {
				return this.duplicate;
			}

		}
		;

		Tester runner = new Tester();
		Thread th1 = new Thread(runner);
		
		th1.start();

		for (int i = 0; i < 100; ++i) {
			instance2 = PersistenceFactory.getInstance();
			assertFalse("Duplicate of singleton in Thread1",
					prevInstance2 != null && prevInstance2 != instance2);
			prevInstance2 = instance2;
		}
		
		th1.join();
		
		instance1 = runner.getInstance();
		assertTrue("Duplicate of singleton over different Threads", instance1 == instance2);
		assertEquals(instance1, instance2);
		assertFalse("Duplicate of singleton in Thread2", runner.isDuplicate());
		if (runner.getThrowable() != null) {
			throw new Exception(runner.getThrowable());
		}
	}

}

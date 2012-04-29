/**
 * File: PersistenceFactoryTest.java
 * Date: 27.04.2012
 */
package org.smartsnip.persistence;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

/**
 * @author littlelion
 * 
 */
public class PersistenceFactoryTest {

	/**
	 * Test the unavalability of the constructor.
	 * {@link org.smartsnip.persistence.PersistenceFactory#PersistenceFactory()}
	 * .
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testGetFactory() throws Throwable {
		try {
			@SuppressWarnings("rawtypes")
			Constructor[] c = PersistenceFactory.class
					.getDeclaredConstructors();
			c[0].setAccessible(true);

			@SuppressWarnings("unused")
			PersistenceFactory fct = (PersistenceFactory) c[0].newInstance();
			fail("InvocationTargetException expected");
		} catch (InvocationTargetException e) {
			assertTrue(e.getCause() instanceof IllegalAccessException);
			assertEquals("This is a static only class.", e.getCause()
					.getMessage());
		}
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.PersistenceFactory#getInstance(int)}.
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testGetInstance() throws Throwable {
		// initializer constant of invalid value
		IPersistence instance = null;
		try {
			instance = PersistenceFactory
					.getInstance(PersistenceFactory.PERSIST_UNINITIALIZED);
			fail("IllegalAccessException expected.");
		} catch (IllegalAccessException iae) {
			assertEquals("Type of persistence unknown.", iae.getMessage());
		}
		assertNull(instance);

		// get an instance
		PersistenceFactory.setDefaultType(PersistenceFactory.PERSIST_BLACKHOLE);
		instance = PersistenceFactory.getInstance();
		assertNotNull(instance);
		assertTrue(instance instanceof BlackholePersistence);

		// Test on duplicate
		IPersistence duplicate = PersistenceFactory
				.getInstance(PersistenceFactory.PERSIST_BLACKHOLE);
		assertTrue(instance == duplicate);

		// test on duplicate of different type
		try {
			duplicate = PersistenceFactory
					.getInstance(PersistenceFactory.PERSIST_SQL_DB);
			fail("IllegalAccessException expected.");
		} catch (IllegalAccessException ie) {
			assertEquals(
					"Mismatch between requested and initialized persistence object.",
					ie.getMessage());
		}
		assertTrue(instance == duplicate);

		// test on duplicate with changed default type - ignore default
		PersistenceFactory
				.setDefaultType(PersistenceFactory.PERSIST_MEMORY_VOLATILE);
		duplicate = PersistenceFactory.getInstance();
		assertNotNull(duplicate);
		assertTrue("instance is not a BlackholePersistence",
				duplicate instanceof BlackholePersistence);
		assertTrue("equality", instance == duplicate);
	}
}

/**
 * File: PersistenceFactoryTest.java
 * Date: 27.04.2012
 */
package org.smartsnip.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.smartsnip.core.Persistence;

/**
 * @author littlelion
 * 
 */
public class PersistenceFactoryTest {

	/**
	 * Test the unavalability of the constructor.
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testGetFactory() throws Throwable {
		try {
			@SuppressWarnings("rawtypes")
			Constructor[] c = PersistenceFactory.class.getDeclaredConstructors();
			c[0].setAccessible(true);

			@SuppressWarnings("unused")
			PersistenceFactory fct = (PersistenceFactory) c[0].newInstance();
			fail("InvocationTargetException expected");
		} catch (InvocationTargetException e) {
			assertTrue(e.getCause() instanceof IllegalAccessException);
			assertEquals("This is a static only class.", e.getCause().getMessage());
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
		IPersistence instance = null;
		try {
			instance = PersistenceFactory.getInstance(PersistenceFactory.PERSIST_UNINITIALIZED);
			fail("IllegalAccessException expected.");
		} catch (IllegalAccessException iae) {
			assertEquals("Type of persistence unknown.", iae.getMessage());
		}
		assertNull(instance);

		instance = PersistenceFactory.getInstance(PersistenceFactory.PERSIST_BLACKHOLE);
		assertNotNull(instance);

		IPersistence duplicate = PersistenceFactory.getInstance(PersistenceFactory.PERSIST_BLACKHOLE);
		assertTrue(instance == duplicate);

		try {
			duplicate = PersistenceFactory.getInstance(PersistenceFactory.PERSIST_SQL_DB);
			fail("IllegalAccessException expected.");
		} catch (IllegalAccessException ie) {
			assertEquals("Mismatch between requested and initialized persistence object.", ie.getMessage());
		}
		assertTrue(instance == duplicate);
	}

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.PersistenceFactory#getInstance()}.
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testGetDefaultInstance() throws Throwable {
		IPersistence instance = null;
		IPersistence duplicate = null;
		PersistenceFactory.setDefaultType(PersistenceFactory.PERSIST_BLACKHOLE);
		instance = PersistenceFactory.getInstance();
		assertNotNull(instance);
		assertTrue(instance instanceof BlackholePersistence);

		PersistenceFactory.setDefaultType(PersistenceFactory.PERSIST_MEMORY_VOLATILE);
		duplicate = PersistenceFactory.getInstance();
		assertNotNull(duplicate);
		assertTrue("instance is a BlackholePersistence", duplicate instanceof BlackholePersistence);
		assertTrue("equality", instance == duplicate);
	}

	@Test
	public void testInstance() throws Throwable {
		IPersistence instance = Persistence.getInstance();
		assertNull("pre: instance != null", instance);
		Persistence.initialize(true);
		instance = Persistence.getInstance();
		assertNotNull("post: instance = null", instance);
	}

}

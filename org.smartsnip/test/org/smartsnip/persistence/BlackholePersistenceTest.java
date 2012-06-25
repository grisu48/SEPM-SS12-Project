/**
 * File: BlackholePersistenceTest.java
 * Date: 27.04.2012
 */
package org.smartsnip.persistence;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.Test;

/**
 * @author Gerhard Aigner
 * 
 */
public class BlackholePersistenceTest {

	/**
	 * Test method for
	 * {@link org.smartsnip.persistence.BlackholePersistence#BlackholePersistence()}
	 * .
	 * <p>
	 * This is an example test to the safety of the used singleton pattern. Due
	 * to the equivalence to the constructors of all other classes which
	 * implement {@link IPersistence}, further tests on their constructors may be
	 * omitted.
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testBlackholePersistence() throws Throwable {

		// get an instance with the proposed getter method
		IPersistence instance = PersistenceFactory
				.getInstance(PersistenceFactory.PERSIST_BLACKHOLE);
		assertNotNull("instance = null", instance);

		// redo the get and assert equality
		IPersistence secondInstance = PersistenceFactory
				.getInstance(PersistenceFactory.PERSIST_BLACKHOLE);
		assertEquals(instance, secondInstance); // same content?
		assertTrue(instance == secondInstance); // same reference?

		// try to get a instance of different type - must fail
		try {
			secondInstance = PersistenceFactory
					.getInstance(PersistenceFactory.PERSIST_MEMORY_VOLATILE);
			fail("IllegalAccessException expected.");
		} catch (IllegalAccessException ie) {
			assertEquals(
					"Mismatch between requested and initialized persistence object.",
					ie.getMessage());
		}
		assertEquals(instance, secondInstance); // same content?
		assertTrue(instance == secondInstance); // same reference?

		// try to get an instance by direct call of the constructor - must fail
		try {
			secondInstance = new BlackholePersistence();
			fail("IllegalAccessException expected.");
		} catch (IllegalAccessException iae) {
			assertEquals(
					"Singleton pattern: caller must be PersistenceFactory class.",
					iae.getMessage());
		}
		assertEquals(instance, secondInstance); // same content?
		assertTrue(instance == secondInstance); // same reference?

		// try to get an instance by using the reflections mechanism - must fail
		try {
			@SuppressWarnings("rawtypes")
			Constructor[] c = BlackholePersistence.class
					.getDeclaredConstructors();
			c[0].setAccessible(true);

			secondInstance = (BlackholePersistence) c[0].newInstance();
			fail("Exception expected");
		} catch (InvocationTargetException e) {
			assertTrue(e.getCause() instanceof IllegalAccessException);
			assertEquals(
					"Singleton pattern: caller must be PersistenceFactory class.",
					e.getCause().getMessage());
		}
		assertEquals(instance, secondInstance); // same content?
		assertTrue(instance == secondInstance); // same reference?
	}
}

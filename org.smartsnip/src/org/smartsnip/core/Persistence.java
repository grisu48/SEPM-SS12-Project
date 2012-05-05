package org.smartsnip.core;

import java.io.IOException;

import org.smartsnip.persistence.IPersistence;
import org.smartsnip.persistence.MemPersistence;
import org.smartsnip.persistence.PersistenceFactory;

/**
 * This class is used to create some test objects in the application layer to
 * test the implementation of the GUI.
 * 
 * It becomes obsolete, when the persistence layer is ready to use
 * 
 */
public class Persistence {

	/** The persistence layer, that is used. If null it is not initialised */
	protected static IPersistence instance = null;

	/**
	 * Initialises the persistence layer with SQL persistence layer
	 * 
	 * @throws IllegalAccessException
	 * @throws IllegalStateException
	 *             Thrown if the persistence layer has already been initialised
	 */
	public static void initialize() throws IllegalAccessException {
		// XXX MEMORY ONLY MODE CURRENTLY ENABLED!!
		initialize(true);
	}

	/**
	 * Initialises the persistence layer. If memOnly is false, the
	 * SqlPersistence is initialised. If the initialisation of the
	 * SQLPersistence fails, the MemPersistence is used instant, and the
	 * resulting {@link IOException} is thrown.
	 * 
	 * So in all cases after this call a Persistence layer is active.
	 * 
	 * @param memOnly
	 *            if true a memory-only persistence layer is used, if false the
	 *            concrete SQL persistence layer is used
	 * @throws IllegalAccessException
	 * @throws IllegalStateException
	 *             Thrown if the persistence layer has already been initialised
	 */
	public synchronized static void initialize(boolean memOnly) throws IllegalAccessException {
		if (instance != null)
			throw new IllegalStateException("Persistence layer already initialised.");

		// TODO
		if (memOnly) {
			System.err.println("WARNING: Persistence is running in memory-only mode!");

			PersistenceFactory.setDefaultType(PersistenceFactory.PERSIST_MEMORY_VOLATILE);
			instance = PersistenceFactory.getInstance();
		} else {
			PersistenceFactory.setDefaultType(PersistenceFactory.PERSIST_SQL_DB);
			try {
				instance = PersistenceFactory.getInstance();
			} catch (IllegalAccessException e) {
				// Fail-safe method. Use memory persistance layer
				PersistenceFactory.setDefaultType(PersistenceFactory.PERSIST_MEMORY_VOLATILE);
				instance = PersistenceFactory.getInstance();
				throw e;
			}

		}
	}

	/**
	 * 
	 * @return true if the persistence layer has been initialised, otherwise
	 *         false
	 */
	public static synchronized boolean isInitialized() {
		return instance != null;
	}

	/**
	 * @return true if the initialised persistence layer is a memory only
	 *         persistence layer
	 */
	public static synchronized boolean isMemoryOnly() {
		// Ex falso quodlibet
		if (instance == null)
			return true;
		return (instance instanceof MemPersistence);
	}

	/**
	 * @return the used instance of the persistence layer or null if no one has
	 *         been initialised
	 */
	public static IPersistence getInstance() {
		return instance;
	}

	/**
	 * Clean shutdown
	 */
	public static void close() {
		// TODO Auto-generated method stub

	}
}

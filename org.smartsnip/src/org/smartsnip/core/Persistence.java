package org.smartsnip.core;

import java.io.IOException;

import org.smartsnip.persistence.IPersistence;
import org.smartsnip.persistence.MemPersistence;
import org.smartsnip.persistence.SqlPersistence;

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
	 * @throws IllegalStateException
	 *             Thrown if the persistence layer has already been initialised
	 */
	public static void initialize() throws IOException {
		initialize(false);
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
	 * @throws IllegalStateException
	 *             Thrown if the persistence layer has already been initialised
	 */
	public synchronized static void initialize(boolean memOnly) throws IOException {
		if (instance != null)
			throw new IllegalStateException("Persistence layer already initialised.");

		// TODO
		if (memOnly) {
			instance = MemPersistence.createInstance();
		} else {
			try {
				instance = SqlPersistence.createInstance();
			} catch (IOException e) {
				// Fail-safe method. Use memory persistance layer
				instance = MemPersistence.createInstance();
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
	 * @return the used instance of the persistence layer or null if no one has
	 *         been initialised
	 */
	public static IPersistence getInstance() {
		return instance;
	}
}

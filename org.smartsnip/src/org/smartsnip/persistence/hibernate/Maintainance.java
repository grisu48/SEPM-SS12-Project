/**
 * File: Maintainance.java
 * Date: 05.06.2012
 */
package org.smartsnip.persistence.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.CacheMode;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.smartsnip.persistence.PersistenceFactory;

/**
 * This class provides maintainance tools to keep the database and it's
 * components up to date.
 * 
 * @author littlelion
 * 
 */
public class Maintainance {

	/**
	 * data logger
	 */
	private static Logger log = Logger.getLogger(Maintainance.class);

	/**
	 * Maintenance is a static only class.
	 */
	private Maintainance() {
	}

	/**
	 * Mass Indexer for building the Lucene index files. This method is useful
	 * to update the full text index files after modifying the database with
	 * external tools. It returns after the indexing has been finished.
	 * <p>
	 * It is recommended to shut down the client access while rebuilding the
	 * index.
	 * 
	 * @param batchSizeToLoadObjects
	 *            Sets the batch size used to load the root entities.
	 * @param threadsToLoadObjects
	 *            Set the number of threads to be used to load the root
	 *            entities.
	 * @param idFetchSize
	 *            Specifies the fetch size to be used when loading primary keys
	 *            if objects to be indexed. Some databases accept special
	 *            values, for example MySQL might benefit from using
	 *            {@link Integer#MIN_VALUE} otherwise it will attempt to preload
	 *            everything in memory.
	 * @param threadsForSubsequentFetching
	 *            Sets the number of threads used to load the lazy collections
	 *            related to the indexed entities.
	 * @throws InterruptedException
	 */
	public static void fullTextMassIndexer(int batchSizeToLoadObjects,
			int threadsToLoadObjects, int idFetchSize,
			int threadsForSubsequentFetching) throws InterruptedException {
		log.info("Build Index for Hibernate Search with a mass-indexer");
		Session session = DBSessionFactory.open();
		FullTextSession fullTextSession = Search.getFullTextSession(session);
		fullTextSession.createIndexer()
				.batchSizeToLoadObjects(batchSizeToLoadObjects)
				.cacheMode(CacheMode.IGNORE)
				.threadsToLoadObjects(threadsToLoadObjects)
				.idFetchSize(idFetchSize)
				.threadsForSubsequentFetching(threadsForSubsequentFetching)
				.startAndWait();
		DBSessionFactory.close(session);
	}

	/**
	 * Mass Indexer for building the Lucene index files. This method is useful
	 * to update the full text index files after modifying the database with
	 * external tools. It returns after the indexing has been finished.
	 * <p>
	 * It is recommended to shut down the client access while rebuilding the
	 * index.
	 * <p>
	 * This indexer is configured to this default values:
	 * <ul>
	 * <li>batchSizeToLoadObjects = 25
	 * <li>threadsToLoadObjects = 2
	 * <li>idFetchSize = 150
	 * <li>threadsForSubsequentFetching = 1
	 * </ul>
	 * 
	 * @see #fullTextMassIndexer(int, int, int, int)
	 * @throws InterruptedException
	 */
	public static void fullTextMassIndexer() throws InterruptedException {
		fullTextMassIndexer(25, 2, 150, 1);
	}
	
	/**
	 * start the indexer on the console
	 * @param args 
	 */
	public static void main(String[] args) {
		try {
			fullTextMassIndexer();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		DBSessionFactory.closeFactory();
	}
}

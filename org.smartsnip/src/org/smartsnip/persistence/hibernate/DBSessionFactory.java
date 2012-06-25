/**
 * File: DBSessionFactory.java
 * Date: 28.04.2012
 */
package org.smartsnip.persistence.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * @author Gerhard Aigner
 * 
 */
public class DBSessionFactory {

	private DBSessionFactory() {
		// static only class
	}

	private static SessionFactory factory = null;
	private static ServiceRegistry serviceRegistry;

	/**
	 * initialize the session factory
	 * 
	 * @param configFile
	 *            the path to the configuration file
	 * @throws ExceptionInInitializerError
	 */
	private static synchronized void initialize(String configFile)
			throws ExceptionInInitializerError {
		if (configFile == null || configFile.isEmpty()) {
			configFile = "/hibernate.cfg.xml";
		}
		if (factory == null) {
			try {
				Configuration configuration = new Configuration();
				configuration.configure(configFile);

				// add entity classes
				configuration.addAnnotatedClass(DBCategory.class);
				configuration.addAnnotatedClass(DBCode.class);
				configuration.addAnnotatedClass(DBCodeFile.class);
				configuration.addAnnotatedClass(DBComment.class);
				configuration.addAnnotatedClass(DBFavourite.class);
				configuration.addAnnotatedClass(DBLanguage.class);
				configuration.addAnnotatedClass(DBLicense.class);
				configuration.addAnnotatedClass(DBLogin.class);
				configuration.addAnnotatedClass(DBNotification.class);
				configuration.addAnnotatedClass(DBRating.class);
				configuration.addAnnotatedClass(DBSnippet.class);
				configuration.addAnnotatedClass(DBTag.class);
				configuration.addAnnotatedClass(DBUser.class);
				configuration.addAnnotatedClass(DBVote.class);
				configuration.addAnnotatedClass(DBRelTagSnippet.class);

				serviceRegistry = new ServiceRegistryBuilder().applySettings(
						configuration.getProperties()).buildServiceRegistry();
				factory = configuration.buildSessionFactory(serviceRegistry);
			} catch (Throwable ex) {
				Logger log = Logger.getLogger(DBSessionFactory.class);
				log.error("Failed to create sessionFactory object.", ex);
				throw new ExceptionInInitializerError(ex);
			}
		}
	}

	/**
	 * Get a SessionFactory instance.
	 * 
	 * @return the factory with default configuration from the hibernate.cfg.xml
	 *         file
	 */
	public static SessionFactory getInstance() {
		return getInstance(null);
	}

	/**
	 * Get a SessionFactory instance.
	 * 
	 * @param configFile
	 *            the file to read the configuration from
	 * @return the factory with custom configuration
	 */
	public static SessionFactory getInstance(String configFile) {
		if (factory == null)
			initialize(configFile);
		return factory;
	}

	/**
	 * close the session factory
	 * 
	 * @see org.hibernate.SessionFactory#close()
	 */
	public static void closeFactory() {
		factory.close();
	}

	/**
	 * Open a new session. It is recommended to handle a session by one thread.
	 * 
	 * @return the session
	 */
	static Session open() {
		SessionFactory factory = DBSessionFactory.getInstance();
		return factory.openSession();
	}

	/**
	 * close the given session
	 * 
	 * @param session
	 */
	static void close(Session session) {
		session.close();
	}
}

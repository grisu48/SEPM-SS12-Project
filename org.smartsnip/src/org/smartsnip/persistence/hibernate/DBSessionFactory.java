/**
 * File: DBSessionFactory.java
 * Date: 28.04.2012
 */
package org.smartsnip.persistence.hibernate;

import java.io.IOException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * @author littlelion
 * 
 */
public class DBSessionFactory {

	private DBSessionFactory() {
		// static only class
	}

	private static SessionFactory factory = null;
	private static ServiceRegistry serviceRegistry;

	private static synchronized void initialize()
			throws ExceptionInInitializerError {
		if (factory == null) {
			try {
				Configuration configuration = new Configuration();
				configuration.configure().setProperty("hibernate.show_sql",
						"true"); // XXX set "hibernate.show_sql" to false

				// add entity classes
				configuration.configure().addAnnotatedClass(DBCategory.class);
				configuration.configure().addAnnotatedClass(DBCode.class);
				configuration.configure().addAnnotatedClass(DBComment.class);
				configuration.configure().addAnnotatedClass(DBFavourite.class);
				configuration.configure().addAnnotatedClass(DBLanguage.class);
				configuration.configure().addAnnotatedClass(DBLicense.class);
				configuration.configure().addAnnotatedClass(DBLogin.class);
				configuration.configure().addAnnotatedClass(DBNotification.class);
				configuration.configure().addAnnotatedClass(DBRating.class);
				configuration.configure().addAnnotatedClass(DBSnippet.class);
				configuration.configure().addAnnotatedClass(DBTag.class);
				configuration.configure().addAnnotatedClass(DBUser.class);
				configuration.configure().addAnnotatedClass(DBVote.class);
				configuration.configure().addAnnotatedClass(DBRelTagSnippet.class);

				serviceRegistry = new ServiceRegistryBuilder().applySettings(
						configuration.getProperties()).buildServiceRegistry();
				factory = configuration.buildSessionFactory(serviceRegistry);
			} catch (Throwable ex) {
				System.err.println("Failed to create sessionFactory object."
						+ ex);
				throw new ExceptionInInitializerError(ex);
			}
		}
	}

	/**
	 * @return the factory
	 */
	public static SessionFactory getInstance() {
		if (factory == null)
			initialize();
		return factory;
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
	 * @param session
	 * @throws IOException
	 */
	static void close(Session session) throws IOException {
		try {
			session.close();
		} catch (HibernateException e) {
			throw new IOException(e);
		}
	}
}

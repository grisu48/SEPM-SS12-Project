/**
 * File: DBSessionFactory.java
 * Date: 28.04.2012
 */
package org.smartsnip.persistence.hibernate;

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
						"true"); // XXX false
				configuration.configure().addAnnotatedClass(DBTag.class);

				serviceRegistry = new ServiceRegistryBuilder().applySettings(
						configuration.getProperties()).buildServiceRegistry();
				factory = configuration.buildSessionFactory();
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
}

/**
 * File: HibernateSessionFactory.java
 * Date: 28.04.2012
 */
package org.smartsnip.persistence.hibernate;

import java.util.List;
import java.util.Iterator;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * @author littlelion
 * 
 */
public class HibernateSessionFactory {
	private static SessionFactory factory;
	private static ServiceRegistry serviceRegistry;

	public static void main(String[] args) {
		
		/*
		 * create session factory
		 */
		try {
			Configuration configuration = new Configuration();
			configuration.configure()
					.setProperty("hibernate.show_sql", "true"); // XXX false
			configuration.configure().addAnnotatedClass(DBTag.class);
			
			serviceRegistry = new ServiceRegistryBuilder().applySettings(
					configuration.getProperties()).buildServiceRegistry();
			factory = configuration.buildSessionFactory(serviceRegistry);
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}
		
		/*
		 * an example read query
		 */
		System.out.println("**Example : Hibernate 4 DBSessionFactory**");
		System.out.println("**------------Read Query--------------**");
		System.out.println("----------------------------------------");
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			List<DBTag> employees = session.createQuery("FROM DBTag").list();
//			Query query = session.createQuery("FROM DBTag");
//			query.setLockOptions(new LockOptions(LockMode.NONE));
			for (Iterator<DBTag> iterator = employees.iterator(); iterator.hasNext();) {
				DBTag tag = iterator.next();
				System.out.println("DBTag Name: " + tag.getName());
				System.out.println("----------------------------------------");
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

		/*
		 * an example write query
		 */
		System.out.println("**------------Wrire Query-------------**");
		System.out.println("----------------------------------------");
		Session session2 = factory.openSession();
		Transaction tx2 = null;
		try {
			tx2 = session2.beginTransaction();
			
			// build a new object to save
			DBTag tag2 = new DBTag();
			tag2.setName("uri");
			
			// save the tag object
			session2.save(tag2);
				System.out.println("DBTag Saved: " + tag2.getName());
				System.out.println("----------------------------------------");
			tx2.commit();
		} catch (HibernateException e) {
			if (tx2 != null)
				tx2.rollback();
			e.printStackTrace();
		} finally {
			session2.close();
		}

	
	}
}
/**
 * File: DBTagTest.java
 * Date: 29.04.2012
 */
package org.smartsnip.persistence.hibernate;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.Factory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author littlelion
 *
 */
public class DBTagTest {

	/**
	 * Test method for {@link org.smartsnip.persistence.hibernate.DBTag#DBTag()}.
	 */
	@Test
	public void testDBTag() {
		// build a new object to save
		DBTag tag2 = new DBTag();
		tag2.setName("!!!JUnit-DBTag-test-tag!!!");
			
		// write a test-tag into the table
		SessionFactory factory = DBSessionFactory.getInstance();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			
			// save the tag object
			session.save(tag2);
				System.out.println("DBTag Saved: " + tag2.getName());
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			throw new HibernateException(e);
		} finally {
			session.close();
		}
//		assertNull("Transaction not closed.", tx);
		
		// read the previously written tag
		session = factory.openSession();
		tx = null;
		try {
			tx = session.beginTransaction();
//			List<DBTag> tags = session.createQuery("FROM DBTag").list();
			Query query = session.createQuery("FROM DBTag");
//			query.setParameter("name", "!!!JUnit-DBTag-test-tag!!!");
			@SuppressWarnings("unchecked")
			List<DBTag> tags = query.list();
			for (Iterator<DBTag> iterator = tags.iterator(); iterator.hasNext();) {
				DBTag tag = iterator.next();
				System.out.println("DBTag Name: " + tag.getName());
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			throw new HibernateException(e);
		} finally {
			session.close();
		}

		// delete the previously written tag
		session = factory.openSession();
		tx = null;
		try {
			tx = session.beginTransaction();
			
//			assertTrue("Table doesnt contain written tag.", session.contains(tag2));
			session.delete(tag2);
			
//			List<DBTag> tags = session.createQuery("FROM DBTag").list();
//			Query query = session.createQuery("FROM DBTag");
//			query.setParameter("name", "!!!JUnit-DBTag-test-tag!!!");
//			@SuppressWarnings("unchecked")
//			List<DBTag> tags = query.list();
//			for (Iterator<DBTag> iterator = tags.iterator(); iterator.hasNext();) {
//				DBTag tag = iterator.next();
//				System.out.println("DBTag Name: " + tag.getName());
//			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			throw new HibernateException(e);
		} finally {
			session.close();
		}
	}
	
	@AfterClass
	public static void cleanup() throws Throwable {
		DBSessionFactory.getInstance().close();
	}

}

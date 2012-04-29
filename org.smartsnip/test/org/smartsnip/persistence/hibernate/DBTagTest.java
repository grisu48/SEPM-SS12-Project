/**
 * File: DBTagTest.java
 * Date: 29.04.2012
 */
package org.smartsnip.persistence.hibernate;

import static org.junit.Assert.*;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
		// write some test-tag into the database
		SessionFactory factory = DBSessionFactory.getInstance();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			// build a new object to save
			DBTag tag2 = new DBTag();
			tag2.setName("!!!JUnit-DBTag-test-tag!!!");
			
			// save the tag object
			session.save(tag2);
				System.out.println("DBTag Saved: " + tag2.getName());
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

}

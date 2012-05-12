/**
 * File: TagFactory.java
 * Date: 11.05.2012
 */
package org.smartsnip.persistence.hibernate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.smartsnip.core.Snippet;
import org.smartsnip.core.Tag;
import org.smartsnip.persistence.IPersistence;

/**
 * Partial implementation of the methods of {@link SqlPersistenceImpl} defined
 * in {@link IPersistence}.
 * <p>
 * This part contains all methods relating to Tag.
 * 
 * @author littlelion
 * 
 */
public class TagFactory {

	private static SqlPersistenceHelper helper = new SqlPersistenceHelper();
	
	private TagFactory() {
		// no instances
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeTag(org.smartsnip.core.Tag,
	 *      int)
	 */
	static void writeTag(Tag tag, int flags) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#writeTag(java.util.List, int)
	 */
	static void writeTag(List<Tag> tags, int flags) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#removeTag(org.smartsnip.core.Tag,
	 *      int)
	 */
	static void removeTag(Tag tag, int flags) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getTags(org.smartsnip.core.Snippet)
	 */
	static List<Tag> getTags(Snippet snippet) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getAllTags(java.lang.Integer,
	 *      java.lang.Integer)
	 */
	static List<Tag> getAllTags(Integer start, Integer count)
			throws IOException {
		SessionFactory factory = DBSessionFactory.getInstance();
		Session session = factory.openSession();
		Transaction tx = null;
		int initialSize = 10;
		if (count != null && count > 0) {
			initialSize = count;
		}
		List<Tag> result = new ArrayList<Tag>(initialSize);

		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("FROM DBTag");
			if (start != null) {
				query.setFirstResult(start);
			}
			if (count != null && count > 0) {
				query.setFetchSize(count);
			}
			@SuppressWarnings("unchecked")
			List<DBTag> tags = query.list();
			for (Iterator<DBTag> iterator = tags.iterator(); iterator.hasNext();) {
				DBTag tag = iterator.next();
				result.add(helper.createTag(tag.getName()));
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			try {
				session.close();
			} catch (HibernateException e) {
				throw new IOException(e);
			}
		}
		return result;
	}

	/**
	 * @see org.smartsnip.persistence.IPersistence#getTagsCount()
	 */
	static int getTagsCount() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

}

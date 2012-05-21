/**
 * File: TagFactory.java
 * Date: 11.05.2012
 */
package org.smartsnip.persistence.hibernate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
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

	private TagFactory() {
		// no instances
	}

	/**
	 * Implementation of {@link IPersistence#writeTag(Tag, int)}
	 * 
	 * @param tag
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.IPersistence#writeTag(org.smartsnip.core.Tag,
	 *      int)
	 */
	static void writeTag(Tag tag, int flags) throws IOException {
		Session session = DBSessionFactory.open();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			DBTag entity = new DBTag();

			entity.setName(tag.toString());

			query.write(entity, flags);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
	}

	/**
	 * Implementation of {@link IPersistence#writeTag(List, int)}
	 * 
	 * @param tags
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.IPersistence#writeTag(java.util.List, int)
	 */
	static void writeTag(List<Tag> tags, int flags) throws IOException {
		Session session = DBSessionFactory.open();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query;
			DBTag entity;

			for (Tag tag : tags) {
				query = new DBQuery(session);
				entity = new DBTag();

				entity.setName(tag.toString());

				query.write(entity, flags);
			}
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
	}

	/**
	 * Implementation of {@link IPersistence#removeTag(Tag, int)}
	 * 
	 * @param tag
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.IPersistence#removeTag(org.smartsnip.core.Tag,
	 *      int)
	 */
	static void removeTag(Tag tag, int flags) throws IOException {
		Session session = DBSessionFactory.open();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			DBTag entity = new DBTag();
			entity.setName(tag.toString());

			query.remove(entity, flags);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
	}

	/**
	 * Implementation of {@link IPersistence#getTags(Snippet)}
	 * 
	 * @param snippet
	 * @return a list of tags
	 * @throws IOException
	 * @see org.smartsnip.persistence.IPersistence#getTags(org.smartsnip.core.Snippet)
	 */
	static List<Tag> getTags(Snippet snippet) throws IOException {
		Session session = DBSessionFactory.open();
		SqlPersistenceHelper helper = new SqlPersistenceHelper();
		List<Tag> result = new ArrayList<Tag>();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);
			DBSnippet entity = new DBSnippet();

			entity.setSnippetId(snippet.getHashId());
			entity = query.fromSingle(entity, DBQuery.QUERY_NOT_NULL);
			for (String tag : entity.getTags()) {
				result.add(helper.createTag(tag));
			}

			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
		return result;
	}

	/**
	 * Implementation of {@link IPersistence#getAllTags(Integer, Integer)}
	 * 
	 * @param start
	 * @param count
	 * @return a list of tags
	 * @throws IOException
	 * @see org.smartsnip.persistence.IPersistence#getAllTags(java.lang.Integer,
	 *      java.lang.Integer)
	 */
	static List<Tag> getAllTags(Integer start, Integer count)
			throws IOException {
		Session session = DBSessionFactory.open();
		SqlPersistenceHelper helper = new SqlPersistenceHelper();
		int initialSize = 10;
		if (count != null && count > 0) {
			initialSize = count;
		}
		List<Tag> result = new ArrayList<Tag>(initialSize);

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);
			DBTag entity = new DBTag();

			for (Iterator<DBTag> iterator = query.iterate(entity, start, count); iterator
					.hasNext();) {
				entity = iterator.next();
				result.add(helper.createTag(entity.getName()));
			}

			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
		return result;
	}

	/**
	 * Implementation of {@link IPersistence#getTagsCount()}
	 * 
	 * @return the number of tags
	 * @throws IOException
	 * @see org.smartsnip.persistence.IPersistence#getTagsCount()
	 */
	static int getTagsCount() throws IOException {
		Session session = DBSessionFactory.open();
		DBTag entity;
		int result = 0;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			entity = new DBTag();

			result = query.count(entity).intValue();
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
		return result;
	}

}

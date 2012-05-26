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
		List<Tag> result;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			result = fetchTags(helper, session, snippet.getHashId());

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

	/**
	 * Helper method to fetch all tags from a snippet.
	 * 
	 * @param helper
	 *            the PersisteceHelper object to create the tags
	 * @param session
	 *            the session in which the query is to execute
	 * @param snippetId
	 *            the id of the snippet as source of the code
	 * @return a list of code fragments
	 */
	static List<Tag> fetchTags(SqlPersistenceHelper helper, Session session,
			Long snippetId) {
		DBQuery query = new DBQuery(session);
		DBRelTagSnippet relationship = new DBRelTagSnippet();
		DBTag tag;
		List<Tag> result = new ArrayList<Tag>();
		relationship.setTagSnippetId(snippetId, null);
		for (Iterator<DBRelTagSnippet> itr = query.iterate(relationship); itr
				.hasNext();) {
			relationship = itr.next();
			query = new DBQuery(session);
			tag = new DBTag();
			tag.setName(relationship.getTagSnippetId().getTagName());
			tag = query.fromSingle(tag, DBQuery.QUERY_NOT_NULL);
			result.add(helper.createTag(tag.getName()));
		}
		return result;
	}

	/**
	 * Helper method to update all relationships of the tags of a snippet. New
	 * relationships to tags are pushed into the database, old are removed.
	 * 
	 * @param session
	 *            the session in which the query is to execute
	 * @param snippetId
	 *            the id of the snippet
	 * @param tags
	 *            a list of tags
	 * @param flags
	 *            the flags of the write query. Details see
	 *            {@link IPersistence#DB_DEFAULT}.
	 */
	static void updateRelTagSnippet(Session session, Long snippetId,
			List<Tag> tags, int flags) {
		System.out.println("pushRelTagSnippet: flags = " + flags);
		if (tags != null && !tags.isEmpty()) {
			DBQuery query = new DBQuery(session);
			DBRelTagSnippet entity = new DBRelTagSnippet();
			entity.setTagSnippetId(snippetId, null);
			List<DBRelTagSnippet> oldTags = query.from(entity);
			System.err.println("old tags: " + oldTags);// XXX

			for (Tag tag : tags) {
				query = new DBQuery(session);
				entity = new DBRelTagSnippet();
				entity.setTagSnippetId(snippetId, tag.toString());
				System.out.println("tag removed: " + oldTags.remove(entity)
						+ ", " + entity); // FIXME equals?
				System.err.println("DBRelTagSnippet: "
						+ entity.getTagSnippetId() + ", "
						+ entity.getTagSnippetId().getTagName() + ", "
						+ entity.getTagSnippetId().getSnippetId());// XXX
				query.write(entity, flags);
			}
			for (DBRelTagSnippet dbTag: oldTags){
				query = new DBQuery(session);
				query.remove(dbTag, flags);
			}
			System.err.println("remaining: " + oldTags);// XXX
		}
	}

	/**
	 * Helper method to persist all tags of a snippet into the database.
	 * 
	 * @param session
	 *            the session in which the query is to execute
	 * @param snippet
	 *            the snippet as source of the code
	 * @param flags
	 *            the flags of the write query. Details see
	 *            {@link IPersistence#DB_DEFAULT}.
	 */
	static void pushTags(Session session, Snippet snippet, int flags) {
		List<Tag> tags = snippet.getTags();
		if (tags != null && !tags.isEmpty()) {
			DBQuery query;
			DBTag entity;
			for (Tag tag : tags) {
				query = new DBQuery(session);
				entity = new DBTag();
				entity.setName(tag.toString());
				query.write(entity, flags);
			}
		}
	}

}

/**
 * File: SnippetFactory.java
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
import org.smartsnip.core.Category;
import org.smartsnip.core.Code;
import org.smartsnip.core.Snippet;
import org.smartsnip.core.Tag;
import org.smartsnip.core.User;
import org.smartsnip.persistence.IPersistence;
import org.smartsnip.persistence.hibernate.DBRating.RatingId;
import org.smartsnip.shared.Pair;

/**
 * @author littlelion
 * 
 */
public class SnippetFactory {

	private static SqlPersistenceHelper helper = new SqlPersistenceHelper();

	private SnippetFactory() {
		// no instances
	}

	/**
	 * Implementation of {@link IPersistence#writeSnippet(Snippet, int)}
	 * 
	 * @param snippet
	 * @param flags
	 * @return the Id
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeSnippet(org.smartsnip.core.Snippet,
	 *      int)
	 */
	static Long writeSnippet(Snippet snippet, int flags) throws IOException {
		Session session = DBSessionFactory.open();
		Long result;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);
			DBQuery licQuery;
			DBLicense dblicense;

			DBSnippet entity = new DBSnippet();
			// snippetId is read-only
			entity.setHeadline(snippet.getName());
			entity.setDescription(snippet.getDescription());
			// viewCount is read-only
			// lastEdited is read-only
			entity.setOwner(snippet.getOwner().getUsername());
			entity.setCategoryId(new Long(snippet.getCategory().hashCode()));

			dblicense = new DBLicense();
			licQuery = new DBQuery(session);
			dblicense.setShortDescr(snippet.getLicense());
			dblicense = licQuery.fromSingle(dblicense, DBQuery.QUERY_NULLABLE);
			entity.setLicenseId(dblicense.getLicenseId());

			entity.setTags(snippet.getStringTags());

			result = (Long) query.write(entity, flags);
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
	 * Implementation of {@link IPersistence#writeSnippet(List, int)}
	 * 
	 * @param snippets
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeSnippet(java.util.List,
	 *      int)
	 */
	static void writeSnippet(List<Snippet> snippets, int flags)
			throws IOException {
		Session session = DBSessionFactory.open();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);
			DBQuery licQuery;
			DBLicense dblicense;
			DBSnippet entity;

			for (Snippet snippet : snippets) {
				entity = new DBSnippet();
				// snippetId is read-only
				entity.setHeadline(snippet.getName());
				entity.setDescription(snippet.getDescription());
				// viewCount is read-only
				// lastEdited is read-only
				entity.setOwner(snippet.getOwner().getUsername());
				entity.setCategoryId(new Long(snippet.getCategory().hashCode()));

				dblicense = new DBLicense();
				licQuery = new DBQuery(session);
				dblicense.setShortDescr(snippet.getLicense());
				dblicense = licQuery.fromSingle(dblicense, DBQuery.QUERY_NULLABLE);
				entity.setLicenseId(dblicense.getLicenseId());

				entity.setTags(snippet.getStringTags());

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
	 * Implementation of {@link IPersistence#writeCode(Code, int)}
	 * 
	 * @param code
	 * @param flags
	 * @return the Id
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeCode(org.smartsnip.core.Code,
	 *      int)
	 */
	static Long writeCode(Code code, int flags) throws IOException {
		Session session = DBSessionFactory.open();
		Long result;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			DBCode entity = new DBCode();
			// codeId is read-only
			entity.setSnippetId(code.getHashID());
			entity.setLanguage(code.getLanguage());
			entity.setFile(code.getCode());
			entity.setVersion(code.getVersion());

			result = (Long) query.write(entity, flags);
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
	 * Implementation of {@link IPersistence#writeCode(List, int)}
	 * 
	 * @param codes
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeCode(java.util.List,
	 *      int)
	 */
	static void writeCode(List<Code> codes, int flags) throws IOException {
		Session session = DBSessionFactory.open();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			DBCode entity;

			for (Code code : codes) {
				entity = new DBCode();
				// codeId is read-only
				entity.setSnippetId(code.getHashID());
				entity.setLanguage(code.getLanguage());
				entity.setFile(code.getCode());
				entity.setVersion(code.getVersion());

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
	 * Implementation of {@link IPersistence#writeLanguage(String, int)}
	 * 
	 * @param language
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeLanguage(java.lang.String,
	 *      int)
	 */
	static void writeLanguage(String language, int flags) throws IOException {
		Session session = DBSessionFactory.open();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			DBLanguage entity = new DBLanguage();
			entity.setLanguage(language);

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
	 * Implementation of
	 * {@link IPersistence#writeRating(Integer, Snippet, User, int)}
	 * 
	 * @param rating
	 * @param snippet
	 * @param user
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeRating(java.lang.Integer,
	 *      org.smartsnip.core.Snippet, org.smartsnip.core.User, int)
	 */
	static void writeRating(Integer rating, Snippet snippet, User user,
			int flags) throws IOException {
		Session session = DBSessionFactory.open();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			DBRating entity = new DBRating();
			entity.setRatingId(snippet.getHashId(), user.getUsername());
			entity.setValue(rating);
			
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
	 * Implementation of {@link IPersistence#unRate(User, Snippet, int)}
	 * 
	 * @param user
	 * @param snippet
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#unRate(org.smartsnip.core.User,
	 *      org.smartsnip.core.Snippet, int)
	 */
	static void unRate(User user, Snippet snippet, int flags)
			throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * Implementation of {@link IPersistence#addFavourite(Snippet, User, int)}
	 * 
	 * @param snippet
	 * @param user
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#addFavourite(org.smartsnip.core.Snippet,
	 *      org.smartsnip.core.User, int)
	 */
	static void addFavourite(Snippet snippet, User user, int flags)
			throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * Implementation of
	 * {@link IPersistence#removeFavourite(Snippet, User, int)}
	 * 
	 * @param snippet
	 * @param user
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeFavourite(org.smartsnip.core.Snippet,
	 *      org.smartsnip.core.User, int)
	 */
	static void removeFavourite(Snippet snippet, User user, int flags)
			throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * Implementation of {@link IPersistence#removeSnippet(Snippet, int)}
	 * 
	 * @param snippet
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeSnippet(org.smartsnip.core.Snippet,
	 *      int)
	 */
	static void removeSnippet(Snippet snippet, int flags) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * Implementation of {@link IPersistence#removeCode(Code, int)}
	 * 
	 * @param code
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeCode(org.smartsnip.core.Code,
	 *      int)
	 */
	static void removeCode(Code code, int flags) throws IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * Implementation of {@link IPersistence#removeLanguage(String, int)}
	 * 
	 * @param language
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeLanguage(java.lang.String,
	 *      int)
	 */
	static void removeLanguage(String language, int flags) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * Implementation of {@link IPersistence#getUserSnippets(User)}
	 * 
	 * @param owner
	 * @return a list of snippets
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getUserSnippets(org.smartsnip.core.User)
	 */
	static List<Snippet> getUserSnippets(User owner) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Implementation of {@link IPersistence#getFavorited(User)}
	 * 
	 * @param user
	 * @return a list of snippets
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getFavorited(org.smartsnip.core.User)
	 */
	static List<Snippet> getFavorited(User user) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Implementation of {@link IPersistence#getSnippets(List)}
	 * 
	 * @param matchingTags
	 * @return a list of snippets
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getSnippets(java.util.List)
	 */
	static List<Snippet> getSnippets(List<Tag> matchingTags) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Implementation of
	 * {@link IPersistence#getSnippets(Category, Integer, Integer)}
	 * 
	 * @param category
	 * @param start
	 * @param count
	 * @return a list of snippets
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getSnippets(org.smartsnip.core.Category,
	 *      java.lang.Integer, java.lang.Integer)
	 */
	static List<Snippet> getSnippets(Category category, Integer start,
			Integer count) throws IOException {
		SessionFactory factory = DBSessionFactory.getInstance();
		Session session = factory.openSession();
		Transaction tx = null;
		int initialSize = 10;
		if (count != null && count > 0) {
			initialSize = count;
		}
		List<Snippet> result = new ArrayList<Snippet>(initialSize);

		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("FROM DBSnippet");
			// TODO implement where clause "category"
			if (start != null) {
				query.setFirstResult(start);
			}
			if (count != null && count > 0) {
				query.setFetchSize(count);
			}
			@SuppressWarnings("unchecked")
			List<DBSnippet> snips = query.list();
			for (Iterator<DBSnippet> iterator = snips.iterator(); iterator
					.hasNext();) {
				DBSnippet snippet = iterator.next();
				// TODO implement attributes for createSnippet
				result.add(helper.createSnippet(snippet.getSnippetId(), null,
						snippet.getHeadline(), snippet.getDescription(), null,
						null, null, null, null, snippet.getViewcount()));
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
	 * Implementation of {@link IPersistence#getSnippet(Long)}
	 * 
	 * @param id
	 * @return a snippet
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getSnippet(java.lang.Long)
	 */
	static Snippet getSnippet(Long id) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Implementation of {@link IPersistence#getCodes(Snippet)}
	 * 
	 * @param snippet
	 * @return a list of code fragments
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getCodes(org.smartsnip.core.Snippet)
	 */
	static List<Code> getCodes(Snippet snippet) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Implementation of {@link IPersistence#getAllLanguages()}
	 * 
	 * @return a list of languages
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getAllLanguages()
	 */
	static List<String> getAllLanguages() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Implementation of {@link IPersistence#getRatings(Snippet)}
	 * 
	 * @param snippet
	 * @return a list of ratings
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getRatings(org.smartsnip.core.Snippet)
	 */
	static List<Pair<User, Integer>> getRatings(Snippet snippet)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Implementation of {@link IPersistence#getAverageRating(Snippet)}
	 * 
	 * @param snippet
	 * @return the average rating value
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getAverageRating(org.smartsnip.core.Snippet)
	 */
	static Float getAverageRating(Snippet snippet) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Implementation of {@link IPersistence#search(String, Integer, Integer)}
	 * 
	 * @param searchString
	 * @param start
	 * @param count
	 * @return a list of snippets
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#search(java.lang.String,
	 *      java.lang.Integer, java.lang.Integer)
	 */
	static List<Snippet> search(String searchString, Integer start,
			Integer count) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Implementation of {@link IPersistence#getSnippetsCount()}
	 * 
	 * @return the number of snippets
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getSnippetsCount()
	 */
	static int getSnippetsCount() throws IOException {
		Session session = DBSessionFactory.open();
		DBSnippet entity;
		int result = 0;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			entity = new DBSnippet();

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

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
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeSnippet(org.smartsnip.core.Snippet,
	 *      int)
	 */
	static Long writeSnippet(Snippet snippet, int flags) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeSnippet(java.util.List,
	 *      int)
	 */
	static void writeSnippet(List<Snippet> snippets, int flags)
			throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeCode(org.smartsnip.core.Code,
	 *      int)
	 */
	static Long writeCode(Code code, int flags) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeCode(java.util.List,
	 *      int)
	 */
	static void writeCode(List<Code> codes, int flags) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeLanguage(java.lang.String,
	 *      int)
	 */
	static void writeLanguage(String language, int flags) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeRating(java.lang.Integer,
	 *      org.smartsnip.core.Snippet, org.smartsnip.core.User, int)
	 */
	static void writeRating(Integer rating, Snippet snippet, User user,
			int flags) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#unRate(org.smartsnip.core.User,
	 *      org.smartsnip.core.Snippet, int)
	 */
	static void unRate(User user, Snippet snippet, int flags)
			throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#addFavourite(org.smartsnip.core.Snippet,
	 *      org.smartsnip.core.User, int)
	 */
	static void addFavourite(Snippet snippet, User user, int flags)
			throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeFavourite(org.smartsnip.core.Snippet,
	 *      org.smartsnip.core.User, int)
	 */
	static void removeFavourite(Snippet snippet, User user, int flags)
			throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeSnippet(org.smartsnip.core.Snippet,
	 *      int)
	 */
	static void removeSnippet(Snippet snippet, int flags) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeLanguage(java.lang.String,
	 *      int)
	 */
	static void removeLanguage(String language, int flags) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getUserSnippets(org.smartsnip.core.User)
	 */
	static List<Snippet> getUserSnippets(User owner) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getFavorited(org.smartsnip.core.User)
	 */
	static List<Snippet> getFavorited(User user) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getSnippets(java.util.List)
	 */
	static List<Snippet> getSnippets(List<Tag> matchingTags) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param category
	 * @param start
	 * @param count
	 * @return
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
				result.add(helper.createSnippet(snippet.getSnippetId(),
						null, snippet.getHeadline(),
						snippet.getDescription(), null, null,
						null, null, null, snippet.getViewcount()));
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
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getSnippet(java.lang.Long)
	 */
	static Snippet getSnippet(Long id) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getCodes(org.smartsnip.core.Snippet)
	 */
	static List<Code> getCodes(Snippet snippet) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getAllLanguages()
	 */
	static List<String> getAllLanguages() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getRatings(org.smartsnip.core.Snippet)
	 */
	static List<Pair<User, Integer>> getRatings(Snippet snippet)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getAverageRating(org.smartsnip.core.Snippet)
	 */
	static Float getAverageRating(Snippet snippet) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#search(java.lang.String,
	 *      java.lang.Integer, java.lang.Integer)
	 */
	static List<Snippet> search(String searchString, Integer start,
			Integer count) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getSnippetsCount()
	 */
	static int getSnippetsCount() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

}

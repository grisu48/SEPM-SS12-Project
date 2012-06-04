/**
 * File: SnippetFactory.java
 * Date: 11.05.2012
 */
package org.smartsnip.persistence.hibernate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.smartsnip.core.Category;
import org.smartsnip.core.Code;
import org.smartsnip.core.Snippet;
import org.smartsnip.core.Tag;
import org.smartsnip.core.User;
import org.smartsnip.persistence.IPersistence;
import org.smartsnip.shared.Pair;

/**
 * @author littlelion
 * 
 */
public class SnippetFactory {

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
			entity.setSnippetId(snippet.getHashId()); // snippetId is read-only
			entity.setHeadline(snippet.getName());
			entity.setDescription(snippet.getDescription());
			// viewCount is read-only
			// lastEdited is read-only
			entity.setOwner(snippet.getOwnerUsername());
			entity.setCategoryId(CategoryFactory
					.fetchCategory(session, snippet).getCategoryId());

			dblicense = new DBLicense();
			licQuery = new DBQuery(session);
			dblicense.setShortDescr(snippet.getLicense());
			dblicense = licQuery.fromSingle(dblicense, DBQuery.QUERY_NULLABLE);
			if (dblicense != null) {
				entity.setLicenseId(dblicense.getLicenseId());
			}
			result = (Long) query.write(entity, flags);

			// allow new tags even if IPersistence.DB_UPDATE_ONLY flag is
			// present
			// skip existing tags even if IPersistence.DB_NEW_ONLY flag is
			// present
			TagFactory
					.pushTags(
							session,
							snippet,
							flags
									& ~(IPersistence.DB_UPDATE_ONLY | IPersistence.DB_NEW_ONLY));

			// allow new tag-snippet relationships even if
			// IPersistence.DB_UPDATE_ONLY flag is present
			TagFactory.updateRelTagSnippet(session, result, snippet.getTags(),
					flags & ~(IPersistence.DB_UPDATE_ONLY));

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
			DBQuery query;
			DBQuery licQuery;
			DBLicense dblicense;
			DBSnippet entity;
			Long snippetId;

			for (Snippet snippet : snippets) {
				query = new DBQuery(session);
				entity = new DBSnippet();
				// snippetId is read-only
				entity.setHeadline(snippet.getName());
				entity.setDescription(snippet.getDescription());
				// viewCount is read-only
				// lastEdited is read-only
				entity.setOwner(snippet.getOwnerUsername());
				entity.setCategoryId(new Long(snippet.getCategory().hashCode()));

				dblicense = new DBLicense();
				licQuery = new DBQuery(session);
				dblicense.setShortDescr(snippet.getLicense());
				dblicense = licQuery.fromSingle(dblicense,
						DBQuery.QUERY_NULLABLE);
				entity.setLicenseId(dblicense.getLicenseId());

				snippetId = (Long) query.write(entity, flags);

				// allow new tags even if IPersistence.DB_UPDATE_ONLY flag is
				// present
				// skip existing tags even if IPersistence.DB_NEW_ONLY flag is
				// present
				TagFactory
						.pushTags(
								session,
								snippet,
								flags
										& ~(IPersistence.DB_UPDATE_ONLY | IPersistence.DB_NEW_ONLY));

				// allow new tag-snippet relationships even if
				// IPersistence.DB_UPDATE_ONLY flag is present
				TagFactory.updateRelTagSnippet(session, snippetId,
						snippet.getTags(), flags
								& ~(IPersistence.DB_UPDATE_ONLY));
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
			entity.setSnippetId(code.getSnippet().getHashId());
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
			DBQuery query;

			DBCode entity;

			for (Code code : codes) {
				query = new DBQuery(session);
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
		Session session = DBSessionFactory.open();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			DBRating entity = new DBRating();
			entity.setRatingId(snippet.getHashId(), user.getUsername());

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
		Session session = DBSessionFactory.open();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			DBFavourite entity = new DBFavourite();
			entity.setFavouriteId(user.getUsername(), snippet.getHashId());
			entity.setFavourite(true);

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
		Session session = DBSessionFactory.open();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			DBFavourite entity = new DBFavourite();
			entity.setFavouriteId(user.getUsername(), snippet.getHashId());

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
	 * Implementation of {@link IPersistence#removeSnippet(Snippet, int)}
	 * 
	 * @param snippet
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeSnippet(org.smartsnip.core.Snippet,
	 *      int)
	 */
	static void removeSnippet(Snippet snippet, int flags) throws IOException {
		Session session = DBSessionFactory.open();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			DBSnippet entity = new DBSnippet();
			entity.setSnippetId(snippet.getHashId());

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
	 * Implementation of {@link IPersistence#removeCode(Code, int)}
	 * 
	 * @param code
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeCode(org.smartsnip.core.Code,
	 *      int)
	 */
	static void removeCode(Code code, int flags) throws IOException {
		Session session = DBSessionFactory.open();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			DBCode entity = new DBCode();
			entity.setCodeId(code.getHashID());

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
	 * Implementation of {@link IPersistence#removeLanguage(String, int)}
	 * 
	 * @param language
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeLanguage(java.lang.String,
	 *      int)
	 */
	static void removeLanguage(String language, int flags) throws IOException {
		Session session = DBSessionFactory.open();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			DBLanguage entity = new DBLanguage();
			entity.setLanguage(language);

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
	 * Implementation of {@link IPersistence#getUserSnippets(User)}
	 * 
	 * @param owner
	 * @return a list of snippets
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getUserSnippets(org.smartsnip.core.User)
	 */
	static List<Snippet> getUserSnippets(User owner) throws IOException {
		Session session = DBSessionFactory.open();
		SqlPersistenceHelper helper = new SqlPersistenceHelper();
		List<Snippet> result = new ArrayList<Snippet>();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);
			DBSnippet entity = new DBSnippet();
			Snippet snippet;
			entity.setOwner(owner.getUsername());

			for (Iterator<DBSnippet> iterator = query.iterate(entity); iterator
					.hasNext();) {
				entity = iterator.next();
				incrementViewcount(session, entity);

				snippet = helper.createSnippet(entity.getSnippetId(), owner
						.getUsername(), entity.getHeadline(), entity
						.getDescription(),
						CategoryFactory.fetchCategory(session, entity)
								.getName(), TagFactory.fetchTags(helper,
								session, entity.getSnippetId()),
						CommentFactory.fetchCommentIds(session,
								entity.getSnippetId()),
						fetchLicense(helper, session, entity).getShortDescr(),
						entity.getViewcount());
				snippet.setCode(fetchNewestCode(helper, session, snippet));
				result.add(snippet);
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
	 * Implementation of {@link IPersistence#getFavorited(User)}
	 * 
	 * @param user
	 * @return a list of snippets
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getFavorited(org.smartsnip.core.User)
	 */
	static List<Snippet> getFavorited(User user) throws IOException {
		Session session = DBSessionFactory.open();
		SqlPersistenceHelper helper = new SqlPersistenceHelper();
		List<Snippet> result = new ArrayList<Snippet>();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);
			DBFavourite entity = new DBFavourite();
			entity.setFavouriteId(user.getUsername(), null);
			entity.setFavourite(true);

			DBSnippet snip;
			DBQuery snipQuery;
			for (Iterator<DBFavourite> iterator = query.iterate(entity); iterator
					.hasNext();) {
				entity = iterator.next();
				snip = new DBSnippet();
				Snippet snippet;
				snip.setSnippetId(entity.getFavouriteId().getSnippetId());
				snipQuery = new DBQuery(session);
				snip = snipQuery.fromSingle(snip, DBQuery.QUERY_NOT_NULL);
				incrementViewcount(session, snip);

				snippet = helper.createSnippet(
						snip.getSnippetId(),
						snip.getOwner(),
						snip.getHeadline(),
						snip.getDescription(),
						CategoryFactory.fetchCategory(session, snip).getName(),
						TagFactory.fetchTags(helper, session,
								snip.getSnippetId()),
						CommentFactory.fetchCommentIds(session,
								snip.getSnippetId()),
						fetchLicense(helper, session, snip).getShortDescr(),
						snip.getViewcount());
				snippet.setCodeWithoutWriting(fetchNewestCode(helper, session,
						snippet));
				result.add(snippet);
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
	 * Implementation of {@link IPersistence#getSnippets(List)}
	 * 
	 * @param matchingTags
	 * @return a list of snippets
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getSnippets(java.util.List)
	 */
	static List<Snippet> getSnippets(List<Tag> matchingTags) throws IOException {
		Session session = DBSessionFactory.open();
		SqlPersistenceHelper helper = new SqlPersistenceHelper();
		List<Snippet> result = new ArrayList<Snippet>();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query;
			DBRelTagSnippet entity;
			DBSnippet snip;
			Snippet snippet;
			Set<Tag> tags = new TreeSet<Tag>(matchingTags);
			Set<Long> snippetIds = new TreeSet<Long>();

			// fetch all relationships of tags
			for (Tag tag : tags) {
				query = new DBQuery(session);
				entity = new DBRelTagSnippet();
				entity.setTagSnippetId(null, tag.toString());
				for (Iterator<DBRelTagSnippet> itr = query.iterate(entity); itr
						.hasNext();) {
					snippetIds.add(itr.next().getTagSnippetId().getSnippetId());
				}
			}

			for (Long id : snippetIds) {
				snip = new DBSnippet();
				snip.setSnippetId(id);
				query = new DBQuery(session);
				snip = query.fromSingle(snip, DBQuery.QUERY_NOT_NULL);
				incrementViewcount(session, snip);

				snippet = helper.createSnippet(snip.getSnippetId(),
						snip.getOwner(), snip.getHeadline(),
						snip.getDescription(),
						CategoryFactory.fetchCategory(session, snip).getName(),
						TagFactory.fetchTags(helper, session, id),
						CommentFactory.fetchCommentIds(session, id),
						fetchLicense(helper, session, snip).getShortDescr(),
						snip.getViewcount());
				snippet.setCode(fetchNewestCode(helper, session, snippet));
				result.add(snippet);
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
		Session session = DBSessionFactory.open();
		SqlPersistenceHelper helper = new SqlPersistenceHelper();
		int initialSize = 10;
		if (count != null && count > 0) {
			initialSize = count;
		}
		List<Snippet> result = new ArrayList<Snippet>(initialSize);

		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			DBQuery query = new DBQuery(session);
			DBCategory cat = new DBCategory();
			cat.setName(category.getName());
			cat = query.fromSingle(cat, DBQuery.QUERY_NOT_NULL);

			query = new DBQuery(session);
			DBSnippet entity = new DBSnippet();
			Snippet snippet;
			entity.setCategoryId(cat.getCategoryId());

			for (Iterator<DBSnippet> iterator = query.iterate(entity); iterator
					.hasNext();) {
				entity = iterator.next();
				incrementViewcount(session, entity);

				snippet = helper.createSnippet(entity.getSnippetId(), entity
						.getOwner(), entity.getHeadline(), entity
						.getDescription(),
						CategoryFactory.fetchCategory(session, entity)
								.getName(), TagFactory.fetchTags(helper,
								session, entity.getSnippetId()),
						CommentFactory.fetchCommentIds(session,
								entity.getSnippetId()),
						fetchLicense(helper, session, entity).getShortDescr(),
						entity.getViewcount());
				snippet.setCodeWithoutWriting(fetchNewestCode(helper, session,
						snippet));
				result.add(snippet);
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
	 * Implementation of {@link IPersistence#getSnippet(Long)}
	 * 
	 * @param id
	 * @return a snippet
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getSnippet(java.lang.Long)
	 */
	static Snippet getSnippet(Long id) throws IOException {
		Session session = DBSessionFactory.open();
		SqlPersistenceHelper helper = new SqlPersistenceHelper();
		Snippet result;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);
			DBSnippet entity = new DBSnippet();
			entity.setSnippetId(id);
			entity = query.fromSingle(entity, DBQuery.QUERY_NOT_NULL);
			incrementViewcount(session, entity);

			result = helper
					.createSnippet(entity.getSnippetId(), entity.getOwner(),
							entity.getHeadline(), entity.getDescription(),
							CategoryFactory.fetchCategory(session, entity)
									.getName(), TagFactory.fetchTags(helper,
									session, entity.getSnippetId()),
							CommentFactory.fetchCommentIds(session,
									entity.getSnippetId()),
							fetchLicense(helper, session, entity)
									.getShortDescr(), entity.getViewcount());
			result.setCodeWithoutWriting(fetchNewestCode(helper, session,
					result));

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
	 * Implementation of {@link IPersistence#getCodes(Snippet)}
	 * 
	 * @param snippet
	 * @return a list of code fragments
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getCodes(org.smartsnip.core.Snippet)
	 */
	static List<Code> getCodes(Snippet snippet) throws IOException {
		Session session = DBSessionFactory.open();
		SqlPersistenceHelper helper = new SqlPersistenceHelper();
		List<Code> result = new ArrayList<Code>();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);
			DBCode entity = new DBCode();

			DBSnippet snip = new DBSnippet();
			snip.setSnippetId(snippet.getHashId());
			snip = query.fromSingle(snip, DBQuery.QUERY_NOT_NULL);

			entity.setSnippetId(snip.getSnippetId());

			for (Iterator<DBCode> iterator = query.iterate(entity); iterator
					.hasNext();) {
				entity = iterator.next();
				result.add(helper.createCode(entity.getCodeId(),
						entity.getFile(), entity.getLanguage(), snippet,
						entity.getVersion()));
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
	 * Implementation of {@link IPersistence#getAllLanguages()}
	 * 
	 * @return a list of languages
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getAllLanguages()
	 */
	static List<String> getAllLanguages() throws IOException {
		Session session = DBSessionFactory.open();
		List<String> result = new ArrayList<String>();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);
			DBLanguage entity = new DBLanguage();

			for (Iterator<DBLanguage> iterator = query.iterate(entity); iterator
					.hasNext();) {
				entity = iterator.next();
				result.add(entity.getLanguage());
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
	 * Implementation of {@link IPersistence#getRatings(Snippet)}
	 * 
	 * @param snippet
	 * @return a list of ratings
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getRatings(org.smartsnip.core.Snippet)
	 */
	static List<Pair<User, Integer>> getRatings(Snippet snippet)
			throws IOException {
		Session session = DBSessionFactory.open();
		SqlPersistenceHelper helper = new SqlPersistenceHelper();
		List<Pair<User, Integer>> result = new ArrayList<Pair<User, Integer>>();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);
			DBRating entity = new DBRating();
			entity.setRatingId(snippet.getHashId(), null);

			DBUser dbUser;
			for (Iterator<DBRating> iterator = query.iterate(entity); iterator
					.hasNext();) {
				entity = iterator.next();

				dbUser = new DBUser();
				dbUser.setUserName(entity.getRatingId().getUserName());
				dbUser = query.fromSingle(dbUser, DBQuery.QUERY_NOT_NULL);

				result.add(new Pair<User, Integer>(helper.createUser(
						dbUser.getUserName(), dbUser.getFullName(),
						dbUser.getEmail(), dbUser.getUserState()), entity
						.getValue()));
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
	 * Implementation of {@link IPersistence#getAverageRating(Snippet)}
	 * 
	 * @param snippet
	 * @return the average rating value
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getAverageRating(org.smartsnip.core.Snippet)
	 */
	static Float getAverageRating(Snippet snippet) throws IOException {
		Session session = DBSessionFactory.open();
		DBSnippet entity;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);
			entity = new DBSnippet();
			entity.setSnippetId(snippet.getHashId());
			entity = query.fromSingle(entity, DBQuery.QUERY_NOT_NULL);

			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
		return entity.getRatingAverage();
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
	static synchronized List<Snippet> search(String searchString,
			Integer start, Integer count) throws IOException {
		Session session = DBSessionFactory.open();
		SqlPersistenceHelper helper = new SqlPersistenceHelper();
		List<Snippet> result = new ArrayList<Snippet>();

		FullTextSession fullTextSession = Search.getFullTextSession(session);
		Transaction tx = null;
		try {
			tx = fullTextSession.beginTransaction();

			// lucene full text query
			QueryBuilder builder = fullTextSession.getSearchFactory()
					.buildQueryBuilder().forEntity(DBSnippet.class).get();
			org.apache.lucene.search.Query ftQuery = builder.keyword()
					.onFields("headline", "description").matching(searchString)
					.createQuery();

			// wrap Lucene query in a org.hibernate.Query
			org.hibernate.Query query = fullTextSession.createFullTextQuery(
					ftQuery, DBSnippet.class);
			if (start != null && start > 0) {
				query.setFirstResult(start);
			}
			if (count != null && count > 0) {
				query.setFetchSize(count);
			}

			// execute search and build Snippets
			DBSnippet entity;
			Snippet snippet;
			@SuppressWarnings("unchecked")
			Iterator<DBSnippet> iterator = query.iterate();
			for (; iterator.hasNext();) {
				entity = iterator.next();
				snippet = helper.createSnippet(entity.getSnippetId(), entity
						.getOwner(), entity.getHeadline(), entity
						.getDescription(),
						CategoryFactory.fetchCategory(session, entity)
								.getName(), TagFactory.fetchTags(helper,
								session, entity.getSnippetId()),
						CommentFactory.fetchCommentIds(session,
								entity.getSnippetId()),
						fetchLicense(helper, session, entity).getShortDescr(),
						entity.getViewcount());
				snippet.setCodeWithoutWriting(fetchNewestCode(helper, session,
						snippet));
				result.add(snippet);
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

	/**
	 * Helper method to fetch all code fragments from a snippet.
	 * 
	 * @param helper
	 *            the PersisteceHelper object to create the tags
	 * @param session
	 *            the session in which the query is to execute
	 * @param snippet
	 *            the snippet as source of the code
	 * @return a list of code fragments
	 */
	static List<Code> fetchCode(SqlPersistenceHelper helper, Session session,
			Snippet snippet) {
		DBQuery query = new DBQuery(session);
		DBCode entity = new DBCode();
		List<Code> result = new ArrayList<Code>();
		entity.setSnippetId(snippet.getHashId());
		for (Iterator<DBCode> itr = query.iterate(entity); itr.hasNext();) {
			entity = itr.next();
			result.add(helper.createCode(entity.getCodeId(), entity.getFile(),
					entity.getLanguage(), snippet, entity.getVersion()));
		}
		return result;
	}

	/**
	 * Helper method to fetch the latest code from a snippet.
	 * 
	 * @param helper
	 *            the PersisteceHelper object to create the tags
	 * @param session
	 *            the session in which the query is to execute
	 * @param snippet
	 *            the snippet as source of the code
	 * @return the code fragment with highest version number
	 */
	static Code fetchNewestCode(SqlPersistenceHelper helper, Session session,
			Snippet snippet) {
		List<Code> codes = fetchCode(helper, session, snippet);
		Code result = null;
		for (Code code : codes) {
			if (result == null || result.getVersion() < code.getVersion()) {
				result = code;
			}
		}
		return result;
	}

	/**
	 * Helper method to fetch a license from a snippet.
	 * 
	 * @param helper
	 *            the PersisteceHelper object to create the tags
	 * @param session
	 *            the session in which the query is to execute
	 * @param snippet
	 *            the snippet as source of the license
	 * @return the DBLicense
	 */
	static DBLicense fetchLicense(SqlPersistenceHelper helper, Session session,
			DBSnippet snippet) {
		DBQuery query = new DBQuery(session);
		DBLicense entity = new DBLicense();
		entity.setLicenseId(snippet.getLicenseId());
		entity = query.fromSingle(entity, DBQuery.QUERY_NOT_NULL);
		return entity;
	}

	/**
	 * increment the viewcount of a snippet
	 * 
	 * @param session
	 *            the session in which the query is to execute
	 * @param snippet
	 *            the snippet as source of the viewcount
	 */
	static void incrementViewcount(Session session, DBSnippet snippet) {
		DBQuery query = new DBQuery(session);
		DBSnippet entity = new DBSnippet();
		entity.setSnippetId(snippet.getSnippetId());
		entity.setViewcount(snippet.getViewcount() + 1);
		query.update(entity, DBQuery.QUERY_SKIP_NULL);
	}
}

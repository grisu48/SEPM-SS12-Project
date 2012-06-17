/**
 * File: CategoryFactory.java
 * Date: 11.05.2012
 */
package org.smartsnip.persistence.hibernate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.smartsnip.core.Category;
import org.smartsnip.core.Snippet;
import org.smartsnip.persistence.IPersistence;

/**
 * @author littlelion
 * 
 */
public class CategoryFactory {

	private CategoryFactory() {
		// no instances
	}

	/**
	 * Implementation of {@link IPersistence#writeCategory(Category, int)}
	 * 
	 * @param category
	 * @param flags
	 * @return the Id
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeCategory(org.smartsnip.core.Category,
	 *      int)
	 */
	static Long writeCategory(Category category, int flags) throws IOException {
		Session session = DBSessionFactory.open();
		Long result;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);
			DBQuery parentQuery;
			DBCategory parent;

			DBCategory entity = new DBCategory();
			// categoryId is read-only

			if (category.getParentName() != null) {
				parent = new DBCategory();
				parentQuery = new DBQuery(session);
				parent.setName(category.getParentName());
				parent = parentQuery.fromSingle(parent, DBQuery.QUERY_NOT_NULL);
				entity.setParentId(parent.getCategoryId());
			} else {
				entity.setParentId(null);
			}

			entity.setName(category.getName()); // is NaturalId
			entity.setDescription(category.getDescription());

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
	 * Implementation of {@link IPersistence#writeCategory(List, int)}
	 * 
	 * @param categories
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeCategory(java.util.List,
	 *      int)
	 */
	static void writeCategory(List<Category> categories, int flags)
			throws IOException {
		Session session = DBSessionFactory.open();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query;
			DBQuery parentQuery;
			DBCategory parent;
			DBCategory entity;

			for (Category category : categories) {
				query = new DBQuery(session);
				entity = new DBCategory();
				// categoryId is read-only

				if (category.getParentName() != null) {
					parent = new DBCategory();
					parentQuery = new DBQuery(session);
					parent.setName(category.getParentName());
					parent = parentQuery.fromSingle(parent,
							DBQuery.QUERY_NOT_NULL);
					entity.setParentId(parent.getCategoryId());
				} else {
					entity.setParentId(null);
				}

				entity.setName(category.getName()); // is NaturalId
				entity.setDescription(category.getDescription());

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
	 * Implementation of {@link IPersistence#removeCategory(Category, int)}
	 * 
	 * @param category
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeCategory(org.smartsnip.core.Category,
	 *      int)
	 */
	static void removeCategory(Category category, int flags) throws IOException {
		Session session = DBSessionFactory.open();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			DBCategory entity = new DBCategory();
			entity.setName(category.getName());

			// connect the parent with the children
			DBCategory cat = query.fromSingle(entity,
					DBQuery.QUERY_UNIQUE_RESULT);
			if (cat != null) {
				query.reset();
				query.addParameter("categoryId", cat.getCategoryId());
				query.addParameter("parentId", cat.getParentId());
				query.customQueryWrite(
						"update DBCategory set parentId = :parentId where parentId = :categoryId",
						IPersistence.DB_DEFAULT);
			}

			// remove the category
			query.reset();
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
	 * Implementation of {@link IPersistence#getAllCategories()}
	 * 
	 * @return a list of categories
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getAllCategories()
	 */
	static List<Category> getAllCategories() throws IOException {
		Session session = DBSessionFactory.open();
		SqlPersistenceHelper helper = new SqlPersistenceHelper();
		Transaction tx = null;
		List<Category> result = new ArrayList<Category>();

		try { // set the parent
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);
			DBCategory entity = new DBCategory();
			Long parent;
			String parentName;

			Map<Long, DBCategory> categoryMap = new TreeMap<Long, DBCategory>();
			for (Iterator<DBCategory> iterator = query.iterate(entity,
					DBQuery.QUERY_CACHEABLE); iterator.hasNext();) {
				entity = iterator.next();
				categoryMap.put(entity.getCategoryId(), entity);
			}
			for (Map.Entry<Long, DBCategory> entry : categoryMap.entrySet()) {
				if ((parent = entry.getValue().getParentId()) != null) {
					parentName = categoryMap.get(parent).getName();
				} else {
					parentName = null;
				}
				result.add(helper.createCategory(entry.getValue().getName(),
						entry.getValue().getDescription(), parentName));
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
	 * Implementation of {@link IPersistence#getCategory(Snippet)}
	 * 
	 * @param snippet
	 * @return the category
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getCategory(org.smartsnip.core.Snippet)
	 */
	static Category getCategory(Snippet snippet) throws IOException {
		Session session = DBSessionFactory.open();
		SqlPersistenceHelper helper = new SqlPersistenceHelper();
		DBCategory entity;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			DBSnippet snip = new DBSnippet();
			snip.setSnippetId(snippet.getHashId());
			snip = query.fromSingle(snip, DBQuery.QUERY_NOT_NULL
					| DBQuery.QUERY_CACHEABLE);

			query = new DBQuery(session);
			entity = new DBCategory();
			entity.setCategoryId(snip.getCategoryId());
			entity = query.fromSingle(entity, DBQuery.QUERY_NOT_NULL
					| DBQuery.QUERY_CACHEABLE);

			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
		return helper.createCategory(entity.getName(), entity.getDescription(),
				fetchParentFlatend(helper, session, entity).getName());
	}

	/**
	 * Implementation of {@link IPersistence#getCategoryCount()}
	 * 
	 * @param name
	 * @return the category
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getCategory(java.lang.String)
	 */
	static Category getCategory(String name) throws IOException {
		Session session = DBSessionFactory.open();
		SqlPersistenceHelper helper = new SqlPersistenceHelper();
		DBCategory entity;
		Category result = null;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			entity = new DBCategory();
			entity.setName(name);
			entity = query.fromSingle(entity, DBQuery.QUERY_NOT_NULL
					| DBQuery.QUERY_CACHEABLE);

			result = helper.createCategory(entity.getName(),
					entity.getDescription(),
					fetchParentFlatend(helper, session, entity).getName());
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
	 * Implementation of {@link IPersistence#getParentCategory(Category)}
	 * 
	 * @param category
	 * @return the parent category
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getParentCategory(org.smartsnip.core.Category)
	 */
	static Category getParentCategory(Category category) throws IOException {
		Session session = DBSessionFactory.open();
		SqlPersistenceHelper helper = new SqlPersistenceHelper();
		DBCategory entity;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			entity = new DBCategory();
			entity.setName(category.getName());
			entity = query.fromSingle(entity, DBQuery.QUERY_NOT_NULL
					| DBQuery.QUERY_CACHEABLE);
			Long id = entity.getParentId();

			query = new DBQuery(session);
			entity = new DBCategory();
			entity.setCategoryId(id);
			entity = query.fromSingle(entity, DBQuery.QUERY_NOT_NULL
					| DBQuery.QUERY_CACHEABLE);

			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
		return helper.createCategory(entity.getName(), entity.getDescription(),
				fetchParentFlatend(helper, session, entity).getName());
	}

	/**
	 * Implementation of {@link IPersistence#getSubcategories(Category)}
	 * 
	 * @param category
	 * @return a list of subcategories
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getSubcategories(org.smartsnip.core.Category)
	 */
	static List<Category> getSubcategories(Category category)
			throws IOException {
		Session session = DBSessionFactory.open();
		SqlPersistenceHelper helper = new SqlPersistenceHelper();
		DBCategory entity;
		List<Category> result = new ArrayList<Category>();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			entity = new DBCategory();
			entity.setName(category.getName());
			entity = query.fromSingle(entity, DBQuery.QUERY_NOT_NULL
					| DBQuery.QUERY_CACHEABLE);
			Long id = entity.getCategoryId();

			query.reset(); // new query
			entity = new DBCategory();
			entity.setParentId(id);
			for (Iterator<DBCategory> iterator = query.iterate(entity,
					DBQuery.QUERY_CACHEABLE); iterator.hasNext();) {
				entity = iterator.next();
				result.add(helper.createCategory(entity.getName(),
						entity.getDescription(), category.getName()));
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
	 * Implementation of {@link IPersistence#getCategoryCount()}
	 * 
	 * @return the number of categories
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getCategoryCount()
	 */
	static int getCategoryCount() throws IOException {
		Session session = DBSessionFactory.open();
		DBCategory entity;
		int result = 0;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			entity = new DBCategory();

			result = query.count(entity, DBQuery.QUERY_CACHEABLE).intValue();
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
	 * Helper method to fetch a category from a snippet. Source is a
	 * {@link org.smartsnip.persistence.hibernate.DBSnippet}.
	 * 
	 * @param session
	 *            the session in which the query is to execute
	 * @param snippet
	 *            the snippet as source of the category
	 * 
	 * @return the category
	 */
	static DBCategory fetchCategory(Session session, DBSnippet snippet) {
		DBQuery query = new DBQuery(session);
		DBCategory entity = new DBCategory();
		entity.setCategoryId(snippet.getCategoryId());
		entity = query.fromSingle(entity, DBQuery.QUERY_NOT_NULL
				| DBQuery.QUERY_CACHEABLE);
		return entity;
	}

	/**
	 * Helper method to fetch a category from a snippet. Source is a
	 * {@link org.smartsnip.core.Snippet}.
	 * 
	 * @param session
	 *            the session in which the query is to execute
	 * @param snippet
	 *            the snippet as source of the category
	 * 
	 * @return the category
	 */
	static DBCategory fetchCategory(Session session, Snippet snippet) {
		DBQuery query = new DBQuery(session);
		DBCategory entity = new DBCategory();
		entity.setName(snippet.getCategoryName());
		entity = query.fromSingle(entity, DBQuery.QUERY_NOT_NULL
				| DBQuery.QUERY_CACHEABLE);
		return entity;
	}

	/**
	 * Helper method to fetch a parent of a category. This method initializes
	 * the parent field with {@code null}, so there is no tree up to the root
	 * created.
	 * 
	 * @param helper
	 *            the PersisteceHelper object to create the tags
	 * @param session
	 *            the session in which the query is to execute
	 * @param category
	 *            the category as source of the parent
	 * @return the category
	 */
	static Category fetchParentFlatend(SqlPersistenceHelper helper,
			Session session, DBCategory category) {
		DBQuery query = new DBQuery(session);
		DBCategory entity = new DBCategory();
		entity.setCategoryId(category.getParentId());
		entity = query.fromSingle(entity, DBQuery.QUERY_NOT_NULL
				| DBQuery.QUERY_CACHEABLE);
		return helper.createCategory(entity.getName(), entity.getDescription(),
				null); // flat object: no parent of the parent
	}

	/**
	 * Helper method to fetch a full initialized parent. This Method iterates
	 * through two queries to get the data to initialize the parent field.
	 * 
	 * @param helper
	 *            the PersisteceHelper object to create the tags
	 * @param session
	 *            the session in which the query is to execute
	 * @param category
	 *            the category as source of the parent
	 * @return the category
	 */
	static Category fetchParent(SqlPersistenceHelper helper, Session session,
			DBCategory category) {
		DBQuery query = new DBQuery(session);
		DBCategory entity = new DBCategory();
		entity.setCategoryId(category.getParentId());
		entity = query.fromSingle(entity, DBQuery.QUERY_NOT_NULL
				| DBQuery.QUERY_CACHEABLE);

		DBCategory parent = new DBCategory();
		parent.setCategoryId(entity.getParentId());
		parent = query.fromSingle(parent, DBQuery.QUERY_NOT_NULL
				| DBQuery.QUERY_CACHEABLE);

		return helper.createCategory(entity.getName(), entity.getDescription(),
				parent.getName());
	}
}

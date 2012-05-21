/**
 * File: CategoryFactory.java
 * Date: 11.05.2012
 */
package org.smartsnip.persistence.hibernate;

import java.io.IOException;
import java.util.List;

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

			parent = new DBCategory();
			parentQuery = new DBQuery(session);
			parent.setName(category.getParent().getName());
			parent = parentQuery.fromSingle(parent, DBQuery.QUERY_NOT_NULL);
			entity.setParentId(parent.getCategoryId());

			entity.setName(category.getName());
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
			DBQuery query = new DBQuery(session);
			DBQuery parentQuery;
			DBCategory parent;
			DBCategory entity;
			
			for (Category category : categories) {
				entity = new DBCategory();
				// categoryId is read-only

				parent = new DBCategory();
				parentQuery = new DBQuery(session);
				parent.setName(category.getParent().getName());
				parent = parentQuery.fromSingle(parent, DBQuery.QUERY_NOT_NULL);
				entity.setParentId(parent.getCategoryId());

				entity.setName(category.getName());
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
		// TODO Auto-generated method stub

	}

	/**
	 * Implementation of {@link IPersistence#getAllCategories()}
	 * 
	 * @return a list of categories
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getAllCategories()
	 */
	static List<Category> getAllCategories() throws IOException {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
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

/**
 * File: UserFactory.java
 * Date: 11.05.2012
 */
package org.smartsnip.persistence.hibernate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.smartsnip.core.Notification;
import org.smartsnip.core.User;
import org.smartsnip.persistence.IPersistence;

/**
 * Partial implementation of the methods of {@link SqlPersistenceImpl} defined
 * in {@link IPersistence}.
 * <p>
 * This part contains all methods relating to User.
 * 
 * @author Gerhard Aigner
 * 
 */
public class UserFactory {

	private UserFactory() {
		// no instances
	}

	/**
	 * Implementation of
	 * {@link IPersistence#writeUser(org.smartsnip.core.User, int)}
	 * 
	 * @param user
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeUser(org.smartsnip.core.User,
	 *      int)
	 */
	static void writeUser(User user, int flags) throws IOException {
		Session session = DBSessionFactory.open();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			DBUser entity = new DBUser();
			if (user == null || user.getUsername() == null
					|| user.getUsername().isEmpty()) {
				throw new IOException("empty key userName is invalid");
			}
			entity.setUserName(user.getUsername());
			entity.setFullName(user.getRealName());
			entity.setEmail(user.getEmail());
			entity.setUserState(user.getState());
			entity.setLastLogin(user.getDBSafeUserLastLogin());

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
	 * Implementation of {@link IPersistence#writeUser(java.util.List, int)}
	 * 
	 * @param users
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeUser(java.util.List,
	 *      int)
	 */
	static void writeUser(List<User> users, int flags) throws IOException {
		Session session = DBSessionFactory.open();
		DBUser entity;
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query;

			for (User user : users) {
				query = new DBQuery(session);
				entity = new DBUser();
				if (user == null || user.getUsername() == null
						|| user.getUsername().isEmpty()) {
					throw new IOException("empty key userName is invalid");
				}
				entity.setUserName(user.getUsername());
				entity.setFullName(user.getRealName());
				entity.setEmail(user.getEmail());
				entity.setUserState(user.getState());
				entity.setLastLogin(user.getDBSafeUserLastLogin());

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
	 * Implementation of
	 * {@link IPersistence#writeLogin(org.smartsnip.core.User, java.lang.String, Boolean, int)}
	 * 
	 * @param user
	 * @param password
	 * @param grantLogin
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeLogin(org.smartsnip.core.User,
	 *      java.lang.String, Boolean, int)
	 */
	static void writeLogin(User user, String password, Boolean grantLogin,
			int flags) throws IOException {
		Session session = DBSessionFactory.open();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			DBLogin entity = new DBLogin();
			if (user == null || user.getUsername() == null
					|| user.getUsername().isEmpty()) {
				throw new IOException("empty key userName is invalid");
			}
			entity.setUser(user.getUsername());
			entity.setPassword(password);
			entity.setGrantLogin(grantLogin);

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
	 * {@link IPersistence#writeNotification(org.smartsnip.core.Notification, int)}
	 * 
	 * @param notification
	 * @param flags
	 * @return the Id of the persisted entity
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeNotification(org.smartsnip.core.Notification,
	 *      int)
	 */
	static Long writeNotification(Notification notification, int flags)
			throws IOException {
		Long result;
		Session session = DBSessionFactory.open();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			DBNotification entity = new DBNotification();
			entity.setNotificationId(notification.getHashId());
			entity.setUserName(notification.getOwner());
			entity.setSnippetId(notification.getRefersToSnippet());
			entity.setViewed(notification.isRead());
			entity.setMessage(notification.getMessage());
			entity.setOrigin(notification.getSource());
			// createdAt not insertable - generated by DB

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
	 * Implementation of
	 * {@link IPersistence#writeNotification(java.util.List, int)}
	 * 
	 * @param notifications
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeNotification(java.util.List,
	 *      int)
	 */
	static void writeNotification(List<Notification> notifications, int flags)
			throws IOException {
		Session session = DBSessionFactory.open();

		DBNotification entity;
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query;

			for (Notification notification : notifications) {
				query = new DBQuery(session);
				entity = new DBNotification();
				entity.setNotificationId(notification.getHashId());
				entity.setUserName(notification.getOwner());
				entity.setSnippetId(notification.getRefersToSnippet());
				entity.setViewed(notification.isRead());
				entity.setMessage(notification.getMessage());
				entity.setOrigin(notification.getSource());
				// createdAt is read-only - generated by DB

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
	 * Implementation of {@link IPersistence#removeUser(User, int)}
	 * 
	 * @param user
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeUser(org.smartsnip.core.User,
	 *      int)
	 */
	static void removeUser(User user, int flags) throws IOException {
		Session session = DBSessionFactory.open();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			DBUser entity = new DBUser();
			if (user.getUsername() == null || user.getUsername().isEmpty()) {
				throw new IOException("empty key userName is invalid");
			}
			entity.setUserName(user.getUsername());

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
	 * Implementation of {@link IPersistence#removeLogin(User, int)}
	 * 
	 * @param user
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeLogin(org.smartsnip.core.User,
	 *      int)
	 */
	static void removeLogin(User user, int flags) throws IOException {
		Session session = DBSessionFactory.open();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			DBLogin entity = new DBLogin();
			if (user.getUsername() == null || user.getUsername().isEmpty()) {
				throw new IOException("empty key userName is invalid");
			}
			entity.setUser(user.getUsername());

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
	 * Implementation of
	 * {@link IPersistence#removeNotification(Notification, int)}
	 * 
	 * @param notification
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeNotification(org.smartsnip.core.Notification,
	 *      int)
	 */
	static void removeNotification(Notification notification, int flags)
			throws IOException {
		Session session = DBSessionFactory.open();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			DBNotification entity = new DBNotification();
			entity.setNotificationId(notification.getHashId());

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
	 * Implementation of {@link IPersistence#getUser(String)}
	 * 
	 * @param nick
	 * @return the user
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getUser(java.lang.String)
	 */
	static User getUser(String nick) throws IOException {
		Session session = DBSessionFactory.open();
		SqlPersistenceHelper helper = new SqlPersistenceHelper();
		DBUser entity;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			entity = new DBUser();
			if (nick == null || nick.isEmpty()) {
				throw new IOException("empty key userName is invalid");
			}
			entity.setUserName(nick);

			entity = query.fromSingle(entity, DBQuery.QUERY_NOT_NULL);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
		return helper
				.createUser(entity.getUserName(), entity.getFullName(),
						entity.getEmail(), entity.getUserState(),
						entity.getLastLogin());
	}

	/**
	 * Implementation of {@link IPersistence#getUserByEmail(String)}
	 * 
	 * @param email
	 * @return the user
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getUserByEmail(java.lang.String)
	 */
	static User getUserByEmail(String email) throws IOException {
		Session session = DBSessionFactory.open();
		SqlPersistenceHelper helper = new SqlPersistenceHelper();
		DBUser entity;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			entity = new DBUser();
			if (email == null || email.isEmpty()) {
				throw new IOException("empty argument email is invalid");
			}
			entity.setEmail(email);

			entity = query.fromSingle(entity, DBQuery.QUERY_NOT_NULL);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
		return helper
				.createUser(entity.getUserName(), entity.getFullName(),
						entity.getEmail(), entity.getUserState(),
						entity.getLastLogin());
	}

	/**
	 * Implementation of {@link IPersistence#getAllUsers(Integer, Integer)}
	 * 
	 * @param start
	 * @param count
	 * @return a list of users of the given range
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getAllUsers(Integer,
	 *      Integer)
	 */
	static List<User> getAllUsers(Integer start, Integer count)
			throws IOException {
		Session session = DBSessionFactory.open();
		SqlPersistenceHelper helper = new SqlPersistenceHelper();
		DBUser entity;
		List<User> result = new ArrayList<User>();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			entity = new DBUser();

			List<DBUser> entities = query.from(entity, start, count,
					IPersistence.DB_DEFAULT);
			for (DBUser user : entities) {
				result.add(helper.createUser(user.getUserName(),
						user.getFullName(), user.getEmail(),
						user.getUserState(), user.getLastLogin()));
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
	 * Implementation of {@link IPersistence#verifyPassword(User, String)}
	 * 
	 * @param user
	 * @param password
	 * @return true if access granted
	 * @throws IOException
	 * @throws UnsupportedOperationException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#verifyPassword(org.smartsnip.core.User,
	 *      java.lang.String)
	 */
	static boolean verifyPassword(User user, String password)
			throws IOException {
		boolean result = false;
		Session session = DBSessionFactory.open();
		DBLogin entity;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			entity = new DBLogin();
			if (user == null || user.getUsername() == null) {
				throw new IOException("empty username is invalid");
			}
			entity.setUser(user.getUsername());
			if (password != null && !password.isEmpty()) {
				entity.setPassword(password);
				Boolean granted = (Boolean) query.selectSingle(entity,
						"grantLogin", DBQuery.QUERY_NULLABLE);
				if (granted != null) {
					result = granted;
				}
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
	 * Implementation of {@link IPersistence#isLoginGranted(User)}
	 * 
	 * @param user
	 * @return true if grantLogin flag is set
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#isLoginGranted(User)
	 */
	static boolean isLoginGranted(User user) throws IOException {
		boolean result = false;
		Session session = DBSessionFactory.open();
		DBLogin entity;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			entity = new DBLogin();
			if (user == null || user.getUsername() == null) {
				throw new IOException("empty username is invalid");
			}
			entity.setUser(user.getUsername());
			Boolean granted = (Boolean) query.selectSingle(entity,
					"grantLogin", DBQuery.QUERY_NULLABLE);
			if (granted != null) {
				result = granted;
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
	 * Implementation of {@link IPersistence#findUser(String)}
	 * 
	 * @param realName
	 * @return a list of users
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#findUser(java.lang.String)
	 */
	static List<User> findUser(String realName) throws IOException {
		Session session = DBSessionFactory.open();
		SqlPersistenceHelper helper = new SqlPersistenceHelper();
		List<User> result = new ArrayList<User>();

		FullTextSession fullTextSession = Search.getFullTextSession(session);
		Transaction tx = null;
		try {
			tx = fullTextSession.beginTransaction();

			// lucene full text query
			QueryBuilder builder = fullTextSession.getSearchFactory()
					.buildQueryBuilder().forEntity(DBUser.class).get();
			org.apache.lucene.search.Query ftQuery = builder.keyword()
					.onFields("full_name").matching(realName).createQuery();

			// wrap Lucene query in a org.hibernate.Query
			org.hibernate.Query query = fullTextSession.createFullTextQuery(
					ftQuery, DBUser.class);

			// execute search and build Snippets
			DBUser entity;
			User user;
			@SuppressWarnings("unchecked")
			Iterator<DBUser> iterator = query.iterate();
			for (; iterator.hasNext();) {
				entity = iterator.next();
				user = helper.createUser(entity.getUserName(),
						entity.getFullName(), entity.getEmail(),
						entity.getUserState(), entity.getLastLogin());
				result.add(user);
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
	 * Implementation of {@link IPersistence#getNotification(Long)}
	 * 
	 * @param id
	 * @return the notification
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getNotification(java.lang.Long)
	 */
	static Notification getNotification(Long id) throws IOException {
		Session session = DBSessionFactory.open();
		SqlPersistenceHelper helper = new SqlPersistenceHelper();
		DBNotification entity;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			entity = new DBNotification();
			entity.setNotificationId(id);

			entity = query.fromSingle(entity, DBQuery.QUERY_NOT_NULL);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
		return helper
				.createNotification(entity.getNotificationId(), entity.getUserName(), entity.getMessage(), entity.isViewed(), entity.getCreatedAt(), entity.getOrigin(), entity.getSnippetId());
	}

	/**
	 * Implementation of {@link IPersistence#getNotifications(String, boolean)}
	 * 
	 * @param userName
	 * @param unreadOnly
	 * @return a list of notifications
	 * @throws IOException
	 * 
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getNotifications(java.lang.String,
	 *      boolean)
	 */
	static List<Notification> getNotifications(String userName,
			boolean unreadOnly) throws IOException {
		Session session = DBSessionFactory.open();
		SqlPersistenceHelper helper = new SqlPersistenceHelper();
		DBNotification entity;
		List<Notification> result = new ArrayList<Notification>();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			entity = new DBNotification();
			entity.setUserName(userName);
			if (unreadOnly) {
				entity.setViewed(false);
			}

			List<DBNotification> entities = query.from(entity,
					IPersistence.DB_DEFAULT);
			for (DBNotification notif : entities) {
				result.add(helper.createNotification(notif.getNotificationId(),
						userName, notif.getMessage(), notif.isViewed(), notif
								.getCreatedAt(), notif.getOrigin(),
						notif.getSnippetId()));
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
	 * Implementation of {@link IPersistence#getUserCount()}
	 * 
	 * @return the number of users
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getUserCount()
	 */
	static int getUserCount() throws IOException {
		Session session = DBSessionFactory.open();
		DBUser entity;
		int result = 0;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			entity = new DBUser();

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
}

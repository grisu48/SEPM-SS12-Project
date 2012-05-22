/**
 * File: UserFactory.java
 * Date: 11.05.2012
 */
package org.smartsnip.persistence.hibernate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.smartsnip.core.Notification;
import org.smartsnip.core.Snippet;
import org.smartsnip.core.User;
import org.smartsnip.persistence.IPersistence;

/**
 * Partial implementation of the methods of {@link SqlPersistenceImpl} defined
 * in {@link IPersistence}.
 * <p>
 * This part contains all methods relating to User.
 * 
 * @author littlelion
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
			entity.setNotificationId(notification.getId());
			entity.setUserName(notification.getOwner().getUsername());
			entity.setSnippetId(notification.getRefersToSnippet().getHashId());
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
				entity.setNotificationId(notification.getId());
				entity.setUserName(notification.getOwner().getUsername());
				entity.setSnippetId(notification.getRefersToSnippet()
						.getHashId());
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
			entity.setNotificationId(notification.getId());

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
		return helper.createUser(entity.getUserName(), entity.getFullName(),
				entity.getEmail(), entity.getUserState());
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
		return helper.createUser(entity.getUserName(), entity.getFullName(),
				entity.getEmail(), entity.getUserState());
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
			throws IOException, UnsupportedOperationException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Implementation of {@link IPersistence#isLoginGranted(User)}
	 * 
	 * @param user
	 * @return true if grantLogin flag is set
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#isLoginGranted(User)
	 */
	static boolean isLoginGranted(User user) {
		// TODO Auto-generated method stub
		return false;
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
		// TODO Auto-generated method stub
		// favourites = null
		return null;
	}

	/**
	 * Implementation of {@link IPersistence#getNotifications(User, boolean)}
	 * 
	 * @param user
	 * @param unreadOnly
	 * @return a list of notifications
	 * @throws IOException
	 * 
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getNotifications(org.smartsnip.core.User,
	 *      boolean)
	 */
	static List<Notification> getNotifications(User user, boolean unreadOnly)
			throws IOException {
		Session session = DBSessionFactory.open();
		SqlPersistenceHelper helper = new SqlPersistenceHelper();
		DBNotification entity;
		DBSnippet snip;
		Snippet snippet;
		List<Notification> result = new ArrayList<Notification>();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			entity = new DBNotification();
			if (user == null || user.getUsername() == null
					|| user.getUsername() == "") {
				throw new IOException("empty argument userName is invalid");
			}
			entity.setUserName(user.getUsername());
			if (unreadOnly) {
				entity.setViewed(false);
			}

			List<DBNotification> entities = query.from(entity);
			for (DBNotification n : entities) {
				if (n.getSnippetId() != null) {
					snip = new DBSnippet();
					snip.setSnippetId(n.getSnippetId());
					snip = new DBQuery(session).fromSingle(snip,
							DBQuery.QUERY_NOT_NULL);

					snippet = helper.createSnippet(entity.getSnippetId(), entity.getUserName(),
							snip.getHeadline(), snip.getDescription(),
							CategoryFactory
									.fetchCategory(helper, session, snip).getName(),
							SnippetFactory.buildTagList(helper, snip), null,
							SnippetFactory.fetchLicense(helper, session, snip)
									.getShortDescr(), snip.getViewcount());
					snippet.setCode(SnippetFactory.fetchNewestCode(helper,
							session, snippet));
				} else {
					snippet = null;
				}
				result.add(helper.createNotification(
						entity.getNotificationId(), user, entity.getMessage(),
						entity.isViewed(), entity.getCreatedAt().toString(),
						entity.getOrigin(), snippet));
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

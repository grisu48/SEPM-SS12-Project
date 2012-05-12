/**
 * File: UserFactory.java
 * Date: 11.05.2012
 */
package org.smartsnip.persistence.hibernate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.smartsnip.core.Notification;
import org.smartsnip.core.User;
import org.smartsnip.persistence.IPersistence;

/**
 * @author littlelion
 *
 */
public class UserFactory {

	private UserFactory() {
		// no instances
	}

	/**
	 * @param user 
	 * @param flags 
	 * @throws IOException 
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeUser(org.smartsnip.core.User, int)
	 */
	public void writeUser(User user, int flags) throws IOException {
		SessionFactory factory = DBSessionFactory.getInstance();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			if((flags & IPersistence.DB_NEW_ONLY) == IPersistence.DB_NEW_ONLY) {
					session.save(new DBUser(user.getUsername(), user.getRealName(), user.getEmail(), user.getState(), false));
			} else if ((flags & IPersistence.DB_UPDATE_ONLY) == IPersistence.DB_UPDATE_ONLY) {
				session.update(new DBUser(user.getUsername(), user.getRealName(), user.getEmail(), user.getState(), false));
			} else {
				session.saveOrUpdate(new DBUser(user.getUsername(), user.getRealName(), user.getEmail(), user.getState(), false));
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
	}

	/**
	 * @param users 
	 * @param flags 
	 * @throws IOException 
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeUser(java.util.List, int)
	 */
	public void writeUser(List<User> users, int flags) throws IOException {
		SessionFactory factory = DBSessionFactory.getInstance();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			if((flags & IPersistence.DB_NEW_ONLY) == IPersistence.DB_NEW_ONLY) {
				for (User u: users) {
					session.save(new DBUser(u.getUsername(), u.getRealName(), u.getEmail(), u.getState(), false));
				}
			} else if ((flags & IPersistence.DB_UPDATE_ONLY) == IPersistence.DB_UPDATE_ONLY) {
				for (User u: users) {
					session.update(new DBUser(u.getUsername(), u.getRealName(), u.getEmail(), u.getState(), false));
				}
			} else {
				for (User u: users) {
					session.saveOrUpdate(new DBUser(u.getUsername(), u.getRealName(), u.getEmail(), u.getState(), false));
				}
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
	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writePassword(org.smartsnip.core.User, java.lang.String, int)
	 */
	
	public void writePassword(User user, String password, int flags)
			throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeNotification(org.smartsnip.core.Notification, int)
	 */
	
	public Long writeNotification(Notification notification, int flags)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeNotification(java.util.List, int)
	 */
	
	public void writeNotification(List<Notification> notifications, int flags)
			throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeUser(org.smartsnip.core.User, int)
	 */
	
	public void removeUser(User user, int flags) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeNotification(org.smartsnip.core.Notification, int)
	 */
	
	public void removeNotification(Notification notification, int flags)
			throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getUser(java.lang.String)
	 */
	
	public User getUser(String nick) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getUserByEmail(java.lang.String)
	 */
	
	public User getUserByEmail(String email) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getPassword(org.smartsnip.core.User)
	 */
	
	public String getPassword(User user) throws IOException,
			UnsupportedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#verifyPassword(org.smartsnip.core.User, java.lang.String)
	 */
	
	public boolean verifyPassword(User user, String password)
			throws IOException, UnsupportedOperationException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#findUser(java.lang.String)
	 */
	
	public List<User> findUser(String realName) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getNotifications(org.smartsnip.core.User, boolean)
	 */
	
	public List<Notification> getNotifications(User user, boolean unreadOnly)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getUserCount()
	 */
	
	public int getUserCount() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}
	
}

/**
 * File: DBLogin.java
 * Date: 30.04.2012
 */
package org.smartsnip.persistence.hibernate;

import javax.persistence.*;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.DynamicInsert;
import org.smartsnip.persistence.IPersistence;

/**
 * Database OR mapping class for table Password
 * 
 * @author littlelion
 * 
 */
@Entity
@DynamicInsert
@Table(name = "Login")
class DBLogin {

	@Id
	@Column(name = "user_name", length = 20)
	private String user;

	@Column(name = "password", length = 255)
	@ColumnTransformer(read = "des_decrypt(password, 'd2aefeac9dc661bc98eebd6cc12f0b82')", write = "des_encrypt(?, 'd2aefeac9dc661bc98eebd6cc12f0b82')")
	private String password;

	@Column(name = "grant_login")
	private Boolean grantLogin;

	/**
	 * Entity, POJO class
	 */
	DBLogin() {
		super();
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return this.user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the grantLogin
	 */
	public Boolean getGrantLogin() {
		return this.grantLogin;
	}

	/**
	 * @param grantLogin
	 *            the grantLogin to set
	 */
	public void setGrantLogin(Boolean grantLogin) {
		this.grantLogin = grantLogin;
	}

	/**
	 * This method is called by the remove methods of IPersistence in
	 * {@code DB_NO_DELETE} mode. If this method is present the
	 * remove method defaults to {@link IPersistence#DB_NO_DELETE}.
	 */
	void disable() {
		this.setGrantLogin(false);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.user == null) ? 0 : this.user.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DBLogin other = (DBLogin) obj;
		if (this.user == null) {
			if (other.user != null)
				return false;
		} else if (!this.user.equals(other.user))
			return false;
		return true;
	}
}

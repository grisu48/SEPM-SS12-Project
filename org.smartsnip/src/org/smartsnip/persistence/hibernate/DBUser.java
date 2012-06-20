/**
 * File: DBUser.java
 * Date: 29.04.2012
 */
package org.smartsnip.persistence.hibernate;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.NaturalId;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.smartsnip.core.User;

/**
 * Database OR mapping class for table User
 * 
 * @author littlelion
 * 
 */
@Entity
@Indexed
@DynamicInsert
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="EntityCache")
@Table(name = "User")
class DBUser {
	@Id
	@DocumentId
	@Column(name = "user_name", length = 20)
	private String userName;

	@Column(name = "full_name", length = 255)
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.YES)
	private String fullName;

	@NaturalId
	@Column(name = "email", length = 255)
	private String email;

	@Column(name = "last_login")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLogin;
	
	@Column(name = "user_state")
	@Enumerated(EnumType.STRING)
	private User.UserState userState;

	/**
	 * Entity, POJO class
	 */
	DBUser() {
		super();
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return this.userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return this.fullName;
	}

	/**
	 * @param fullName
	 *            the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * @return the lastLogin
	 */
	public Date getLastLogin() {
		return this.lastLogin;
	}

	/**
	 * @param lastLogin the lastLogin to set
	 */
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the userState
	 */
	public User.UserState getUserState() {
		return this.userState;
	}

	/**
	 * @param userState
	 *            the userState to set
	 */
	public void setUserState(User.UserState userState) {
		this.userState = userState;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.email == null) ? 0 : this.email.hashCode());
		result = prime * result
				+ ((this.fullName == null) ? 0 : this.fullName.hashCode());
		result = prime * result
				+ ((this.lastLogin == null) ? 0 : this.lastLogin.hashCode());
		result = prime * result
				+ ((this.userName == null) ? 0 : this.userName.hashCode());
		result = prime * result
				+ ((this.userState == null) ? 0 : this.userState.hashCode());
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
		DBUser other = (DBUser) obj;
		if (this.email == null) {
			if (other.email != null)
				return false;
		} else if (!this.email.equals(other.email))
			return false;
		if (this.fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!this.fullName.equals(other.fullName))
			return false;
		if (this.lastLogin == null) {
			if (other.lastLogin != null)
				return false;
		} else if (!this.lastLogin.equals(other.lastLogin))
			return false;
		if (this.userName == null) {
			if (other.userName != null)
				return false;
		} else if (!this.userName.equals(other.userName))
			return false;
		if (this.userState != other.userState)
			return false;
		return true;
	}
}
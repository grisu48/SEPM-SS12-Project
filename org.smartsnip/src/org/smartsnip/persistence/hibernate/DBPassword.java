/**
 * File: DBPassword.java
 * Date: 30.04.2012
 */
package org.smartsnip.persistence.hibernate;

import javax.persistence.*;

import org.hibernate.annotations.ForeignKey;

/**
 * Database OR mapping class for table Password
 * @author littlelion
 * 
 */
@Entity
@Table(name = "Password")
class DBPassword {
	
	@Id
	@OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	@ForeignKey(name="DBUser.user_name")
	@Column(name="user_name", nullable=false, unique=true)
	String user;
	
	@Column(name="pwd_string", nullable=false)
	String password;

	/**
	 * 
	 */
	DBPassword() {
		super();
	}

	/**
	 * @param user
	 * @param password
	 */
	DBPassword(String user, String password) {
		super();
		this.user = user;
		this.password = password;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return this.user;
	}

	/**
	 * @param user the user to set
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
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}

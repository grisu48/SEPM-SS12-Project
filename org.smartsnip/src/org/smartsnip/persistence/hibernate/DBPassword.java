/**
 * File: DBPassword.java
 * Date: 30.04.2012
 */
package org.smartsnip.persistence.hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Database OR mapping class for table Password
 * 
 * @author littlelion
 * 
 */
@Entity
@Table(name = "Password")
class DBPassword {

	@Id
	@OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@ForeignKey(name = "DBUser.userName")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@Column(name = "user_name", length = 20)
	String user;

	@NotNull
	@Column(name = "pwd_string", length = 255)
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
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	// XXX remove this item if unused or on security reasons
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
}

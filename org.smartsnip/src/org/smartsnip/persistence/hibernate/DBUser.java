/**
 * File: DBUser.java
 * Date: 29.04.2012
 */
package org.smartsnip.persistence.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.NaturalId;
import org.smartsnip.core.User;

/**
 * Database OR mapping class for table User
 * 
 * @author littlelion
 * 
 */
@Entity
//TODO update hibernate see issue HHH-7074
//"the replacement annotations of @Entity are not working"
@SuppressWarnings("deprecation")
@org.hibernate.annotations.Entity(dynamicInsert = true)
//@DynamicInsert
@Table(name = "User")
class DBUser {
	@Id
	@Column(name = "user_name", length = 20)
	private String userName;

	@Column(name = "full_name", length = 255)
	private String fullName;

	@NaturalId
	@Column(name = "email", length = 255)
	private String email;

	@Column(name = "user_state")
	@Enumerated(EnumType.STRING)
	private User.UserState userState;

	/**
	 * 
	 */
	DBUser() {
		super();
	}

	// XXX remove constructor
	// /**
	// * @param userName
	// * @param fullName
	// * @param email
	// * @param userState
	// */
	// DBUser(String nickName, String fullName, String email,
	// User.UserState userState) {
	// super();
	// this.userName = nickName;
	// this.fullName = fullName;
	// this.email = email;
	// this.userState = userState;
	// }

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
}
/**
 * File: DBNotification.java
 * Date: 04.05.2012
 */
package org.smartsnip.persistence.hibernate;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author littlelion
 * 
 */
@Entity
//TODO update hibernate see issue HHH-7074
//"the replacement annotations of @Entity are not working"
@SuppressWarnings("deprecation")
@org.hibernate.annotations.Entity(dynamicInsert = true)
//@DynamicInsert
@Table(name = "Notification")
public class DBNotification {

	@Id
	@GeneratedValue
	@Column(name = "notification_id", insertable = false, updatable = false)
	private Long notificationId;

//	@ManyToOne(targetEntity = DBUser.class, fetch = FetchType.EAGER)
//	@ForeignKey(name = "DBUser.userName")
//	@OnDelete(action = OnDeleteAction.CASCADE)
	@Column(name = "user_name", length = 20)
	private String userName;

//	@ManyToOne(targetEntity = DBSnippet.class, fetch = FetchType.EAGER)
//	@ForeignKey(name = "DBSnippet.snippetId")
	@Column(name = "snippet_id")
	private Long snippetId;

	@Column(name = "viewed")
	private Boolean viewed;

	@Lob
	@Column(name = "message")
	private String message;

	@Column(name = "origin", length = 255)
	private String origin;

	@GeneratedValue
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", insertable = false, updatable = false)
	private Date createdAt;

	/**
	 * 
	 */
	public DBNotification() {
		super();
	}

	// XXX remove constructor
//	/**
//	 * @param notificationId
//	 * @param userName
//	 * @param snippetId
//	 * @param viewed
//	 * @param message
//	 * @param origin
//	 * @param createdAt
//	 */
//	DBNotification(Long notificationId, String userName, Long snippetId,
//			Boolean viewed, String message, String origin, Date createdAt) {
//		super();
//		this.notificationId = notificationId;
//		this.userName = userName;
//		this.snippetId = snippetId;
//		this.viewed = viewed;
//		this.message = message;
//		this.origin = origin;
//		this.createdAt = createdAt;
//	}

	/**
	 * @return the notificationId
	 */
	public Long getNotificationId() {
		return this.notificationId;
	}

	/**
	 * @param notificationId
	 *            the notificationId to set
	 */
	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
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
	 * @return the snippetId
	 */
	public Long getSnippetId() {
		return this.snippetId;
	}

	/**
	 * @param snippetId
	 *            the snippetId to set
	 */
	public void setSnippetId(Long snippetId) {
		this.snippetId = snippetId;
	}

	/**
	 * @return the viewed
	 */
	public Boolean isViewed() {
		return this.viewed;
	}

	/**
	 * @param viewed
	 *            the viewed to set
	 */
	public void setViewed(Boolean viewed) {
		this.viewed = viewed;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the origin
	 */
	public String getOrigin() {
		return this.origin;
	}

	/**
	 * @param origin
	 *            the origin to set
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	/**
	 * @return the createdAt
	 */
	public Date getCreatedAt() {
		return this.createdAt;
	}

	/**
	 * @param createdAt
	 *            the createdAt to set
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((this.notificationId == null) ? 0 : this.notificationId
						.hashCode());
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
		DBNotification other = (DBNotification) obj;
		if (this.notificationId == null) {
			if (other.notificationId != null)
				return false;
		} else if (!this.notificationId.equals(other.notificationId))
			return false;
		return true;
	}
}

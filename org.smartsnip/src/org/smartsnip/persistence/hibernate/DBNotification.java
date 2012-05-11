/**
 * File: DBNotification.java
 * Date: 04.05.2012
 */
package org.smartsnip.persistence.hibernate;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * @author littlelion
 * 
 */
@Entity
@Table(name = "Notification")
public class DBNotification {

	@Id
	@GeneratedValue
	@Column(name = "notification_id", insertable = false, updatable = false)
	private long notificationId;

	@NotNull
	@ManyToOne(targetEntity = DBUser.class, fetch = FetchType.EAGER)
	@ForeignKey(name = "DBUser.userName")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@Column(name = "user_name", length = 20)
	private String userName;

	@ManyToOne(targetEntity = DBSnippet.class, fetch = FetchType.EAGER)
	@ForeignKey(name = "DBSnippet.snippetId")
	@Column(name = "snippet_id")
	private long snippetId;

	@NotNull
	@Column(name = "viewed")
	private boolean viewed;

	@Lob
	@Column(name = "message")
	private String message;

	@Column(name = "origin", length = 255)
	private String origin;

	@NotNull
	@GeneratedValue
	@Column(name = "created_at", insertable = false, updatable = false)
	private Date createdAt;

	/**
	 * 
	 */
	public DBNotification() {
		super();
	}

	/**
	 * @param notificationId
	 * @param userName
	 * @param snippetId
	 * @param viewed
	 * @param message
	 * @param origin
	 * @param createdAt
	 */
	DBNotification(long notificationId, String userName, long snippetId,
			boolean viewed, String message, String origin, Date createdAt) {
		super();
		this.notificationId = notificationId;
		this.userName = userName;
		this.snippetId = snippetId;
		this.viewed = viewed;
		this.message = message;
		this.origin = origin;
		this.createdAt = createdAt;
	}

	/**
	 * @return the notificationId
	 */
	public long getNotificationId() {
		return this.notificationId;
	}

	/**
	 * @param notificationId
	 *            the notificationId to set
	 */
	public void setNotificationId(long notificationId) {
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
	public long getSnippetId() {
		return this.snippetId;
	}

	/**
	 * @param snippetId
	 *            the snippetId to set
	 */
	public void setSnippetId(long snippetId) {
		this.snippetId = snippetId;
	}

	/**
	 * @return the viewed
	 */
	public boolean isViewed() {
		return this.viewed;
	}

	/**
	 * @param viewed
	 *            the viewed to set
	 */
	public void setViewed(boolean viewed) {
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
}

package org.smartsnip.core;

import java.io.IOException;

import org.smartsnip.persistence.IPersistence;
import org.smartsnip.shared.XNotification;

public class Notification {

	/**
	 * identifier
	 */
	private final Long id;

	/**
	 * This user is the owner of the notificaiton. The owner is always the
	 * receiver of the notification
	 */
	private final String owner;
	/**
	 * Message text
	 */
	private final String message;
	/**
	 * True if the notification is read, otherwise false
	 */
	private boolean read;
	/**
	 * Time the notification was created
	 */
	private final String time;
	/**
	 * Source of the notification
	 */
	private final String source;

	/**
	 * Snippet to which the notification refers. Set null if the notification
	 * desn't refer to a snippet.
	 */
	private final Long refersToSnippet;

	/**
	 * Constructor for DB access
	 * 
	 * @param id
	 *            Hash id of the item
	 * @param owner
	 *            Username of the owner of the item
	 * @param message
	 *            Notification message
	 * @param read
	 *            Boolean, if the notification is read
	 * @param time
	 *            Timestamp (UNIX time)
	 * @param source
	 *            String to the source, that has sent this notification
	 * @param refersToSnippet
	 *            If the notification refers to a snippet (the id of the
	 *            snippet)
	 */
	Notification(Long id, String owner, String message, boolean read,
			String time, String source, Long refersToSnippet) {
		super();
		this.id = id;
		this.owner = owner;
		this.message = message;
		this.read = read;
		this.time = time;
		this.source = source;
		this.refersToSnippet = refersToSnippet;
	}

	/**
	 * @deprecated Use {@link #getHashId()} instant due to unique nomenclature
	 * @return the id
	 */
	@Deprecated
	public Long getId() {
		return this.id;
	}

	/**
	 * @return the hash id id
	 */
	public Long getHashId() {
		return this.id;
	}

	/**
	 * @return the owner of the notification
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * 
	 * @return the message of the notification
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the long of the snippet, where the object belongs to or Null, if
	 *         none
	 */
	public Long getRefersToSnippet() {
		return this.refersToSnippet;
	}

	/**
	 * @return true if read, false if unread
	 */
	public boolean isRead() {
		return read;
	}

	/**
	 * @return the sending time of the notification
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @return the source of the notification
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Marks the notification as read
	 */
	void markRead() {
		if (read == true)
			return;
		read = true;
		refreshDB();
	}

	/**
	 * Marks the notification as unread
	 */
	void markUnread() {
		if (read == false)
			return;
		read = false;
		refreshDB();
	}

	/**
	 * Invokes the refreshing czcle of the persistence
	 */
	protected synchronized void refreshDB() {
		try {
			Persistence.getInstance().writeNotification(this,
					IPersistence.DB_DEFAULT);
		} catch (IOException ex) {
			System.err.println("IOException during writing notification (id="
					+ this.getId() + "): " + ex.getMessage());
			ex.printStackTrace(System.err);
		}
	}

	/**
	 * Converts this object to a {@link XNotification} object
	 * 
	 * @return the converted {@link XNotification} object
	 */
	public XNotification toXNotification() {
		XNotification result = new XNotification();

		result.id = id;
		result.message = message;
		result.owner = owner;
		result.read = read;
		result.refersToSnippet = refersToSnippet;
		result.source = source;
		result.time = time;

		return result;
	}
}

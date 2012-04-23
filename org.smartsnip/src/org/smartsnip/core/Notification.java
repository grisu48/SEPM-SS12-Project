package org.smartsnip.core;

public class Notification {
	/**
	 * This user is the owner of the notificaiton. The owner is always the
	 * receiver of the notification
	 */
	private User owner;
	/**
	 * Message text
	 */
	private String message;
	/**
	 * True if the notification is read, otherwise false
	 */
	private boolean read;
	/**
	 * Time the notification was created
	 */
	private String time;
	/**
	 * Source of the notification
	 */
	private String source;

	/**
	 * @return the owner of the notification
	 */
	User getOwner() {
		return owner;
	}

	/**
	 * 
	 * @return the message of the notification
	 */
	String getMessage() {
		return message;
	}

	/**
	 * @return true if read, false if unread
	 */
	boolean isRead() {
		return read;
	}

	/**
	 * @return the sending time of the notification
	 */
	String getTime() {
		return time;
	}

	/**
	 * @return the source of the notification
	 */
	String getSource() {
		return source;
	}

	/**
	 * Marks the notification as read
	 */
	void markRead() {
		read = true;
		refreshDB();
	}

	/**
	 * Marks the notification as unread
	 */
	void markUnread() {
		read = false;
		refreshDB();
	}

	/**
	 * Invokes the refreshing czcle of the persistence
	 */
	protected synchronized void refreshDB() {
		// TODO Implement me
	}
}

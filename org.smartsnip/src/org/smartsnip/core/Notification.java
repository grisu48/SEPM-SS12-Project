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
	public User getOwner() {
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

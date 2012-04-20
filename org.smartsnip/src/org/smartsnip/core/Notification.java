package org.smartsnip.core;

public class Notification {
	private User owner;
	private String message;
	private boolean read;
	private String time;

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

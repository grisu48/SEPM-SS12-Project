package org.smartsnip.core;

public class Notification {
	/** Owner of the notification */
	private final User owner;
	/** Notification message */
	private final String message;
	/** Indicating if the notification message has been read */
	private boolean read;
	/** Send time of the notification */
	private final String time;

	/**
	 * 
	 * @param owner
	 *            of the notification. If null the notification is a system
	 *            notification
	 * @param message
	 *            of the notification. Must not be null or empty
	 * @param time
	 *            Sending time of the notification
	 */
	Notification(User owner, String message, String time) {
		super();
		this.owner = owner;
		this.message = message;
		this.time = time;
	}

	/**
	 * @return the owner of the notification. If null, the notification is a
	 *         system notification
	 */
	public synchronized final User getOwner() {
		return owner;
	}

	/**
	 * @return the message of the notification
	 */
	public synchronized final String getMessage() {
		return message;
	}

	/**
	 * @return if the notification is marked as read
	 */
	public synchronized final boolean isRead() {
		return read;
	}

	/**
	 * @return if the notification is marked as read
	 */
	public synchronized final void setRead(boolean isRead) {
		read = isRead;
	}

	/**
	 * @return the time the notification has been sent
	 */
	public synchronized final String getTime() {
		return time;
	}

}

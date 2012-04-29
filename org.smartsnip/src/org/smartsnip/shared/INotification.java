package org.smartsnip.shared;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * This class represents the access to a notification
 * 
 * @author phoenix
 * 
 */
@RemoteServiceRelativePath("notification")
public interface INotification extends RemoteService, IsSerializable {

	/** This class provides easy access to the proxy object */
	public static class Util {
		private static INotificationAsync instance = null;

		/** Get the proxy object instance */
		public static INotificationAsync getInstance() {
			if (instance == null) instance = GWT.create(INotification.class);
			return instance;
		}
	}

	/**
	 * Marks the notification as read
	 */
	public void markRead();

	/**
	 * Marks the notification as unread
	 */
	public void markUnread();

	/**
	 * Gets the read status of the notification
	 * 
	 * @return true if read, false if unread
	 */
	public boolean isRead();

	/**
	 * Gets the sender of the message.
	 * 
	 * @return the sender of the notification message
	 */
	public String getSource();

	/**
	 * @return the notification message text
	 */
	public String getMessage();

	/**
	 * @return the send time of the notification message
	 */
	public String getTime();

	/**
	 * Deletes the notification message
	 */
	public void delete();
}

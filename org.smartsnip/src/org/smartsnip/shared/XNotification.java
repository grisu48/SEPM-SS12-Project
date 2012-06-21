package org.smartsnip.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Notification exchange object.
 * 
 * @author Felix Niederwanger
 * 
 */
public class XNotification implements IsSerializable {
	/**
	 * identifier
	 */
	public Long id;

	/**
	 * This user is the owner of the notificaiton. The owner is always the
	 * receiver of the notification
	 */
	public String owner;
	/**
	 * Message text
	 */
	public String message;
	/**
	 * True if the notification is read, otherwise false
	 */
	public boolean read;
	/**
	 * Time the notification was created
	 */
	public String time;
	/**
	 * Source of the notification
	 */
	public String source;

	/**
	 * Snippet to which the notification refers. Set null if the notification
	 * desn't refer to a snippet.
	 */
	public Long refersToSnippet;
}

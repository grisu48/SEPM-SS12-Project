package org.smartsnip.core;

public class Notification {
	
	/**
	 * identifier
	 */
	private Long id;
	
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
	 * Snippet to which the notification refers.
	 * Set null if the notification desn't refer to a snippet.
	 */
	private Snippet refersToSnippet;

	/**
	 * Constructor for DB access
	 * @param id
	 * @param owner
	 * @param message
	 * @param read
	 * @param time
	 * @param source
	 * @param refersToSnippet
	 */
	Notification(Long id, User owner, String message, boolean read, String time,
			String source, Snippet refersToSnippet) {
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
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

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
	 * @return the refersToSnippet
	 */
	public Snippet getRefersToSnippet() {
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

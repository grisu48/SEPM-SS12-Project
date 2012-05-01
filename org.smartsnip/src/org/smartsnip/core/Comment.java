package org.smartsnip.core;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.smartsnip.persistence.IPersistence;
import org.smartsnip.shared.Pair;

public class Comment {
	/** The owner of the message is fixed */
	public final User owner;
	/** The snippet where the comment belongs is fixed */
	public final Snippet snippet;
	/** Comment message. */
	private String message = "";

	/** Hash code of the comment */
	private long hashcode;

	/** Last change time */
	private Date time = null;

	/** Positive votes are stored as chocolates */
	private int chocolates = 0;
	/** Negative votes are stored as lemons */
	private int lemons = 0;

	/**
	 * Creates a new comment. If one of the arguments if null a new
	 * {@link NullPointerException} will be thrown, and if the message is empty
	 * a new {@link IllegalArgumentException} will be thrown
	 * 
	 * @param owner
	 *            of the comment
	 * @param snippet
	 *            of the comment
	 * @param message
	 *            of the comment
	 */
	Comment(User owner, Snippet snippet, String message) {
		if (owner == null || snippet == null || message == null)
			throw new NullPointerException();
		if (message.length() == 0)
			throw new IllegalArgumentException("Cannot create empty comment box");

		this.owner = owner;
		this.snippet = snippet;
		this.message = message;
		setCurrentSystemTime();

		chocolates = 0;
		lemons = 0;
	}

	/**
	 * Creates a comment and adds this comment to the snippet
	 * 
	 * If one of the arguments if null a new {@link NullPointerException} will
	 * be thrown, and if the message is empty a new
	 * {@link IllegalArgumentException} will be thrown
	 * 
	 * @param owner
	 *            of the comment
	 * @param snippet
	 *            of the comment
	 * @param message
	 *            of the comment
	 * @return the newly created comment if success
	 */
	static Comment createComment(User owner, Snippet snippet, String message) {
		Comment comment = new Comment(owner, snippet, message);
		snippet.addComment(comment);
		comment.setCurrentSystemTime();
		return comment;
	}

	/**
	 * The given user wants to rate the comment positive. If the user has
	 * already voted, this call will be ignored
	 * 
	 * @param user
	 *            that wants to vote
	 */
	synchronized void votePositive(User user) {
		if (user == null)
			return;
		try {
			Persistence.instance.votePositive(user, this, IPersistence.DB_DEFAULT);
			updateVotes();
		} catch (IOException e) {
			System.err.println("IOException during votePositive(" + user.getUsername() + ") " + e.getMessage());
			e.printStackTrace(System.err);
		}
	}

	/**
	 * The given user wants to rate the comment negative. If the user has
	 * already voted, this call will be ignored
	 * 
	 * @param user
	 *            that wants to vote
	 */
	synchronized void voteNegative(User user) {
		if (user == null)
			return;
		try {
			Persistence.instance.voteNegative(user, this, IPersistence.DB_DEFAULT);
			updateVotes();
		} catch (IOException e) {
			System.err.println("IOException during voteNegative(" + user.getUsername() + ") " + e.getMessage());
			e.printStackTrace(System.err);
		}

	}

	/**
	 * Unvotes the vote of the given user. If the user has no vote given,
	 * nothing happens.
	 * 
	 * @param user
	 *            that wants to unvote
	 */
	synchronized void unvote(User user) {
		if (user == null)
			return;
		try {
			Persistence.instance.unVote(user, this, IPersistence.DB_DEFAULT);
			updateVotes();
		} catch (IOException e) {
			System.err.println("IOException during unvote(" + user.getUsername() + ") " + e.getMessage());
			e.printStackTrace(System.err);
		}
	}

	/**
	 * @return the negative votes of the comment
	 */
	public synchronized int getNegativeVotes() {
		updateVotes();
		return lemons;
	}

	/**
	 * @return the positive votes of the comment
	 */
	public synchronized int getPositiveVotes() {
		updateVotes();
		return chocolates;
	}

	/**
	 * @return the total votes of the comment
	 */
	public synchronized int getTotalVotes() {
		updateVotes();
		return lemons + chocolates;
	}

	/**
	 * @return the comments message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the last time the comment was edited
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * Sets the time to the current system time
	 */
	private void setCurrentSystemTime() {
		Calendar cal = Calendar.getInstance();
		this.time = cal.getTime();
	}

	@Override
	public String toString() {
		return message;
	}

	/**
	 * Deletes this comment out of the database
	 */
	void delete() {
		snippet.removeComment(this);
		removeFromDB();
	}

	/**
	 * Edits the current message to a new message. If the given message is null
	 * or empty, the method returns without effect.
	 * 
	 * @param newMessage
	 */
	void edit(String newMessage) {
		if (newMessage == null || newMessage.isEmpty())
			return;

		// TODO: Message change history

		this.message = newMessage;
		setCurrentSystemTime();
	}

	/**
	 * Invokes the refreshing process for the database
	 */
	protected void refreshDB() {

	}

	/**
	 * Removes this object from the database
	 */
	protected void removeFromDB() {
		// Nothing to do yet
	}

	/**
	 * The hash code of the comment object
	 */
	@Override
	public int hashCode() {
		return ((Long) hashcode).hashCode();
	}

	/**
	 * @return The internal unique hash code of the comment
	 */
	long getHash() {
		return hashcode;
	}

	/**
	 * Updates the votes
	 */
	private void updateVotes() {
		Pair<Integer, Integer> votes;
		try {
			votes = Persistence.instance.getVotes(this);
			chocolates = votes.first;
			lemons = votes.second;
		} catch (IOException e) {
			System.err.println("IOException during getVotes() " + e.getMessage());
			e.printStackTrace(System.err);
		}
	}
}

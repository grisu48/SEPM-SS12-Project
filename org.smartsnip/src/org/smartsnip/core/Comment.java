package org.smartsnip.core;

import java.util.HashMap;

public class Comment {
	/** The owner of the message is fixed */
	public final User owner;
	/** The snippet where the comment belongs is fixed */
	public final Snippet snippet;
	/** Comment message */
	private String message = "";

	/** Positive votes are stored as chocolates */
	private int chocolates = 0;
	/** Negative votes are stored as lemons */
	private int lemons = 0;

	/**
	 * Votes that are given for this comment. A 1 as key indicates a chocolate,
	 * a -1 a lemon. All other entries are ignored
	 */
	private HashMap<User, Integer> votes = new HashMap<User, Integer>();

	/**
	 * Creates a new comment. If one of the arguments if null a new
	 * {@link NullPointerException} will be thrown, and if the message is empty
	 * a new {@link IllegalArgumentException} is thrown
	 * 
	 * @param owner
	 *            of the comment
	 * @param snippet
	 *            of the comment
	 * @param message
	 *            of the comment
	 */
	Comment(User owner, Snippet snippet, String message) {
		if (owner == null || snippet == null || message == null) throw new NullPointerException();
		if (message.length() == 0) throw new IllegalArgumentException("Cannot create empty comment box");

		this.owner = owner;
		this.snippet = snippet;
		this.message = message;
	}

	/**
	 * The given user wants to rate the comment positive. If the user has
	 * already voted, this call will be ignored
	 * 
	 * @param user
	 *            that wants to vote
	 */
	synchronized void votePositive(User user) {
		if (votes.containsKey(user)) if (Math.abs(votes.get(user)) == 1) return;
		chocolates++;
		votes.put(user, 1);
		refreshDB();
	}

	/**
	 * The given user wants to rate the comment negative. If the user has
	 * already voted, this call will be ignored
	 * 
	 * @param user
	 *            that wants to vote
	 */
	synchronized void voteNegative(User user) {
		if (votes.containsKey(user)) if (Math.abs(votes.get(user)) == 1) return;
		chocolates++;
		votes.put(user, -1);
		refreshDB();
	}

	/**
	 * Unvotes the vote of the given user. If the user has no vote given,
	 * nothing happens.
	 * 
	 * @param user
	 *            that wants to unvote
	 */
	synchronized void unvote(User user) {
		if (!votes.containsKey(user)) return;
		int vote = votes.get(user);
		if (vote == 1) {
			chocolates--;
		} else if (vote == -1) {
			lemons--;
		}
		votes.remove(user);
		refreshDB();
	}

	/**
	 * @return the negative votes of the comment
	 */
	public synchronized int getNegativeVotes() {
		return lemons;
	}

	/**
	 * @return the positive votes of the comment
	 */
	public synchronized int getPositiveVotes() {
		return chocolates;
	}

	/**
	 * @return the total votes of the comment
	 */
	public synchronized int getTotalVotes() {
		return lemons + chocolates;
	}

	/**
	 * @return the comments message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message of the comment. If the message is null or emtpy, nothing
	 * is done.
	 * 
	 * @param message
	 *            the message to set
	 */
	void setMessage(String message) {
		if (message == null || message.length() == 0) return;
		this.message = message;
		refreshDB();
	}

	@Override
	public String toString() {
		return message;
	}

	/**
	 * Invokes the refreshing process for the database
	 */
	protected void refreshDB() {

	}

}

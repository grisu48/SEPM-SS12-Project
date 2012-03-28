package org.smartsnip.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A snippet of the system. TODO: Write me
 * 
 * The hash code if the snippet is the internal integer hash.
 * 
 */
public class Snippet {
	/** All stored snippets of the system */
	private final static HashMap<Integer, Snippet> allSnippets = new HashMap<Integer, Snippet>();

	/** Internal hash counter used to get the next hash */
	private static int hashCounter = 0;

	/** Owner (creator) of the snippet */
	public final User owner;
	/** Snippet name */
	private String name;
	/** Unique hash code of the snippet */
	public final int hash;
	/** Snippet description */
	private String description;
	/** Snippet associated category */
	private Category category;
	/** Tags of the snippet */
	private List<Tag> tags = new ArrayList<Tag>();
	/** Comments of the snippet */
	private List<Comment> comments = new ArrayList<Comment>();
	/** Concrete snippet code */
	private Code code;
	/** Licence of the snippet. By default GPLv3 */
	private String license = "GPLv3";
	/** View counter */
	private int viewcount = 0;

	@Override
	public int hashCode() {
		return hash;
	}

	/**
	 * Constructor with all fields that MUST be initialised. If one of the field
	 * is null a {@link NullPointerException} will be thrown. If one of the
	 * field is empty, a {@link IllegalArgumentException} will be thrown
	 * 
	 * @param owner
	 *            of the snippet
	 * @param name
	 *            of the snippet
	 * @param description
	 *            of the snippet
	 * @param code
	 *            of the snippet
	 * @param hash
	 *            hash code of the snippet
	 * @throws IllegalArgumentException
	 *             Thrown if one of the arguments is illegal
	 * @throws IllegalStateException
	 *             Thrown if the hash code is already in the system
	 */
	Snippet(User owner, String name, String description, Code code, int hash) {
		if (owner == null || name == null || description == null || code == null) throw new NullPointerException();
		if (name.length() == 0) throw new IllegalArgumentException("Snippet name cannot be empty");
		if (description.length() == 0) throw new IllegalArgumentException("Snippet description cannot be empty");
		if (exists(hash)) throw new IllegalStateException("Given hash code already exists");

		this.owner = owner;
		this.name = name;
		this.description = description;
		this.code = code;
		this.hash = hash;
	}

	/**
	 * @return the next available hash code
	 */
	private synchronized static int getNextHashCode() {
		while (exists(++hashCounter))
			;
		return hashCounter;
	}

	/**
	 * Checks if the given hash code exists
	 * 
	 * @param hash
	 *            hash code to be checks
	 * @return true if already registered hash, otherwise false
	 */
	synchronized static boolean exists(int hash) {
		return allSnippets.containsKey(hash);
	}

	/**
	 * Searches for a snipped by his hash code and returns it if found. If no
	 * snippet is found, null is returned
	 * 
	 * @param hash
	 *            hash code of the snippet
	 * @return the found snippet or null if not existsing
	 */
	public synchronized Snippet getSnippet(int hash) {
		return allSnippets.get(hash);
	}

	/**
	 * @return the name
	 */
	String getName() {
		return name;
	}

	/**
	 * Sets the name of the snippet. If the name is null or empty, nothing will
	 * be done.
	 * 
	 * @param name
	 *            the name to set
	 */
	void setName(String name) {
		if (name == null || name.length() == 0) return;
		if (this.name.equals(name)) return;

		this.name = name;
		refreshDB();
	}

	/**
	 * @return the description
	 */
	String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the snippet. If the description is empty or null,
	 * nothing is done.
	 * 
	 * @param description
	 *            the description to set
	 */
	void setDescription(String description) {
		if (description == null || description.length() == 0) return;
		if (this.description.equals(description)) return;

		this.description = description;
		refreshDB();
	}

	/**
	 * @return the code of the snippet
	 */
	Code getCode() {
		return code;
	}

	/**
	 * Sets the code of the snippet. If null, nothing is done.
	 * 
	 * @param code
	 *            the code to set
	 */
	void setCode(Code code) {
		if (code == null) return;
		if (this.code.equals(code)) return;

		this.code = code;
		refreshDB();
	}

	@Override
	public String toString() {
		return "Snippet: " + name;
	}

	/**
	 * @return the category of the snippet
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * Set the snippet category. If null nothing happens
	 * 
	 * @param category
	 *            the category to set
	 */
	void setCategory(Category category) {
		if (category == null) return;
		if (this.category == category) return;

		this.category = category;
		refreshDB();
	}

	/**
	 * @return the license of the snippet
	 */
	public String getLicense() {
		return license;
	}

	/**
	 * Sets the new license of the snippet. If the license is null or empty,
	 * nothing will be done
	 * 
	 * @param license
	 *            the license to set
	 */
	void setLicense(String license) {
		if (license == null || license.length() == 0) return;
		if (this.license.equals(license)) return;
		this.license = license;
		refreshDB();
	}

	/**
	 * @return the tags of the snippet
	 */
	List<Tag> getTags() {
		return tags;
	}

	/**
	 * @return the comments of the snippet
	 */
	List<Comment> getComments() {
		return comments;
	}

	/**
	 * @return the counts of view
	 */
	int getViewcount() {
		return viewcount;
	}

	synchronized void increaseViewCounter() {
		viewcount++;
		refreshDB();
	}

	/**
	 * Adds a comment to the snippet.
	 * 
	 * @param comment
	 *            to be added
	 */
	void addComment(Comment comment) {
		comments.add(comment);
		refreshDB();
	}

	/**
	 * Removes a comment from the snippet
	 * 
	 * @param comment
	 *            to be removed
	 */
	void removeComment(Comment comment) {
		if (comments.contains(comment)) comments.remove(comment);
		refreshDB();
	}

	/**
	 * Invokes the refreshing process for the database
	 */
	protected void refreshDB() {

	}
}

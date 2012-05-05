package org.smartsnip.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.smartsnip.persistence.IPersistence;
import org.smartsnip.shared.XSnippet;

/**
 * A snippet of the system. TODO: Write me
 * 
 * The hash code if the snippet is the internal integer hash.
 * 
 */
public class Snippet {
	/** Internal hash counter used to get the next hash */
	private static int hashCounter = 0;

	/** Owner (creator) of the snippet */
	public final User owner;
	/** Snippet name */
	private String name;
	/** Unique hash code of the snippet */
	public Long id = null;
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

	/**
	 * DB constructor must initialise all fields.
	 * 
	 * If a field is empty or null, a new {@link IllegalArgumentException} is
	 * thrown
	 * 
	 * @param owner
	 *            of the new snippet. Must not be null
	 * @param name
	 *            of the new snippet. Must not be null
	 * @param description
	 *            of the new snippet. Must not be null
	 * @param id
	 *            of the new snippet. Must not be null
	 * @param code
	 *            of the new snippet. Must not be null
	 * @param category
	 *            of the new snippet. Must not be null
	 * @param license
	 *            of the new snippet. Must not be null
	 * @param tags
	 *            of the new snippet. Must not be null
	 * @param comments
	 *            of the new snippet. Must not be null
	 * @param viewcount
	 *            of the new snippet. If less than zero, will be set to zero
	 * @throws IllegalArgumentException
	 *             Thrown, if at least one of the arguments is null or empty
	 */
	Snippet(User owner, String name, String description, long id, Code code, Category category, String license,
			List<Tag> tags, List<Comment> comments, int viewcount) {

		if (owner == null)
			throw new IllegalArgumentException("Owner of snippet cannot be null");
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("Name of snippet cannot be null");
		if (description == null || description.isEmpty())
			throw new IllegalArgumentException("Description of snippet cannot be null");
		if (code == null)
			throw new IllegalArgumentException("Code of snippet cannot be null");
		if (category == null)
			throw new IllegalArgumentException("Category of snippet cannot be null");
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("Name of snippet cannot be null");
		if (license == null || license.isEmpty())
			throw new IllegalArgumentException("Licence of snippet cannot be null");
		if (tags == null)
			throw new IllegalArgumentException("Tags of snippet cannot be null");
		if (comments == null)
			throw new IllegalArgumentException("Comments of snippet cannot be null");
		if (viewcount < 0) {
			viewcount = 0;
		}

		this.owner = owner;
		this.name = name;
		this.description = description;
		this.code = code;
		this.id = id;
		this.category = category;
		this.license = license;
		this.tags = tags;
		this.comments = comments;
		this.viewcount = viewcount;
	}

	/**
	 * Default constructor must initialise all fields except hash id.
	 * 
	 * If a field is empty or null, a new {@link IllegalArgumentException} is
	 * thrown
	 * 
	 * @param owner
	 *            of the new snippet. Must not be null
	 * @param name
	 *            of the new snippet. Must not be null
	 * @param description
	 *            of the new snippet. Must not be null
	 * @param code
	 *            of the new snippet. Must not be null
	 * @param category
	 *            of the new snippet. Must not be null
	 * @param license
	 *            of the new snippet. Must not be null
	 * @param tags
	 *            of the new snippet. Must not be null
	 * @param comments
	 *            of the new snippet. Must not be null
	 * @param viewcount
	 *            of the new snippet. If less than zero, will be set to zero
	 * @throws IllegalArgumentException
	 *             Thrown, if at least one of the arguments is null or empty
	 */
	private Snippet(User owner, String name, String description, Code code, Category category, String license,
			List<Tag> tags, List<Comment> comments, int viewcount) {

		if (owner == null)
			throw new IllegalArgumentException("Owner of snippet cannot be null");
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("Name of snippet cannot be null");
		if (description == null || description.isEmpty())
			throw new IllegalArgumentException("Description of snippet cannot be null");
		if (code == null)
			throw new IllegalArgumentException("Code of snippet cannot be null");
		if (category == null)
			throw new IllegalArgumentException("Category of snippet cannot be null");
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("Name of snippet cannot be null");
		if (license == null || license.isEmpty())
			throw new IllegalArgumentException("Licence of snippet cannot be null");
		if (tags == null)
			throw new IllegalArgumentException("Tags of snippet cannot be null");
		if (comments == null)
			throw new IllegalArgumentException("Comments of snippet cannot be null");
		if (viewcount < 0) {
			viewcount = 0;
		}

		this.owner = owner;
		this.name = name;
		this.description = description;
		this.code = code;
		this.id = null;
		this.category = category;
		this.license = license;
		this.tags = tags;
		this.comments = comments;
		this.viewcount = viewcount;
	}

	@Override
	public int hashCode() {
		if (id == null)
			return 0;
		return id.hashCode();
	}

	/**
	 * Creates a snippet with the given parameters and adds the snippet to the
	 * database. If the given snippet is alredy in the database, the given
	 * snippet is not overwritten!
	 * 
	 * Throws an {@link IllegalArgumentException} if an argument is invalid
	 * 
	 * @param owner
	 *            of the new snippet
	 * @param name
	 *            of the new snippet
	 * @param description
	 *            of the new snippet
	 * @param hash
	 *            of the new snippet
	 * @param code
	 *            of the new snippet
	 * @param category
	 *            of the new snippet
	 * @param license
	 *            of the new snippet
	 * @param tags
	 *            of the new snippet
	 * @param comments
	 *            of the new snippet
	 * @param viewcount
	 *            of the new snippet
	 * @throws IllegalArgumentException
	 *             Thrown if an argument is null or empty
	 * @return the newly created snippet
	 */
	public static Snippet createSnippet(User owner, String name, String description, long hash, Code code,
			Category category, String license, List<Tag> tags, List<Comment> comments, int viewcount)
			throws IOException {

		Snippet snippet = new Snippet(owner, name, description, code, category, license, tags, comments, viewcount);
		addToDB(snippet);

		return snippet;
	}

	/**
	 * @return the next available hash code
	 */
	private synchronized static int getNextHashCode() {
		while (exists(++hashCounter)) {
			;
		}
		return hashCounter;
	}

	/**
	 * Checks if the given hash code exists
	 * 
	 * @param hash
	 *            to be checks
	 * @return true if already registered hash, otherwise false
	 */
	synchronized static boolean exists(int hash) {
		try {
			Snippet snippet = Persistence.instance.getSnippet(hash);
			return snippet != null;
		} catch (IOException e) {
			System.err.println("IOException while exists(" + hash + ") " + e.getMessage());
			e.printStackTrace(System.err);
			return false;
		}
	}

	/**
	 * Searches for a snipped by his hash code and returns it if found. If no
	 * snippet is found, null is returned
	 * 
	 * @param hash
	 *            hash code of the snippet
	 * @return the found snippet or null if not existsing
	 */
	public synchronized static Snippet getSnippet(int hash) {
		try {
			Snippet snippet = Persistence.instance.getSnippet(hash);
			return snippet;
		} catch (IOException e) {
			System.err.println("IOException while getSnippet(" + hash + ") " + e.getMessage());
			e.printStackTrace(System.err);
			return null;
		}
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
		if (name == null || name.length() == 0)
			return;
		if (this.name.equals(name))
			return;

		this.name = name;
		refreshDB();
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
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
		if (description == null || description.length() == 0)
			return;
		if (this.description.equals(description))
			return;

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
		if (code == null)
			return;
		if (this.code.equals(code))
			return;

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
	 * Set the snippet category. If null nothing happens.
	 * 
	 * This call removes the snippet from the old category and adds the snippet
	 * to the new category.
	 * 
	 * @param category
	 *            the category to set
	 */
	void setCategory(Category category) {
		if (category == null || this.category.equals(category))
			return;

		// IMPORTANT: This command must be in this order,
		// oderwise we are in an endless loop!
		this.category = category;
		category.addSnippet(this);

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
		if (license == null || license.length() == 0)
			return;
		if (this.license.equals(license))
			return;
		this.license = license;
		refreshDB();
	}

	/**
	 * @return the tags of the snippet
	 */
	public List<Tag> getTags() {
		return tags;
	}

	/**
	 * Adds a tag to the snippet. If the given tag is null, nothing will be
	 * done. If the snippet has already been tagged with the tag, nothing is
	 * done.
	 * 
	 * @param tag
	 *            to be added to the snippet
	 */
	synchronized void addTag(Tag tag) {
		if (tag == null)
			return;
		if (tags.contains(tag))
			return;

		tags.add(tag);
		refreshDB();
	}

	/**
	 * Removes a tag from the snippet. If the given tag is null, nothing will be
	 * done. If the snippet has not been tagged with the tag, nothing is done.
	 * 
	 * @param tag
	 *            to be removed from the snippet
	 */
	synchronized void removeTag(Tag tag) {
		if (tag == null)
			return;

		if (!tags.contains(tag))
			return;

		tags.remove(tag);

		refreshDB();

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
	 * Adds a comment to the snippet. If the comment is null, nothing will be
	 * done. If comment has already been added, nothing is done
	 * 
	 * @param comment
	 *            to be added
	 */
	void addComment(Comment comment) {
		try {
			if (comment == null)
				return;
			if (!comment.snippet.equals(this))
				throw new IOException("Comment owner not equals snippet to be added");

			Persistence.instance.writeComment(comment, IPersistence.DB_DEFAULT);

		} catch (IOException e) {
			Logging.printError("IOException during addComment(Comment object) " + e.getMessage(), e);
			e.printStackTrace(Logging.err);
		}
	}

	/**
	 * Removes a comment from the snippet. If the comment is null, the nothing
	 * is done. If the given comment is not in the comments list of the snippet
	 * also the method returns without effect.
	 * 
	 * @param comment
	 *            to be removed
	 */
	void removeComment(Comment comment) {
		if (comment == null)
			return;

		// TODO Implement this
	}

	/**
	 * Checks if the snippet has a given tag
	 * 
	 * @param tag
	 *            to be checked
	 * @return true if the snippet has been tagged with the given tag, otherwise
	 *         false
	 */
	boolean hasTag(Tag tag) {
		if (tag == null)
			return false;
		return tags.contains(tag);
	}

	/**
	 * @return the owner of the session
	 */
	public User getOwner() {
		return owner;
	}

	/**
	 * 
	 * @return the total number of snippets in the system
	 */
	public static int totalCount() {
		try {
			return Persistence.instance.getSnippetsCount();
		} catch (IOException e) {
			System.err.println("IOException while totalCount() " + e.getMessage());
			e.printStackTrace(System.err);
			return 0;
		}
	}

	/**
	 * Invokes the refreshing process for the database
	 */
	protected synchronized void refreshDB() {
		try {
			Persistence.instance.writeSnippet(this, IPersistence.DB_DEFAULT);
		} catch (IOException e) {
			System.err.println("Error writing snippet " + name + " (" + id + "): " + e.getMessage());
			e.printStackTrace(System.err);
			return;
		}
	}

	/**
	 * Adds the given snippet to the database. If null, nothing happens
	 * 
	 * @param snippet
	 *            to be added.
	 */
	static protected void addToDB(Snippet snippet) throws IOException {
		if (snippet == null)
			return;

		snippet.id = Persistence.instance.writeSnippet(snippet, IPersistence.DB_NEW_ONLY);
	}

	/**
	 * Deletes the snippet
	 */
	void delete() {
		// TODO Auto-generated method stub

	}

	public List<String> getStringTags() {
		List<String> result = new ArrayList<String>();
		for (Tag tag : tags) {
			result.add(tag.name);
		}
		return result;
	}

	public int getCommentCount() {
		return comments.size();
	}

	/**
	 * Converts this snippet to a {@link XSnippet} parameter object
	 * 
	 * @return
	 */
	synchronized public XSnippet toXSnippet() {
		List<Integer> commentList = new ArrayList<Integer>();
		// TODO Comment list with hash codes

		return new XSnippet(owner.getUsername(), id, description, getStringTags(), commentList, code.getCode(),
				code.getFormattedHTML(), code.getLanguage(), license, viewcount);
	}

	/**
	 * @return the id hash code of the snippet
	 * @deprecated because of names convention. Use getHashID() instant.
	 */
	@Deprecated
	public Long getHash() {
		return id;
	}

	/**
	 * @return the id hash code of the snippet
	 */
	public Long getHashId() {
		return id;
	}
}

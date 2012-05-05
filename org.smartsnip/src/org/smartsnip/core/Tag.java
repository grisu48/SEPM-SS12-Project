package org.smartsnip.core;

import java.util.HashMap;

/**
 * A tag for the snippets. This method also serves as containment for all tags
 * in the system, the corresponding methods to access them and as TagFactory.
 * 
 */
public class Tag {

	/** Container for all tags. The key is the tags name in lowercase. Allways! */
	private static final HashMap<String, Tag> allTags = new HashMap<String, Tag>();

	public final String name;

	private Tag(String name) {
		if (name.length() == 0)
			throw new IllegalArgumentException("Tag name cannot be empty");
		this.name = name;
	}

	/**
	 * Gets a tag defined by the name. If the tag doesn't exists, null will be
	 * the result. If the name is null or empty, null will be returned
	 * 
	 * @param name
	 *            of the tag to be searched
	 * @return the given tag if exists, otherwise false
	 */
	public static synchronized Tag getTag(String name) {
		if (name == null || name.length() == 0)
			return null;
		return allTags.get(name.toLowerCase());
	}

	/**
	 * Creates a given tag with the name and returns the result. If it already
	 * exists, the existing Tag will be returned. If the name is empty or null
	 * also the result is null. In this case no internal changes are applied.
	 * 
	 * @param name
	 *            of the new tag. If null or empty, the returning result is also
	 *            empty
	 * @return Tag with the given name if existing, otherwise the newly
	 *         generated one
	 */
	public static synchronized Tag createTag(String name) {
		if (name == null || name.length() == 0)
			return null;
		name = trimName(name);

		if (exists(name))
			return getTag(name);
		Tag newTag = new Tag(name);
		allTags.put(name.toLowerCase(), newTag);
		addToDB(newTag);
		return newTag;
	}

	/**
	 * Checks if the given tag name exists. If the name is empty false is
	 * returned
	 * 
	 * @param name
	 *            of the tag to be checked
	 * @return true if existsing otherwise false
	 */
	public static synchronized boolean exists(String name) {
		if (name.length() == 0)
			return false;
		name = trimName(name);
		return allTags.containsKey(name);
	}

	/**
	 * Trims the given TAG name into the internal used format.
	 * 
	 * The name will be trimmed and lowercased.
	 * 
	 * @param name
	 *            to be trimmed
	 * @return the trimmed and lowercased name for internal representation in
	 *         the hash list
	 */
	private static String trimName(String name) {
		return name.trim().toLowerCase();
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * Invokes the refreshing process for the database for a single tag that has
	 * been created
	 */
	static protected void addToDB(Tag tag) {

	}

	static protected void refreshDB() {

	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Tag))
			return false;

		Tag tag = (Tag) obj;
		return tag.equals(this.name);

	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}

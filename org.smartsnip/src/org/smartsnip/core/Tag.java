package org.smartsnip.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A tag for the snippets. This method also serves as containment for all tags
 * in the system, the corresponing methods to access them and as TagFactory.
 * 
 */
public class Tag {
	/** Container for all tags. The key is the tags name in lowercase. Allways! */
	private static final HashMap<String, Tag> allTags = new HashMap<String, Tag>();

	/** Name of the tag */
	public final String name;

	private Tag(String name) {
		if (name.length() == 0) throw new IllegalArgumentException("Tag name cannot be empty");
		this.name = name;
	}

	/**
	 * Gets a tag defined by the name. If the tag doesn't exists, null will be
	 * the result If the name is empty, null will be returned
	 * 
	 * @param name
	 *            of the tag to be searched
	 * @return the given tag if exists, otherwise false
	 */
	synchronized Tag getTag(String name) {
		if (name.length() == 0) return null;
		return allTags.get(name.toLowerCase());
	}

	/**
	 * Creates a given tag with the name and returns the result. If it already
	 * exists, the existing Tag will be returned. If the name is empty a new
	 * {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param name
	 *            of the new tag
	 * @return Tag with the given name if existing, otherwise the newly
	 *         generated one
	 * @throws IllegalArgumentException
	 */
	synchronized Tag createTag(String name) throws IllegalArgumentException {
		if (name.length() == 0) throw new IllegalArgumentException("Empty tag name not allowed");

		if (exists(name)) return getTag(name);
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
	synchronized boolean exists(String name) {
		if (name.length() == 0) return false;
		name = name.toLowerCase();
		return allTags.containsKey(name);
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * Check for unused tags and remove them
	 */
	synchronized void removeUnusedTags() {
		// TODO: Write me
	}

	/**
	 * Invokes the refreshing process for the database
	 */
	protected void addToDB(Tag tag) {

	}
}

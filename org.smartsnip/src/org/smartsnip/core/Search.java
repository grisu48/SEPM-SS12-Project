package org.smartsnip.core;

import java.util.ArrayList;
import java.util.List;

public class Search {
	/** Defined search string */
	private final String searchString;
	/** Tag filter list */
	private List<Tag> tags = new ArrayList<Tag>();
	/** Category filter */
	private List<Category> categories = new ArrayList<Category>();
	/** Total results according to the search string */
	private List<Snippet> totalResults;

	/** Filtered results */
	private List<Snippet> filteredResults;

	Search(String searchString) {
		if (searchString == null) throw new NullPointerException();
		this.searchString = searchString;
	}

	/**
	 * @return the filtered search results
	 */
	public synchronized List<Snippet> getResults() {
		applyFilter();
		return filteredResults;
	}

	/**
	 * Applies all filter and resets filterResults
	 */
	synchronized void applyFilter() {
		// TODO: Write me
		filteredResults = totalResults;

	}

	/**
	 * Adds a tag to the search filter. If the tag is null or already added,
	 * nothing happens
	 * 
	 * @param tag
	 *            to be added
	 */
	public synchronized void addTag(Tag tag) {
		if (tag == null) return;
		if (tags.contains(tag)) return;
		tags.add(tag);
	}

	/**
	 * Removes a tag from the search filter. If the given tag is null or not in
	 * the list, nothing happens
	 * 
	 * @param tag
	 *            to be remove
	 */
	public synchronized void removeTag(Tag tag) {
		if (tag == null) return;
		if (!tags.contains(tag)) return;
		tags.remove(tag);
	}

	/**
	 * Adds a category to the search filter. If the category is null or already
	 * added, nothing happens
	 * 
	 * @param category
	 *            to be added
	 */
	public synchronized void addCategory(Category category) {
		if (category == null) return;
		if (categories.contains(category)) return;
		categories.add(category);
	}

	/**
	 * Removes a tag from the search filter. If the given tag is null or not in
	 * the list, nothing happens
	 * 
	 * @param tag
	 *            to be remove
	 */
	public synchronized void removeCategory(Category category) {
		if (category == null) return;
		if (!categories.contains(category)) return;
		categories.remove(category);
	}

}

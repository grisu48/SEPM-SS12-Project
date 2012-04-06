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
	/**
	 * Total results according to the search string. This list is cached, and
	 * then the filtering tags and categories are applied to it
	 */
	private List<Snippet> totalResults;

	/**
	 * Filtered results. If null, they need to be re-filtered. This occurs if a
	 * filter criterium changes
	 */
	private List<Snippet> filterResults;

	Search(String searchString) {
		if (searchString == null) throw new NullPointerException();
		this.searchString = searchString;
		this.totalResults = populateTotalResults(searchString);
	}

	/**
	 * Creates a new search with the given string to search for. If the string
	 * is null or empty, null will be returned.
	 * 
	 * @param searchString
	 * @return
	 */
	static Search createSearch(String searchString) {
		if (searchString == null || searchString.length() == 0) return null;

		// TODO: Build a search string cache.
		Search result = new Search(searchString);
		return result;
	}

	/**
	 * @return the filtered search results
	 */
	public synchronized List<Snippet> getResults() {
		if (filterResults == null) applyFilter();
		return filterResults;
	}

	/**
	 * Applies all filter and resets filterResults
	 */
	synchronized void applyFilter() {
		// TODO: Write me
		filterResults = new ArrayList<Snippet>();
		for (Snippet snippet : totalResults) {
			if (checkSnippet(snippet)) filterResults.add(snippet);
		}
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
		filterResults = null; // new criterium added. Need for new filtering
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
		filterResults = null; // new criterium added. Need for new filtering
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
		filterResults = null; // new criterium added. Need for new filtering
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
		filterResults = null; // new criterium added. Need for new filtering
	}

	/**
	 * Checks a snippet if it applies to the given filter list of tags and
	 * categories
	 * 
	 * @param snippet
	 *            to be checked
	 * @return true if it contains at least one filter category and all filter
	 *         tags
	 */
	private boolean checkSnippet(Snippet snippet) {
		if (snippet == null) return false;

		if (!categories.contains(snippet.getCategory())) return false;

		for (Tag tag : tags)
			if (!snippet.hasTag(tag)) return false;

		return true;
	}

	/**
	 * This call is used to redirect the search for the search string to the
	 * persistence layer.
	 * 
	 * @return list of found snippets that match to the searchString
	 */
	private List<Snippet> populateTotalResults(String searchString) {
		// TODO Implement me
		return new ArrayList<Snippet>();
	}
}

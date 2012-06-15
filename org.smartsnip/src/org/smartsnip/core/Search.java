package org.smartsnip.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.smartsnip.persistence.IPersistence;
import org.smartsnip.shared.XSearch;
import org.smartsnip.shared.XSearch.SearchSorting;

public class Search {
	/** Defined search string */
	private final String searchString;
	/** Tag filter list */
	private final List<Tag> tags = new ArrayList<Tag>();
	/** Category filter */
	private final List<Category> categories = new ArrayList<Category>();
	/**
	 * Total results according to the search string. This list is cached, and
	 * then the filtering tags and categories are applied to it
	 */
	private List<Snippet> totalResults;

	/** Class managing the statistics for the searches */
	public static class Stats {
		/** Total number of searches done */
		private static long totalSearched = 0L;

		/** The last search strings */
		private static List<String> lastSearches = new ArrayList<String>();

		/** Maximum number of log entries */
		private static int maxLogEntries = 2048;

		/**
		 * @return the number of total searches done in this session
		 */
		public static long getTotalSearchCount() {
			return totalSearched;
		}

		/**
		 * Gets a list of popular search strings
		 * 
		 * @param count
		 *            Maximal number of search strings to return
		 * @return a list containing the defined ammount of popular search
		 *         strings
		 */
		public synchronized static List<String> getPopularSearchStrings(int count) {
			if (count < 1) count = 1;
			List<String> result = new ArrayList<String>(count);

			// TODO: Hard coded - Replace me!
			result.add("Snippet");
			result.add("SmartSnip");
			result.add("Comment");

			return result;
		}

		/**
		 * Protocols a given search string. A Null string or an empty string is
		 * ignored in the statistics
		 * 
		 * NOTE: This call has to be synchronised
		 * 
		 * @param searchString
		 *            to be protocoled
		 */
		private synchronized static void protocolSearch(String searchString) {
			if (searchString == null || searchString.isEmpty()) return;

			if (lastSearches.size() > maxLogEntries) lastSearches.remove(0);
			lastSearches.add(searchString);

		}
	}

	/** Current sorting */
	private int sorting;

	/**
	 * Filtered results. If null, they need to be re-filtered. This occurs if a
	 * filter criterium changes
	 */
	private List<Snippet> filterResults;

	private Search(String searchString, SearchSorting sorting) {
		if (searchString == null) searchString = "";

		this.searchString = searchString;
		switch (sorting) // Must be done before searchDB
		{
		case highestRated:
			this.sorting = IPersistence.SORT_BEST_RATED;
			break;
		case mostViewed:
			this.sorting = IPersistence.SORT_MOSTVIEWED;
			break;
		case time:
			this.sorting = IPersistence.SORT_LATEST;
			break;
		case unsorted:
			this.sorting = IPersistence.SORT_UNSORTED;
			break;
		}

		this.totalResults = searchDB(searchString);
	}

	/**
	 * Creates a new search with the given string to search for. If the string
	 * is null or empty, null will be returned.
	 * 
	 * @param searchString
	 * @return
	 */
	public static Search createSearch(String searchString) {
		return createSearch(searchString, SearchSorting.unsorted);
	}

	/**
	 * Creates a new search with the given string to search for. If the string
	 * is null or empty, null will be returned.
	 * 
	 * @param searchString
	 * @return
	 */
	public static Search createSearch(String searchString, SearchSorting sorting) {
		Search result = new Search(searchString, sorting);
		return result;
	}

	/**
	 * @return the filtered search results
	 */
	public synchronized List<Snippet> getResults(int start, int count) {
		if (start < 0) start = 0;
		if (count < 1) return new ArrayList<Snippet>();

		if (filterResults == null) applyFilter();
		List<Snippet> result = new ArrayList<Snippet>(count);
		int maxSize = filterResults.size() - 1;
		for (int i = 0; i < count; i++) {
			int index = i + start;
			if (index > maxSize) break;

			result.add(filterResults.get(index));
		}

		return filterResults;
	}

	/**
	 * Applies all filter and resets filterResults
	 */
	synchronized void applyFilter() {
		filterResults = new ArrayList<Snippet>();
		for (Snippet snippet : totalResults) {
			if (checkSnippet(snippet)) filterResults.add(snippet);
		}
	}

	/**
	 * 
	 * @return the total number of results for this search
	 */
	public int getTotalResults() {
		if (filterResults == null) applyFilter();
		return filterResults.size();
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

		// Do we have to check categories?
		if (categories != null && categories.size() > 0) {
			// Check if the category of the snippet matches the search category
			if (!categories.contains(snippet.getCategory())) return false;
		}

		// Do we have tags to check?
		if (tags != null && tags.size() > 0) {

			// Check if the snippet has at least one tag of the matching tags
			boolean result = false;
			for (Tag tag : tags) {
				if (snippet.hasTag(tag)) {
					result = true;
					break;
				}
			}
			if (result == false) return false;
		}

		// Seems legit
		return true;
	}

	/**
	 * This call is used to redirect the search for the search string to the
	 * persistence layer.
	 * 
	 * @param searchString
	 *            String to be searched for
	 * 
	 * @return list of found snippets that match to the searchString
	 */
	private synchronized List<Snippet> searchDB(final String searchString) {

		// Need to re-filter after this call
		filterResults = null;

		try {
			if (searchString == null || searchString.isEmpty()) return Persistence.getInstance().getAllSnippets(null,
					null, sorting);
			else
				return Persistence.instance.search(searchString, null, null, sorting);
		} catch (IOException e) {
			System.err.println("IOException during search: " + e.getMessage());
			e.printStackTrace(System.err);
			return null;
		} finally {
			/* Protocol search, if not empty */
			if (searchString != null && !searchString.isEmpty()) {

				Thread protocol = new Thread(new Runnable() {

					@Override
					public void run() {
						Stats.protocolSearch(searchString);
					}
				});
				protocol.setDaemon(true);
				protocol.start();

			}

		}
	}

	/**
	 * Searches for the given tag and adds it, if found. If the tags was not
	 * found or null or empty, nothing happens
	 * 
	 * @param tag
	 *            to be added
	 */
	public void addTag(String tag) {
		if (tag == null || tag.isEmpty()) return;
		addTag(Tag.createTag(tag));
	}

	/**
	 * Searches for the given category name and adds it, if found. Otherwise
	 * nothing is done
	 * 
	 * @param category
	 *            to be searched for
	 */
	public void addCategory(String category) {
		Category cat = Category.getCategory(category);
		if (cat == null) return;

		addCategory(cat);
	}

	/**
	 * @return a list of all tags that match the given search criteria
	 */
	public List<Tag> getAllTagsMatchingSearchCriteria() {
		if (filterResults == null) applyFilter();

		List<Tag> result = new ArrayList<Tag>();
		for (Snippet snippet : totalResults) {
			List<Tag> tags = snippet.getTags();
			for (Tag tag : tags)
				if (!result.contains(tag)) result.add(tag);
		}

		return result;
	}

	/**
	 * 
	 * @return a list of all categories that match the given search criteria
	 */
	public List<String> getAllCategoriesMatchingSearchString() {
		if (filterResults == null) applyFilter();

		List<String> result = new ArrayList<String>();
		for (Snippet snippet : filterResults) {
			Category category = snippet.getCategory();
			if (category != null && !result.contains(category.getName())) result.add(category.getName());
		}

		return result;
	}

	/**
	 * Sets the sorting method for this search
	 * 
	 * @param sorting
	 *            Sorting method can be any of the {@link IPersistence}-sorting
	 *            constants
	 */
	public void setSorting(int sorting) {
		if (this.sorting == sorting) return;

		synchronized (this) {
			this.sorting = sorting;
			refetchResults();
		}
		filterResults = null;
	}

	/**
	 * Re-fetches all results from the database
	 */
	private synchronized void refetchResults() {
		totalResults = searchDB(searchString);
		filterResults = null;
	}
}

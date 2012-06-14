package org.smartsnip.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.smartsnip.persistence.IPersistence;
import org.smartsnip.shared.XSearch;

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

	private Search(String searchString) {
		if (searchString == null) searchString = "";

		this.searchString = searchString;
		this.totalResults = searchDB(searchString, tags, categories);
	}

	/**
	 * Creates a new search with the given string to search for. If the string
	 * is null or empty, null will be returned.
	 * 
	 * @param searchString
	 * @return
	 */
	public static Search createSearch(String searchString) {
		Search result = new Search(searchString);
		return result;
	}

	/**
	 * @return the filtered search results
	 */
	public synchronized List<Snippet> getResults(XSearch.SearchSorting sorting, int start, int count) {

		switch (sorting) {
		case time:
			sortByTime();
			break;
		case highestRated:
			sortByHightestRating();
			break;
		case mostViewed:
			sortByViewCount();
			break;
		}

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
	 * Sorts filterResults by time
	 */
	private void sortByTime() {
		setSorting(IPersistence.SORT_LATEST);
	}

	/**
	 * Sorts filterResults by highest ratings
	 */
	private void sortByHightestRating() {
		setSorting(IPersistence.SORT_BEST_RATED);
	}

	/**
	 * Sorts filterResults by highest view counts
	 */
	private void sortByViewCount() {
		setSorting(IPersistence.SORT_MOSTVIEWED);
	}

	/**
	 * Do not sort
	 */
	private void sortUnsorted() {
		setSorting(IPersistence.SORT_UNSORTED);
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

		if (categories != null && categories.size() > 0) if (!categories.contains(snippet.getCategory())) return false;

		if (tags != null && tags.size() > 0) for (Tag tag : tags)
			if (!snippet.hasTag(tag)) return false;

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
	private List<Snippet> searchDB(final String searchString, final List<Tag> tags, final List<Category> categories) {

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

			/* Protocol search */
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

	/**
	 * Searches for the given tag and adds it, if found. If the tags was not
	 * found or null or empty, nothing happens
	 * 
	 * @param tag
	 *            to be added
	 */
	public void addTag(String tag) {
		if (!Tag.exists(tag)) return;
		addTag(Tag.getTag(tag));
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
		for (Snippet snippet : filterResults) {
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
		totalResults = searchDB(searchString, tags, categories);
		filterResults = null;
	}
}

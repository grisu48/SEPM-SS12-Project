package org.smartsnip.client;

import java.util.ArrayList;
import java.util.List;

import org.smartsnip.shared.ISession;
import org.smartsnip.shared.XSearch;
import org.smartsnip.shared.XSearch.SearchSorting;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * This class is not a GUI entity but used to store informations and handler for
 * the search
 * 
 */
public class Search {
	/** Tags that have to be applied on next search */
	private List<String> tags = new ArrayList<String>();
	/** Categories that have to be searched for in the next search */
	private List<String> categories = new ArrayList<String>();

	/**
	 * The last search result or null, if currently searching or not yet invoked
	 */
	private XSearch lastResult = null;

	/** List of all tags, that appear in all results of the search string */
	private List<String> allTagsAppearinginSearch = new ArrayList<String>();
	/** Last search string */
	private String searchString = "";

	/** Sorting algorithm placed on search results (on server) */
	private SearchSorting sorting = SearchSorting.unsorted;

	/** This callback are used as "observable", when a search is done */
	private List<AsyncCallback<XSearch>> callbacks = new ArrayList<AsyncCallback<XSearch>>();

	/** Callback that redirects it to the other callback */
	private final AsyncCallback<XSearch> observableCallback = new AsyncCallback<XSearch>() {

		@Override
		public void onSuccess(final XSearch result) {
			// Check matching ID
			if (result == null) return;
			if (result.id != getID()) return;

			searchTime = System.currentTimeMillis() - time;

			searchString = result.searchString;
			allTagsAppearinginSearch = result.tagsAppearingInSearchString;

			for (AsyncCallback<XSearch> callback : callbacks)
				callback.onSuccess(result);
		}

		@Override
		public void onFailure(Throwable caught) {
			time = System.currentTimeMillis() - time;

			for (AsyncCallback<XSearch> callback : callbacks)
				callback.onFailure(caught);
		}
	};

	/** Start index for the next search */
	private int start = 0;
	/** Number of results to be returned */
	private int count = 10;

	/** Time value for calculating the time, the search took */
	private long time = 0L;

	/** Time value, the last search took */
	private long searchTime = 0L;

	/**
	 * Search id, used if multiple searches are done parallel to just take the
	 * last one
	 */
	private static int id = Integer.MIN_VALUE;

	/**
	 * Invokes a search on the server with all parameters given above. With this
	 * call a previous search, that is currently in progress on the server will
	 * be completely ignored
	 * 
	 */
	private void invokeSearch() {
		time = System.currentTimeMillis();
		int searchID = getNextID(); // Get next search ID. This call causes
									// previous search procedures to be ignored

		ISession.Util.getInstance().doSearch(searchString, tags, categories, sorting, start, count, searchID,
				observableCallback);
	}

	/**
	 * Increases the id counter for the searches and returns the current one
	 * 
	 * @return current search id
	 */
	private static int getNextID() {
		return ++id;
	}

	/**
	 * @return the current used search id
	 */
	private static int getID() {
		return id;
	}

	/**
	 * Set search string without invoking a new search
	 * 
	 * @param newString
	 *            to be set
	 */
	public void setSearchString(String newString) {
		this.searchString = newString;
	}

	/**
	 * Adds a tag to the search tags. If null, it is ignored
	 * 
	 * @param tag
	 *            to be added
	 */
	public void addTag(String tag) {
		if (tag == null || tag.isEmpty()) return;

		if (containsTag(tag)) return;
		tags.add(tag);
	}

	/**
	 * Removes a tag from the search tags. This search is not case sensitive. If
	 * null it is ignored
	 * 
	 * @param tag
	 *            to be removed
	 */
	public void removeTag(String tag) {
		if (tag == null || tag.isEmpty()) return;

		List<String> removeList = new ArrayList<String>();
		for (String cTag : tags)
			if (cTag.equalsIgnoreCase(tag)) removeList.add(cTag);

		for (String remove : removeList)
			tags.remove(remove);
	}

	/**
	 * Checks if a given tag exists. This search is not case sensitive. If the
	 * given tag is null, false is returned
	 * 
	 * @param tag
	 *            to be searched for. Non case sensitive
	 * @return true if the tag exists (not case sensitive) otherwise false. If
	 *         the given tag is null also false is returned
	 */
	public boolean containsTag(String tag) {
		if (tag == null) return false;
		for (String cTag : tags)
			if (cTag.equalsIgnoreCase(tag)) return true;
		return false;
	}

	/**
	 * Adds a category to the search category. If null, it is ignored
	 * 
	 * @param category
	 *            to be added
	 */
	public void addCategory(String category) {
		if (category == null || category.isEmpty()) return;

		if (containsCategory(category)) return;
		tags.add(category);
	}

	/**
	 * Removes a category from the search. This search is not case sensitive. If
	 * null it is ignored
	 * 
	 * @param category
	 *            to be removed
	 */
	public void removeCategory(String category) {
		if (category == null || category.isEmpty()) return;

		List<String> removeList = new ArrayList<String>();
		for (String ccategory : categories)
			if (ccategory.equalsIgnoreCase(category)) removeList.add(category);

		for (String remove : removeList)
			categories.remove(remove);

	}

	/**
	 * Clears all selected categories, but <b>WITHOUT</b> invoking a new search
	 */
	public void clearCategories() {
		categories.clear();
	}

	/**
	 * Checks if a given category exists. This search is not case sensitive. If
	 * the category is null, false is returned
	 * 
	 * @param category
	 *            to be searched for. Non case sensitive
	 * @return true if the category exists (not case sensitive) otherwise false.
	 *         If the category is null also false is returned
	 */
	public boolean containsCategory(String category) {
		if (category == null) return false;
		for (String ccategory : categories)
			if (ccategory.equalsIgnoreCase(category)) return true;
		return false;
	}

	/**
	 * Sets the categories of the search. This call itself does not invokes a
	 * search. Use {@link Search#search()} to invoke the search on the server.
	 * 
	 * @param categories
	 *            to be set. If null all existing categories will be erased
	 */
	public void setCategories(List<String> categories) {
		if (categories == null) categories = new ArrayList<String>();
		this.categories = categories;
	}

	/**
	 * Sets the tags of the search. This call itself does not invokes a search.
	 * Use {@link Search#search()} to invoke the search on the server.
	 * 
	 * @param tags
	 *            to be set. If null all existing tags will be erased
	 */
	public void setTags(List<String> tags) {
		if (tags == null) tags = new ArrayList<String>();
		this.tags = tags;
	}

	/**
	 * Clears all tags
	 */
	public void clearTags() {
		this.tags.clear();
	}

	/**
	 * Adds a callback to the search entity. If null or already added, nothing
	 * is done
	 * 
	 * @param callback
	 *            to be added
	 */
	public void addCallback(AsyncCallback<XSearch> callback) {
		if (callback == null) return;
		if (callbacks.contains(callback)) return;
		callbacks.add(callback);
	}

	/**
	 * Removes a callback from the search entity. If null nothing is done. If
	 * not added, nothing is done
	 * 
	 * @param callback
	 *            to be removed
	 */
	public void removeCallback(AsyncCallback<XSearch> callback) {
		if (callback == null) return;
		while (callbacks.contains(callback))
			callbacks.remove(callback);
	}

	/**
	 * Gets the time, the last search request took. The time is given in
	 * milliseconds
	 * 
	 * @return search time in milliseconds
	 */
	public long getSearchTime() {
		return searchTime;
	}

	/**
	 * Gets the last search result
	 * 
	 * @return the last search result
	 */
	public XSearch getLastResult() {
		return lastResult;
	}

	/**
	 * Sets the new sorting for a search
	 * 
	 * @param sorting
	 *            new sorting algorithm
	 */
	public void setSorting(SearchSorting sorting) {
		this.sorting = sorting;
	}

	/**
	 * Performs a search with the given parameters. This call also sets the
	 * active page in GUI to the search page
	 */
	public void search() {
		Control.myGUI.onSearchStart();
		invokeSearch();
	}

	/**
	 * Sets the search string and performs {@link Search#search() }
	 * 
	 * @param searchString
	 *            to be set
	 */
	public void search(String searchString) {
		setSearchString(searchString);
		search();
	}

	/**
	 * Sets the given arguments and performs {@link Search#search() }
	 * 
	 * @param searchString
	 *            to be set
	 */
	public void search(String searchString, List<String> tags, List<String> categories) {
		setTags(tags);
		setCategories(categories);
		setSearchString(searchString);
		search();
	}

	/**
	 * Sets the start index of the search results without invoking a new search.
	 * This method does <b>NOT</b> invoke a new search
	 * 
	 * @param start
	 *            new start index. If less than zero, 0 is set
	 */
	public void setStartIndex(int start) {
		if (start < 0) start = 0;
		this.start = start;
	}

	/**
	 * Sets the maximum number of search results. If less than five, 5 is set
	 * automatically. This method does <b>NOT</b> invoke a new search
	 * 
	 * @param count
	 *            new maximum number of search results. Is always at least 5
	 */
	public void setSearchResultCounts(int count) {
		if (count < 5) count = 5;
		this.count = count;
	}
}

package org.smartsnip.shared;

import java.util.List;

public class XSearch {

	public enum SearchSorting {
		mostViewed, highestRated, time;
	}
	
	public int totalresults;
	public int start;
	public int count;
	
	public String searchString;
	public List<String> tags;
	public List<String> tagsAppearingInSearchString;
	public List<String> categories;
	
	public List<XSnippet> snippets;
}

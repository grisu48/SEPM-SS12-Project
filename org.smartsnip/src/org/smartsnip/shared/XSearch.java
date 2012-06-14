package org.smartsnip.shared;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class XSearch implements IsSerializable {

	public enum SearchSorting implements IsSerializable {
		mostViewed, highestRated, time, unsorted;
	}

	public int totalresults = 0;
	public int start = 0;
	public int count = 0;

	public String searchString = "";
	public List<String> tags = new ArrayList<String>();
	public List<String> tagsAppearingInSearchString = new ArrayList<String>();
	public List<String> categories = new ArrayList<String>();

	public List<XSnippet> snippets = new ArrayList<XSnippet>();
}

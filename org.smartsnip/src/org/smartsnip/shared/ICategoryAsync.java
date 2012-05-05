package org.smartsnip.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ICategoryAsync {

	void getCategory(String name, AsyncCallback<XCategory> callback);

	void getSnippets(String category, int start, int count, AsyncCallback<List<XSnippet>> callback);

	void getSnippetCount(String name, AsyncCallback<Integer> callback);

	void add(String name, String description, String parent, AsyncCallback<XCategory> callback);

	void delete(String name, AsyncCallback<Void> callback);

	void getCategories(String root, AsyncCallback<List<XCategory>> callback);

	void createCategory(XCategory category, AsyncCallback<Void> callback);

}

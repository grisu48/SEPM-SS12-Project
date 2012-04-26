package org.smartsnip.shared;

import java.util.List;

import org.smartsnip.core.Pair;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ICategoryAsync {
	
	void getName(AsyncCallback<String> callback)
			throws IllegalArgumentException;
	void setName(String name, AsyncCallback<Void> callback)
			throws IllegalArgumentException;
	void getDescription(AsyncCallback<String> callback)
			throws IllegalArgumentException;
	void setDescription(String desc, AsyncCallback<Void> callback)
			throws IllegalArgumentException;
	void addSnippet(String name, String description, String code, String language, AsyncCallback<Void> callback)
			throws IllegalArgumentException;
	void getSnippets(AsyncCallback<List<Pair<Integer, String>>> callback)
			throws IllegalArgumentException;
	void getISnippets(AsyncCallback<List<ISnippet>> callback)
			throws IllegalArgumentException;

}

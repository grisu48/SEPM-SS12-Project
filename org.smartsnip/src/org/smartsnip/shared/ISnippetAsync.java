package org.smartsnip.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ISnippetAsync {
	
	
	void getName(AsyncCallback<String> callback)
			throws IllegalArgumentException;
	void getOwner(AsyncCallback<IUser> callback)
			throws IllegalArgumentException;
	void getDesc(AsyncCallback<String> callback)
			throws IllegalArgumentException;
	void getLanguage(AsyncCallback<String> callback)
			throws IllegalArgumentException;
	void getHash(AsyncCallback<Integer> callback)
			throws IllegalArgumentException;
	void getCodeHTML(AsyncCallback<String> callback)
			throws IllegalArgumentException;
	void getTags(AsyncCallback<List<String>> callback)
			throws IllegalArgumentException;
	void getCategory(AsyncCallback<String> callback)
			throws IllegalArgumentException;
	void addTag(String tag, AsyncCallback<Void> callback)
			throws IllegalArgumentException;
	void removeTag(String tag, AsyncCallback<Void> callback)
			throws IllegalArgumentException;
	void getComments(AsyncCallback<List<IComment>> callback)
			throws IllegalArgumentException;
	void getViewCount(AsyncCallback<Integer> callback)
			throws IllegalArgumentException;
	void increaseViewCounter(AsyncCallback<Void> callback)
			throws IllegalArgumentException;
	void addComment(String comment, AsyncCallback<IComment> callback)
			throws IllegalArgumentException;
	void addFavorite(AsyncCallback<Void> callback)
			throws IllegalArgumentException;
	void removeFavorite(AsyncCallback<Void> callback)
			throws IllegalArgumentException;
	void delete(AsyncCallback<Void> callback)
			throws IllegalArgumentException;

}

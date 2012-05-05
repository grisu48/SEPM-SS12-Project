package org.smartsnip.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ISnippetAsync {

	void getComments(long snippet, int start, int count, AsyncCallback<List<XComment>> callback);

	void getCommentCount(long snippet, AsyncCallback<Integer> callback);

	void getSnippet(long snippet, AsyncCallback<XSnippet> callback);

	void delete(long snippet, AsyncCallback<Void> callback);

	void rateSnippet(long id, int rate, AsyncCallback<Void> callback);

	void setDescription(long id, String desc, AsyncCallback<Void> callback);

	void setCode(long id, String code, AsyncCallback<Void> callback);

	void addTag(long id, String tag, AsyncCallback<Void> callback);

	void removeTag(long id, String tag, AsyncCallback<Void> callback);

	void addComment(long id, String comment, AsyncCallback<Void> callback);

	void create(XSnippet snippet, AsyncCallback<Void> callback);

}

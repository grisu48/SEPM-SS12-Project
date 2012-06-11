package org.smartsnip.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ISnippetAsync {

	void getComments(long snippet, int start, int count,
			AsyncCallback<List<XComment>> callback);

	void getCommentCount(long snippet, AsyncCallback<Integer> callback);

	void getSnippet(long snippet, AsyncCallback<XSnippet> callback);

	void delete(long snippet, AsyncCallback<Void> callback);

	void rateSnippet(long id, int rate, AsyncCallback<Void> callback);

	void setDescription(long id, String desc, AsyncCallback<Void> callback);

	void setCode(long id, String code, AsyncCallback<Void> callback);

	void addTag(long id, String tag, AsyncCallback<Void> callback);

	void removeTag(long id, String tag, AsyncCallback<Void> callback);

	void addComment(long id, String comment, AsyncCallback<Void> callback);

	void create(String name, String desc, String code, String language,
			String category, List<String> tags, AsyncCallback<Void> callback);

	void addToFavorites(long id, AsyncCallback<Void> callback);

	void edit(XSnippet snippet, AsyncCallback<Void> callback);

	void getSupportedLanguages(AsyncCallback<List<String>> callback);

	void getDownloadSourceTicket(long snippet_id, AsyncCallback<Long> callback);

	void hasDownloadableSource(long snippet_id, AsyncCallback<Boolean> callback);

	void canEdit(long snippet_id, AsyncCallback<Boolean> callback);

	void getSnippetOfDay(AsyncCallback<XSnippet> callback);

}

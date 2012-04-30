package org.smartsnip.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ISnippetAsync {

	void getComments(int snippet, int start, int count, AsyncCallback<List<XComment>> callback);

	void getCommentCount(int snippet, AsyncCallback<Integer> callback);

	void getSnippet(int snippet, AsyncCallback<XSnippet> callback);

	void getServerStatus(AsyncCallback<XServerStatus> callback);

}

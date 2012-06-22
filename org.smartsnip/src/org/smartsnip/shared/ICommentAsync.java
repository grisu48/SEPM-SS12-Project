package org.smartsnip.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ICommentAsync {

	void getComments(long snippethash, int start, int count, AsyncCallback<List<XComment>> callback);

	void votePositive(long commentID, AsyncCallback<XComment> callback);

	void voteNegative(long commentID, AsyncCallback<XComment> callback);

	void unvote(long commentID, AsyncCallback<XComment> callback);

	void edit(long commentID, String newMessage, AsyncCallback<XComment> callback);

	void delete(long commentID, AsyncCallback<Void> callback);

	void canComment(long snippetID, AsyncCallback<Boolean> callback);

	void canRate(long commentID, AsyncCallback<Boolean> callback);

	void canEdit(long commentID, AsyncCallback<Boolean> callback);

	void getComment(long commentID, AsyncCallback<XComment> callback);

}

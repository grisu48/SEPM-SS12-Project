package org.smartsnip.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ICommentAsync {

	void getComments(long snippethash, int start, int count,
			AsyncCallback<List<XComment>> callback);

	void votePositive(long commentID, AsyncCallback<Void> callback);

	void voteNegative(long commentID, AsyncCallback<Void> callback);

	void unvote(long commentID, AsyncCallback<Void> callback);

	void edit(long commentID, String newMessage, AsyncCallback<Void> callback);

	void delete(long commentID, AsyncCallback<Void> callback);

	void canComment(long commentID, AsyncCallback<Boolean> callback);

}

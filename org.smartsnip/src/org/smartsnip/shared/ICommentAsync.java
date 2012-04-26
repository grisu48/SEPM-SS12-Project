package org.smartsnip.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ICommentAsync {

	void getMessage(AsyncCallback<String> callback)
			throws IllegalArgumentException;
	void getLastModificationTime(AsyncCallback<Date> callback)
			throws IllegalArgumentException;
	void ratePositive(AsyncCallback<Void> callback)
			throws IllegalArgumentException;
	void rateNegative(AsyncCallback<Void> callback)
			throws IllegalArgumentException;
	void removeRating(AsyncCallback<Void> callback)
			throws IllegalArgumentException;
	void getOwner(AsyncCallback<IUser> callback)
			throws IllegalArgumentException;
	void delete(AsyncCallback<Void> callback)
			throws IllegalArgumentException;
	void edit(String newComment, AsyncCallback<Void> callback)
			throws IllegalArgumentException;
	void report(AsyncCallback<Void> callback)
			throws IllegalArgumentException;
	
}

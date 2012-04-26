package org.smartsnip.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface INotificationAsync {
	
	void markRead(AsyncCallback<Void> callback)
			throws IllegalArgumentException;
	void markUnread(AsyncCallback<Void> callback)
			throws IllegalArgumentException;
	void isRead(AsyncCallback<Boolean> callback)
			throws IllegalArgumentException;
	void getSource(AsyncCallback<String> callback)
			throws IllegalArgumentException;
	void getMessage(AsyncCallback<String> callback)
			throws IllegalArgumentException;
	void getTime(AsyncCallback<String> callback)
			throws IllegalArgumentException;
	void delete(AsyncCallback<Void> callback)
			throws IllegalArgumentException;

}

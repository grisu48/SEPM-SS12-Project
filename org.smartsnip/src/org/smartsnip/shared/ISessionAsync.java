package org.smartsnip.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ISessionAsync {
	public void getUsername(AsyncCallback<String> callback);

	public void getActiveSessionCount(AsyncCallback<Integer> callback);

	public void getGuestSessionCount(AsyncCallback<Integer> callback);

	public void getUserCount(AsyncCallback<Integer> callback);

	public void getCategoryCount(AsyncCallback<Integer> callback);

	public void getSnippetCount(AsyncCallback<Integer> callback);

	public void login(String username, String password, AsyncCallback<Boolean> callback) throws NoAccessException;

	public void logout(@SuppressWarnings("rawtypes") AsyncCallback callback);

	public void getCategory(String name, AsyncCallback<ICategory> callback) throws NoAccessException;

	public void getSnippet(int hash, AsyncCallback<ISnippet> callback) throws NoAccessException;

	public void getUser(String username, AsyncCallback<IUser> callback) throws NoAccessException;

	public void isLoggedIn(AsyncCallback<Boolean> callback);
}

package org.smartsnip.shared;

import org.smartsnip.shared.XSearch.SearchSorting;

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

	public void isLoggedIn(AsyncCallback<Boolean> callback);

	void doSearch(String searchString, String[] tags, String[] categories,
			SearchSorting sorting, int start, int count,
			AsyncCallback<XSearch> callback);

	void getServerStatus(AsyncCallback<XServerStatus> callback);

	void registerNewUser(String username, String password, String email, AsyncCallback<Boolean> callback);

	void getSessionCookie(AsyncCallback<String> callback);
}

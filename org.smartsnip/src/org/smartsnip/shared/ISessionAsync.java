package org.smartsnip.shared;

import java.util.List;

import org.smartsnip.shared.XSearch.SearchSorting;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ISessionAsync {
	public void getUsername(AsyncCallback<String> callback);

	public void getActiveSessionCount(AsyncCallback<Integer> callback);

	public void getGuestSessionCount(AsyncCallback<Integer> callback);

	public void getUserCount(AsyncCallback<Integer> callback);

	public void getCategoryCount(AsyncCallback<Integer> callback);

	public void getSnippetCount(AsyncCallback<Integer> callback);

	public void login(String username, String password, AsyncCallback<Boolean> callback);

	public void logout(@SuppressWarnings("rawtypes") AsyncCallback callback);

	public void isLoggedIn(AsyncCallback<Boolean> callback);

	void doSearch(String searchString, List<String> tags, List<String> categories, SearchSorting sorting, int start, int count, int id,
			AsyncCallback<XSearch> callback);

	void getServerStatus(AsyncCallback<XServerStatus> callback);

	void registerNewUser(String username, String password, String email, AsyncCallback<Boolean> callback);

	void getSessionCookie(AsyncCallback<String> callback);

	public void getUser(String username, AsyncCallback<XUser> callback);

	void getFavorites(AsyncCallback<List<XSnippet>> callback);

	void getSessionInfo(AsyncCallback<XSession> callback);

	void getNotificationCount(boolean unreadOnly, AsyncCallback<Long> callback);

	void getNotifications(boolean unreadOnly, AsyncCallback<List<XNotification>> callback);
}

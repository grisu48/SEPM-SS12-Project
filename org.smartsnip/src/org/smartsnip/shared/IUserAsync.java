package org.smartsnip.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IUserAsync {

	void login(String username, String password, AsyncCallback<Boolean> callback);

	void logout(AsyncCallback<Void> callback);

	void setEmail(String newAddress, AsyncCallback<Void> callback);

	void setRealName(String newName, AsyncCallback<Void> callback);

	void getSnippets(AsyncCallback<List<XSnippet>> callback);

	void getFavorites(AsyncCallback<List<XSnippet>> callback);

}

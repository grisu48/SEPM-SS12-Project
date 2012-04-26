package org.smartsnip.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IUserAsync {
	
	void getName(AsyncCallback<String> callback)
			throws IllegalArgumentException;
	
	void getEmail(AsyncCallback<String> callback)
			throws IllegalArgumentException;
	
	void setEmail(String email, AsyncCallback<Boolean> callback)
			throws IllegalArgumentException;
	
	void setRealName(String email, AsyncCallback<Boolean> callback)
			throws IllegalArgumentException;
	
	void getRealName(AsyncCallback<String> callback)
			throws IllegalArgumentException;
	
	void logout(AsyncCallback<Boolean> callback)
			throws IllegalArgumentException;
	
	void getSnippets(AsyncCallback<List<ISnippet>> callback)
			throws IllegalArgumentException;
	
	void getFavorites(AsyncCallback<List<ISnippet>> callback)
			throws IllegalArgumentException;
	
	void report(String reason, AsyncCallback<Boolean> callback)
			throws IllegalArgumentException;
	

}

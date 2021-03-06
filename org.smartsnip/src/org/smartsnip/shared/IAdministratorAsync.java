package org.smartsnip.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous interface for {@link IAdministrator}
 * 
 * @author Felix Niederwanger
 * 
 */
public interface IAdministratorAsync extends IModeratorAsync {

	void isAdministrator(AsyncCallback<Boolean> callback);

	void setPassword(String username, String password, AsyncCallback<Void> callback);

	void deleteUser(String username, AsyncCallback<Void> asyncCallback);

	void getSessions(AsyncCallback<List<XSession>> callback);

	void closeSession(String key, AsyncCallback<Void> callback);

	void messageToAdmin(String message, String email, AsyncCallback<Void> callback);

}

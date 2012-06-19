package org.smartsnip.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IModeratorAsync {

	void isModerator(AsyncCallback<Boolean> callback);

	void getSessions(AsyncCallback<List<XSession>> callback);

	void closeSession(String key, AsyncCallback<Void> callback);

	void setUserState(String username, XUser.UserState state,
			AsyncCallback<Void> callback);

}

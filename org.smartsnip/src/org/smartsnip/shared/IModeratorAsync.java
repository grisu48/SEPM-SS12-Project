package org.smartsnip.shared;

import java.util.List;

import org.smartsnip.core.User.UserState;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IModeratorAsync {

	void isModerator(AsyncCallback<Boolean> callback);

	void getSessions(AsyncCallback<List<XSession>> callback);

	void closeSession(String key, AsyncCallback<Void> callback);

	void setUserState(String username, UserState state,
			AsyncCallback<Void> callback);

}

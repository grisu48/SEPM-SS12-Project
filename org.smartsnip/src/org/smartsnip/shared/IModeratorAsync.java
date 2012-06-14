package org.smartsnip.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IModeratorAsync {

	void isModerator(AsyncCallback<Boolean> callback);

}

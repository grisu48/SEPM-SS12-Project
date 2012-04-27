package org.smartsnip.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ISessionAsync {
	public void getUsername(AsyncCallback<String> callback);
}

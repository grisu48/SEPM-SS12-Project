package org.smartsnip.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IRipAsync {
	void testTalk(String input, AsyncCallback<Boolean> callback)
			throws IllegalArgumentException;

}

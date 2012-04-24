package org.smartsnip.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>Communication</code>.
 */
public interface ICommunicationAsync {
	void testTalk(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;
}

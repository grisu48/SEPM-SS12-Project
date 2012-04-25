package org.smartsnip.client;

import java.util.ArrayList;

import org.smartsnip.core.Snippet;
import org.smartsnip.shared.IRip;
import org.smartsnip.shared.ISnippet;
import org.smartsnip.shared.Rip;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>Communication</code>.
 */
public interface ICommunicationAsync {
	void testTalk(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	void login(String user, String password, AsyncCallback<Boolean> callback)
			throws IllegalArgumentException;
	void logout(String user, String password, AsyncCallback<Boolean> callback)
			throws IllegalArgumentException;
	void search(String search, AsyncCallback<ArrayList<String>> callback)
			throws IllegalArgumentException;
	void getSnippet(String name, AsyncCallback<IRip> callback)
			throws IllegalArgumentException;
}

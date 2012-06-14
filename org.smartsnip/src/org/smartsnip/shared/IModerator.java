package org.smartsnip.shared;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("moderator")
public interface IModerator extends RemoteService, IsSerializable {

	/** This class provides easy access to the proxy object */
	public static class Util {
		private static IModeratorAsync instance = null;

		/** Get the proxy object instance */
		public static IModeratorAsync getInstance() {
			if (instance == null) {
				instance = GWT.create(IModerator.class);
			}
			return instance;
		}
	}

	/**
	 * Requests on the server, if the logged in user in the current session is a
	 * moderator. If a guest session (not logged in) the result is always false
	 * 
	 * @return true if the current user has moderator status, otherwise false
	 */
	public boolean isModerator();
}

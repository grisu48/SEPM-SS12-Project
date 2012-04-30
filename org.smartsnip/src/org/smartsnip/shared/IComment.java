package org.smartsnip.shared;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * This interface handles the interactions of the GUI on a concrete comment. It
 * is given by the session to the GUI
 * 
 */
@RemoteServiceRelativePath("comment")
public interface IComment extends RemoteService, IsSerializable {

	/** This class provides easy access to the proxy object */
	public static class Util {
		private static ICommentAsync instance = null;

		/** Get the proxy object instance */
		public static ICommentAsync getInstance() {
			if (instance == null) {
				instance = GWT.create(IComment.class);
			}
			return instance;
		}
	}

}

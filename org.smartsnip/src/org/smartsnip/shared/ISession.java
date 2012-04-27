package org.smartsnip.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * This interface handles the main interactions between the client and the
 * server.
 * 
 * 
 */
@RemoteServiceRelativePath("session")
public interface ISession extends RemoteService {

	/** Gets the username of the current session, or null if a guest session */
	public String getUsername();
}

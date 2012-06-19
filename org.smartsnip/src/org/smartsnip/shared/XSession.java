package org.smartsnip.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Exchange object, that contains informations about the current session
 * 
 * @author Felix Niederwanger
 * 
 */
public class XSession implements IsSerializable {
	public int activeSessions;
	public int guestSessions;
	public int loggedInUsers;
	public int totalUsers;
}

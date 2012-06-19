package org.smartsnip.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Exchange object, that contains informations about a session
 * 
 * @author Felix Niederwanger
 * 
 */
public class XSession implements IsSerializable {
	public int activeSessions;
	public int guestSessions;
	public int loggedInUsers;
	public int totalUsers;

	/** This is the obfuscated session key, that a moderator can use */
	public String key;

	/** User that is logged in or null, if a guest session */
	public String user;
}

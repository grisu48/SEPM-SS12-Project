package org.smartsnip.core;


import org.smartsnip.client.ICommunication;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class CommunicationImpl extends RemoteServiceServlet implements
		ICommunication {

	@Override
	public String testTalk(String name) throws IllegalArgumentException {
				return "Hallo Client" + name;
	}
}

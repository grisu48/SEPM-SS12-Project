package org.smartsnip.core;


import java.util.ArrayList;

import org.smartsnip.client.ICommunication;
import org.smartsnip.shared.IRip;
import org.smartsnip.shared.ISnippet;
import org.smartsnip.shared.Rip;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class CommunicationImpl extends RemoteServiceServlet implements
		ICommunication {
	
	private Session currentSession;

	@Override
	public String testTalk(String name) throws IllegalArgumentException {
				return "Hallo Client" + name;
	}

	@Override
	public Boolean login(String user, String password)
			throws IllegalArgumentException {
		currentSession = Session.getSession("bla");
		//currentSession.login(user, password);
		
		
		return null;
	}

	@Override
	public Boolean logout(String user, String password)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> search(String search)
			throws IllegalArgumentException {
		//TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRip getSnippet(String name) throws IllegalArgumentException {
	//	TODO Auto-generated method stub
		return null;
		}
}

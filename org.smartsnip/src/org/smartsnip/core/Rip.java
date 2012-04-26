package org.smartsnip.core;

import java.util.ArrayList;

import org.smartsnip.client.ICommunication;
import org.smartsnip.shared.IRip;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class Rip extends RemoteServiceServlet implements
IRip {
	
	private int zahl;
	
	public Rip() {
		
	}
	@Override
	public Boolean testTalk(String test) {
		System.out.println(test);
		return true;
	}

	
	


}

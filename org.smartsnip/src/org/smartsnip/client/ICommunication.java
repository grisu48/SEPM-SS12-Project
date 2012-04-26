package org.smartsnip.client;

import java.util.ArrayList;

import org.smartsnip.core.Snippet;
import org.smartsnip.shared.IRip;
import org.smartsnip.shared.ISnippet;
import org.smartsnip.shared.Rip;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("comm")
public interface ICommunication extends RemoteService {
	String testTalk(String name) throws IllegalArgumentException;
	Boolean login(String user, String password) throws IllegalArgumentException;
	Boolean logout(String user, String password) throws IllegalArgumentException;
	ArrayList<String> search(String search) throws IllegalArgumentException;
	//IRip getSnippet(String name) throws IllegalArgumentException;
	
	
	
	
	
}

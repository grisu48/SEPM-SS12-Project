package org.smartsnip.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("comm")
public interface ICommunication extends RemoteService {
	String testTalk(String name) throws IllegalArgumentException;
	
	
	
	
	
}

package org.smartsnip.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This is a test class, that is used to test a message transfer from the server
 * to the client.
 * 
 * Normally it should not be used in running mode ...
 * 
 * @author phoenix
 * 
 */
public class XServerStatus implements IsSerializable {
	private String message;

	public String servername;
	public long totalMemory;
	public long freeMemory;
	public long maxMemory;

	public XServerStatus() {
		this("Hello World!");
	}

	public XServerStatus(String message) {
		super();
		if (message == null) {
			message = "";
		}
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return message;
	}
}

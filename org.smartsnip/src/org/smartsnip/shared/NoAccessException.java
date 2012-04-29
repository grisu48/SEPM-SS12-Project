package org.smartsnip.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This exception is thrown, if the client invokes a method on the server, that
 * is denied due to security restrictions
 * 
 */
public class NoAccessException extends Exception implements IsSerializable {

	/** Serialisation ID */
	private static final long serialVersionUID = -960131256427751101L;

	public NoAccessException() {
		super();
	}

	public NoAccessException(String message) {
		super(message);
	}

	public NoAccessException(Throwable cause) {
		super(cause);
	}

	public NoAccessException(String message, Throwable cause) {
		super(message, cause);
	}
}

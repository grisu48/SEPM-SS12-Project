package org.smartsnip.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Kind of runtime exception that is thrown by the server if an object cannot be
 * found
 * 
 */
public class NotFoundException extends RuntimeException implements
		IsSerializable {

	/**
	 * Serialisation ID
	 */
	private static final long serialVersionUID = -8211568975962922119L;

	public NotFoundException() {
		super();
	}

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundException(Throwable cause) {
		super(cause);
	}

	@Override
	public String toString() {
		return "Not found: " + getMessage();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		return obj instanceof NotFoundException;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ getMessage().hashCode();
	}
}

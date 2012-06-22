package org.smartsnip.client;

/**
 * Interface for a callback with a result
 * 
 * @author Felix Niederwanger
 * 
 * @param <E>
 *            Generic type of the result
 */
public interface ResultCallback<E> {

	/**
	 * When the return callback is reached.
	 * 
	 * @param result
	 *            Fetched result
	 */
	public void onReturn(E result);

}

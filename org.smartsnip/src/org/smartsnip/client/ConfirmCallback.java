package org.smartsnip.client;

/**
 * This interface acts as asynchronous callback for a confirmation
 * 
 * @author Felix Niederwanger
 * 
 */
public interface ConfirmCallback {

	/** Yes callback */
	public void onYes();

	/** No callback */
	public void onNo();
}

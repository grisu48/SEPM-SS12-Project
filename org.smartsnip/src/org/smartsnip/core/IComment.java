package org.smartsnip.core;

/**
 * This interface handles the interactions of the GUI on a concrete comment. It
 * is given by the session to the GUI
 * 
 */
public interface IComment {
	/**
	 * Rates the comment positive. If already rated positive nothing happesn. If
	 * rated negative, the negative rate will be deleted, and instant a positive
	 * rating applied
	 * 
	 * @throws IllegalAccessException
	 *             Thrown if the call cannot be executed by this session
	 */
	public void ratePositive() throws IllegalAccessException;

	/**
	 * Rates the comment negative. If already rated negative nothing happens. If
	 * rated positive, the positive rate will be deleted, and instant a negative
	 * rating applied
	 * 
	 * @throws IllegalAccessException
	 *             Thrown if the call cannot be executed by this session
	 */
	public void rateNegative() throws IllegalAccessException;

	/**
	 * Removes teh current rate given to this comment
	 * @throws IllegalAccessException
	 *             Thrown if the call cannot be executed by this session
	 */
	public void removeRating() throws IllegalAccessException;
	
	/**
	 * 
	 * @return the owner of the comment
	 * @throws IllegalAccessException
	 *             Thrown if the call cannot be executed by this session
	 */
	public IUser getOwner() throws IllegalAccessException;
}

package org.smartsnip.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This interface handles the interactions of the GUI on a concrete comment. It
 * is given by the session to the GUI
 * 
 */
public interface IComment extends IsSerializable {
	/**
	 * @return the comment's message text
	 */
	public String getMessage();

	/**
	 * @return the last modification date and time of the comment
	 */
	public Date getLastModificationTime();

	/**
	 * Rates the comment positive. If already rated positive nothing happesn. If
	 * rated negative, the negative rate will be deleted, and instant a positive
	 * rating applied
	 * 
	 * @throws NoAccessException
	 *             Thrown if the call cannot be executed by this session
	 */
	public void ratePositive() throws NoAccessException;

	/**
	 * Rates the comment negative. If already rated negative nothing happens. If
	 * rated positive, the positive rate will be deleted, and instant a negative
	 * rating applied
	 * 
	 * @throws NoAccessException
	 *             Thrown if the call cannot be executed by this session
	 */
	public void rateNegative() throws NoAccessException;

	/**
	 * Removes teh current rate given to this comment
	 * 
	 * @throws NoAccessException
	 *             Thrown if the call cannot be executed by this session
	 */
	public void removeRating() throws NoAccessException;

	/**
	 * 
	 * @return the owner of the comment
	 * @throws NoAccessException
	 *             Thrown for security reasons if the call cannot be executed by
	 *             this session
	 */
	public IUser getOwner() throws NoAccessException;

	/**
	 * Deletes a comment from the system
	 * 
	 * @throws NoAccessException
	 *             Thrown if the access policy denies the process
	 */
	public void delete() throws NoAccessException;

	/**
	 * Sets a new comment text. See the security restrictions to find out, who
	 * can do this.
	 * 
	 * If the given new comment is null or empty, nothing is done.
	 * 
	 * @param newComment
	 *            New comment that should be changed to.
	 * @throws NoAccessException
	 *             Thrown if the access policy denies the process
	 */
	public void edit(String newComment) throws NoAccessException;

	/**
	 * Reports the comment as abusive.
	 * 
	 * @throws NoAccessException
	 *             Thrown if the access policy denies the process
	 */
	public void report() throws NoAccessException;

}

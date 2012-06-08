package org.smartsnip.shared;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * This interface handles the interactions of the GUI on a concrete comment. It
 * is given by the session to the GUI
 * 
 */
@RemoteServiceRelativePath("comment")
public interface IComment extends RemoteService, IsSerializable {

	/** This class provides easy access to the proxy object */
	public static class Util {
		private static ICommentAsync instance = null;

		/** Get the proxy object instance */
		public static ICommentAsync getInstance() {
			if (instance == null) {
				instance = GWT.create(IComment.class);
			}
			return instance;
		}
	}

	/**
	 * Gets a single comment defined by it's ID.
	 * 
	 * @param commentID
	 *            ID of the comment to get
	 * @return The comment
	 * @throws NotFoundException
	 *             Thrown, if the given id did not return a comment
	 * @throws NoAccessException
	 *             Thrown if the server denies the access
	 */
	public XComment getComment(long commentID) throws NotFoundException,
			NoAccessException;

	/**
	 * Gets the comments from a snippet. If the snippet does not exists, the
	 * result is null
	 * 
	 * To safe traffic the method has the parameters start and count. From the
	 * total comment set of a snippet, start gives the starting index of the
	 * result set, and count the number of total results to get
	 * 
	 * @param snippethash
	 *            hash code of the snippet to get the comments from
	 * @param start
	 *            Starting index of the total results set
	 * @param count
	 *            Maximum numbers of comments to pull from server
	 * @return With start and count reduced set of the comments of a snippet
	 * @throws NoAccessException
	 *             Thrown, if there is an access denial on the server
	 */
	public List<XComment> getComments(long snippethash, int start, int count)
			throws NoAccessException;

	/**
	 * Votes positive for a comment given by it's id If the comment has not been
	 * found, nothing happens on the server
	 * 
	 * @param commentID
	 *            id of the comment to rate
	 * @throws NoAccessException
	 *             Thrown, if there is an access denial on the server
	 */
	public void votePositive(long commentID) throws NoAccessException;

	/**
	 * Votes negative for a comment given by it's id If the comment has not been
	 * found, nothing happens on the server
	 * 
	 * @param commentID
	 *            id of the comment to rate
	 * @throws NoAccessException
	 *             Thrown, if there is an access denial on the server
	 */
	public void voteNegative(long commentID) throws NoAccessException;

	/**
	 * Removes the vote for a comment given by it's id If the comment has not
	 * been found, nothing happens on the server
	 * 
	 * @param commentID
	 *            id of the comment to rate
	 * @throws NoAccessException
	 *             Thrown, if there is an access denial on the server
	 */
	public void unvote(long commentID) throws NoAccessException;

	/**
	 * Changes the message of a given comment entry, identified by its hash id.
	 * If the comment has not been found, nothing happens on the server If the
	 * given new message is null or empty, nothing happens
	 * 
	 * @param commentID
	 *            Hash id of the comment to be edited
	 * @param newMessage
	 *            New message to be set to
	 * @throws NoAccessException
	 *             If an access denial happens on the server
	 */
	public void edit(long commentID, String newMessage)
			throws NoAccessException;

	/**
	 * Deletes a comment, identified by its hash id. If the comment is not
	 * found, nothing happens
	 * 
	 * @param commentID
	 *            Hash ID of the comment to be deleted
	 * @throws NoAccessException
	 *             Thrown, if a access denial was thrown on the server
	 */
	public void delete(long commentID) throws NoAccessException;

	/**
	 * Checks if the current session/user can comment on a snippet
	 * 
	 * @return true if commenting is possible, false if commenting is denied by
	 *         the server
	 */
	public boolean canComment(long snippetID) throws NotFoundException;

	/**
	 * Checks if the current session/user can rate a comment
	 * 
	 * @return true if rating is possible, false if rating is denied by the
	 *         server
	 */
	public boolean canRate(long commentID) throws NotFoundException;

	/**
	 * Checks if the current session/user can edit a comment
	 * 
	 * @return true if it is possible, false if denied by the server
	 */
	public boolean canEdit(long commentID) throws NotFoundException;
}

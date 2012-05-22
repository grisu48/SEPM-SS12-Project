package org.smartsnip.shared;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * This interface handles the interactions of the GUI on a concrete snippet. It
 * is given by the session to the GUI
 * 
 */
@RemoteServiceRelativePath("snippet")
public interface ISnippet extends RemoteService, IsSerializable {

	/** This class provides easy access to the proxy object */
	public static class Util {
		private static ISnippetAsync instance = null;

		/** Get the proxy object instance */
		public static ISnippetAsync getInstance() {
			if (instance == null) {
				instance = GWT.create(ISnippet.class);
			}
			return instance;
		}
	}

	/**
	 * Gets a list of the comments of the snippet. The resulting list is sorted
	 * chronological.
	 * 
	 * If the given snippet hash doesn't exists, null is the result
	 * 
	 * The parameter start gives the start index of the total list of comments,
	 * the parameter count the maximum objects to pull.
	 * 
	 * @param snippet
	 *            Hash code of the source snippet
	 * @param start
	 *            Starting index of the list of total comments
	 * @param count
	 *            Maximum number of comments to pull
	 * @return a list containing at most count number of comments of the snippet
	 *         or null, if the snippet hash is not found
	 */
	public List<XComment> getComments(long snippet, int start, int count);

	/**
	 * @param snippet
	 *            Hash id of the source snippet
	 * @return the total number of comments or -1, if the given snippet hash is
	 *         not found
	 */
	public int getCommentCount(long snippet);

	/**
	 * Gets the snippet given with the hash id, or null, if no such snippet
	 * exists
	 * 
	 * @param snippet
	 *            hash of the snippet to get
	 * @return a new {@link XSnippet} object with the attributes of the snippet,
	 *         or null, if no such snippet exists
	 */
	public XSnippet getSnippet(long snippet);

	/**
	 * Deletes a snippet from the server. The snippet is identified by it's id.
	 * 
	 * @throws NoAccessException
	 *             Thrown, if the access was denied by the server
	 */
	public void delete(long snippet) throws NoAccessException;

	/**
	 * Rates a snippet, identified by it's hash code. If the rateing score is
	 * out of the valid range or if the snippet was not found, nothing happens
	 * 
	 * @param id
	 *            hash id of the snippet to be rated
	 * @param rate
	 *            rating score from 1 to 5
	 * @throws NoAccessException
	 *             Thrown if the server denies the access
	 */
	public void rateSnippet(long id, int rate) throws NoAccessException;

	/**
	 * Changes the description of a snippet, based on it's id hash. If the given
	 * snippet is not found, nothing happens.
	 * 
	 * @param id
	 *            hash id of the snippet to be edited
	 * @param desc
	 *            new description of the snippet
	 * @throws NoAccessException
	 *             Thrown if you don't have the permission to edit the snippet
	 */
	public void setDescription(long id, String desc) throws NoAccessException;

	/**
	 * Changes the code of a snippet, based on it's id hash. If the given
	 * snippet is not found, nothing happens.
	 * 
	 * @param id
	 *            hash id of the snippet to be edited
	 * @param code
	 *            new code of the snippet
	 * @throws NoAccessException
	 *             Thrown if you don't have the permission to edit the snippet
	 */
	public void setCode(long id, String code) throws NoAccessException;

	/**
	 * Adds a tag to a given snippet. If the snippet is not found, nothing
	 * happens.
	 * 
	 * @param id
	 *            hash id of the snippet to tag
	 * @param tag
	 *            to be added
	 * @throws NoAccessException
	 *             Thrown if the server denies the access to tag the snippet
	 */
	public void addTag(long id, String tag) throws NoAccessException;

	/**
	 * Removes a tag to a given snippet. If the snippet is not found, nothing
	 * happens.
	 * 
	 * @param id
	 *            hash id of the snippet to tag
	 * @param tag
	 *            to be removed
	 * @throws NoAccessException
	 *             Thrown if the server denies the access to tag the snippet
	 */
	public void removeTag(long id, String tag) throws NoAccessException;

	/**
	 * Adds a comment to a snippet, identified by its hash id. If the comment is
	 * null or the snippet is not found, nothing happens.
	 * 
	 * @param id
	 *            of the snippet to comment
	 * @param comment
	 *            Comment message
	 * @throws NoAccessException
	 *             Thrown if the server denies the access
	 */
	public void addComment(long id, String comment) throws NoAccessException;

	/**
	 * Creates a new snippet with the given parameters.
	 * 
	 * 
	 * @throws NoAccessException
	 *             Thrown if the server denies the access
	 * @throws IllegalArgumentException
	 *             Thrown if at least one argument is invalid
	 */
	public void create(String name, String desc, String code, String language,
			String category, List<String> tags) throws NoAccessException,
			IllegalArgumentException;
}

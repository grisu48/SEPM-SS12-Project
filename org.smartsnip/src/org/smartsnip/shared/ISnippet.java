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
	public List<XComment> getComments(long snippet, int start, int count)
			throws NotFoundException;

	/**
	 * @param snippet
	 *            Hash id of the source snippet
	 * @return the total number of comments or -1, if the given snippet hash is
	 *         not found
	 */
	public int getCommentCount(long snippet) throws NotFoundException;

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
	 * Gets the snippet of the day
	 * 
	 * @return the snippet of the day
	 */
	public XSnippet getSnippetOfDay();

	/**
	 * Deletes a snippet from the server. The snippet is identified by it's id.
	 * 
	 * @throws NoAccessException
	 *             Thrown, if the access was denied by the server
	 */
	public void delete(long snippet) throws NoAccessException,
			NotFoundException;

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
	public void rateSnippet(long id, int rate) throws NoAccessException,
			NotFoundException;

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
	public void setDescription(long id, String desc) throws NoAccessException,
			NotFoundException;

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
	public void setCode(long id, String code) throws NoAccessException,
			NotFoundException;

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
	public void addTag(long id, String tag) throws NoAccessException,
			NotFoundException;

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
	public void removeTag(long id, String tag) throws NoAccessException,
			NotFoundException;

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
	public void addComment(long id, String comment) throws NoAccessException,
			NotFoundException;

	/**
	 * Adds a snippet that is identified by the id to the favorites
	 * 
	 * @param id
	 *            hash id of the snippet to be added to the favorites
	 * @throws NoAccessException
	 *             Thrown if the access is denied by the server
	 */
	public void addToFavorites(long id) throws NoAccessException,
			NotFoundException;

	/**
	 * Creates a new snippet with the given parameters.
	 * 
	 * 
	 * @throws NoAccessException
	 *             Thrown if the server denies the access
	 * @throws IllegalArgumentException
	 *             Thrown if at least one argument is invalid
	 */
	public void create(String name, String desc, String code, String language, String license,
			String category, List<String> tags) throws NoAccessException,
			IllegalArgumentException;

	/**
	 * Edits the given snippet (identified by the hash id) with the new data
	 * 
	 * 
	 * @param snippet
	 *            Snippet data and hash id of the snippet to be edited
	 * @throws NoAccessException
	 *             Thrown if the server denies the access
	 * @throws NotFoundException
	 *             Thrown if the given snippet is not found
	 */
	public void edit(XSnippet snippet) throws NoAccessException,
			NotFoundException, IllegalArgumentException;

	/**
	 * Retrieves a list of supported languages from the server
	 * 
	 * @return List of supported languages
	 */
	public List<String> getSupportedLanguages();

	/**
	 * Checks if there is a downloadable source
	 * 
	 * @return true if a downloadable source is found
	 */
	public boolean hasDownloadableSource(long snippet_id)
			throws NotFoundException;

	/**
	 * Gets a download ticket for the source code.
	 * 
	 * @return the ticket id for the source
	 * @throws NoAccessException
	 *             Thrown if the server denies the access
	 */
	public long getDownloadSourceTicket(long snippet_id)
			throws NotFoundException, NoAccessException;

	/**
	 * Checks if the current session/user can edit a given snippet
	 * 
	 * @param snippet_id
	 *            Hash id of the snippet to be edited
	 * @return true if possible, false if the edit process is denied
	 * @throws NotFoundException
	 *             Thrown if the given snippet id is not found
	 */
	public boolean canEdit(long snippet_id) throws NotFoundException;
}

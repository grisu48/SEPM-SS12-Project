package org.smartsnip.core;

import java.util.List;

/**
 * This interface handles the interactions of the GUI on a concrete snippet. It
 * is given by the session to the GUI
 * 
 */
public interface ISnippet {
	/**
	 * @return the name of the snippet
	 */
	public String getName();

	/**
	 * @return the owner of the snippet
	 */
	public IUser getOwner();

	/**
	 * @return the description of the snippet
	 */
	public String getDesc();

	/**
	 * @return the codeing language of the snippet
	 */
	public String getLanguage();

	/**
	 * @return the unique hash code of the snippet
	 */
	public int getHash();

	/**
	 * @return the formatted and highlighted HTML code of the snippet's code
	 */
	public String getCodeHTML();

	/**
	 * @return a list of the tags of the snippet
	 */
	public List<String> getTags();

	/**
	 * @return the category the snippet belongs to
	 */
	public String getCategory();

	/**
	 * Adds a tag to the snippet. If the tag doesn't exists, it will be created.
	 * If the tag has already been added, it won't be added twice. If the given
	 * tag is null or empty, nothing is done.
	 * 
	 * @param tag
	 *            to be added. If null or empty the call doesn't do anything
	 * @throws IllegalAccessException
	 *             Thrown as security exception if the current session is not
	 *             allowed to do that
	 */
	public void addTag(String tag) throws IllegalAccessException;

	/**
	 * Removes a tag from the snippet. If the snippet doesn't has this tag,
	 * nothing happens. If the tag is null or empty, also nothing happens.
	 * 
	 * @param tag
	 *            to be removed. If null or empty the call doesn't do anything
	 * @throws IllegalAccessException
	 *             Thrown as security exception if the current session is not
	 *             allowed to do that
	 */
	public void removeTag(String tag) throws IllegalAccessException;

	/**
	 * Gets a list of the comments written to this snippet.
	 * 
	 * After creation the list isn't refreshed. So changes on comments (e.g.
	 * adding new comment or deleting old comments) are not observed in this
	 * list.
	 * 
	 * @return a new list containing all currently available comments.
	 * @throws IllegalAccessException
	 *             Thrown as security exception if the current session is not
	 *             allowed to do that
	 */
	public List<IComment> getComments() throws IllegalAccessException;

	/**
	 * @return the number of views of the snippet
	 */
	public int getViewCount();

	/**
	 * Increases the view count by one.
	 * 
	 * @throws IllegalAccessException
	 *             Currently not used
	 */
	public void increaseViewCounter() throws IllegalAccessException;

	/**
	 * Adds a comment with the given user of the session to the snippet. If the
	 * given comment is null or empty, the call returns without any changes on
	 * the system and returns null as well.
	 * 
	 * 
	 * If no user is logged in with the session, a new
	 * {@link IllegalAccessException} will be thrown.
	 * 
	 * @param comment
	 *            To be added to the snippet. If null or empty the call will
	 *            return without a system modification and returns null
	 * @return the created comment or null if the given comment string was null
	 *         or empty
	 * @throws IllegalAccessException
	 *             Thrown as security exception if the current session is not
	 *             allowed to do that
	 */
	public IComment addComment(String comment) throws IllegalAccessException;
}

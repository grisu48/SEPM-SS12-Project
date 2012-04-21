package org.smartsnip.security;

import org.smartsnip.core.*;

/**
 * Interface that is used for concrete access policies
 * 
 */
public interface IAccessPolicy {

	/**
	 * Indicating if the current session can do a register process
	 * 
	 * @param session
	 *            To be checked
	 * @return true if operation succeeds if denied false
	 */
	public boolean canRegister(Session session);

	/**
	 * Indicating if the current session can do a login process
	 * 
	 * @param session
	 *            To be checked
	 * @return true if operation succeeds if denied false
	 */
	public boolean canLogin(Session session);

	/**
	 * Indicating if the current session can do a search
	 * 
	 * @param session
	 *            To be checked
	 * @return true if operation succeeds if denied false
	 */
	public boolean canSearch(Session session);

	/**
	 * Indicating if the current session can do a register process
	 * 
	 * @param session
	 *            To be checked
	 * @param user
	 *            Data of this user should be changed
	 * @return true if operation succeeds if denied false
	 */
	public boolean canEditUserData(Session session, User user);

	/**
	 * Inidicating if the given session can comment
	 * 
	 * @param session
	 *            to be investigated
	 * @return true if operation succeeds if denied false
	 */
	public boolean canComment(Session session);

	/**
	 * Indicating if the given session can edit a given comment.
	 * 
	 * @param session
	 *            the call belongs to
	 * @param comment
	 *            to be checked
	 * @return true if operation succeeds if denied false
	 */
	public boolean canEditComment(Session session, Comment comment);

	/**
	 * Indicating if the current session can create a snippet in the given
	 * category.
	 * 
	 * @param session
	 *            To be checked
	 * @param category
	 *            Category where the snippet should be created
	 * @return true if operation succeeds if denied false
	 */
	public boolean canCreateSnippet(Session session, Category category);

	/**
	 * Indicating if the current session can do a register process
	 * 
	 * @param session
	 *            To be checked
	 * @param snippet
	 *            Concrete snippet that should be deleted
	 * @return true if operation succeeds if denied false
	 */
	public boolean canDeleteSnippet(Session session, Snippet snippet);

	/**
	 * Indicating if the current session can do a register process
	 * 
	 * @param session
	 *            To be checked
	 * @param snippet
	 *            Concrete snippet, that should be edited
	 * @return true if operation succeeds if denied false
	 */
	public boolean canEditSnippet(Session session, Snippet snippet);

	/**
	 * Indicating if the given session can tag the given snippet
	 * 
	 * @param session
	 *            to be checked. Concrete, the user of the session will be
	 *            checked
	 * @param snippet
	 *            to be tagged
	 * @return true if operation succeeds if denied false
	 */
	public boolean canTagSnippet(Session session, Snippet snippet);

	/**
	 * Indicating if the given session is able to rate a snippet
	 * 
	 * @param session
	 *            to be checked. Concrete, the user of the session will be
	 *            checked
	 * @param snippet
	 *            to be tagged
	 * @return true if operation succeeds if denied false
	 */
	public boolean canRateSnippet(Session session, Snippet snippet);

	/**
	 * Indicating if the given session can edit a given category.
	 * 
	 * @param session
	 *            the call belongs to
	 * @param category
	 *            to be checked
	 * @return true if operation succeeds if denied false
	 */
	public boolean canEditCategory(Session session, Category category);
}

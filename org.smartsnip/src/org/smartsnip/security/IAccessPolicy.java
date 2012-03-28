package org.smartsnip.security;

import org.smartsnip.core.*;

/**
 * Interface that is used for concrete access policies
 * 
 */
public interface IAccessPolicy {

	/**
	 * Indicating if the current session can register a new user
	 * 
	 * @param session
	 *            that is checked
	 * @return true if operation succeeds if denied false
	 */
	public boolean canRegister(Session session);

	/**
	 * Indicating if the current session can do a login process
	 * 
	 * @param session
	 *            that is checked
	 * @return true if operation succeeds if denied false
	 */
	public boolean canLogin(Session session);

	/**
	 * Indicating if the current session can do a search
	 * 
	 * @param session
	 *            that is checked
	 * @return true if operation succeeds if denied false
	 */
	public boolean canSearch(Session session);

	/**
	 * Indicating if the current session can do a register process
	 * 
	 * @param session
	 *            that is checked
	 * @param user
	 *            Data of this user should be changed
	 * @return true if operation succeeds if denied false
	 */
	public boolean canEditUserData(Session session, User user);

	/**
	 * Indicating if the current session can create a snippet in the given
	 * category.
	 * 
	 * @param session
	 *            that is checked
	 * @param category
	 *            Category where the snippet should be created
	 * @return true if operation succeeds if denied false
	 */
	public boolean canCreateSnippet(Session session, Category category);

	/**
	 * Indicating if the current session can do a register process
	 * 
	 * @param session
	 *            that is checked
	 * @param snippet
	 *            Concrete snippet that should be deleted
	 * @return true if operation succeeds if denied false
	 */
	public boolean canDeleteSnippet(Session session, Snippet snippet);

	/**
	 * Indicating if the current session can do a register process
	 * 
	 * @param session
	 *            that is checked
	 * @param snippet
	 *            Concrete snippet, that should be edited
	 * @return true if operation succeeds if denied false
	 */
	public boolean canEditSnippet(Session session, Snippet snippet);
}

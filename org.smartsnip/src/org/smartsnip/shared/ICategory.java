package org.smartsnip.shared;

import java.util.List;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * This interface handles the interactions of the GUI on a concrete category. It
 * is given by the session to the GUI
 * 
 */
@RemoteServiceRelativePath("category")
public interface ICategory extends RemoteService {
	/**
	 * @return the name of the category
	 */
	public String getName();

	/**
	 * Sets the name of the category
	 * 
	 * @param name
	 *            new name of the category. Must not be null or empty
	 * @throws NoAccessException
	 *             Thrown if the current session doesn't fit the necessary
	 *             access privileges
	 */
	public void setName(String name) throws NoAccessException;

	/**
	 * 
	 * @return Description of the category
	 */
	public String getDescription();

	/**
	 * Sets the description of the category. Must not be null or empty.
	 * 
	 * @param desc
	 *            New description of the category. Must not be null or empty.
	 * @throws NoAccessException
	 *             Thrown if the access privilege forbids this action.
	 */
	public void setDescription(String desc) throws NoAccessException;

	/**
	 * Adds a new snippet to the category.
	 * 
	 * @param name
	 *            Name of the new snippet. Must not be null or empty, otherwise
	 *            a new {@link IllegalArgumentException} is thrown
	 * @param description
	 *            Description of the snippet. Must not be null or empty,
	 *            otherwise a new {@link IllegalArgumentException} is thrown
	 * @param code
	 *            Concrete code of the snippet. Must not be null or empty,
	 *            otherwise a new {@link IllegalArgumentException} is thrown
	 * @param language
	 *            Code language of the snippet. Must not be null or empty,
	 *            otherwise a new {@link IllegalArgumentException} is thrown. If
	 *            the code language is not supported, also a new
	 *            {@link IllegalArgumentException} is thrown
	 * @throws IllegalArgumentException
	 *             Thrown if the language is not supported, or if an argument is
	 *             null or empty
	 * @throws NoAccessException
	 *             Thrown if the session has not the privileges to add snippets
	 *             to this category
	 */
	public void addSnippet(String name, String description, String code, String language)
			throws IllegalArgumentException, NoAccessException;

	/**
	 * @return A list containing pairs with all snippets. The integer value is
	 *         the hash code of the snippets, the string is the name of the
	 *         snippets
	 */
	public List<Pair<Integer, String>> getSnippets();

	/**
	 * This call creates interfaces for all snippets of the category. Be
	 * careful, this method may be a performance brake!
	 * 
	 * @return a list containing interfaces to all snippets of the category.
	 */
	public List<ISnippet> getISnippets();
}

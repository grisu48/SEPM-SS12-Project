package org.smartsnip.shared;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * This interface handles the interactions of the GUI on a concrete category. It
 * is given by the session to the GUI
 * 
 */
@RemoteServiceRelativePath("category")
public interface ICategory extends RemoteService {

	/** This class provides easy access to the proxy object */
	public static class Util {
		private static ICategoryAsync instance = null;

		/** Get the proxy object instance */
		public static ICategoryAsync getInstance() {
			if (instance == null) {
				instance = GWT.create(ICategory.class);
			}
			return instance;
		}
	}

	/**
	 * Gets a concrete category object by it's name. If the given category name
	 * doesn't exists, null is returned.
	 * 
	 * @param name
	 *            Name of the category to get
	 * @return A new {@link XCategory} object, containing the attributes of the
	 *         category
	 * @throws NoAccessException
	 *             Thrown if the server denies the access to this category
	 */
	public XCategory getCategory(String name);

	/**
	 * Gets list of child-categories of a given category. If the given root
	 * category name is null or empty, the method returns all root categories.
	 * 
	 * @param root
	 *            Name of the parent category
	 * @return List containing all direct childs of the given root (parent)
	 *         category
	 */
	public List<XCategory> getCategories(String root);

	/**
	 * Gets all categories
	 * 
	 * @return All categories
	 */
	public List<XCategory> getAllCategories();

	/**
	 * Gets a list of {@link XSnippet} of a given category (given by it's name)
	 * If the given category doesn't exists, null is returned.
	 * 
	 * There can be multiple snippets. The parameter start gives the starting
	 * indices of all snippets, and the parameter count the number of snippets
	 * to get with this call.
	 * 
	 * @param category
	 *            Name of the category the snippets should get from
	 * @param start
	 *            Starting index of the snippets
	 * @param count
	 *            Number of snippets maximal to pull
	 * @return A list containing at most count snippets or null, if the given
	 *         category is not found
	 * @throws NoAccessException
	 *             Thrown if the server denies the access to this category
	 */
	public List<XSnippet> getSnippets(String category, int start, int count);

	/**
	 * Gets the number of snippets of a given category. If the category doesn't
	 * exists, the result value is -1.
	 * 
	 * @return the number of snippets in this category
	 */
	public int getSnippetCount(String name);

	public XCategory add(String name, String description, String parent) throws NoAccessException;

	/**
	 * Deletes a category and all of it's snippets out of the system
	 * 
	 * @param name
	 *            Name of the category to be deleted
	 * @throws NoAccessException
	 *             Thrown if the server denies the access
	 */
	public void delete(String name) throws NoAccessException;

	/**
	 * Creates a new category with the given catgory object.
	 * 
	 * @param category
	 *            Information container of the new category to be created
	 * @throws NoAccessException
	 *             Thrown if the server denies the access
	 * @throws IllegalArgumentException
	 *             Thrown if at least one argument is invalid
	 */
	public void createCategory(XCategory category) throws NoAccessException, IllegalArgumentException;

	/**
	 * Edits the category given with the name, with the new data
	 * 
	 * @param name
	 *            category name to be edited
	 * @param newData
	 *            new data to be set
	 * @throws NoAccessException
	 *             Thrown if the server denies the access
	 * @throws NotFoundException
	 *             Thrown if the given category name is not found
	 * @throws IllegalArgumentException
	 *             Thrown if an argument was invalid
	 */
	public void edit(String name, XCategory newData) throws NoAccessException, NotFoundException, IllegalArgumentException;
}

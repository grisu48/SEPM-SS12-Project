package org.smartsnip.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.smartsnip.persistence.IPersistence;
import org.smartsnip.shared.XCategory;

public class Category {

	/** Name of the category */
	private final String name;
	/** Description of the category */
	private String description;
	/** Parent category, if present, or null if a root category */
	private Category parent = null;

	/**
	 * Constructor for a new category. If one of the fields (except parent) if
	 * null, a {@link NullPointerException} will be thrown If the name or the
	 * description is empty, a {@link IllegalArgumentException} is thrown
	 * 
	 * @param name
	 *            of the category. Must not be null or empty
	 * @param description
	 *            of the category. Must not be null or empty
	 * @param parent
	 *            of the category. Can be null
	 */
	private Category(String name, String description, Category parent) {
		super();
		if (name == null || description == null)
			throw new NullPointerException();
		if (name.length() == 0)
			throw new IllegalArgumentException("Empty category name not allowed");
		if (description.length() == 0)
			throw new IllegalArgumentException("Empty category description not allowed");

		this.name = name;
		this.description = description;
		this.parent = parent;
	}

	/**
	 * Creates a new category in the system. The name and description must not
	 * be null or empty.
	 * 
	 * If the parent category is null, it is added as a new root category
	 * 
	 * @param name
	 *            Of the category
	 * @param description
	 *            Of the category
	 * @param parent
	 *            Parent category. If null, the new category is added as root
	 *            category
	 * @return the newly created category
	 * @throws IOException
	 *             Thrown if occuring during access to IPersistence
	 */
	synchronized static Category createCategory(String name, String description, Category parent) throws IOException {
		Category category = new Category(name, description, parent);
		addToDB(category);
		return category;
	}

	/**
	 * Gets a category by it's name.
	 * 
	 * @param name
	 *            to be searched for
	 * @return the corresponding category or null, if not found
	 */
	synchronized static Category getCategory(String name) {
		if (name == null || name.isEmpty())
			return null;

		// TODO return Persistence.instance.getCategory(name);
		return null;
	}

	/**
	 * @return a String list containing all root categories
	 */
	synchronized static List<String> getCategories() {
		List<String> result = new ArrayList<String>();

		// TODO return Persistence.instance.getCategories();
		return null;
	}

	/**
	 * Creates a new list containing all sub-categories of a given category. If
	 * the given parent is null or empty the root categories will be returned.
	 * 
	 * @return a String list containing all sub-categories of a category. Empty
	 *         if the parent could not be found.
	 */
	synchronized static List<String> getCategories(String parent) {
		if (parent == null || parent.isEmpty())
			return getCategories();

		// Category prnt = getCategory(parent);
		// TODO return Persistence.instance.getCategories(prnt);
		return null;
	}

	/**
	 * @return the name of the category
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the desc of the category. If null or empty, nothing happends
	 * 
	 * @param description
	 *            the description to set
	 */
	void setDescription(String description) {
		if (description == null || description.length() == 0)
			return;
		if (this.description.equals(description))
			return;
		this.description = description;
		refreshDB();
	}

	/**
	 * @return the parent
	 */
	public Category getParent() {
		try {
			if (parent == null) {
				parent = Persistence.instance.getParentCategory(this);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}

		return parent;
	}

	/**
	 * Sets the parent of the category
	 * 
	 * @param parent
	 *            the parent to set
	 */
	void setParent(Category parent) {
		if (this.parent == parent)
			return;
		this.parent = parent;
		refreshDB();
	}

	/**
	 * Adds a subcategory to the category. If already added or null, nothing
	 * will happen
	 * 
	 * @param category
	 *            to be added
	 */
	void addSubCategory(Category category) {
		if (category == null)
			return;

		// TODO Implement me
	}

	/**
	 * Removes a subcategory to the category. If null or not in the
	 * subcategories list, nothing happens will happen
	 * 
	 * @param category
	 *            to be removed
	 */
	void removeSubCategory(Category category) {
		if (category == null)
			return;

		// TODO Implement me
	}

	/**
	 * Invokes the refreshing process for the database
	 */
	synchronized protected void refreshDB() {

	}

	/**
	 * Adds a category object to the database. If null, nothing happens
	 * 
	 * @param category
	 *            to be added.
	 * @throws IOException
	 *             The {@link IOException} of IPersistence is not caught
	 */
	synchronized protected static void addToDB(Category category) throws IOException {
		if (category == null)
			return;

		Persistence.instance.writeCategory(category, IPersistence.DB_NEW_ONLY);
	}

	/**
	 * Removes a snippet from this category. If the snippet is null or not
	 * listed in this category, nothing happens
	 * 
	 * This call does not affect the snippet's internal category.
	 * 
	 * @param snippet
	 *            to be removed
	 */
	void removeSnippet(Snippet snippet) {
		if (snippet == null)
			return;

		// TODO Write me
	}

	/**
	 * Adds a snippet to this category. If the snippet is null, nothing happens.
	 * 
	 * This call does not affect the snippet's internal category.
	 * 
	 * @param snippet
	 */
	void addSnippet(Snippet snippet) {
		if (snippet == null)
			return;

		// TODO Write me
	}

	/**
	 * Gets the total number of categories or -1 if 1 {@link IOException}
	 * occurs.
	 * 
	 * If the {@link IOException} occurs the exception is written into
	 * System.err
	 * 
	 * @return the total number of categories
	 */
	synchronized static int totalCount() {
		try {
			return Persistence.instance.getCategoryCount();
		} catch (IOException e) {
			System.err.println(e.getMessage() + " while getting category count: ");
			e.printStackTrace(System.err);
			return -1;
		}
	}

	/**
	 * Deletes a category out of the system. If the category cannot be found,
	 * nothing is done.
	 * 
	 * @param name
	 *            the name of the category to be deleted
	 */
	synchronized static void deleteCategory(String name) {
		// TODO Implement me
	}

	/**
	 * Checks if a category given by it's name exists. If hte argument is null
	 * or empty false is returned.
	 * 
	 * @param name
	 *            of the category to be checked
	 * @return true if existing, otherwise false
	 */
	synchronized static boolean exists(String name) {
		// TODO Implement me
		return false;
	}

	/**
	 * Gets a list with the direct subcategories of this category.
	 * 
	 * If an {@link IOException} happens during the access to the persistence,
	 * the result is null and the exception is written to the System.err
	 * 
	 * @return A list containing all subcategories of this category
	 */
	private List<Category> getSubCategories() {
		try {
			return Persistence.instance.getSubcategories(this);
		} catch (IOException e) {
			System.err.println("IOException getting subcategories of " + this.name);
			e.printStackTrace(System.err);
			return null;
		}
	}

	/**
	 * Gets a list of all snippets of this category. Returns null, if an
	 * {@link IOException} occurs during access to the persistence. In tihs case
	 * the exception is printed to System.err
	 * 
	 * @return a list containing all snippets of this category
	 */
	private List<Snippet> getSnippets() {
		try {
			return Persistence.instance.getSnippets(this);
		} catch (IOException e) {
			System.err.println("IOException getting snippets of " + this.name);
			e.printStackTrace(System.err);
			return null;
		}
	}

	XCategory toXCategory() {
		XCategory result = null;
		List<String> childs = new ArrayList<String>();
		List<Category> subTree = getSubCategories();
		for (Category root : subTree) {
			childs.add(root.name);
		}

		if (isRoot()) {
			result = new XCategory(name, description, null, childs);
		} else {
			result = new XCategory(name, description, parent.name, childs);
		}

		return result;
	}

	private boolean isRoot() {
		// TODO Implement me ...
		return parent == null;
	}
}

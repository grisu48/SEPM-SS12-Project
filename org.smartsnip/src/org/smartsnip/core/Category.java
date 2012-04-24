package org.smartsnip.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Category {
	/** List of all root categories */
	private static List<Category> rootCategories = new ArrayList<Category>();
	/** All categories */
	private static HashMap<String, Category> allCategories = new HashMap<String, Category>();
	/** Snippets */
	private final List<Snippet> snippets = new ArrayList<Snippet>();

	/** Name of the category */
	private String name;
	/** Description of the category */
	private String description;
	/** Parent category, if present, or null if a root category */
	private Category parent = null;
	/** Sub-categories */
	private final List<Category> subcategories;

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
		this.subcategories = new ArrayList<Category>();
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
	 */
	synchronized static Category createCategory(String name, String description, Category parent) {
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
		return allCategories.get(name.trim().toLowerCase());
	}

	/**
	 * @return a String list containing all root categories
	 */
	synchronized static List<String> getCategories() {
		List<String> result = new ArrayList<String>();

		for (Category item : rootCategories) {
			result.add(item.name);
		}
		return result;
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
		Category prnt = getCategory(parent);
		if (prnt == null)
			return new ArrayList<String>();

		List<String> result = new ArrayList<String>(prnt.subcategories.size());
		for (Category item : prnt.subcategories) {
			result.add(item.name);
		}

		return result;
	}

	/**
	 * @return the name of the category
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the category. If null or empty, nothing happends
	 * 
	 * @param name
	 *            the name to set
	 */
	void setName(String name) {
		if (name == null || name.length() == 0)
			return;
		if (this.name.equals(name))
			return;
		this.name = name;
		refreshDB();
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
			if (parent == null)
			parent = Persistence.persistence.getParentCategory(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
		if (subcategories.contains(category))
			return;
		subcategories.add(category);
		refreshDB();
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
		if (!subcategories.contains(category))
			return;
		subcategories.remove(category);
		refreshDB();
	}

	/**
	 * Gets the list of snippets of this category
	 * 
	 * @return the list of snippets for this category
	 */
	List<Snippet> getSnippets() {
		// TODO: Implement me!
		return new ArrayList<Snippet>();
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
	 */
	synchronized protected static void addToDB(Category category) {
		if (category == null)
			return;
		String name = category.name.trim().toLowerCase();

		if (category.parent == null) {
			// Root category
			if (!rootCategories.contains(category)) {
				rootCategories.add(category);
			}
		} else {
			// Subcategory
			if (!category.parent.subcategories.contains(category)) {
				category.parent.subcategories.add(category);
			}
		}

		allCategories.put(name, category);

		// TODO Implement me!
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

		if (!snippets.contains(snippet))
			return;
		snippets.remove(snippet);
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

		if (snippets.contains(snippet))
			return;
		snippets.add(snippet);
	}

	/**
	 * @return the total number of categories
	 */
	synchronized static int totalCount() {
		return allCategories.size();
	}

	/**
	 * Deletes a category out of the system. If the category cannot be found,
	 * nothing is done.
	 * 
	 * @param name
	 *            the name of the category to be deleted
	 */
	synchronized static void deleteCategory(String name) {
		if (!exists(name))
			return;

		name = name.trim().toLowerCase();
		Category category = allCategories.get(name);
		if (category == null)
			return;
		if (category.parent != null) {
			category.parent.subcategories.remove(category);
		}
		allCategories.remove(name);
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
		if (name == null || name.isEmpty())
			return false;

		name = name.trim().toLowerCase();
		return allCategories.containsKey(name);
	}
}

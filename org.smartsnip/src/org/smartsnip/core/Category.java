package org.smartsnip.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Category {
	/** Container for all categories. The key string is always in lowercase */
	private static final HashMap<String, Category> allCategories = new HashMap<String, Category>();
	/**
	 * List of all categories, used as a cache for returning all categories in
	 * getCategory. Do not directly access this attribute - it may be null. In
	 * this case the method getCategory rebuilds the list.
	 */
	private static List<Category> categories = new ArrayList<Category>();

	/** Name of the category */
	private String name;
	/** Description of the category */
	private String description;
	/** Parent category, if present, or null if a root category */
	private Category parent;
	/** Subcategories */
	private List<Category> subcategories;

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
		if (name == null || description == null) throw new NullPointerException();
		if (name.length() == 0) throw new IllegalArgumentException("Empty category name not allowed");
		if (description.length() == 0) throw new IllegalArgumentException("Empty category description not allowed");

		this.name = name;
		this.description = description;
		this.parent = parent;
		this.subcategories = new ArrayList<Category>();
	}

	/**
	 * Gets a category by his name. If the name is null or empty, null is
	 * returned. If no category is found, null is returned
	 * 
	 * @param name
	 *            of the category to be searched
	 * @return the category with the given name if found, otherwise null
	 */
	static Category getCategory(String name) {
		if (name == null || name.length() == 0) return null;
		return allCategories.get(name.toLowerCase());
	}

	/**
	 * @return a list with all categories
	 */
	static List<Category> getCategories() {
		synchronized (categories) {
			if (categories != null) return categories;

			categories = new ArrayList<Category>(allCategories.size());
			Iterator<Category> iterator = allCategories.values().iterator();
			while (iterator.hasNext())
				categories.add(iterator.next());
			return categories;
		}
	}

	/**
	 * @return the name of the category
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this category. If null or empty, nothing happends
	 * 
	 * @param name
	 *            the name to set
	 */
	void setName(String name) {
		if (name == null || name.length() == 0) return;
		if (this.name.equals(name)) return;
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
		if (description == null || description.length() == 0) return;
		if (this.description.equals(description)) return;
		this.description = description;
		refreshDB();
	}

	/**
	 * @return the parent
	 */
	public Category getParent() {
		return parent;
	}

	/**
	 * Sets the parent of the category
	 * 
	 * @param parent
	 *            the parent to set
	 */
	void setParent(Category parent) {
		if (this.parent == parent) return;
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
		if (category == null) return;
		if (subcategories.contains(category)) return;
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
		if (category == null) return;
		if (!subcategories.contains(category)) return;
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
	protected void refreshDB() {

	}
}

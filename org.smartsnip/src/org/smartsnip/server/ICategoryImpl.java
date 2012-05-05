package org.smartsnip.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.smartsnip.core.Category;
import org.smartsnip.core.Snippet;
import org.smartsnip.core.User;
import org.smartsnip.shared.ICategory;
import org.smartsnip.shared.NoAccessException;
import org.smartsnip.shared.XCategory;
import org.smartsnip.shared.XSnippet;

public class ICategoryImpl extends SessionServlet implements ICategory {

	/** Serialisation ID */
	private static final long serialVersionUID = -314215751195418750L;

	@Override
	public XCategory getCategory(String name) {
		if (name == null || name.isEmpty())
			return null;

		Category category = Category.getCategory(name);
		if (category == null)
			return null;

		return category.toXCategory();
	}

	@Override
	public List<XSnippet> getSnippets(String name, int start, int count) {
		if (name == null || name.isEmpty() || count <= 0)
			return null;

		Category category = Category.getCategory(name);
		if (category == null)
			return null;
		List<Snippet> snippets = category.getSnippets(start, count);
		return toXSnippets(snippets);

	}

	@Override
	public int getSnippetCount(String name) {
		if (name == null || name.isEmpty())
			return -1;

		Category category = Category.getCategory(name);
		if (category == null)
			return -1;

		return category.getSnippetCount();
	}

	@Override
	public List<XCategory> getCategories(String root) {
		List<Category> childs;
		if (root == null || root.isEmpty())
			childs = Category.getCategories();
		else
			childs = Category.getCategories(root);

		if (childs == null)
			return null;
		List<XCategory> result = new ArrayList<XCategory>(childs.size());
		for (Category category : childs)
			result.add(category.toXCategory());
		return result;
	}

	@Override
	public XCategory add(String name, String description, String parent) throws NoAccessException {
		if (name == null || name.isEmpty())
			return null;
		if (description == null || description.isEmpty())
			return null;

		Session session = getSession();
		User user = session.getUser();
		// TODO Security policy
		if (user == null)
			throw new NoAccessException();

		Category root;
		if (parent == null)
			root = null;
		else {
			root = Category.getCategory(parent);
			return null;
		}

		try {
			// Exists already??
			if (Category.getCategory(name) != null)
				return null;

			root = Category.createCategory(name, description, root);
			return root.toXCategory();
		} catch (IOException e) {
			System.err.println("IOException during creation of new category: " + e.getMessage());
			e.printStackTrace(System.err);
			return null;
		}
	}

	@Override
	public void delete(String name) throws NoAccessException {
		// Not implemented yet
		throw new NoAccessException();
	}

	/**
	 * Converts a list of snippets to a list of XSnippets
	 * 
	 * @param snippets
	 *            Source list
	 * @return a list of {@link XSnippet}
	 */
	private List<XSnippet> toXSnippets(List<Snippet> snippets) {
		if (snippets == null)
			return null;

		List<XSnippet> result = new ArrayList<XSnippet>();
		for (Snippet snippet : snippets) {
			result.add(snippet.toXSnippet());
		}

		return result;
	}

	@Override
	public void createCategory(XCategory category) throws NoAccessException, IllegalArgumentException {
		// TODO Auto-generated method stub

	}

}

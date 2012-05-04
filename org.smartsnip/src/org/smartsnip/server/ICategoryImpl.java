package org.smartsnip.server;

import java.util.ArrayList;
import java.util.List;

import org.smartsnip.core.Category;
import org.smartsnip.core.Snippet;
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
	public List<XCategory> getChildCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<XCategory> getCategories(String root) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XCategory add(String name, String parent) throws NoAccessException {
		// Not implemented yet
		throw new NoAccessException();
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

}

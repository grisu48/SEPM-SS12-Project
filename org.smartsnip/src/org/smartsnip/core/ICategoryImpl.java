package org.smartsnip.core;

import java.util.ArrayList;
import java.util.List;

import org.smartsnip.core.Code.UnsupportedLanguageException;
import org.smartsnip.shared.ICategory;
import org.smartsnip.shared.ISnippet;
import org.smartsnip.shared.NoAccessException;
import org.smartsnip.shared.Pair;

public class ICategoryImpl extends SessionServlet implements ICategory {

	/** Serialisation ID */
	private static final long serialVersionUID = 6002779840902948383L;

	/** Associated category to this category object */
	protected final Category category;

	public ICategoryImpl(Category category) {
		if (category == null) throw new NullPointerException();
		this.category = category;
	}

	@Override
	public synchronized String getName() {
		return category.getName();
	}

	@Override
	public synchronized void setName(String name) throws NoAccessException {
		Session session = getSession();

		if (!session.getPolicy().canEditCategory(session, category)) throw new NoAccessException();
		category.setName(name);
	}

	@Override
	public synchronized String getDescription() {
		return category.getDescription();
	}

	@Override
	public synchronized void setDescription(String desc) throws NoAccessException {
		Session session = getSession();

		if (!session.getPolicy().canEditCategory(session, category)) throw new NoAccessException();

		category.setDescription(desc);
	}

	@Override
	public synchronized void addSnippet(String name, String description, String code, String language)
			throws IllegalArgumentException, NoAccessException {
		Session session = getSession();

		if (!session.getPolicy().canCreateSnippet(session, category)) throw new NoAccessException();

		User user = session.getUser();
		if (user == null) throw new NoAccessException();
		try {
			Snippet snippet = Snippet.createSnippet(user, name, description, code, language);

			category.addSnippet(snippet);
		} catch (UnsupportedLanguageException e) {
			// Language is not supported
			throw new IllegalArgumentException("Unsupported language: " + language);
		}
	}

	@Override
	public synchronized List<Pair<Integer, String>> getSnippets() {
		List<Snippet> snippets = category.getSnippets();

		ArrayList<Pair<Integer, String>> result = new ArrayList<Pair<Integer, String>>();
		for (Snippet snippet : snippets) {
			result.add(new Pair<Integer, String>(snippet.hash, snippet.getName()));
		}

		return result;
	}

	@Override
	public synchronized List<ISnippet> getISnippets() {
		Session session = getSession();
		List<Snippet> snippets = category.getSnippets();

		ArrayList<ISnippet> result = new ArrayList<ISnippet>();
		for (Snippet snippet : snippets) {
			result.add(session.getISnippet(snippet.hash));
		}

		return result;
	}
}

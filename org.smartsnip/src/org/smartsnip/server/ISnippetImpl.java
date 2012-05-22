package org.smartsnip.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.smartsnip.core.Category;
import org.smartsnip.core.Snippet;
import org.smartsnip.core.Tag;
import org.smartsnip.core.User;
import org.smartsnip.shared.ISnippet;
import org.smartsnip.shared.NoAccessException;
import org.smartsnip.shared.XComment;
import org.smartsnip.shared.XSnippet;

public class ISnippetImpl extends SessionServlet implements ISnippet {

	/** Serialisation ID */
	private static final long serialVersionUID = -1493947420774219096L;

	@Override
	public List<XComment> getComments(long snippet, int start, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCommentCount(long hash) {
		Snippet snippet = Snippet.getSnippet(hash);
		if (snippet == null)
			return 0;

		return snippet.getCommentCount();
	}

	@Override
	public XSnippet getSnippet(long hash) {
		Snippet snippet = Snippet.getSnippet(hash);
		if (snippet == null)
			return null;

		return snippet.toXSnippet();
	}

	@Override
	public void delete(long hash) throws NoAccessException {
		Snippet snippet = Snippet.getSnippet(hash);
		if (snippet == null)
			return;

		Session session = getSession();
		if (!session.getPolicy().canDeleteSnippet(session, snippet))
			throw new NoAccessException();

		snippet.delete();
	}

	@Override
	public void rateSnippet(long id, int rate) throws NoAccessException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDescription(long id, String desc) throws NoAccessException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCode(long id, String code) throws NoAccessException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addTag(long id, String tag) throws NoAccessException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeTag(long id, String tag) throws NoAccessException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addComment(long id, String comment) throws NoAccessException {
		// TODO Auto-generated method stub

	}

	@Override
	public void create(String name, String desc, String code, String language,
			String category, List<String> tags) throws NoAccessException,
			IllegalArgumentException {

		if (name == null || name.isEmpty())
			throw new IllegalArgumentException(
					"New snippet name cannot be empty");
		if (desc == null || desc.isEmpty())
			throw new IllegalArgumentException(
					"New snippet desc cannot be empty");
		if (code == null || code.isEmpty())
			throw new IllegalArgumentException(
					"New snippet code cannot be empty");
		if (language == null || language.isEmpty())
			throw new IllegalArgumentException(
					"New snippet language cannot be empty");

		if (category.isEmpty())
			category = null;
		if (tags == null)
			tags = new ArrayList<String>();

		Category cat = Category.getCategory(category);
		if (category != null && cat == null)
			throw new IllegalArgumentException("Category not found");

		Session session = getSession();
		if (!session.getPolicy().canCreateSnippet(session, cat))
			throw new NoAccessException();

		User owner = session.getUser();
		if (owner == null)
			throw new NoAccessException();

		List<Tag> tagList = new ArrayList<Tag>();
		for (String tag : tags)
			tagList.add(Tag.createTag(tag));

		try {
			Snippet result = Snippet.createSnippet(owner.getUsername(), name,
					desc, category, code, language, "", tagList);

			if (result == null)
				throw new RuntimeException("Unknown error");
		} catch (IOException e) {
			System.err.println("IOException while creating new snippet: "
					+ e.getMessage());
			e.printStackTrace(System.err);

			throw new RuntimeException("Database access error");
		}

	}

}

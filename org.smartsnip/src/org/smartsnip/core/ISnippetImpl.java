package org.smartsnip.core;

import java.util.ArrayList;
import java.util.List;

import org.smartsnip.shared.IComment;
import org.smartsnip.shared.ISnippet;
import org.smartsnip.shared.IUser;
import org.smartsnip.shared.NoAccessException;

public class ISnippetImpl extends SessionServlet implements ISnippet {

	/** Associated snippet */
	protected final Snippet snippet;

	/** Default constructors initialises comment */
	public ISnippetImpl(Snippet snippet) {
		super();

		if (snippet == null) throw new NullPointerException();
		this.snippet = snippet;
	}

	@Override
	public String getName() {
		return snippet.getName();
	}

	@Override
	public IUser getOwner() {
		Session session = getSession();

		return session.getIUser(snippet.owner);
	}

	@Override
	public String getDesc() {
		return snippet.getDescription();
	}

	@Override
	public String getLanguage() {
		return snippet.getCode().getLanguage();
	}

	@Override
	public int getHash() {
		return snippet.hash;
	}

	@Override
	public String getCodeHTML() {
		return snippet.getCode().getFormattedHTML();
	}

	@Override
	public List<String> getTags() {
		List<Tag> tags = snippet.getTags();
		ArrayList<String> result = new ArrayList<String>(tags.size());

		for (Tag tag : tags) {
			result.add(tag.name);
		}

		return result;
	}

	@Override
	public String getCategory() {
		return snippet.getCategory().getName();
	}

	@Override
	public void addTag(String tag) throws NoAccessException {
		Session session = getSession();

		if (!session.getPolicy().canTagSnippet(session, snippet)) throw new NoAccessException();

		snippet.addTag(Tag.createTag(tag));
	}

	@Override
	public void removeTag(String tag) throws NoAccessException {
		Session session = getSession();

		if (!session.getPolicy().canTagSnippet(session, snippet)) throw new NoAccessException();

		Tag tagO = Tag.getTag(tag);
		if (tagO == null) return; // Tag doesn't exists

		snippet.removeTag(tagO);

	}

	@Override
	public List<IComment> getComments() throws NoAccessException {
		Session session = getSession();

		List<Comment> comments = snippet.getComments();
		ArrayList<IComment> result = new ArrayList<IComment>();

		for (Comment comment : comments) {
			result.add(session.getIComment(comment));
		}

		return result;
	}

	@Override
	public int getViewCount() {
		return snippet.getViewcount();
	}

	@Override
	public void increaseViewCounter() throws NoAccessException {
		// XXX Caution: To invoke this call on the client can be a security
		// issue
		snippet.increaseViewCounter();
	}

	@Override
	public IComment addComment(String comment) throws NoAccessException {
		Session session = getSession();

		if (comment == null || comment.isEmpty()) return null;
		if (!session.getPolicy().canComment(session)) throw new NoAccessException();

		User user = session.getUser();
		if (user == null) throw new NoAccessException();
		Comment result = Comment.createComment(user, snippet, comment);
		snippet.addComment(result);
		return session.getIComment(result);
	}

	@Override
	public void addFavorite() {
		Session session = getSession();

		session.addFavorite(snippet);
	}

	@Override
	public void removeFavorite() {
		Session session = getSession();

		session.removeFavorite(snippet);
	}

	@Override
	public void delete() throws NoAccessException {
		Session session = getSession();

		if (!session.getPolicy().canDeleteSnippet(session, snippet)) throw new NoAccessException();

		snippet.delete();
	}

}

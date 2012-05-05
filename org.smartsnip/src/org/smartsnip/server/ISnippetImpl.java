package org.smartsnip.server;

import java.util.List;

import org.smartsnip.core.Snippet;
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
	public void create(XSnippet snippet) throws NoAccessException, IllegalArgumentException {
		// TODO Auto-generated method stub

	}

}

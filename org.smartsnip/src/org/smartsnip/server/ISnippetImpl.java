package org.smartsnip.server;

import java.util.List;

import org.smartsnip.core.Snippet;
import org.smartsnip.shared.ISnippet;
import org.smartsnip.shared.XComment;
import org.smartsnip.shared.XSnippet;

public class ISnippetImpl extends SessionServlet implements ISnippet {

	/** Serialisation ID */
	private static final long serialVersionUID = -1493947420774219096L;

	@Override
	public List<XComment> getComments(int snippet, int start, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCommentCount(int hash) {
		Snippet snippet = Snippet.getSnippet(hash);
		if (snippet == null)
			return 0;

		return snippet.getCommentCount();
	}

	@Override
	public XSnippet getSnippet(int hash) {
		Snippet snippet = Snippet.getSnippet(hash);
		if (snippet == null)
			return null;

		return snippet.toXSnippet();
	}

}

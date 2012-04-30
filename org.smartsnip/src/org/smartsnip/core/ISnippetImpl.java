package org.smartsnip.core;

import java.util.List;

import org.smartsnip.shared.ISnippet;
import org.smartsnip.shared.XComment;
import org.smartsnip.shared.XSnippet;

public class ISnippetImpl extends SessionServlet implements ISnippet {

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

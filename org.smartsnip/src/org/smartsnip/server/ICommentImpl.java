package org.smartsnip.server;

import java.util.ArrayList;
import java.util.List;

import org.smartsnip.core.Comment;
import org.smartsnip.core.Snippet;
import org.smartsnip.core.User;
import org.smartsnip.shared.IComment;
import org.smartsnip.shared.NoAccessException;
import org.smartsnip.shared.NotFoundException;
import org.smartsnip.shared.XComment;

public class ICommentImpl extends SessionServlet implements IComment {

	/** Serialisation ID */
	private static final long serialVersionUID = 5843093547608960627L;

	@Override
	public List<XComment> getComments(long snippethash, int start, int count)
			throws NoAccessException {
		Snippet snippet = Snippet.getSnippet(snippethash);
		if (snippet == null)
			return null;

		List<Comment> comments = snippet.getComments();
		comments = org.smartsnip.core.Util.cropList(comments, start, count);
		List<XComment> result = new ArrayList<XComment>(comments.size());
		for (Comment comment : comments)
			result.add(comment.toXComment());

		return result;
	}

	@Override
	public void votePositive(long commentID) throws NoAccessException {
		Session session = getSession();
		if (!session.getPolicy().canComment(session))
			throw new NoAccessException();
		User user = session.getUser();
		if (user == null)
			throw new NoAccessException();
		Comment comment = Comment.getComment(commentID);
		if (comment == null)
			return;

		comment.votePositive(user);
	}

	@Override
	public void voteNegative(long commentID) throws NoAccessException {
		Session session = getSession();
		if (!session.getPolicy().canComment(session))
			throw new NoAccessException();
		User user = session.getUser();
		if (user == null)
			throw new NoAccessException();
		Comment comment = Comment.getComment(commentID);
		if (comment == null)
			return;

		comment.voteNegative(user);
	}

	@Override
	public void unvote(long commentID) throws NoAccessException {
		Session session = getSession();
		if (!session.getPolicy().canComment(session))
			throw new NoAccessException();
		User user = session.getUser();
		if (user == null)
			throw new NoAccessException();
		Comment comment = Comment.getComment(commentID);
		if (comment == null)
			return;

		comment.unvote(user);
	}

	@Override
	public void edit(long commentID, String newMessage)
			throws NoAccessException {
		Session session = getSession();
		Comment comment = Comment.getComment(commentID);
		if (comment == null)
			return;
		if (!session.getPolicy().canEditComment(session, comment))
			throw new NoAccessException();
		User user = session.getUser();
		if (user == null)
			throw new NoAccessException();

		comment.edit(newMessage);
	}

	@Override
	public void delete(long commentID) throws NoAccessException {
		Session session = getSession();
		Comment comment = Comment.getComment(commentID);
		if (comment == null)
			return;
		if (!session.getPolicy().canEditComment(session, comment))
			throw new NoAccessException();
		User user = session.getUser();
		if (user == null)
			throw new NoAccessException();

		comment.delete();
	}

	@Override
	public boolean canComment(long commentID) throws NotFoundException {
		Comment comment = Comment.getComment(commentID);
		if (comment == null)
			throw new NotFoundException();

		Session session = getSession();
		Snippet snippet = Snippet.getSnippet(comment.snippet);
		if (!session.getPolicy().canRateSnippet(session, snippet))
			return false;

		User user = session.getUser();
		if (user == null)
			return false;

		return true;
	}

}

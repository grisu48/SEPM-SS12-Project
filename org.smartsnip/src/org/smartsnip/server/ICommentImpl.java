package org.smartsnip.server;

import java.util.ArrayList;
import java.util.List;

import org.smartsnip.core.Comment;
import org.smartsnip.core.Logging;
import org.smartsnip.core.Snippet;
import org.smartsnip.core.User;
import org.smartsnip.shared.IComment;
import org.smartsnip.shared.NoAccessException;
import org.smartsnip.shared.NotFoundException;
import org.smartsnip.shared.XComment;

public class ICommentImpl extends GWTSessionServlet implements IComment {

	/** Serialisation ID */
	private static final long serialVersionUID = 5843093547608960627L;

	@Override
	public List<XComment> getComments(long snippethash, int start, int count) throws NoAccessException {
		Snippet snippet = Snippet.getSnippet(snippethash);
		if (snippet == null)
			return null;

		Session session = getSession();
		List<Comment> comments = snippet.getComments();
		comments = org.smartsnip.core.Util.cropList(comments, start, count);
		List<XComment> result = new ArrayList<XComment>(comments.size());
		for (Comment comment : comments)
			result.add(toXComment(comment, session));

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

		Logging.printInfo("Votes positive on comment " + commentID);
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

		Logging.printInfo("Votes negative on comment " + commentID);
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

		Logging.printInfo("Unvotes on comment " + commentID);
		comment.unvote(user);
	}

	@Override
	public void edit(long commentID, String newMessage) throws NoAccessException {
		Session session = getSession();
		Comment comment = Comment.getComment(commentID);
		if (comment == null)
			return;
		if (!session.getPolicy().canEditComment(session, comment))
			throw new NoAccessException();
		User user = session.getUser();
		if (user == null)
			throw new NoAccessException();

		Logging.printInfo("Editing comment wit id " + commentID);
		comment.edit(newMessage);

		User owner = comment.getOwner();
		if (!user.equals(owner))
			owner.createNotification("Your comment \"" + comment.getMessage() + "\" has been edited by " + user.getUsername(), "Snippet "
					+ comment.getSnippet().getName());
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

		Logging.printInfo("Deleting comment wit id " + commentID);
		comment.delete();

		User owner = comment.getOwner();
		if (!user.equals(owner))
			owner.createNotification("Your comment \"" + comment.getMessage() + "\" has been deleted", user.getUsername());
	}

	@Override
	public boolean canComment(long snippetID) throws NotFoundException {
		Session session = getSession();
		Snippet snippet = Snippet.getSnippet(snippetID);
		if (snippet == null)
			throw new NotFoundException();
		if (!session.getPolicy().canComment(session))
			return false;

		User user = session.getUser();
		if (user == null)
			return false;

		return true;
	}

	@Override
	public boolean canRate(long commentID) throws NotFoundException {
		Comment comment = Comment.getComment(commentID);
		if (comment == null)
			throw new NotFoundException();

		Session session = getSession();
		Snippet snippet = Snippet.getSnippet(comment.getSnippetId());
		if (snippet == null)
			throw new NotFoundException();
		if (!session.getPolicy().canRateSnippet(session, snippet))
			return false;

		User user = session.getUser();
		if (user == null)
			return false;

		return true;
	}

	@Override
	public boolean canEdit(long commentID) throws NotFoundException {
		Comment comment = Comment.getComment(commentID);
		if (comment == null)
			throw new NotFoundException();

		Session session = getSession();
		if (!session.getPolicy().canEditComment(session, comment))
			return false;

		User user = session.getUser();
		if (user == null)
			return false;

		return true;
	}

	@Override
	public XComment getComment(long commentID) throws NotFoundException, NoAccessException {
		Comment comment = Comment.getComment(commentID);
		if (comment == null)
			throw new NotFoundException();

		return toXComment(comment, getSession());
	}

	/**
	 * Converts a given comment to a {@link XComment} object. Call this instant
	 * of {@link Comment#toXComment()} to also apply the session-specific values
	 * 
	 * @param comment
	 *            to be converted
	 * @param session
	 *            to apply the session specific values
	 * @return converted {@link XComment} value
	 */
	static XComment toXComment(Comment comment, Session session) {
		if (comment == null)
			return null;
		XComment result = comment.toXComment();
		if (session != null) {
			result.canDelete = session.getPolicy().canDeleteComment(session, comment);
			result.canEdit = session.getPolicy().canEditComment(session, comment);
		}
		return result;
	}
}

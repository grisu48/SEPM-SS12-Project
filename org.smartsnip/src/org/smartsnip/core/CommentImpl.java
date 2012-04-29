package org.smartsnip.core;

import java.util.Date;

import org.smartsnip.shared.IComment;
import org.smartsnip.shared.IUser;
import org.smartsnip.shared.NoAccessException;

public class CommentImpl extends SessionServlet implements IComment {

	/** Serialisation ID */
	private static final long serialVersionUID = 5843093547608960627L;

	/** Associated comment */
	protected final Comment comment;

	/** Default constructors initialises comment */
	public CommentImpl(Comment comment) {
		super();

		if (comment == null)
			throw new NullPointerException();
		this.comment = comment;
	}

	@Override
	public String getMessage() {
		return comment.getMessage();
	}

	@Override
	public Date getLastModificationTime() {
		return comment.getTime();
	}

	@Override
	public void ratePositive() throws NoAccessException {
		if (!session.getPolicy().canRateSnippet(session, comment.snippet))
			throw new NoAccessException();

		User user = session.getUser();
		if (user == null)
			throw new NoAccessException();
		comment.votePositive(user);
	}

	@Override
	public void rateNegative() throws NoAccessException {
		if (!session.getPolicy().canRateSnippet(session, comment.snippet))
			throw new NoAccessException();

		User user = session.getUser();
		if (user == null)
			throw new NoAccessException();
		comment.voteNegative(user);
	}

	@Override
	public void removeRating() throws NoAccessException {
		if (!session.getPolicy().canRateSnippet(session, comment.snippet))
			throw new NoAccessException();

		User user = session.getUser();
		if (user == null)
			throw new NoAccessException();
		comment.unvote(user);
	}

	@Override
	public IUser getOwner() throws NoAccessException {
		return session.getIUser(comment.owner);
	}

	@Override
	public void delete() throws NoAccessException {
		if (!session.getPolicy().canEditComment(session, comment))
			throw new NoAccessException();

		comment.delete();
	}

	@Override
	public void edit(String newComment) throws NoAccessException {
		if (!session.getPolicy().canEditComment(session, comment))
			throw new NoAccessException();
		comment.edit(newComment);
	}

	@Override
	public void report() throws NoAccessException {
		session.report(comment);
	}

}

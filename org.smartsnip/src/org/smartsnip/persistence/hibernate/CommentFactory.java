/**
 * File: CommentFactory.java
 * Date: 11.05.2012
 */
package org.smartsnip.persistence.hibernate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.smartsnip.core.Comment;
import org.smartsnip.core.Snippet;
import org.smartsnip.core.User;
import org.smartsnip.persistence.IPersistence;
import org.smartsnip.shared.Pair;

/**
 * @author Gerhard Aigner
 * 
 */
public class CommentFactory {

	private CommentFactory() {
		// no instances
	}

	/**
	 * Implementation of {@link IPersistence#writeComment(Comment, int)}
	 * 
	 * @param comment
	 * @param flags
	 * @return the Id
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeComment(org.smartsnip.core.Comment,
	 *      int)
	 */
	static Long writeComment(Comment comment, int flags) throws IOException {
		Session session = DBSessionFactory.open();
		Long result;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			DBComment entity = new DBComment();
			entity.setCommentId(comment.getHashID()); // commentId is read-only
			entity.setSnippetId(comment.getSnippetId());
			entity.setMessage(comment.getMessage());
			// createdAt is read-only
			entity.setUserName(comment.getOwnerName());
			// posVotes is read-only
			// negVotes is read-only

			result = (Long) query.write(entity, flags);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
		return result;
	}

	/**
	 * Implementation of {@link IPersistence#writeComment(List, int)}
	 * 
	 * @param comments
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeComment(List,
	 *      int)
	 */
	static void writeComment(List<Comment> comments, int flags)
			throws IOException {
		Session session = DBSessionFactory.open();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query;

			DBComment entity;
			for (Comment comment : comments) {
				query = new DBQuery(session);
				entity = new DBComment();
				entity.setCommentId(comment.getHashID()); // commentId is
															// read-only
				entity.setSnippetId(comment.getSnippetId());
				entity.setMessage(comment.getMessage());
				// createdAt is read-only
				entity.setUserName(comment.getOwnerName());
				// posVotes is read-only
				// negVotes is read-only

				query.write(entity, flags);
			}
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
	}

	/**
	 * Implementation of
	 * {@link IPersistence#writeVote(Integer, Comment, User, int)}
	 * 
	 * @param vote
	 * @param comment
	 * @param user
	 * @param flags
	 * @return a pair of numbers representing the positive and negative votes
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeVote(java.lang.Integer,
	 *      org.smartsnip.core.Comment, org.smartsnip.core.User, int)
	 */
	static Pair<Integer, Integer> writeVote(Integer vote, Comment comment,
			User user, int flags) throws IOException {
		Session session = DBSessionFactory.open();
		Integer posVotes;
		Integer negVotes;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			// fetch old votes before trigger updates them
			DBComment dbComment = fetchComment(comment.getHashID(), session);
			posVotes = dbComment.getPosVotes();
			negVotes = dbComment.getNegVotes();

			DBQuery query = new DBQuery(session);
			DBVote entity = new DBVote();

			entity.setVoteId(comment.getHashID(), user.getUsername());
			DBVote.Vote prevValue = (DBVote.Vote) query.selectSingle(entity,
					"vote", DBQuery.QUERY_NULLABLE
							| DBQuery.QUERY_UNIQUE_RESULT);
			if (prevValue != null) {
				switch (prevValue) {
				case positive:
					posVotes--;
					break;
				case negative:
					negVotes--;
					break;
				default:
					// do nothing
					break;
				}
			}

			// write new vote
			query.reset();
			DBVote.Vote dbVote = DBVote.Vote.none;
			if (vote > 0) {
				dbVote = DBVote.Vote.positive;
				posVotes++;
			}
			if (vote < 0) {
				dbVote = DBVote.Vote.negative;
				negVotes++;
			}
			entity.setVote(dbVote);

			query.write(entity, flags);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
		return new Pair<Integer, Integer>(posVotes, negVotes);
	}

	/**
	 * Implementation of {@link IPersistence#votePositive(User, Comment, int)}
	 * 
	 * @param user
	 * @param comment
	 * @param flags
	 * @return a pair of numbers representing the positive and negative votes
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#votePositive(org.smartsnip.core.User,
	 *      org.smartsnip.core.Comment, int)
	 */
	static Pair<Integer, Integer> votePositive(User user, Comment comment,
			int flags) throws IOException {
		Session session = DBSessionFactory.open();
		Integer posVotes;
		Integer negVotes;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			// fetch old votes before trigger updates them
			DBComment dbComment = fetchComment(comment.getHashID(), session);
			posVotes = dbComment.getPosVotes();
			negVotes = dbComment.getNegVotes();

			DBQuery query = new DBQuery(session);

			DBVote entity = new DBVote();

			entity.setVoteId(comment.getHashID(), user.getUsername());
			DBVote.Vote prevValue = (DBVote.Vote) query.selectSingle(entity,
					"vote", DBQuery.QUERY_CACHEABLE | DBQuery.QUERY_NULLABLE
							| DBQuery.QUERY_UNIQUE_RESULT);
			if (prevValue != null) {
				switch (prevValue) {
				case positive:
					posVotes--;
					break;
				case negative:
					negVotes--;
					break;
				default:
					// do nothing
					break;
				}
			}

			// write new vote
			query.reset();
			entity.setVote(DBVote.Vote.positive);

			query.write(entity, flags);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
		return new Pair<Integer, Integer>(posVotes + 1, negVotes);
	}

	/**
	 * Implementation of {@link IPersistence#voteNegative(User, Comment, int)}
	 * 
	 * @param user
	 * @param comment
	 * @param flags
	 * @return a pair of numbers representing the positive and negative votes
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#voteNegative(org.smartsnip.core.User,
	 *      org.smartsnip.core.Comment, int)
	 */
	static Pair<Integer, Integer> voteNegative(User user, Comment comment,
			int flags) throws IOException {
		Session session = DBSessionFactory.open();
		Integer posVotes;
		Integer negVotes;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			// fetch old votes before trigger updates them
			DBComment dbComment = fetchComment(comment.getHashID(), session);
			posVotes = dbComment.getPosVotes();
			negVotes = dbComment.getNegVotes();

			DBQuery query = new DBQuery(session);

			DBVote entity = new DBVote();

			entity.setVoteId(comment.getHashID(), user.getUsername());
			DBVote.Vote prevValue = (DBVote.Vote) query.selectSingle(entity,
					"vote", DBQuery.QUERY_CACHEABLE | DBQuery.QUERY_NULLABLE
							| DBQuery.QUERY_UNIQUE_RESULT);
			if (prevValue != null) {
				switch (prevValue) {
				case positive:
					posVotes--;
					break;
				case negative:
					negVotes--;
					break;
				default:
					// do nothing
					break;
				}
			}

			// write new vote
			query.reset();
			entity.setVote(DBVote.Vote.negative);

			query.write(entity, flags);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
		return new Pair<Integer, Integer>(posVotes, negVotes + 1);
	}

	/**
	 * Implementation of {@link IPersistence#unVote(User, Comment, int)}
	 * 
	 * @param user
	 * @param comment
	 * @param flags
	 * @return a pair of numbers representing the positive and negative votes
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#unVote(org.smartsnip.core.User,
	 *      org.smartsnip.core.Comment, int)
	 */
	static Pair<Integer, Integer> unVote(User user, Comment comment, int flags)
			throws IOException {
		Session session = DBSessionFactory.open();
		Integer posVotes;
		Integer negVotes;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			// fetch old votes before trigger updates them
			DBComment dbComment = fetchComment(comment.getHashID(), session);
			posVotes = dbComment.getPosVotes();
			negVotes = dbComment.getNegVotes();

			DBQuery query = new DBQuery(session);

			DBVote entity = new DBVote();
			entity.setVoteId(comment.getHashID(), user.getUsername());
			DBVote.Vote prevValue = (DBVote.Vote) query.selectSingle(entity,
					"vote", DBQuery.QUERY_NULLABLE
							| DBQuery.QUERY_UNIQUE_RESULT);
			if (prevValue != null) {
				switch (prevValue) {
				case positive:
					posVotes--;
					break;
				case negative:
					negVotes--;
					break;
				default:
					// do nothing
					break;
				}
			}

			// remove vote
			query.reset();

			query.remove(entity, flags);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
		return new Pair<Integer, Integer>(posVotes, negVotes);
	}

	/**
	 * Implementation of {@link IPersistence#removeComment(Comment, int)}
	 * 
	 * @param comment
	 * @param flags
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeComment(org.smartsnip.core.Comment,
	 *      int)
	 */
	static void removeComment(Comment comment, int flags) throws IOException {
		Session session = DBSessionFactory.open();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			DBComment entity = new DBComment();
			entity.setCommentId(comment.getHashID());

			query.remove(entity, flags);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
	}

	/**
	 * Implementation of {@link IPersistence#getComment(Long)}
	 * 
	 * @param id
	 * @return a comment
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getComment(java.lang.Long)
	 */
	static Comment getComment(Long id) throws IOException {
		Session session = DBSessionFactory.open();
		SqlPersistenceHelper helper = new SqlPersistenceHelper();
		DBComment entity;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			entity = new DBComment();
			entity.setCommentId(id);
			entity = query.fromSingle(entity, DBQuery.QUERY_NOT_NULL
					| DBQuery.QUERY_CACHEABLE);

			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
		return helper.createComment(entity.getUserName(),
				entity.getSnippetId(), entity.getMessage(),
				entity.getCommentId(), entity.getCreatedAt(),
				entity.getPosVotes(), entity.getNegVotes());
	}

	/**
	 * Implementation of {@link IPersistence#getComments(Snippet)}
	 * 
	 * @param snippet
	 * @return a list of comments
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getComments(org.smartsnip.core.Snippet)
	 */
	static List<Comment> getComments(Snippet snippet) throws IOException {
		Session session = DBSessionFactory.open();
		SqlPersistenceHelper helper = new SqlPersistenceHelper();
		List<Comment> result = new ArrayList<Comment>();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			result = fetchComments(helper, session, snippet.getHashId());
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
		return result;
	}

	/**
	 * Implementation of {@link IPersistence#getVotes(Comment)}
	 * 
	 * @param comment
	 * @return a pair of numbers representing the positive and negative votes
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getVotes(org.smartsnip.core.Comment)
	 */
	static Pair<Integer, Integer> getVotes(Comment comment) throws IOException {
		Session session = DBSessionFactory.open();
		DBComment entity;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			entity = fetchComment(comment.getHashID(), session);

			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
		return new Pair<Integer, Integer>(entity.getPosVotes(),
				entity.getNegVotes());
	}

	/**
	 * Implementation of {@link IPersistence#getVote(User, Comment)}
	 * 
	 * @param user
	 * @param comment
	 * @return the vote
	 * @throws IOException
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getVote(org.smartsnip.core.User,
	 *      org.smartsnip.core.Comment)
	 */
	static Integer getVote(User user, Comment comment) throws IOException {
		Session session = DBSessionFactory.open();
		DBVote entity;
		Integer result = 0;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DBQuery query = new DBQuery(session);

			entity = new DBVote();
			entity.setVoteId(comment.getHashID(), user.getUsername());
			entity = query.fromSingle(entity, DBQuery.QUERY_NOT_NULL);
			if (entity.getVote() == DBVote.Vote.positive) {
				result = 1;
			}
			if (entity.getVote() == DBVote.Vote.negative) {
				result = -1;
			}

			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw new IOException(e);
		} finally {
			DBSessionFactory.close(session);
		}
		return result;
	}

	/**
	 * Helper method to fetch all comments of a snippet.
	 * 
	 * @param helper
	 *            the PersisteceHelper object to create the tags
	 * @param session
	 *            the session in which the query is to execute
	 * @param snippetId
	 *            the id of the snippet as source of the code
	 * @return a list of comments
	 */
	static List<Comment> fetchComments(SqlPersistenceHelper helper,
			Session session, Long snippetId) {
		DBQuery query = new DBQuery(session);
		DBComment entity = new DBComment();
		entity.setSnippetId(snippetId);
		List<Comment> result = new ArrayList<Comment>();

		for (Iterator<DBComment> iterator = query.iterate(entity,
				DBQuery.QUERY_CACHEABLE); iterator.hasNext();) {
			entity = iterator.next();
			result.add(helper.createComment(entity.getUserName(),
					entity.getSnippetId(), entity.getMessage(),
					entity.getCommentId(), entity.getCreatedAt(),
					entity.getPosVotes(), entity.getNegVotes()));
		}
		return result;
	}

	/**
	 * Helper method to fetch all is's of the comments of a snippet.
	 * 
	 * @param session
	 *            the session in which the query is to execute
	 * @param snippetId
	 *            the id of the snippet as source of the code
	 * @return a list of commentId's
	 */
	static List<Long> fetchCommentIds(Session session, Long snippetId) {
		DBQuery query = new DBQuery(session);
		DBComment entity = new DBComment();
		entity.setSnippetId(snippetId);
		List<Long> result = new ArrayList<Long>();

		for (Iterator<DBComment> iterator = query.iterate(entity,
				DBQuery.QUERY_CACHEABLE); iterator.hasNext();) {
			result.add(iterator.next().getCommentId());
		}
		return result;
	}

	/**
	 * Helper method to fetch a comment by it's Id
	 * 
	 * @param commentId
	 *            the id of the comment as source of the code
	 * @param session
	 *            the session in which the query is to execute
	 * @return the comment
	 */
	private static DBComment fetchComment(Long commentId, Session session) {
		DBComment entity;
		DBQuery query = new DBQuery(session);

		entity = new DBComment();
		entity.setCommentId(commentId);
		entity = query.fromSingle(entity, DBQuery.QUERY_NOT_NULL);
		return entity;
	}
}

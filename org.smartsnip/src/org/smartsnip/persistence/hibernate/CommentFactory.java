/**
 * File: CommentFactory.java
 * Date: 11.05.2012
 */
package org.smartsnip.persistence.hibernate;

import java.io.IOException;
import java.util.List;

import org.smartsnip.core.Comment;
import org.smartsnip.core.Snippet;
import org.smartsnip.core.User;
import org.smartsnip.shared.Pair;

/**
 * @author littlelion
 *
 */
public class CommentFactory {

	private CommentFactory() {
		// no instances
	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeComment(org.smartsnip.core.Comment, int)
	 */
	static Long writeComment(Comment comment, int flags) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeComment(List, int)
	 */
	static Long writeComment(List<Comment> comments, int flags) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#writeVote(java.lang.Integer, org.smartsnip.core.Comment, org.smartsnip.core.User, int)
	 */
	static void writeVote(Integer vote, Comment comment, User user, int flags)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#votePositive(org.smartsnip.core.User, org.smartsnip.core.Comment, int)
	 */
	static void votePositive(User user, Comment comment, int flags)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#voteNegative(org.smartsnip.core.User, org.smartsnip.core.Comment, int)
	 */
	static void voteNegative(User user, Comment comment, int flags)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#unVote(org.smartsnip.core.User, org.smartsnip.core.Comment, int)
	 */
	static void unVote(User user, Comment comment, int flags)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#removeComment(org.smartsnip.core.Comment, int)
	 */
	static void removeComment(Comment comment, int flags) throws IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getComment(java.lang.Long)
	 */
	static Comment getComment(Long id) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getComments(org.smartsnip.core.Snippet)
	 */
	static List<Comment> getComments(Snippet snippet) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getVotes(org.smartsnip.core.Comment)
	 */
	static Pair<Integer, Integer> getVotes(Comment comment) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.smartsnip.persistence.hibernate.SqlPersistenceImpl#getVote(org.smartsnip.core.User, org.smartsnip.core.Comment)
	 */
	static Integer getVote(User user, Comment comment) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}

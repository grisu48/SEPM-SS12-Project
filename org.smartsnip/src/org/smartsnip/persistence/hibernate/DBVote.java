/**
 * File: DBVote.java
 * Date: 04.05.2012
 */
package org.smartsnip.persistence.hibernate;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.smartsnip.persistence.IPersistence;

/**
 * @author littlelion
 * 
 */
@Entity
@DynamicInsert
@Table(name = "Vote")
public class DBVote {

	@EmbeddedId
	private VoteId voteId;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "vote")
	private Vote vote;

	/**
	 * Entity, POJO class
	 */
	public DBVote() {
		super();
	}

	// XXX remove constructor
	// /**
	// * @param vote
	// */
	// DBVote(Long commentId, String userName, Vote vote) {
	// super();
	// this.voteId = new VoteId(commentId, userName);
	// this.vote = vote;
	// }

	/**
	 * @return the voteId
	 */
	public VoteId getVoteId() {
		return this.voteId;
	}

	/**
	 * @param voteId
	 *            the voteId to set
	 */
	public void setVoteId(VoteId voteId) {
		this.voteId = voteId;
	}

	/**
	 * @param commentId
	 *            the commentId to set
	 * @param userName
	 *            the userName to set
	 */
	public void setVoteId(Long commentId, String userName) {
		this.voteId = new VoteId(commentId, userName);
	}

	/**
	 * @return the vote
	 */
	public Vote getVote() {
		return this.vote;
	}

	/**
	 * @param vote
	 *            the vote to set
	 */
	public void setVote(Vote vote) {
		this.vote = vote;
	}

	/**
	 * This enum represents the vote of a comment
	 * 
	 * @author littlelion
	 * 
	 */
	public enum Vote {
		/**
		 * The value of the vote: not set
		 */
		none,
		/**
		 * The value of the vote: negative vote
		 */
		negative,
		/**
		 * The value of the vote: positive vote
		 */
		positive
	}

	/**
	 * This method is called by the remove methods of IPersistence in
	 * {@code DB_NO_DELETE} mode. If this method is present the remove method
	 * defaults to {@link IPersistence#DB_NO_DELETE}.
	 */
	void disable() {
		this.vote = Vote.none;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.voteId == null) ? 0 : this.voteId.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DBVote other = (DBVote) obj;
		if (this.voteId == null) {
			if (other.voteId != null)
				return false;
		} else if (!this.voteId.equals(other.voteId))
			return false;
		return true;
	}
}

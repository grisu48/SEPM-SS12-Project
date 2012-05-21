/**
 * File: DBVote.java
 * Date: 04.05.2012
 */
package org.smartsnip.persistence.hibernate;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.smartsnip.persistence.IPersistence;

/**
 * @author littlelion
 * 
 */
@Entity
@Table(name = "Vote")
public class DBVote {

	@EmbeddedId
	private VoteId voteId;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "vote")
	private Vote vote;

	/**
	 * 
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
	 * 
	 * @author littlelion
	 * 
	 */
	enum Vote {
		none, negative, positive
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
	 * 
	 * @author littlelion
	 * 
	 */
	@Embeddable
	class VoteId implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 776274109892022330L;

		@NotNull
		// @ManyToOne(targetEntity = DBComment.class, fetch = FetchType.EAGER)
		// @ForeignKey(name = "DBComment.commentId")
		@Column(name = "comment_id")
		private Long commentId;

		@NotNull
		// @ManyToOne(targetEntity = DBUser.class, fetch = FetchType.EAGER)
		// @ForeignKey(name = "DBUser.userName")
		@Column(name = "user_name", length = 20)
		private String userName;

		/**
		 * 
		 */
		VoteId() {
			super();
		}

		/**
		 * @param commentId
		 * @param userName
		 */
		VoteId(Long commentId, String userName) {
			super();
			this.commentId = commentId;
			this.userName = userName;
		}

		/**
		 * @return the commentId
		 */
		public Long getCommentId() {
			return this.commentId;
		}

		/**
		 * @param commentId
		 *            the commentId to set
		 */
		public void setCommentId(Long commentId) {
			this.commentId = commentId;
		}

		/**
		 * @return the userName
		 */
		public String getUserName() {
			return this.userName;
		}

		/**
		 * @param userName
		 *            the userName to set
		 */
		public void setUserName(String userName) {
			this.userName = userName;
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ (int) (this.commentId ^ (this.commentId >>> 32));
			result = prime * result
					+ ((this.userName == null) ? 0 : this.userName.hashCode());
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
			VoteId other = (VoteId) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (this.commentId != other.commentId)
				return false;
			if (this.userName == null) {
				if (other.userName != null)
					return false;
			} else if (!this.userName.equals(other.userName))
				return false;
			return true;
		}

		private DBVote getOuterType() {
			return DBVote.this;
		}
	}
}

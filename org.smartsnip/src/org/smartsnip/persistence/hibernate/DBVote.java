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

/**
 * @author littlelion
 * 
 */
@Entity
@Table(name = "Vote")
public class DBVote {

	@EmbeddedId
	private VoteId voteId;

	@NotNull
	@Enumerated(value = EnumType.STRING)
	@Column(name = "vote")
	private Vote vote;

	/**
	 * 
	 */
	public DBVote() {
		super();
	}

	/**
	 * @param vote
	 */
	DBVote(long commentId, String userName, Vote vote) {
		super();
		this.voteId = new VoteId(commentId, userName);
		this.vote = vote;
	}

	/**
	 * @return the voteId
	 */
	public VoteId getVoteId() {
		return this.voteId;
	}

	/**
	 * @param voteId the voteId to set
	 */
	public void setVoteId(VoteId voteId) {
		this.voteId = voteId;
	}

	/**
	 * @return the vote
	 */
	public Vote getVote() {
		return this.vote;
	}

	/**
	 * @param vote the vote to set
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
		@ManyToOne(targetEntity = DBComment.class, fetch = FetchType.EAGER)
		@ForeignKey(name = "DBComment.commentId")
		@Column(name = "comment_id")
		private long commentId;

		@NotNull
		@ManyToOne(targetEntity = DBUser.class, fetch = FetchType.EAGER)
		@ForeignKey(name = "DBUser.userName")
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
		VoteId(long commentId, String userName) {
			super();
			this.commentId = commentId;
			this.userName = userName;
		}

		/**
		 * @return the commentId
		 */
		public long getCommentId() {
			return this.commentId;
		}

		/**
		 * @param commentId the commentId to set
		 */
		public void setCommentId(long commentId) {
			this.commentId = commentId;
		}

		/**
		 * @return the userName
		 */
		public String getUserName() {
			return this.userName;
		}

		/**
		 * @param userName the userName to set
		 */
		public void setUserName(String userName) {
			this.userName = userName;
		}
	}
}

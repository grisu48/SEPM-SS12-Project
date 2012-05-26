/**
 * File: VoteId.java
 * Date: 26.05.2012
 */
package org.smartsnip.persistence.hibernate;

import java.io.Serializable;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

/**
 * @author littlelion
 *
 */
class VoteId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 776274109892022330L;

	@NotNull
	@Column(name = "comment_id")
	private Long commentId;

	@NotNull
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
		result = prime * result
				+ ((this.commentId == null) ? 0 : this.commentId.hashCode());
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
		if (this.commentId == null) {
			if (other.commentId != null)
				return false;
		} else if (!this.commentId.equals(other.commentId))
			return false;
		if (this.userName == null) {
			if (other.userName != null)
				return false;
		} else if (!this.userName.equals(other.userName))
			return false;
		return true;
	}
}

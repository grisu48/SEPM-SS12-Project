/**
 * File: DBComment.java
 * Date: 04.05.2012
 */
package org.smartsnip.persistence.hibernate;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;

/**
 * @author littlelion
 * 
 */
@Entity
@DynamicInsert
@Table(name = "Comment")
public class DBComment {

	@Id
	@GeneratedValue
	@Column(name = "comment_id", insertable = false, updatable = false)
	private Long commentId;

	@Column(name = "snippet_id")
	private Long snippetId;

	@Column(name = "refers_to", length = 255)
	private String refersTo;

	@Lob
	@Column(name = "message", columnDefinition="TEXT")
	private String message;

	@GeneratedValue
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", insertable = false, updatable = false)
	private Date createdAt;

	@Column(name = "user_name", length = 20)
	private String userName;

	@GeneratedValue
	@Column(name = "pos_votes_sum", insertable = false, updatable = false)
	private Integer posVotes;

	@GeneratedValue
	@Column(name = "neg_votes_sum", insertable = false, updatable = false)
	private Integer negVotes;

	/**
	 * Entity, POJO class
	 */
	DBComment() {
		super();
	}

	// XXX remove constructor
//	/**
//	 * @param commentId
//	 * @param snippetId
//	 * @param refersTo
//	 * @param message
//	 * @param createdAt
//	 * @param userName
//	 * @param posVotes
//	 * @param negVotes
//	 */
//	DBComment(Long commentId, Long snippetId, String refersTo, String message,
//			Date createdAt, String userName, Integer posVotes, Integer negVotes) {
//		super();
//		this.commentId = commentId;
//		this.snippetId = snippetId;
//		this.refersTo = refersTo;
//		this.message = message;
//		this.createdAt = createdAt;
//		this.userName = userName;
//		this.posVotes = posVotes;
//		this.negVotes = negVotes;
//	}

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
	 * @return the snippetId
	 */
	public Long getSnippetId() {
		return this.snippetId;
	}

	/**
	 * @param snippetId
	 *            the snippetId to set
	 */
	public void setSnippetId(Long snippetId) {
		this.snippetId = snippetId;
	}

	/**
	 * @return the refersTo
	 */
	public String getRefersTo() {
		return this.refersTo;
	}

	/**
	 * @param refersTo
	 *            the refersTo to set
	 */
	public void setRefersTo(String refersTo) {
		this.refersTo = refersTo;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the createdAt
	 */
	public Date getCreatedAt() {
		return this.createdAt;
	}

	/**
	 * @param createdAt
	 *            the createdAt to set
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
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
	 * @return the posVotes
	 */
	public Integer getPosVotes() {
		return this.posVotes;
	}

	/**
	 * @param posVotes
	 *            the posVotes to set
	 */
	public void setPosVotes(Integer posVotes) {
		this.posVotes = posVotes;
	}

	/**
	 * @return the negVotes
	 */
	public Integer getNegVotes() {
		return this.negVotes;
	}

	/**
	 * @param negVotes
	 *            the negVotes to set
	 */
	public void setNegVotes(Integer negVotes) {
		this.negVotes = negVotes;
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
		DBComment other = (DBComment) obj;
		if (this.commentId == null) {
			if (other.commentId != null)
				return false;
		} else if (!this.commentId.equals(other.commentId))
			return false;
		return true;
	}
}

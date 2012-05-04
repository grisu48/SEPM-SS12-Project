/**
 * File: DBComment.java
 * Date: 04.05.2012
 */
package org.smartsnip.persistence.hibernate;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.smartsnip.core.Snippet;

/**
 * @author littlelion
 *
 */
@Entity
@Table(name="Comment")
public class DBComment {

	@Id
	@GeneratedValue
	@Column(name="comment_id", insertable=false, updatable=false)
	private long commentId;
	
	@NotNull
	@ManyToOne(targetEntity=Snippet.class, fetch=FetchType.EAGER)
	@ForeignKey(name="Snippet.snippetId")
	@OnDelete(action=OnDeleteAction.CASCADE)
	@Column(name="snippet_id")
	private long snippetId;
	
	@Column(name="refers_to", length = 200)
	private String refersTo;
	
	@Lob
	@Column(name="message", length = 200)
	private String message;
	
	@NotNull
	@GeneratedValue
	@Column(name="created_at", insertable=false, updatable=false)
	private Date createdAt;
	
	@ManyToOne(targetEntity=DBUser.class, fetch=FetchType.EAGER)
	@ForeignKey(name="User.userName")
	@Column(name="user_name", length = 20)
	private String userName;
	
	@NotNull
	@GeneratedValue
	@Column(name="pos_votes", insertable=false, updatable=false)
	private int posVotes;
	
	@NotNull
	@GeneratedValue
	@Column(name="neg_votes", insertable=false, updatable=false)
	private int negVotes;
	
	/**
	 * 
	 */
	DBComment() {
		super();
	}
	
	/**
	 * @param commentId
	 * @param snippetId
	 * @param refersTo
	 * @param message
	 * @param createdAt
	 * @param userName
	 * @param posVotes
	 * @param negVotes
	 */
	DBComment(long commentId, long snippetId, String refersTo, String message,
			Date createdAt, String userName, int posVotes, int negVotes) {
		super();
		this.commentId = commentId;
		this.snippetId = snippetId;
		this.refersTo = refersTo;
		this.message = message;
		this.createdAt = createdAt;
		this.userName = userName;
		this.posVotes = posVotes;
		this.negVotes = negVotes;
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
	 * @return the snippetId
	 */
	public long getSnippetId() {
		return this.snippetId;
	}

	/**
	 * @param snippetId the snippetId to set
	 */
	public void setSnippetId(long snippetId) {
		this.snippetId = snippetId;
	}

	/**
	 * @return the refersTo
	 */
	public String getRefersTo() {
		return this.refersTo;
	}

	/**
	 * @param refersTo the refersTo to set
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
	 * @param message the message to set
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
	 * @param createdAt the createdAt to set
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
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the posVotes
	 */
	public int getPosVotes() {
		return this.posVotes;
	}

	/**
	 * @param posVotes the posVotes to set
	 */
	public void setPosVotes(int posVotes) {
		this.posVotes = posVotes;
	}

	/**
	 * @return the negVotes
	 */
	public int getNegVotes() {
		return this.negVotes;
	}

	/**
	 * @param negVotes the negVotes to set
	 */
	public void setNegVotes(int negVotes) {
		this.negVotes = negVotes;
	}
}

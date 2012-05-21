/**
 * File: DBRating.java
 * Date: 04.05.2012
 */
package org.smartsnip.persistence.hibernate;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "Rating")
public class DBRating {

	@EmbeddedId
	private RatingId ratingId;

	@Column(name = "rating_value")
	private Integer value;

	/**
	 * 
	 */
	DBRating() {
		super();
	}

	// XXX remove constructor
	// /**
	// * @param snippetId
	// * @param userName
	// * @param value
	// */
	// DBRating(Long snippetId, String userName, Integer value) {
	// super();
	// this.ratingId = new RatingId(snippetId, userName);
	// this.value = value;
	// }

	/**
	 * @return the ratingId
	 */
	public RatingId getRatingId() {
		return this.ratingId;
	}

	/**
	 * @param ratingId
	 *            the ratingId to set
	 */
	public void setRatingId(RatingId ratingId) {
		this.ratingId = ratingId;
	}

	/**
	 * @param snippetId
	 *            the snippetId to set
	 * @param userName
	 *            the userName to set
	 */
	public void setRatingId(Long snippetId, String userName) {
		this.ratingId = new RatingId(snippetId, userName);
	}

	/**
	 * @return the value
	 */
	public Integer getValue() {
		return this.value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Integer value) {
		this.value = value;
	}

	/**
	 * This method is called by the remove methods of IPersistence in
	 * {@code DB_NO_DELETE} mode. If this method is present the remove method
	 * defaults to {@link IPersistence#DB_NO_DELETE}.
	 */
	void disable() {
		this.value = 0;
	}
	
	/**
	 * 
	 * @author littlelion
	 * 
	 */
	@Embeddable
	class RatingId implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2664532834291488550L;

		@NotNull
		// @ManyToOne(targetEntity = DBSnippet.class, fetch = FetchType.EAGER)
		// @ForeignKey(name = "DBSnippet.snippetId")
		@Column(name = "snippet_id")
		private Long snippetId;

		@NotNull
		// @ManyToOne(targetEntity = DBUser.class, fetch = FetchType.EAGER)
		// @ForeignKey(name = "DBUser.userName")
		@Column(name = "user_name", length = 20)
		private String userName;

		/**
		 * 
		 */
		RatingId() {
			super();
		}

		/**
		 * @param snippetId
		 * @param userName
		 */
		RatingId(Long snippetId, String userName) {
			super();
			this.snippetId = snippetId;
			this.userName = userName;
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
					+ (int) (this.snippetId ^ (this.snippetId >>> 32));
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
			RatingId other = (RatingId) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (this.snippetId != other.snippetId)
				return false;
			if (this.userName == null) {
				if (other.userName != null)
					return false;
			} else if (!this.userName.equals(other.userName))
				return false;
			return true;
		}

		private DBRating getOuterType() {
			return DBRating.this;
		}

	}
}

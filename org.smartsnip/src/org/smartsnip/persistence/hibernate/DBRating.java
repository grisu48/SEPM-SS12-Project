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

/**
 * @author littlelion
 * 
 */
@Entity
@Table(name = "Rating")
public class DBRating {

	@EmbeddedId
	private RatingId ratingId;

	@NotNull
	@Column(name = "rating_value")
	private int value;

	/**
	 * 
	 */
	DBRating() {
		super();
	}

	/**
	 * @param snippetId
	 * @param userName
	 * @param value
	 */
	DBRating(long snippetId, String userName, int value) {
		super();
		this.ratingId = new RatingId(snippetId, userName);
		this.value = value;
	}

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
	 * @return the value
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(int value) {
		this.value = value;
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
		@ManyToOne(targetEntity = DBSnippet.class, fetch = FetchType.EAGER)
		@ForeignKey(name = "DBSnippet.snippetId")
		@Column(name = "snippet_id")
		private long snippetId;

		@NotNull
		@ManyToOne(targetEntity = DBUser.class, fetch = FetchType.EAGER)
		@ForeignKey(name = "DBUser.userName")
		@Column(name = "user_name", length = 20)
		private String userName;

		/**
		 * 
		 */
		public RatingId() {
			super();
		}

		/**
		 * @param snippetId
		 * @param userName
		 */
		RatingId(long snippetId, String userName) {
			super();
			this.snippetId = snippetId;
			this.userName = userName;
		}

		/**
		 * @return the snippetId
		 */
		public long getSnippetId() {
			return this.snippetId;
		}

		/**
		 * @param snippetId
		 *            the snippetId to set
		 */
		public void setSnippetId(long snippetId) {
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

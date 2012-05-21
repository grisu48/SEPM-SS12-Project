/**
 * File: DBFavourite.java
 * Date: 03.05.2012
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
@Table(name = "Favourite")
public class DBFavourite {

	@EmbeddedId
	private FavouriteId favouriteId;

	@Column(name = "is_favourite")
	private Boolean favourite;

	/**
	 * 
	 */
	DBFavourite() {
		super();
	}

	// XXX remove constructor
	// /**
	// * @param favouriteId
	// * @param favourite
	// */
	// DBFavourite(String userName, Long snippetId, Boolean favourite) {
	// super();
	// this.favouriteId = new FavouriteId(userName, snippetId);
	// this.favourite = favourite;
	// }

	/**
	 * @return the favourite
	 */
	public Boolean isFavourite() {
		return this.favourite;
	}

	/**
	 * @param favourite
	 *            the favourite to set
	 */
	public void setFavourite(Boolean favourite) {
		this.favourite = favourite;
	}

	/**
	 * @return the favouriteId
	 */
	public FavouriteId getFavouriteId() {
		return favouriteId;
	}

	/**
	 * @param favouriteId
	 *            the favouriteId to set
	 */
	public void setFavouriteId(FavouriteId favouriteId) {
		this.favouriteId = favouriteId;
	}

	/**
	 * This method is called by the remove methods of IPersistence in
	 * {@code DB_NO_DELETE} mode. If this method is present the
	 * remove method defaults to {@link IPersistence#DB_NO_DELETE}.
	 */
	void disable() {
		this.setFavourite(false);
	}

	/**
	 * 
	 * @author littlelion
	 * 
	 */
	@Embeddable
	class FavouriteId implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5392254231626295258L;

		@NotNull
		// @ManyToOne(targetEntity = DBUser.class, fetch = FetchType.EAGER)
		// @ForeignKey(name = "User.userName")
		@Column(name = "user_name", length = 20)
		private String userName;

		@NotNull
		// @ManyToOne(targetEntity = DBSnippet.class, fetch = FetchType.EAGER)
		// @ForeignKey(name = "Snippet.snippetId")
		@Column(name = "snippet_id")
		private Long snippetId;

		/**
		 * 
		 */
		public FavouriteId() {
			super();
		}

		/**
		 * @param userName
		 * @param snippetId
		 */
		FavouriteId(String userName, Long snippetId) {
			super();
			this.userName = userName;
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
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			FavouriteId other = (FavouriteId) obj;
			if (!getOuterType().equals(other.getOuterType())) {
				return false;
			}
			if (this.snippetId != other.snippetId) {
				return false;
			}
			if (this.userName == null) {
				if (other.userName != null) {
					return false;
				}
			} else if (!this.userName.equals(other.userName)) {
				return false;
			}
			return true;
		}

		private DBFavourite getOuterType() {
			return DBFavourite.this;
		}

	}
}

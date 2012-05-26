/**
 * File: FavouriteId.java
 * Date: 26.05.2012
 */
package org.smartsnip.persistence.hibernate;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * Id class for the entity {@link DBFavourite}
 * @author littlelion
 *
 */
@Embeddable
class FavouriteId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1167154781761177082L;

	@NotNull
	@Column(name = "user_name", length = 20)
	private String userName;

	@NotNull
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
		result = prime * result
				+ ((this.snippetId == null) ? 0 : this.snippetId.hashCode());
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
		FavouriteId other = (FavouriteId) obj;
		if (this.snippetId == null) {
			if (other.snippetId != null)
				return false;
		} else if (!this.snippetId.equals(other.snippetId))
			return false;
		if (this.userName == null) {
			if (other.userName != null)
				return false;
		} else if (!this.userName.equals(other.userName))
			return false;
		return true;
	}
}

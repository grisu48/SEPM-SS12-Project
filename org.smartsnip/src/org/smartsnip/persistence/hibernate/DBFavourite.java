/**
 * File: DBFavourite.java
 * Date: 03.05.2012
 */
package org.smartsnip.persistence.hibernate;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.smartsnip.persistence.IPersistence;

/**
 * @author Gerhard Aigner
 * 
 */
@Entity
@DynamicInsert
@Table(name = "Favourite")
public class DBFavourite {

	@EmbeddedId
	private FavouriteId favouriteId;

	@Column(name = "is_favourite")
	private Boolean favourite;

	/**
	 * Entity, POJO class
	 */
	DBFavourite() {
		super();
	}

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
	 * @param userName
	 *            the userName to set
	 * @param snippetId
	 *            the snippetId to set
	 */
	public void setFavouriteId(String userName, Long snippetId) {
		this.favouriteId = new FavouriteId(userName, snippetId);
	}

	/**
	 * This method is called by the remove methods of IPersistence in
	 * {@code DB_NO_DELETE} mode. If this method is present the remove method
	 * defaults to {@link IPersistence#DB_NO_DELETE}.
	 */
	void disable() {
		this.setFavourite(false);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((this.favouriteId == null) ? 0 : this.favouriteId.hashCode());
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
		DBFavourite other = (DBFavourite) obj;
		if (this.favouriteId == null) {
			if (other.favouriteId != null)
				return false;
		} else if (!this.favouriteId.equals(other.favouriteId))
			return false;
		return true;
	}
}

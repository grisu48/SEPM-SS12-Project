/**
 * File: DBRating.java
 * Date: 04.05.2012
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
@Table(name = "Rating")
public class DBRating {

	@EmbeddedId
	private RatingId ratingId;

	@Column(name = "rating_value")
	private Integer value;

	/**
	 * Entity, POJO class
	 */
	DBRating() {
		super();
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
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.ratingId == null) ? 0 : this.ratingId.hashCode());
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
		DBRating other = (DBRating) obj;
		if (this.ratingId == null) {
			if (other.ratingId != null)
				return false;
		} else if (!this.ratingId.equals(other.ratingId))
			return false;
		return true;
	}
}

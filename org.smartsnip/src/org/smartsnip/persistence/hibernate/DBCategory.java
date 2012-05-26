/**
 * File: DBCategory.java
 * Date: 03.05.2012
 */
package org.smartsnip.persistence.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

/**
 * Database OR mapping class for table Category
 * 
 * @author littlelion
 * 
 */
@Entity
//TODO update hibernate see issue HHH-7074
//"the replacement annotations of @Entity are not working"
@SuppressWarnings("deprecation")
@org.hibernate.annotations.Entity(dynamicInsert = true)
//@DynamicInsert
@Table(name = "Category")
public class DBCategory {

	@Id
	@GeneratedValue
	@Column(name = "category_id", insertable = false, updatable = false)
	private Long categoryId;

	@Column(name = "parent_id")
//	@ManyToOne(targetEntity = DBCategory.class, fetch = FetchType.EAGER)
//	@ForeignKey(name = "DBCategory.categoryId")
	private Long parentId;

	@NaturalId
	@Column(name = "name", length = 255)
	private String name;

	@Lob
	@Column(name = "description")
	private String description;

	/**
	 * 
	 */
	DBCategory() {
		super();
	}

	// XXX remove constructor
//	/**
//	 * @param categoryId
//	 * @param parentId
//	 * @param name
//	 * @param description
//	 */
//	DBCategory(Long categoryId, Long parentId, String name, String description) {
//		super();
//		this.categoryId = categoryId;
//		this.parentId = parentId;
//		this.name = name;
//		this.description = description;
//	}

	/**
	 * @return the categoryId
	 */
	public Long getCategoryId() {
		return this.categoryId;
	}

	/**
	 * @param categoryId
	 *            the categoryId to set
	 */
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return the parentId
	 */
	public Long getParentId() {
		return this.parentId;
	}

	/**
	 * @param parentId
	 *            the parentId to set
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.categoryId == null) ? 0 : this.categoryId.hashCode());
		result = prime * result
				+ ((this.name == null) ? 0 : this.name.hashCode());
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
		DBCategory other = (DBCategory) obj;
		if (this.categoryId == null) {
			if (other.categoryId != null)
				return false;
		} else if (!this.categoryId.equals(other.categoryId))
			return false;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
			return false;
		return true;
	}
}

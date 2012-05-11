/**
 * File: DBCategory.java
 * Date: 03.05.2012
 */
package org.smartsnip.persistence.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

/**
 * Database OR mapping class for table Category
 * 
 * @author littlelion
 * 
 */
@Entity
@Table(name = "Category")
public class DBCategory {

	@Id
	@GeneratedValue
	@Column(name = "category_id", insertable = false, updatable = false)
	private long categoryId;

	@Column(name = "parent_id")
	@ManyToOne(targetEntity = DBCategory.class, fetch = FetchType.EAGER)
	@ForeignKey(name = "DBCategory.categoryId")
	private long parentId;

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

	/**
	 * @param categoryId
	 * @param parentId
	 * @param name
	 * @param description
	 */
	DBCategory(long categoryId, long parentId, String name, String description) {
		super();
		this.categoryId = categoryId;
		this.parentId = parentId;
		this.name = name;
		this.description = description;
	}

	/**
	 * @return the categoryId
	 */
	public long getCategoryId() {
		return this.categoryId;
	}

	/**
	 * @param categoryId
	 *            the categoryId to set
	 */
	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return the parentId
	 */
	public long getParentId() {
		return this.parentId;
	}

	/**
	 * @param parentId
	 *            the parentId to set
	 */
	public void setParentId(long parentId) {
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
}

/**
 * File: DBTag.java
 * Date: 28.04.2012
 */
package org.smartsnip.persistence.hibernate;

import javax.persistence.*;

/**
 * Database OR mapping class for table Tag
 * @author littlelion
 * 
 */
@Entity
@Table(name = "Tag")
class DBTag {
	@Id
	@Column(name = "name", nullable=false, unique=true)
	private String name;

	@Column(name = "usage_freq", nullable=false, unique=false)
	private int usageFrequence;

	DBTag() {
	}

	DBTag(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the usageFrequence
	 */
	public int getUsageFrequence() {
		return this.usageFrequence;
	}

	/**
	 * @param usageFrequence the usageFrequence to set
	 */
	public void setUsageFrequence(int usageFrequence) {
		this.usageFrequence = usageFrequence;
	}

}
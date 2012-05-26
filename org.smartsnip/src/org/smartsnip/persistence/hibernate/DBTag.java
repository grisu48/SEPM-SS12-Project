/**
 * File: DBTag.java
 * Date: 28.04.2012
 */
package org.smartsnip.persistence.hibernate;

import javax.persistence.*;

/**
 * Database OR mapping class for table Tag
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
@Table(name = "Tag")
class DBTag {
	@Id
	@Column(name = "tag_name", length = 50)
	private String name;

	@GeneratedValue
	@Column(name = "usage_freq", insertable = false, updatable = false)
	private Integer usageFrequence;

	/**
	 * 
	 */
	DBTag() {
	}

	// XXX remove constructor
//	DBTag(String name) {
//		this.name = name;
//	}

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
	 * @return the usageFrequence
	 */
	public Integer getUsageFrequence() {
		return this.usageFrequence;
	}

	/**
	 * @param usageFrequence
	 *            the usageFrequence to set
	 */
	public void setUsageFrequence(Integer usageFrequence) {
		this.usageFrequence = usageFrequence;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		DBTag other = (DBTag) obj;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
			return false;
		return true;
	}
}
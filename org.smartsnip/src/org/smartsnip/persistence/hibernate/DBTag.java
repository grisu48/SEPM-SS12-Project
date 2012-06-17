/**
 * File: DBTag.java
 * Date: 28.04.2012
 */
package org.smartsnip.persistence.hibernate;

import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

/**
 * Database OR mapping class for table Tag
 * 
 * @author littlelion
 * 
 */
@Entity
@Indexed
@DynamicInsert
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="EntityCache")
@Table(name = "Tag")
class DBTag {
	@Id
	@DocumentId
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.YES)
	@Column(name = "tag_name", length = 50)
	private String name;

	@GeneratedValue
	@Column(name = "usage_freq", insertable = false, updatable = false)
	private Integer usageFrequence;

	/**
	 * Entity, POJO class
	 */
	DBTag() {
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
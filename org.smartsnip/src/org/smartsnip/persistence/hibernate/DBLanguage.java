/**
 * File: DBLanguage.java
 * Date: 30.04.2012
 */
package org.smartsnip.persistence.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Database OR mapping class for table Language
 * @author littlelion
 * 
 */
@Entity
//TODO update hibernate see issue HHH-7074
//"the replacement annotations of @Entity are not working"
@SuppressWarnings("deprecation")
@org.hibernate.annotations.Entity(dynamicInsert = true)
//@DynamicInsert
@Table(name = "Language")
public class DBLanguage {

	@Id
	@Column(name = "language", length=50)
	String language;

	/**
	 * 
	 */
	DBLanguage() {
		super();
	}

	// XXX remove constructor
//	/**
//	 * @param language
//	 */
//	DBLanguage(String language) {
//		super();
//		this.language = language;
//	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return this.language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.language == null) ? 0 : this.language.hashCode());
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
		DBLanguage other = (DBLanguage) obj;
		if (this.language == null) {
			if (other.language != null)
				return false;
		} else if (!this.language.equals(other.language))
			return false;
		return true;
	}
}

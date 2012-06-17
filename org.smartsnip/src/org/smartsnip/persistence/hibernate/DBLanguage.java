/**
 * File: DBLanguage.java
 * Date: 30.04.2012
 */
package org.smartsnip.persistence.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;

/**
 * Database OR mapping class for table Language
 * @author littlelion
 * 
 */
@Entity
@DynamicInsert
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="LanguagesCache")
@Table(name = "Language")
public class DBLanguage {

	@Id
	@Column(name = "language", length=50)
	private String language;
	
	@Column(name="highlighter", length=255)
	private String highlighter;
	
	@Column(name="is_default")
	private Boolean isDefault;

	/**
	 * Entity, POJO class
	 */
	DBLanguage() {
		super();
	}

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
	 * @return the highlighter
	 */
	public String getHighlighter() {
		return this.highlighter;
	}

	/**
	 * @param highlighter the highlighter to set
	 */
	public void setHighlighter(String highlighter) {
		this.highlighter = highlighter;
	}

	/**
	 * @return the isDefault
	 */
	public Boolean getIsDefault() {
		return this.isDefault;
	}

	/**
	 * @param isDefault the isDefault to set
	 */
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
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
				+ ((this.highlighter == null) ? 0 : this.highlighter.hashCode());
		result = prime * result
				+ ((this.isDefault == null) ? 0 : this.isDefault.hashCode());
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
		if (this.highlighter == null) {
			if (other.highlighter != null)
				return false;
		} else if (!this.highlighter.equals(other.highlighter))
			return false;
		if (this.isDefault == null) {
			if (other.isDefault != null)
				return false;
		} else if (!this.isDefault.equals(other.isDefault))
			return false;
		if (this.language == null) {
			if (other.language != null)
				return false;
		} else if (!this.language.equals(other.language))
			return false;
		return true;
	}
}

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
@Table(name = "Language")
public class DBLanguage {

	@Id
	@Column(name = "name", nullable=false, unique=true)
	String language;

	/**
	 * 
	 */
	DBLanguage() {
		super();
	}

	/**
	 * @param language
	 */
	DBLanguage(String language) {
		super();
		this.language = language;
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
}

/**
 * File: DBTag.java
 * Date: 28.04.2012
 */
package org.smartsnip.persistence.hibernate;

import javax.persistence.*;

/**
 * @author littlelion
 * 
 */
@Entity
@Table(name = "Tag")
class DBTag {
	@Id
	@Column(name = "name", nullable=false, unique=true)
	private String name;

	DBTag() {
	}

	DBTag(String name) {
		this.name = name;
	}

	String getName() {
		return this.name;
	}

	void setName(String name) {
		this.name = name;
	}

}